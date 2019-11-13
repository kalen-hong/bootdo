package com.test.service;

import com.test.domain.MessageDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-24 15:01:07
 */
public interface MessageService {
	
	MessageDO get(Integer id);
	
	List<MessageDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MessageDO message);
	
	int update(MessageDO message);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
