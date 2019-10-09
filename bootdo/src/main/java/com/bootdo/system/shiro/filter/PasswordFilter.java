package com.bootdo.system.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

/**
 * 登陆密码过滤器
 * @author liy
 * @date 2019年10月5日 下午10:54:27
 * @discript
 *
 */
public class PasswordFilter extends AccessControlFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return false;
	}
}
