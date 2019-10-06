package com.bootdo.system.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * api认证异常信息
 * 
 * @author liy
 * @date 2019年10月6日 下午2:36:58
 * @discript
 *
 */
public class ApiAuthenticationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code = 500;

	public ApiAuthenticationException(String msg) {
		super(msg);
	}

	public ApiAuthenticationException(String msg, Throwable e) {
		super(msg, e);
	}

	public ApiAuthenticationException(String msg, int code) {
		super(msg);
		this.code = code;
	}

	public ApiAuthenticationException(String msg, int code, Throwable e) {
		super(msg, e);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
