package com.openapi.system.service;

import com.openapi.system.domain.ApiInvokeRecordDO;

public interface ApiInvokeRecordService {
	public void asyncSaveInvokeRecord(ApiInvokeRecordDO record);
}
