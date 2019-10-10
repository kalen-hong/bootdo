package com.openapi.system.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openapi.system.dao.UserBusinessDao;
import com.openapi.system.domain.UserBusinessDO;
import com.openapi.system.service.UserBusinessService;



@Service
public class UserBusinessServiceImpl implements UserBusinessService {
	@Autowired
	private UserBusinessDao userBusinessDao;
	
	@Override
	public UserBusinessDO get(Long id){
		return userBusinessDao.get(id);
	}
	
	@Override
	public List<UserBusinessDO> list(Map<String, Object> map){
		return userBusinessDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return userBusinessDao.count(map);
	}
	
	@Override
	public int save(UserBusinessDO userBusiness){
		return userBusinessDao.save(userBusiness);
	}
	
	@Override
	public int update(UserBusinessDO userBusiness){
		return userBusinessDao.update(userBusiness);
	}
	
	@Override
	public int remove(Long id){
		return userBusinessDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return userBusinessDao.batchRemove(ids);
	}

	@Override
	public UserBusinessDO getByClientId(String clientId) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("clientId", clientId);
		List<UserBusinessDO> list=list(map);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
	@Override
	public boolean exit(Map<String, Object> params) {
		boolean exit;
		exit = userBusinessDao.list(params).size() > 0;
		return exit;
	}
}
