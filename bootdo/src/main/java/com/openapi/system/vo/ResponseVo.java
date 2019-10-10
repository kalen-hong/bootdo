package com.openapi.system.vo;

import java.io.Serializable;

public class ResponseVo<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SUCCESS = "0";
	public static final String FAIL = "1";
	private String code;
	private String msg;
	private T data;

	public ResponseVo(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
