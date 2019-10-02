package com.bootdo.system.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootdo.system.constant.ApiUrlConstants;
import com.bootdo.system.utils.JsonWebTokenUtil;
import com.bootdo.system.vo.JwtAccount;
import com.bootdo.system.vo.ResponseVo;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token维护
 * @title 
 * @author liy
 * @date 2019年10月1日
 * @discript
 */
@RestController
@RequestMapping("api/token")
public class ApiTokenCrontroller {
	protected Logger log=LoggerFactory.getLogger(ApiTokenCrontroller.class);
	
	@RequestMapping("/getToken")
	public ResponseVo<Map<String, Object>> getToken(@RequestParam("clientId") String clientId,
			@RequestParam("clientSecret") String clientSecret) {
		log.info("获取token信息入参，clientId【"+clientId+"】，clientSecret【"+clientSecret+"】");
		//验证用户名和密码
		String token = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), clientId, "token-server",
				ApiUrlConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS512);
		Map<String, Object> data=new HashMap<String,Object>();
		data.put("accessToken", token);
		data.put("expiresIn", ApiUrlConstants.TOKEN_EXPIRE_TIME);
		return new ResponseVo<Map<String, Object>>("0", "success", data);
	}
	
	@RequestMapping("/refreshToken")
	public ResponseVo<Map<String, Object>> refreshToken(
			@RequestParam("accessToken") String accessToken) {
		JwtAccount jwtAccount=JsonWebTokenUtil.parseJwt(accessToken, JsonWebTokenUtil.SECRET_KEY);
		String token = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), String.valueOf(jwtAccount.getAppId()), "token-server",
				ApiUrlConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS512);
		Map<String, Object> data=new HashMap<String,Object>();
		data.put("accessToken", token);
		data.put("expiresIn", ApiUrlConstants.TOKEN_EXPIRE_TIME);
		return new ResponseVo<Map<String, Object>>("0", "success", data);
	}
	
}
