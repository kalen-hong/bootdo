package com.openapi.system.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 发短信
 * @author liy
 * @date 2019年10月3日 下午2:08:01
 * @discript 模拟后端服务
 *
 */

import com.openapi.system.vo.ResponseVo;

import io.jsonwebtoken.JwtException;
@RestController
@RequestMapping("/api/sms")
public class SendMsgController {
	protected Logger log=LoggerFactory.getLogger(SendMsgController.class);
	
	@PostMapping("/sendMsg")
	public ResponseVo<Map<String, Object>> sendMsg(HttpServletRequest request){
		try {
			String accessToken=request.getParameter("accessToken");
			log.info("发送短信入参："+accessToken);
			log.info("发送短信成功");
			return new ResponseVo<Map<String,Object>>(ResponseVo.SUCCESS, "success", null);
		} catch (ShiroException e) {
			return new ResponseVo<Map<String,Object>>(ResponseVo.FAIL, e.getMessage(), null);
		} catch (JwtException e) {
			return new ResponseVo<Map<String,Object>>(ResponseVo.FAIL, "token失效", null);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return new ResponseVo<Map<String,Object>>(ResponseVo.FAIL, "success", null);
		}
	}
}
