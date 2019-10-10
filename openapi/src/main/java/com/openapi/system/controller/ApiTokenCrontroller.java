package com.openapi.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openapi.common.redis.shiro.RedisManager;
import com.openapi.system.constant.ApiConstants;
import com.openapi.system.constant.RedisConstants;
import com.openapi.system.domain.UserBusinessDO;
import com.openapi.system.exception.ApiAuthenticationException;
import com.openapi.system.service.UserBusinessService;
import com.openapi.system.utils.JsonWebTokenUtil;
import com.openapi.system.vo.JwtAccount;
import com.openapi.system.vo.ResponseVo;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token维护
 * 
 * @title
 * @author liy
 * @date 2019年10月1日
 * @discript
 */
@RestController
@RequestMapping("/api/token")
public class ApiTokenCrontroller {
	protected Logger log = LoggerFactory.getLogger(ApiTokenCrontroller.class);
	@Autowired
	private UserBusinessService userBusinessService;
	@Autowired
	protected RedisManager redisManager;

	@PostMapping("/getToken")
	public ResponseVo<Map<String, Object>> getToken(@RequestParam("clientId") String clientId,
			@RequestParam("clientSecret") String clientSecret) {
		try {
			log.info("获取token信息入参，clientId【" + clientId + "】，clientSecret【" + clientSecret + "】");
			// 验证用户名和密码
			if (!checkClientSecret(clientId, clientSecret)) {
				return new ResponseVo<Map<String, Object>>(ResponseVo.FAIL, "密钥错误", null);
			}
			String token = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), clientId, ApiConstants.TOKEN_ISSUER,
					ApiConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS512);
			saveTokenToRedis(clientId, token);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("accessToken", token);
			data.put("expiresIn", ApiConstants.TOKEN_EXPIRE_TIME);
			return new ResponseVo<Map<String, Object>>(ResponseVo.SUCCESS, "success", data);
		} catch (ApiAuthenticationException e) {
			return new ResponseVo<Map<String, Object>>(ResponseVo.FAIL, e.getMessage(), null);
		} catch (Exception e) {
			return new ResponseVo<Map<String, Object>>(ResponseVo.FAIL, "获取token失败，请重试", null);
		}
	}

	private boolean checkClientSecret(String clientId, String clientSecret) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("clientId", clientId);
		List<UserBusinessDO> list = userBusinessService.list(map);
		if (CollectionUtils.isEmpty(list)) {
			throw new ApiAuthenticationException("用户不存在");
		}
		if(!ApiConstants.ACCESS_USER_STATUS_ENABLED.equals(String.valueOf(list.get(0).getStatus()))) {
			throw new ApiAuthenticationException("账号已被锁定,请联系平台客服");
		}
		return clientSecret.equals(list.get(0).getClientSecret());
	}
	
	private void saveTokenToRedis(String clientId,String token) {
		try {
			String key=String.format(RedisConstants.CLIENT_TOKEN, clientId);
			redisManager.set(key.getBytes(), token.getBytes(), ApiConstants.TOKEN_EXPIRE_TIME.intValue());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

	@PostMapping("/refreshToken")
	public ResponseVo<Map<String, Object>> refreshToken(@RequestParam("accessToken") String accessToken) {
		JwtAccount jwtAccount = JsonWebTokenUtil.parseJwt(accessToken, JsonWebTokenUtil.SECRET_KEY);
		String token = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), String.valueOf(jwtAccount.getAppId()),
				ApiConstants.TOKEN_ISSUER, ApiConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS512);
		saveTokenToRedis(jwtAccount.getAppId(), token);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("accessToken", token);
		data.put("expiresIn", ApiConstants.TOKEN_EXPIRE_TIME);
		return new ResponseVo<Map<String, Object>>("0", "success", data);
	}

}
