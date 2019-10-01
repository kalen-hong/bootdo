package com.bootdo.system.service;

import com.bootdo.system.domain.ApiContentDO;

import java.util.List;
import java.util.Map;

/**
 * api业务内容表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-09-29 15:51:59
 */
public interface ApiContentService {
	
	ApiContentDO get(Long id);
	
	List<ApiContentDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ApiContentDO apiContent);
	
	int update(ApiContentDO apiContent);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	List<ApiContentDO> listAllApi();
}
