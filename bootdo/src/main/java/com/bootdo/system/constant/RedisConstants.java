package com.bootdo.system.constant;
/**
 * redis相关常量
 * @author liy
 * @date 2019年10月6日 下午4:55:32
 * @discript
 *
 */
public class RedisConstants {
	/**
	 * api调用计数：clientId：接口id
	 */
	public static final String API_INVOKE_COUNTER="openapi:api_invoke_counter:%s:%s";
	/**
	 * api调用计数器：redis过期时间(秒)
	 */
	public static final Integer API_INVOKE_COUNTER_EXPIRETIME=3;
	/**
	 * api调用计数器：限制次数
	 */
	public static final Integer API_INVOKE_LIMIT=3;
}
