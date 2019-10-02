package com.bootdo.system.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * api业务内容表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-09-29 15:51:59
 */
public class ApiContentDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//接口地址
	private String apiUrl;
	//接口描述
	private String apiDesc;
	//请求方式
	private String requestMode;
	//调用费用
	private String requestCost;
	//状态，1启用 2禁用
	private Integer status;
	//创建时间
	private Date gtmCreate;
	//修改时间
	private Date gtmModified;

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
	 * 设置：接口地址
	 */
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	/**
	 * 获取：接口地址
	 */
	public String getApiUrl() {
		return apiUrl;
	}
	/**
	 * 设置：接口描述
	 */
	public void setApiDesc(String apiDesc) {
		this.apiDesc = apiDesc;
	}
	/**
	 * 获取：接口描述
	 */
	public String getApiDesc() {
		return apiDesc;
	}
	/**
	 * 设置：请求方式
	 */
	public void setRequestMode(String requestMode) {
		this.requestMode = requestMode;
	}
	/**
	 * 获取：请求方式
	 */
	public String getRequestMode() {
		return requestMode;
	}
	/**
	 * 设置：调用费用
	 */
	public void setRequestCost(String requestCost) {
		this.requestCost = requestCost;
	}
	/**
	 * 获取：调用费用
	 */
	public String getRequestCost() {
		return requestCost;
	}
	/**
	 * 设置：状态，1启用 2禁用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态，1启用 2禁用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：创建时间
	 */
	public void setGtmCreate(Date gtmCreate) {
		this.gtmCreate = gtmCreate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getGtmCreate() {
		return gtmCreate;
	}
	/**
	 * 设置：修改时间
	 */
	public void setGtmModified(Date gtmModified) {
		this.gtmModified = gtmModified;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getGtmModified() {
		return gtmModified;
	}
}
