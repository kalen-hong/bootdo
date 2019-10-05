package com.bootdo.system.shiro.filter;

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
import com.bootdo.system.domain.ApiContentDO;
import com.bootdo.system.service.ApiContentService;
import com.bootdo.system.service.ApiInvokeRecordService;
import com.bootdo.system.shiro.token.ApiInvokeToken;
import com.bootdo.system.utils.IpUtil;
import com.bootdo.system.utils.JsonWebTokenUtil;
import com.bootdo.system.utils.RequestResponseUtil;
import com.bootdo.system.vo.ResponseVo;

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

	@Override
	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
			Object mappedValue) throws Exception {
		String url = ((HttpServletRequest) servletRequest).getRequestURL().toString();
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
				String clientId=JsonWebTokenUtil.parseJwt(accessToken, JsonWebTokenUtil.SECRET_KEY).getAppId();
				apiInvokeRecordService.asyncSaveInvokeRecord(clientId, apiContentDO);
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
		AuthenticationToken token = createApiInvokeToken(servletRequest);
		try {
			subject.login(token);
			return true;
		} catch (AuthenticationException e) {
			LOGGER.error(e.getMessage(),e);
			// 如果是JWT过期
			if ("expiredJwt".equals(e.getMessage())) {
				ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "token已过期", null);
				RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
				return false;

			}
			// 其他的判断为JWT错误无效
			ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "token信息有误，请重新申请token", null);
			RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
			return false;

		} catch (Exception e) {
			// 其他错误
			LOGGER.error(IpUtil.getIpFromRequest(WebUtils.toHttp(servletRequest)) + "--JWT认证失败" + e.getMessage(), e);
			// 告知客户端token错误,需重新登录申请token
			ResponseVo<String> responseVo = new ResponseVo<String>(ResponseVo.FAIL, "token信息有误，请重新申请token", null);
			RequestResponseUtil.responseWrite(JSON.toJSONString(responseVo), servletResponse);
			return false;
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
		String url=req.getRequestURL().toString();
		String appId = JsonWebTokenUtil.parseJwt(token, JsonWebTokenUtil.SECRET_KEY).getAppId();
		return new ApiInvokeToken(appId, token, url);
	}

	public void setApiInvokeRecordService(ApiInvokeRecordService apiInvokeRecordService) {
		this.apiInvokeRecordService = apiInvokeRecordService;
	}

	public void setApiContentService(ApiContentService apiContentService) {
		this.apiContentService = apiContentService;
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
}
