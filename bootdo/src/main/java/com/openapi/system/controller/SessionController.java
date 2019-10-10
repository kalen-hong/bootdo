package com.openapi.system.controller;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openapi.common.utils.R;
import com.openapi.system.domain.UserOnline;
import com.openapi.system.service.SessionService;

@RequestMapping("/sys/online")
@Controller
public class SessionController {
	@Autowired
	SessionService sessionService;
	
	@RequiresPermissions("sys:online:user")
	@GetMapping()
	public String online() {
		return "system/online/online";
	}

	@RequiresPermissions("sys:online:list")
	@ResponseBody
	@RequestMapping("/list")
	public List<UserOnline> list() {
		return sessionService.list();
	}

	@RequiresPermissions("sys:online:forceLogout")
	@ResponseBody
	@RequestMapping("/forceLogout/{sessionId}")
	public R forceLogout(@PathVariable("sessionId") String sessionId, RedirectAttributes redirectAttributes) {
		try {
			sessionService.forceLogout(sessionId);
			return R.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return R.error();
		}

	}
	
	@RequiresPermissions("sys:online:sessionList")
	@ResponseBody
	@RequestMapping("/sessionList")
	public Collection<Session> sessionList() {
		return sessionService.sessionList();
	}


}
