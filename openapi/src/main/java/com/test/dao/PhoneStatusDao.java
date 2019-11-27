package com.test.dao;

import com.test.domain.MessageDO;
import com.test.domain.PhoneStatusDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-24 15:01:07
 */
@Mapper
public interface PhoneStatusDao {


	List<PhoneStatusDO> list(Map<String, Object> map);
	

}
