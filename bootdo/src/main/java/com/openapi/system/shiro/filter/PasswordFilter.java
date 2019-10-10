package com.openapi.system.shiro.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.openapi.common.utils.HttpServletUtils;
import com.openapi.system.constant.AdminSystemConstants;

/**
 * 登陆密码过滤器
 * @author liy
 * @date 2019年10月5日 下午10:54:27
 * @discript
 *
 */
public class PasswordFilter extends AccessControlFilter {
	protected Logger log=LoggerFactory.getLogger(PasswordFilter.class);
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		HttpServletRequest req=(HttpServletRequest)request;
		boolean ajaxRequest=HttpServletUtils.jsAjax(req);
		if(ajaxRequest) {
			return true;
		}
		String url=req.getServletPath();
		Map<String, String[]> paramMap=req.getParameterMap();
		log.info("后台系统请求参数："+JSON.toJSONString(paramMap.keySet()));
		if(needIntercept(url)&&paramMap.containsKey("limit")&&paramMap.containsKey("offset")) {
			return true;
		}
		((HttpServletResponse)response).sendRedirect("login");
		return false;
	}
	
	private boolean needIntercept(String url) {
		for(String prefix:AdminSystemConstants.NEED_INTERCEPT_URL_PREFIX) {
			if(url.endsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return false;
	}
}
