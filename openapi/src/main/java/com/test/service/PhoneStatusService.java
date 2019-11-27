package com.test.service;

import com.test.domain.MessageDO;
import com.test.domain.PhoneStatusDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-24 15:01:07
 */
public interface PhoneStatusService {
	

	List<PhoneStatusDO> list(Map<String, Object> map);

}
