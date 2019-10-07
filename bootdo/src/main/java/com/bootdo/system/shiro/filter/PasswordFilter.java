package com.bootdo.system.shiro.filter;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 登陆密码过滤器
 * @author liy
 * @date 2019年10月5日 下午10:54:27
 * @discript
 *
 */
public class PasswordFilter extends AccessControlFilter {
	private static final Logger log = LoggerFactory.getLogger(PasswordFilter.class);
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		String path=((HttpServletRequest)request).getServletPath();
		log.info("【"+path+"】请求参数："+JSON.toJSONString(request.getParameterMap()));
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return false;
	}
}
