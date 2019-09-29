package com.bootdo.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.bootdo.system.dao.ApiContentDao;
import com.bootdo.system.domain.ApiContentDO;
import com.bootdo.system.service.ApiContentService;



@Service
public class ApiContentServiceImpl implements ApiContentService {
	@Autowired
	private ApiContentDao apiContentDao;
	
	@Override
	public ApiContentDO get(Long id){
		return apiContentDao.get(id);
	}
	
	@Override
	public List<ApiContentDO> list(Map<String, Object> map){
		return apiContentDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return apiContentDao.count(map);
	}
	
	@Override
	public int save(ApiContentDO apiContent){
		return apiContentDao.save(apiContent);
	}
	
	@Override
	public int update(ApiContentDO apiContent){
		return apiContentDao.update(apiContent);
	}
	
	@Override
	public int remove(Long id){
		return apiContentDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return apiContentDao.batchRemove(ids);
	}
	
}
