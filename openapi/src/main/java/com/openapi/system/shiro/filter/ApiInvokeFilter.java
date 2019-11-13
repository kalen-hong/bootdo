package com.openapi.system.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.openapi.common.redis.shiro.RedisManager;
import com.openapi.common.utils.IPUtils;
import com.openapi.system.constant.ApiConstants;
import com.openapi.system.constant.RedisConstants;
import com.openapi.system.domain.ApiContentDO;
import com.openapi.system.domain.ApiInvokeRecordDO;
import com.openapi.system.exception.ApiAuthenticationException;
import com.openapi.system.service.ApiContentService;
import com.openapi.system.service.ApiInvokeRecordService;
import com.openapi.system.shiro.token.ApiInvokeToken;
import com.openapi.system.utils.IpUtil;
import com.openapi.system.utils.JsonWebTokenUtil;
import com.openapi.system.utils.RequestResponseUtil;
import com.openapi.system.vo.JwtAccount;
import com.openapi.system.vo.ResponseVo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import java.io.InputStream;

/**
 * 第三方api调用过滤器
 * 
 * @author liy
 * @date 2019年10月5日 下午1:22:17
 * @discript
 *
 */
public class ApiInvokeFilter extends AbstractPathMatchingFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiInvokeFilter.class);

	private ApiInvokeRecordService apiInvokeRecordService;

	private ApiContentService apiContentService;

	private RedisManager redisManager;

	@Override
	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
			Object mappedValue) throws Exception {
		//return true;
		String url = ((HttpServletRequest) servletRequest).getServletPath();
		if (url.endsWith("/getToken")) {
			return true;
		}
		Subject subject = getSubject(servletRequest, servletResponse);
		// 记录api调用记录
		InputStream in = servletRequest.getInputStream();
		String request = IOUtils.toString(in, "UTF-8");
		String accessToken = JSONObject.parseObject(request).getString("accessToken");
		// 判断是否为JWT认证请求
		if ((null != subject && !subject.isAuthenticated()) && StringUtils.isNotEmpty(accessToken)) {
			boolean success = tokenAuthenticatedSuccess(subject, servletRequest, servletResponse);
			if (success) {
				ApiContentDO apiContentDO = apiContentService.getApiContent(url);
				// 校验api接口是否禁用
				if (apiContentDO == null) {
					ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "接口不存在", null);
					RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
					return false;
				} else if (!ApiConstants.API_STATUS_ENABLED.equals(String.valueOf(apiContentDO.getStatus()))) {
					ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "接口已被禁用", null);
					RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
					return false;
				}
				String clientId = JsonWebTokenUtil.parseJwt(accessToken, JsonWebTokenUtil.SECRET_KEY).getAppId();
				if(!latestToken(accessToken, clientId)) {
					ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "请输入最新的token", null);
					RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
					return false;
				}
				// 接口调用redis计数--断路限流
				if (apiInvokeIsNeedLimit(clientId, String.valueOf(apiContentDO.getId()))) {
					ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "接口调用频繁，请稍后再试", null);
					RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
					return false;
				}
				asyncSaveInvokeRecord(url, apiContentDO.getApiDesc(), clientId, servletRequest);
			}
			return success;
		}
		// 请求未携带token 判断为无效请求
		ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "请求未传token信息", null);
		RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
		return false;
	}

	private boolean tokenAuthenticatedSuccess(Subject subject, ServletRequest servletRequest,
			ServletResponse servletResponse) {
		String exceptionMsg = "";
		try {
			ApiInvokeToken token = createApiInvokeToken(servletRequest);
			subject.login(token);
			return true;
		} catch (ApiAuthenticationException e) {
			exceptionMsg = e.getMessage();
		} catch (ExpiredJwtException e) {
			exceptionMsg = "token已过期，请刷新token信息";
		} catch (SignatureException e) {
			exceptionMsg = "token非法，请重新获取token";
		} catch (AuthenticationException e) {
			LOGGER.error(e.getMessage(), e);
			exceptionMsg = "token无效，请重新获取token";
		} catch (Exception e) {
			LOGGER.error(IpUtil.getIpFromRequest(WebUtils.toHttp(servletRequest)) + "--JWT认证失败" + e.getMessage(), e);
			exceptionMsg = "token无效，请重新获取token";
		}
		ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, exceptionMsg, null);
		RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
		return false;
	}

	private boolean latestToken(String token, String clientId) {
		try {
			String key = String.format(RedisConstants.CLIENT_TOKEN, clientId);
			String cacheToken = redisManager.get(key);
			if (StringUtils.isEmpty(cacheToken)) {
				return true;
			}
			return cacheToken.equals(token);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return true;
		}
	}

	private void asyncSaveInvokeRecord(String url, String interfaceName, String clientId,
			ServletRequest servletRequest) {
		try {
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

	private ApiInvokeToken createApiInvokeToken(ServletRequest request) {
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
	 * api调用是否需要限制(针对同一接口,3秒之内只允许请求3次)
	 * 
	 * @param clientId
	 * @param apiUrlId
	 * @return
	 */
	private boolean apiInvokeIsNeedLimit(String clientId, String apiUrlId) {
		try {
			String key = String.format(RedisConstants.API_INVOKE_COUNTER, clientId, apiUrlId);
			long singleApiInvokeNum = redisManager.incr(key, 1);
			apiInvokeCounterSetExpire(key, singleApiInvokeNum);
			return singleApiInvokeNum > RedisConstants.API_INVOKE_LIMIT;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}

	private void apiInvokeCounterSetExpire(String key, long singleApiInvokeNum) {
		try {
			if (singleApiInvokeNum == 1) {
				redisManager.expire(key, RedisConstants.API_INVOKE_COUNTER_EXPIRETIME);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
