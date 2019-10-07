package com.bootdo.system.shiro.realm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.bootdo.common.config.ApplicationContextRegister;
import com.bootdo.system.constant.ApiConstants;
import com.bootdo.system.domain.UserBusinessDO;
import com.bootdo.system.exception.ApiAuthenticationException;
import com.bootdo.system.service.ApiContentService;
import com.bootdo.system.service.UserBusinessService;
import com.bootdo.system.shiro.token.ApiInvokeToken;

/**
 * 后端服务接口调用认证与授权
 * 
 * @author liy
 *
 */
public class ApiInvokeRealm extends AuthorizingRealm {
	/**
	 * 接口调用授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 返回用户能看到禁用的所有url
		ApiContentService apiContentService = ApplicationContextRegister.getBean(ApiContentService.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ApiConstants.API_STATUS_ENABLED);
		Set<String> urlSet = apiContentService.listAllEnabledUrl();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(urlSet);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		ApiInvokeToken apiToken=(ApiInvokeToken) token;
		String clientId =apiToken.getAppId();
		UserBusinessService userBusinessService = ApplicationContextRegister.getBean(UserBusinessService.class);
		UserBusinessDO user = userBusinessService.getByClientId(clientId);
		if (user == null) {
			throw new ApiAuthenticationException("账号不存在");
		}
		if (!ApiConstants.ACCESS_USER_STATUS_ENABLED.equals(String.valueOf(user.getStatus()))) {
			throw new ApiAuthenticationException("账号已被锁定,请联系平台客服");
		}
		return new SimpleAuthenticationInfo(user.getClientId(), user.getClientSecret(), user.getUsername());
	}
	
}
