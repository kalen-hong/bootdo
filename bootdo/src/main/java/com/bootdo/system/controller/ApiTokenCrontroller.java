package com.bootdo.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootdo.system.constant.ApiUrlConstants;
import com.bootdo.system.domain.UserBusinessDO;
import com.bootdo.system.service.UserBusinessService;
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
	@Autowired
	private UserBusinessService userBusinessService;
	
	@RequestMapping("/getToken")
	public ResponseVo<Map<String, Object>> getToken(@RequestParam("clientId") String clientId,
			@RequestParam("clientSecret") String clientSecret) {
		log.info("获取token信息入参，clientId【"+clientId+"】，clientSecret【"+clientSecret+"】");
		//验证用户名和密码
		if(!checkClientSecret(clientId, clientSecret)) {
			return new ResponseVo<Map<String, Object>>(ResponseVo.FAIL, "密钥错误", null);
		}
		String token = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), clientId, "token-server",
				ApiUrlConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS512);
		Map<String, Object> data=new HashMap<String,Object>();
		data.put("accessToken", token);
		data.put("expiresIn", ApiUrlConstants.TOKEN_EXPIRE_TIME);
		return new ResponseVo<Map<String, Object>>(ResponseVo.SUCCESS, "success", data);
	}
	
	private boolean checkClientSecret(String clientId,String clientSecret) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("clientId", clientId);
		List<UserBusinessDO> list= userBusinessService.list(map);
		if(CollectionUtils.isEmpty(list)) {
			return false;
		}
		return clientSecret.equals(list.get(0).getClientSecret());
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
