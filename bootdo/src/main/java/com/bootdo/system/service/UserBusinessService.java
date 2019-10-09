package com.bootdo.system.service;

import com.bootdo.system.domain.UserBusinessDO;

import java.util.List;
import java.util.Map;

/**
 * 业务用户表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-09-29 18:20:03
 */
public interface UserBusinessService {
	
	UserBusinessDO get(Long id);
	
	List<UserBusinessDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UserBusinessDO userBusiness);
	
	int update(UserBusinessDO userBusiness);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	UserBusinessDO getByClientId(String clientId);

	boolean exit(Map<String, Object> params);
}
