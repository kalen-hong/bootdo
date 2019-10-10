package com.openapi.common.controller;

import com.openapi.system.domain.UserToken;
import org.springframework.stereotype.Controller;
import com.openapi.common.utils.ShiroUtils;
import com.openapi.system.domain.UserDO;

@Controller
public class BaseController {
	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}