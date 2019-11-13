package com.openapi.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.openapi.common.controller.BaseController;
import com.openapi.system.vo.RequestVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class ApiRiskController  extends BaseController {

	@Autowired
	protected ApiContentService apiContentService;

	@PostMapping("/listOpenApi")
	public ResponseVo<List<Map<String, Object>>> listOpenApi(@RequestBody RequestVo RequestVo) {

		//调用父类操作，进行sign签名验证
		ResponseVo rvo = super.initSign(RequestVo.getAccessToken(),RequestVo.getTimestamp(),RequestVo.getSign());
		if(rvo.getCode().equals(ResponseVo.FAIL)){
			return rvo;
		}

		List<ApiContentDO> list = apiContentService.listAllApi();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (CollectionUtils.isEmpty(list)) {
			rvo.setCode(ResponseVo.FAIL);
			rvo.setData(resultList);
			rvo.setMsg("暂无接口对外开放");
			return rvo;
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
		rvo.setCode(ResponseVo.SUCCESS);
		rvo.setData(resultList);
		rvo.setMsg("success");
		return rvo;
	}
}
