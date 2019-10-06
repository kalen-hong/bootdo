package com.bootdo.system.service;

import com.bootdo.system.domain.ApiInvokeRecordDO;

public interface ApiInvokeRecordService {
	public void asyncSaveInvokeRecord(ApiInvokeRecordDO record);
}
