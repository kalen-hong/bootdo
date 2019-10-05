package com.bootdo.system.service.impl;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootdo.system.dao.ApiInvokeRecordDao;
import com.bootdo.system.domain.ApiContentDO;
import com.bootdo.system.domain.ApiInvokeRecord;
import com.bootdo.system.service.ApiInvokeRecordService;

@Service
public class ApiInvokeRecordServiceImpl implements ApiInvokeRecordService {
	protected Logger log = LoggerFactory.getLogger(ApiInvokeRecordServiceImpl.class);
	protected ExecutorService executors = new ThreadPoolExecutor(3, 30, 3, TimeUnit.MINUTES,
			new LinkedBlockingQueue<>(1000));

	@Autowired
	protected ApiInvokeRecordDao apiInvokeRecordDao;

	@Override
	public void asyncSaveInvokeRecord(String clientId,ApiContentDO apiContentDO) {
		try {
			ApiInvokeRecord record = new ApiInvokeRecord();
			record.setInterfaceName(apiContentDO.getApiDesc());
			record.setUrl(apiContentDO.getApiUrl());
			record.setInvokeTime(new Date());
			record.setClientId(String.valueOf(clientId));
			executors.execute(new Runnable() {
				@Override
				public void run() {
					apiInvokeRecordDao.insert(record);
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
