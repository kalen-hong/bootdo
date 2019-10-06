package com.bootdo.system.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootdo.system.domain.ApiContentDO;
import com.bootdo.system.service.ApiContentService;
import com.bootdo.system.utils.MD5Util;
import com.bootdo.system.vo.ResponseVo;

/**
 * 风控接口维护
 * @title 
 * @author liy
 * @date 2019年10月1日
 * @discript
 */
@RestController
@RequestMapping("/api/risk")
public class ApiRiskController {
	protected Logger log=LoggerFactory.getLogger(ApiRiskController.class);
	@Autowired
	protected ApiContentService apiContentService;
	
	@PostMapping("/listOpenApi")
	public ResponseVo<List<ApiContentDO>> listOpenApi(@RequestParam("accessToken")String accessToken,@RequestParam("timestamp")String timestamp,@RequestParam("sign")String sign){
		log.info("查询所有的风控接口入参accessToken【"+accessToken+"】，timestamp【"+timestamp+"】，sign【"+sign+"】");
		if(StringUtils.isEmpty(accessToken)||StringUtils.isEmpty(timestamp)||StringUtils.isEmpty(sign)) {
			return new ResponseVo<List<ApiContentDO>>(ResponseVo.FAIL, "【accessToken,timestamp,sign】中有参数传空", null);
		}
		//MD5验签
		String md5Param=new StringBuffer(accessToken).append(timestamp).toString();
		if(!sign.equals(MD5Util.md5(md5Param))) {
			return new ResponseVo<List<ApiContentDO>>(ResponseVo.FAIL, "签名错误", null);
		}
		List<ApiContentDO> list=apiContentService.listAllApi();
		//转成map
		return new ResponseVo<List<ApiContentDO>>(ResponseVo.SUCCESS, "success", list);
	}
}
