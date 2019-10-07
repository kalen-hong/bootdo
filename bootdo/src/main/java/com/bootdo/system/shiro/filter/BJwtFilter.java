package com.bootdo.system.shiro.filter;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.bootdo.common.redis.shiro.RedisManager;
import com.bootdo.common.utils.IPUtils;
import com.bootdo.system.constant.RedisConstants;
import com.bootdo.system.domain.ApiContentDO;
import com.bootdo.system.domain.ApiInvokeRecordDO;
import com.bootdo.system.exception.ApiAuthenticationException;
import com.bootdo.system.service.ApiContentService;
import com.bootdo.system.service.ApiInvokeRecordService;
import com.bootdo.system.shiro.token.ApiInvokeToken;
import com.bootdo.system.utils.IpUtil;
import com.bootdo.system.utils.JsonWebTokenUtil;
import com.bootdo.system.utils.RequestResponseUtil;
import com.bootdo.system.vo.JwtAccount;
import com.bootdo.system.vo.ResponseVo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

/**
 * 第三方api调用过滤器
 * 
 * @author liy
 * @date 2019年10月5日 下午1:22:17
 * @discript
 *
 */
public class BJwtFilter extends BPathMatchingFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(BJwtFilter.class);

	private ApiInvokeRecordService apiInvokeRecordService;

	private ApiContentService apiContentService;
	
	private RedisManager redisManager;

	@Override
	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
			Object mappedValue) throws Exception {
		String url = ((HttpServletRequest) servletRequest).getServletPath();
		if (url.endsWith("/getToken")) {
			return true;
		}
		Subject subject = getSubject(servletRequest, servletResponse);
		// 记录api调用记录
		String accessToken = servletRequest.getParameter("accessToken");
		// 判断是否为JWT认证请求
		if ((null != subject && !subject.isAuthenticated()) && StringUtils.isNotEmpty(accessToken)) {
			boolean success = tokenAuthenticatedSuccess(subject, servletRequest, servletResponse);
			if (success && providerOuterBusinessApi(url)) {
				ApiContentDO apiContentDO = apiContentService.getApiContent(url);
				// 校验api接口是否禁用
				if (apiContentDO == null) {
					ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "接口已被禁用", null);
					RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
					return false;
				}
				asyncSaveInvokeRecord(url, apiContentDO.getApiDesc(), accessToken, servletRequest);
			}
			// 接口调用redis计数--断路限流
			
			return success;
		}
		// 请求未携带token 判断为无效请求
		ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "请求未传token信息", null);
		RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
		return false;
	}

	private boolean tokenAuthenticatedSuccess(Subject subject, ServletRequest servletRequest,
			ServletResponse servletResponse) {
		String exceptionMsg="";
		try {
			AuthenticationToken token = createApiInvokeToken(servletRequest);
			subject.login(token);
			return true;
		} catch (ApiAuthenticationException e) {
			exceptionMsg=e.getMessage();
		} catch (ExpiredJwtException e) {
			exceptionMsg="token已过期，请刷新token信息";
		} catch (SignatureException e) {
			exceptionMsg="token非法，请重新获取token";
		} catch (AuthenticationException e) {
			LOGGER.error(e.getMessage(), e);
			exceptionMsg="token无效，请重新获取token";
		} catch (Exception e) {
			LOGGER.error(IpUtil.getIpFromRequest(WebUtils.toHttp(servletRequest)) + "--JWT认证失败" + e.getMessage(), e);
			exceptionMsg="token无效，请重新获取token";
		}
		ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, exceptionMsg, null);
		RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
		return false;
	}

	private void asyncSaveInvokeRecord(String url, String interfaceName, String accessToken,
			ServletRequest servletRequest) {
		try {
			String clientId = JsonWebTokenUtil.parseJwt(accessToken, JsonWebTokenUtil.SECRET_KEY).getAppId();
			String clientIp = IPUtils.getIpAddr((HttpServletRequest) servletRequest);
			ApiInvokeRecordDO record = new ApiInvokeRecordDO();
			record.setClientId(clientId);
			record.setClientIp(clientIp);
			record.setUrl(url);
			record.setInterfaceName(interfaceName);
			apiInvokeRecordService.asyncSaveInvokeRecord(record);
		} catch (Exception e) {
			LOGGER.info("异步保存接口调用记录失败", e);
		}
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		Subject subject = getSubject(servletRequest, servletResponse);

		// 未认证的情况
		if (null == subject || !subject.isAuthenticated()) {
			// 告知客户端token认证失败,需重新申请token
			ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "token信息有误，请重新申请token", null);
			RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
		} else {
			// 告知客户端token认证成功,但没有权限访问此资源
			ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "没有访问该接口的权限", null);
			RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
		}
		// 过滤链终止
		return false;
	}

	private AuthenticationToken createApiInvokeToken(ServletRequest request) {
		String token = request.getParameter("accessToken");
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURL().toString();
		JwtAccount jwt = JsonWebTokenUtil.parseJwt(token, JsonWebTokenUtil.SECRET_KEY);
		return new ApiInvokeToken(jwt.getAppId(), token, url);
	}

	public void setApiInvokeRecordService(ApiInvokeRecordService apiInvokeRecordService) {
		this.apiInvokeRecordService = apiInvokeRecordService;
	}

	public void setApiContentService(ApiContentService apiContentService) {
		this.apiContentService = apiContentService;
	}

	public void setRedisManager(RedisManager redisManager) {
		this.redisManager = redisManager;
	}

	/**
	 * 是否是提供给外部的业务api
	 * 
	 * @param url
	 * @return
	 */
	private boolean providerOuterBusinessApi(String url) {
		if (url.endsWith("/getToken") || url.endsWith("/refreshToken") || url.endsWith("listOpenApi")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 是否频繁调用第三方接口(限制：针对同一接口,3秒之内只允许请求3次)
	 * @param clientId
	 * @param apiUrlId
	 * @return
	 */
	private boolean callOverrun(String clientId,String apiUrlId) {
		try {
			String key=String.format(RedisConstants.API_INVOKE_COUNTER, clientId,apiUrlId);
			return false;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			return false;
		}
	}
}
