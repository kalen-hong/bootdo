package com.openapi.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.openapi.system.dao.InvokeRecordDao;
import com.openapi.system.domain.InvokeRecordDO;
import com.openapi.system.service.InvokeRecordService;



@Service
public class InvokeRecordServiceImpl implements InvokeRecordService {
	@Autowired
	private InvokeRecordDao invokeRecordDao;
	
	@Override
	public InvokeRecordDO get(Long id){
		return invokeRecordDao.get(id);
	}
	
	@Override
	public List<InvokeRecordDO> list(Map<String, Object> map){
		return invokeRecordDao.list(map);
	}



	@Override
	public int count(Map<String, Object> map){
		return invokeRecordDao.count(map);
	}


	@Override
	public int save(InvokeRecordDO invokeRecord){
		return invokeRecordDao.save(invokeRecord);
	}
	
	@Override
	public int update(InvokeRecordDO invokeRecord){
		return invokeRecordDao.update(invokeRecord);
	}
	
	@Override
	public int remove(Long id){
		return invokeRecordDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return invokeRecordDao.batchRemove(ids);
	}
	
}
