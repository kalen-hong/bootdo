package com.bootdo.system.service;

import com.bootdo.system.domain.ApiContentDO;

public interface ApiInvokeRecordService {
	public void asyncSaveInvokeRecord(String token,ApiContentDO apiContentDO);
}
