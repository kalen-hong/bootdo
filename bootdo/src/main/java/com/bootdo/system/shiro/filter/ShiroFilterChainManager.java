package com.bootdo.system.shiro.filter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.bootdo.common.redis.shiro.RedisManager;
import com.bootdo.system.service.ApiContentService;
import com.bootdo.system.service.ApiInvokeRecordService;

/**
 * token过滤器配置
 * @author liy
 * @date 2019年10月5日 下午10:06:35
 * @discript
 *
 */
@Component
public class ShiroFilterChainManager {

	private final ApiInvokeRecordService apiInvokeRecordService;
	private final ApiContentService apiContentService;
	private RedisManager redisManager;
	@Autowired
	public ShiroFilterChainManager(ApiInvokeRecordService apiInvokeRecordService,
			ApiContentService apiContentService,RedisManager redisManager) {
		this.apiInvokeRecordService = apiInvokeRecordService;
		this.apiContentService = apiContentService;
		this.redisManager=redisManager;
	}

	// 初始化获取过滤链规则
	public Map<String, String> initGetFilterChain() {
		Map<String, String> filterChain = new LinkedHashMap<>();
		// -------------anon 默认过滤器忽略的URL
		List<String> defalutAnon = Arrays.asList("/files/**", "/upload/**", "/druid/**", "/docs/**", "/img/**", "fonts",
				"/css/**", "/js/**", "/login", "/getVerify", "**/getToken", "**/refreshToken", "**/listOpenApi");
		defalutAnon.forEach(ignored -> filterChain.put(ignored, "anon"));
		// 登陆退出
		filterChain.put("/logout", "logout");
		// -------------auth 默认需要认证过滤器的URL，后台管理系统
		List<String> defalutAuth = Arrays.asList("/activiti/**", "/blog/**", "/common/**", "/oa/**", "/system/**");
		defalutAuth.forEach(auth -> filterChain.put(auth, "anon"));
		List<String> apiAuth = Arrays.asList("/api/**");
		apiAuth.forEach(auth -> filterChain.put(auth, "api"));
		return filterChain;
	}

	// 初始化获取过滤链
	public Map<String, Filter> initGetFilters() {
		Map<String, Filter> filters = new LinkedHashMap<>();
		PasswordFilter passwordFilter = new PasswordFilter();
		filters.put("auth", passwordFilter);
		BJwtFilter jwtFilter = new BJwtFilter();
		jwtFilter.setApiContentService(apiContentService);
		jwtFilter.setApiInvokeRecordService(apiInvokeRecordService);
		jwtFilter.setRedisManager(redisManager);
		filters.put("api", jwtFilter);
		return filters;
	}
}
