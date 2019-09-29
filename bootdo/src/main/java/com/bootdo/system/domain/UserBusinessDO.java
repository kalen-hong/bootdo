package com.bootdo.system.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 业务用户表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-09-29 18:20:03
 */
public class UserBusinessDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//用户账号
	private String clientId;



	//用户密码
	private String clientSecret;
	//用户名
	private String username;
	//状态，1男 2女 0未知
	private Integer sex;
	//手机号
	private String mobile;
	//状态 0:禁用，1:正常
	private Integer status;
	//用户备注
	private String note;
	//创建时间
	private Date gmtCreate;
	//修改时间
	private Date gmtModified;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户账号
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	/**
	 * 获取：用户账号
	 */
	public String getClientId() {
		return clientId;
	}
	/**
	 * 设置：用户密码
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	/**
	 * 获取：用户密码
	 */
	public String getClientSecret() {
		return clientSecret;
	}
	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：状态，1男 2女 0未知
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	/**
	 * 获取：状态，1男 2女 0未知
	 */
	public Integer getSex() {
		return sex;
	}
	/**
	 * 设置：手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机号
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：状态 0:禁用，1:正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0:禁用，1:正常
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：用户备注
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 获取：用户备注
	 */
	public String getNote() {
		return note;
	}
	/**
	 * 设置：创建时间
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}
	/**
	 * 设置：修改时间
	 */
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getGmtModified() {
		return gmtModified;
	}
}
