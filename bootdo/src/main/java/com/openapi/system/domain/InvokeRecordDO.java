package com.openapi.system.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-10-10 11:23:49
 */
public class InvokeRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//接口名称
	private String interfaceName;
	//
	private String url;
	//接口备注
	private String remark;
	//接口调用时间
	private Date invokeTime;
	//客户端ID
	private String clientId;
	//客户端ip
	private String clientIp;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：接口名称
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	/**
	 * 获取：接口名称
	 */
	public String getInterfaceName() {
		return interfaceName;
	}
	/**
	 * 设置：
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：接口备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：接口备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：接口调用时间
	 */
	public void setInvokeTime(Date invokeTime) {
		this.invokeTime = invokeTime;
	}
	/**
	 * 获取：接口调用时间
	 */
	public Date getInvokeTime() {
		return invokeTime;
	}
	/**
	 * 设置：客户端ID
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	/**
	 * 获取：客户端ID
	 */
	public String getClientId() {
		return clientId;
	}
	/**
	 * 设置：客户端ip
	 */
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	/**
	 * 获取：客户端ip
	 */
	public String getClientIp() {
		return clientIp;
	}
}
