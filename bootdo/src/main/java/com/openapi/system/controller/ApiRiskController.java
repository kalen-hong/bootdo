package com.openapi.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openapi.system.domain.ApiContentDO;
import com.openapi.system.service.ApiContentService;
import com.openapi.system.utils.MD5Util;
import com.openapi.system.vo.ResponseVo;

/**
 * 风控接口维护
 * 
 * @title
 * @author liy
 * @date 2019年10月1日
 * @discript
 */
@RestController
@RequestMapping("/api/risk")
public class ApiRiskController {
	protected Logger log = LoggerFactory.getLogger(ApiRiskController.class);
	@Autowired
	protected ApiContentService apiContentService;

	@PostMapping("/listOpenApi")
	public ResponseVo<List<Map<String, Object>>> listOpenApi(@RequestParam("accessToken") String accessToken,
			@RequestParam("timestamp") String timestamp, @RequestParam("sign") String sign) {
		log.info("查询所有的风控接口入参accessToken【" + accessToken + "】，timestamp【" + timestamp + "】，sign【" + sign + "】");
		if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(sign)) {
			return new ResponseVo<List<Map<String, Object>>>(ResponseVo.FAIL, "【accessToken,timestamp,sign】中有参数传空",
					null);
		}
		// MD5验签
		String md5Param = new StringBuffer(accessToken).append(timestamp).toString();
		if (!sign.equals(MD5Util.md5(md5Param))) {
			return new ResponseVo<List<Map<String, Object>>>(ResponseVo.FAIL, "签名错误", null);
		}
		List<ApiContentDO> list = apiContentService.listAllApi();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (CollectionUtils.isEmpty(list)) {
			return new ResponseVo<List<Map<String, Object>>>(ResponseVo.FAIL, "暂无接口对外开放", resultList);
		}
		// 转成map
		list.forEach(api -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("apiUrl", api.getApiUrl());
			map.put("apiDesc", api.getApiDesc());
			map.put("requestMode", api.getRequestMode());
			map.put("requestCost", api.getRequestCost());
			resultList.add(map);
		});
		return new ResponseVo<List<Map<String, Object>>>(ResponseVo.SUCCESS, "success", resultList);
	}
}
