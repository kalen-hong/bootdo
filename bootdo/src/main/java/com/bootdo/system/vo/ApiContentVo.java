package com.bootdo.system.vo;

import com.bootdo.system.domain.ApiContentDO;

public class ApiContentVo extends ApiContentDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String httpMethod;
	private String ipPort;
	private String requestPrefix;
	private String requestPath;

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

	public String getRequestPrefix() {
		return requestPrefix;
	}

	public void setRequestPrefix(String requestPrefix) {
		this.requestPrefix = requestPrefix;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

}
