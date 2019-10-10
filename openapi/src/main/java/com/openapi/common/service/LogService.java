package com.openapi.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.openapi.common.domain.LogDO;
import com.openapi.common.domain.PageDO;
import com.openapi.common.utils.Query;
@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
