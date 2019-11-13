package com.test.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-24 15:01:07
 */
public class MessageDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//编号
	private String msgNo;
	//号码
	private String iphoneNo;
	//内容
	private String msgContent;
	//发送时间
	private long msgTime;
	//状态
	private String status;
	//
	private String createBy;
	//
	private Date createDate;
	//
	private String updateBy;
	//
	private Date updateDate;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：编号
	 */
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}
	/**
	 * 获取：编号
	 */
	public String getMsgNo() {
		return msgNo;
	}
	/**
	 * 设置：号码
	 */
	public void setIphoneNo(String iphoneNo) {
		this.iphoneNo = iphoneNo;
	}
	/**
	 * 获取：号码
	 */
	public String getIphoneNo() {
		return iphoneNo;
	}
	/**
	 * 设置：内容
	 */
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	/**
	 * 获取：内容
	 */
	public String getMsgContent() {
		return msgContent;
	}
	/**
	 * 设置：发送时间
	 */
	public void setMsgTime(long msgTime) {
		this.msgTime = msgTime;
	}
	/**
	 * 获取：发送时间
	 */
	public long getMsgTime() {
		return msgTime;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：
	 */
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
}
