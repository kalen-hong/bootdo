package com.bootdo.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.bootdo.system.dao.UserBusinessDao;
import com.bootdo.system.domain.UserBusinessDO;
import com.bootdo.system.service.UserBusinessService;



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
	
}
