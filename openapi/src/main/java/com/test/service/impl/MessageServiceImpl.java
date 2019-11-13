package com.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.test.dao.MessageDao;
import com.test.domain.MessageDO;
import com.test.service.MessageService;



@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageDao messageDao;
	
	@Override
	public MessageDO get(Integer id){
		return messageDao.get(id);
	}
	
	@Override
	public List<MessageDO> list(Map<String, Object> map){
		return messageDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return messageDao.count(map);
	}
	
	@Override
	public int save(MessageDO message){
		return messageDao.save(message);
	}
	
	@Override
	public int update(MessageDO message){
		return messageDao.update(message);
	}
	
	@Override
	public int remove(Integer id){
		return messageDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return messageDao.batchRemove(ids);
	}
	
}
