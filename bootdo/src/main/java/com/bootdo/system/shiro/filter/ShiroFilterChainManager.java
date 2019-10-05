package com.bootdo.system.shiro.filter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.bootdo.system.service.ApiContentService;
import com.bootdo.system.service.ApiInvokeRecordService;

/* *
 * @Author tomsun28
 * @Description Filter 管理器
 * @Date 11:16 2018/2/28
 */
@Component
public class ShiroFilterChainManager {

	private final StringRedisTemplate redisTemplate;
	private final ApiInvokeRecordService apiInvokeRecordService;
	private final ApiContentService apiContentService;

	@Autowired
	public ShiroFilterChainManager(StringRedisTemplate redisTemplate, ApiInvokeRecordService apiInvokeRecordService,
			ApiContentService apiContentService) {
		this.redisTemplate = redisTemplate;
		this.apiInvokeRecordService = apiInvokeRecordService;
		this.apiContentService = apiContentService;
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
		List<String> defalutAuth = Arrays.asList("/activiti/**","/blog/**","/common/**","/oa/**","/system/**");
		defalutAuth.forEach(auth -> filterChain.put(auth, "anon"));
		List<String> apiAuth = Arrays.asList("/api/**");
		apiAuth.forEach(auth -> filterChain.put(auth, "anon"));
		// -------------dynamic 动态URL
//        if (shiroFilterRulesProvider != null) {
//            List<RolePermRule> rolePermRules = this.shiroFilterRulesProvider.loadRolePermRules();
//            if (null != rolePermRules) {
//                rolePermRules.forEach(rule -> {
//                    StringBuilder Chain = rule.toFilterChain();
//                    if (null != Chain) {
//                        filterChain.putIfAbsent(rule.getUrl(),Chain.toString());
//                    }
//                });
//            }
//        }
		return filterChain;
	}
}
