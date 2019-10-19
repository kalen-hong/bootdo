package com.test.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-19 19:09:03
 */
public class TestUserDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//用户号
	private String userCode;

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：用户号
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	/**
	 * 获取：用户号
	 */
	public String getUserCode() {
		return userCode;
	}
}
