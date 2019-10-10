package com.openapi.system.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

public class ApiInvokeToken implements AuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appId; // 用户的标识
	private String jwt; // json web token值
	private String url;

	public ApiInvokeToken(String appId, String jwt, String url) {
		super();
		this.appId = appId;
		this.jwt = jwt;
		this.url = url;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public Object getPrincipal() {
		return appId;
	}

	@Override
	public Object getCredentials() {
		return jwt;
	}

}
