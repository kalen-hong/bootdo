package com.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.test.dao.TestUserDao;
import com.test.domain.TestUserDO;
import com.test.service.TestUserService;



@Service
public class TestUserServiceImpl implements TestUserService {
	@Autowired
	private TestUserDao testUserDao;
	
	@Override
	public TestUserDO get(Integer id){
		return testUserDao.get(id);
	}
	
	@Override
	public List<TestUserDO> list(Map<String, Object> map){
		return testUserDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return testUserDao.count(map);
	}
	
	@Override
	public int save(TestUserDO testUser){
		return testUserDao.save(testUser);
	}
	
	@Override
	public int update(TestUserDO testUser){
		return testUserDao.update(testUser);
	}
	
	@Override
	public int remove(Integer id){
		return testUserDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return testUserDao.batchRemove(ids);
	}
	
}
