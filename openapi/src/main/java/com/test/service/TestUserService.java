package com.test.service;

import com.test.domain.TestUserDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-19 19:09:03
 */
public interface TestUserService {
	
	TestUserDO get(Integer id);
	
	List<TestUserDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TestUserDO testUser);
	
	int update(TestUserDO testUser);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
