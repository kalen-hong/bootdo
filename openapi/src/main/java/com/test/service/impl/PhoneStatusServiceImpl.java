package com.test.service.impl;

import com.test.dao.MessageDao;
import com.test.dao.PhoneStatusDao;
import com.test.domain.MessageDO;
import com.test.domain.PhoneStatusDO;
import com.test.service.PhoneStatusService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @功能名称:
 * @APP_ID: openapi
 * @包名称: com.test.service.impl
 * @Date: 2019-11-27 17:11.
 * @Author: kalen.hong
 * @Copyright（C）: 2014-2019 X-Financial Inc.   All rights reserved.
 * 注意：本内容仅限于小赢科技有限责任公司内部传阅，禁止外泄以及用于其他的商业目的。
 */
public class PhoneStatusServiceImpl  implements PhoneStatusService {

    @Autowired
    private PhoneStatusDao phoneStatusDao;


    @Override
    public List<PhoneStatusDO> list(Map<String, Object> map){
        return phoneStatusDao.list(map);
    }

}
