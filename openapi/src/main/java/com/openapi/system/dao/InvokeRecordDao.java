package com.openapi.system.dao;

import com.openapi.system.domain.InvokeRecordDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-10-10 11:23:49
 */
@Mapper
public interface InvokeRecordDao {

	InvokeRecordDO get(Long id);
	
	List<InvokeRecordDO> list(Map<String, Object> map);


	int count(Map<String, Object> map);


	int save(InvokeRecordDO invokeRecord);
	
	int update(InvokeRecordDO invokeRecord);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
