package com.test.dao;

import com.test.domain.MessageDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-24 15:01:07
 */
@Mapper
public interface MessageDao {

	MessageDO get(Integer id);
	
	List<MessageDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MessageDO message);
	
	int update(MessageDO message);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
