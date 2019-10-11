package com.bootdo.system.dao;

import com.bootdo.system.domain.ApiContentDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * api业务内容表
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-09-29 15:51:59
 */
@Mapper
public interface ApiContentDao {

	ApiContentDO get(Long id);
	
	List<ApiContentDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ApiContentDO apiContent);
	
	int update(ApiContentDO apiContent);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
