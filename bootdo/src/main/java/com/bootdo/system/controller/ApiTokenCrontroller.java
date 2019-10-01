package com.bootdo.system.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootdo.system.constant.ApiTokenConstants;
import com.bootdo.system.utils.JsonWebTokenUtil;
import com.bootdo.system.vo.JwtAccount;
import com.bootdo.system.vo.ResponseVo;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token维护
 * 
 * @author liy
 *
 */
@RestController
@RequestMapping("api/token")
public class ApiTokenCrontroller {

	@RequestMapping("/getToken")
	public ResponseVo<Map<String, Object>> getToken(@RequestParam("clientId") Long clientId,
			@RequestParam("clientSecret") String clientSecret) {
		// 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
		String token = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), String.valueOf(clientId), "token-server",
				ApiTokenConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS512);
		Map<String, Object> data=new HashMap<String,Object>();
		data.put("accessToken", token);
		data.put("expiresIn", ApiTokenConstants.TOKEN_EXPIRE_TIME);
		return new ResponseVo<Map<String, Object>>("0", "success", data);
	}
	
	@RequestMapping("/refreshToken")
	public ResponseVo<Map<String, Object>> refreshToken(
			@RequestParam("accessToken") String accessToken) {
		// 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
//		String token = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), String.valueOf(clientId), "token-server",
//				ApiTokenConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS512);
//		Map<String, Object> data=new HashMap<String,Object>();
//		data.put("accessToken", token);
//		data.put("expiresIn", ApiTokenConstants.TOKEN_EXPIRE_TIME);
		JwtAccount jwtAccount=JsonWebTokenUtil.parseJwt(accessToken, JsonWebTokenUtil.SECRET_KEY);
		if(new Date().after(jwtAccount.getExpiration())) {
			//token已过期
			
		}
		return new ResponseVo<Map<String, Object>>("0", "success", null);
	}
	
}
