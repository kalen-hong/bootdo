package com.bootdo.system.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 发短信
 * @author liy
 * @date 2019年10月3日 下午2:08:01
 * @discript 模拟后端服务
 *
 */

import com.bootdo.system.shiro.token.ApiInvokeToken;
import com.bootdo.system.utils.JsonWebTokenUtil;
import com.bootdo.system.vo.JwtAccount;
import com.bootdo.system.vo.ResponseVo;
@RestController
@RequestMapping("/api/sms")
public class SendMsgController {
	protected Logger log=LoggerFactory.getLogger(SendMsgController.class);
	
	@GetMapping("/sendMsg")
	public ResponseVo<Map<String, Object>> sendMsg(HttpServletRequest request){
		try {
			String accessToken=request.getParameter("accessToken");
			String url=request.getRequestURL().toString();
			log.info("发送短信入参："+accessToken);
			log.info("发送短信成功");
			Subject subject = SecurityUtils.getSubject();
			if(!subject.isAuthenticated()) {
				JwtAccount account=JsonWebTokenUtil.parseJwt(accessToken,JsonWebTokenUtil.SECRET_KEY);
				if(account.getExpiration().before(new Date())) {
					throw new ShiroException("token已过期");
				}
				ApiInvokeToken token=new ApiInvokeToken(account.getAppId(), accessToken, url);
				subject.login(token);
			}
			return new ResponseVo<Map<String,Object>>(ResponseVo.SUCCESS, "success", null);
		}catch (ShiroException e) {
			return new ResponseVo<Map<String,Object>>(ResponseVo.FAIL, e.getMessage(), null);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return new ResponseVo<Map<String,Object>>(ResponseVo.FAIL, "success", null);
		}
	}
}
