package com.openapi.common.controller;

import com.openapi.system.controller.ApiRiskController;
import com.openapi.system.domain.UserToken;
import com.openapi.system.utils.MD5Util;
import com.openapi.system.vo.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.openapi.common.utils.ShiroUtils;
import com.openapi.system.domain.UserDO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class BaseController {

	protected Logger log = LoggerFactory.getLogger(BaseController.class);
	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}


	//签名验证
	public ResponseVo initSign(String accessToken,
					  String timestamp, String sign){

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

		return new ResponseVo();
	}
}