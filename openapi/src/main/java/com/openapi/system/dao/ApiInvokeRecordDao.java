package com.openapi.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.openapi.system.domain.ApiInvokeRecordDO;

/**
 * 接口调用记录表
 * 
 * @author liy
 * @email
 * @date 2019-10-02
 */
@Mapper
public interface ApiInvokeRecordDao {
	ApiInvokeRecordDO get(Long id);

	List<ApiInvokeRecordDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(ApiInvokeRecordDO invokeRecord);

	int update(ApiInvokeRecordDO invokeRecord);

	int remove(Long id);

	int batchRemove(Long[] ids);
}