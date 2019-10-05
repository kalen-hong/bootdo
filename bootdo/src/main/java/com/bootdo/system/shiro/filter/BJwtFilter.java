//package com.bootdo.system.shiro.filter;
//
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Stream;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.web.util.WebUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.util.StringUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.bootdo.system.constant.ApiUrlConstants;
//import com.bootdo.system.service.ApiInvokeRecordService;
//import com.bootdo.system.shiro.token.ApiInvokeToken;
//import com.bootdo.system.utils.IpUtil;
//import com.bootdo.system.utils.JsonWebTokenUtil;
//import com.bootdo.system.utils.RequestResponseUtil;
//import com.bootdo.system.vo.Message;
//
//import io.jsonwebtoken.SignatureAlgorithm;
//
///**
// * 第三方api调用过滤器
// * 
// * @author liy
// * @date 2019年10月5日 下午1:22:17
// * @discript
// *
// */
//public class BJwtFilter extends BPathMatchingFilter {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(BJwtFilter.class);
//
//	private StringRedisTemplate redisTemplate;
//
//	private ApiInvokeRecordService apiInvokeRecordService;
//
//	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
//			Object mappedValue) throws Exception {
//		Subject subject = getSubject(servletRequest, servletResponse);
//
//		// 记录api调用记录
//		String accessToken = servletRequest.getParameter("accessToken");
//		String url=((HttpServletRequest) servletRequest).getRequestURL().toString();
//		apiInvokeRecordService.asyncSaveInvokeRecord(accessToken, url);
//
//		// 判断是否为JWT认证请求
//		if ((null != subject && !subject.isAuthenticated()) && isJwtSubmission(servletRequest)) {
////            AuthenticationToken token = createJwtToken(servletRequest);
//			AuthenticationToken token = createApiInvokeToken(servletRequest);
//			try {
//				subject.login(token);
//				//校验api接口是否禁用
////				return this.checkRoles(subject, mappedValue);
//				return true;
//			} catch (AuthenticationException e) {
//
//				// 如果是JWT过期
//				if ("expiredJwt".equals(e.getMessage())) {
//					// 这里初始方案先抛出令牌过期，之后设计为在Redis中查询当前appId对应令牌，其设置的过期时间是JWT的两倍，此作为JWT的refresh时间
//					// 当JWT的有效时间过期后，查询其refresh时间，refresh时间有效即重新派发新的JWT给客户端，
//					// refresh也过期则告知客户端JWT时间过期重新认证
//
//					// 当存储在redis的JWT没有过期，即refresh time 没有过期
//					String appId = JsonWebTokenUtil.parseJwt(accessToken, JsonWebTokenUtil.SECRET_KEY).getAppId();
//					String refreshJwt = redisTemplate.opsForValue().get("JWT-SESSION-" + appId);
//					if (null != refreshJwt && refreshJwt.equals(accessToken)) {
//						// 重新申请新的JWT
//						String newJwt = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), appId, "token-server",
//								ApiUrlConstants.TOKEN_EXPIRE_TIME, null, SignatureAlgorithm.HS512);
//						// 将签发的JWT存储到Redis： {JWT-SESSION-{appID} , jwt}
//						redisTemplate.opsForValue().set("JWT-SESSION-" + appId, newJwt,
//								ApiUrlConstants.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
//						Message message = new Message().ok(1005, "new jwt").addData("jwt", newJwt);
//						RequestResponseUtil.responseWrite(JSON.toJSONString(message), servletResponse);
//						return false;
//					} else {
//						// jwt时间失效过期,jwt refresh time失效 返回jwt过期客户端重新登录
//						Message message = new Message().error(1006, "expired jwt");
//						RequestResponseUtil.responseWrite(JSON.toJSONString(message), servletResponse);
//						return false;
//					}
//
//				}
//				// 其他的判断为JWT错误无效
//				Message message = new Message().error(1007, "error Jwt");
//				RequestResponseUtil.responseWrite(JSON.toJSONString(message), servletResponse);
//				return false;
//
//			} catch (Exception e) {
//				// 其他错误
//				LOGGER.error(IpUtil.getIpFromRequest(WebUtils.toHttp(servletRequest)) + "--JWT认证失败" + e.getMessage(),
//						e);
//				// 告知客户端JWT错误1005,需重新登录申请jwt
//				Message message = new Message().error(1007, "error jwt");
//				RequestResponseUtil.responseWrite(JSON.toJSONString(message), servletResponse);
//				return false;
//			}
//		} else {
//			// 请求未携带jwt 判断为无效请求
//			Message message = new Message().error(1111, "error request");
//			RequestResponseUtil.responseWrite(JSON.toJSONString(message), servletResponse);
//			return false;
//		}
//	}
//
//	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
//		Subject subject = getSubject(servletRequest, servletResponse);
//
//		// 未认证的情况
//		if (null == subject || !subject.isAuthenticated()) {
//			// 告知客户端JWT认证失败需跳转到登录页面
//			Message message = new Message().error(1006, "error jwt");
//			RequestResponseUtil.responseWrite(JSON.toJSONString(message), servletResponse);
//		} else {
//			// 已经认证但未授权的情况
//			// 告知客户端JWT没有权限访问此资源
//			Message message = new Message().error(1008, "no permission");
//			RequestResponseUtil.responseWrite(JSON.toJSONString(message), servletResponse);
//		}
//		// 过滤链终止
//		return false;
//	}
//
//	private boolean isJwtSubmission(ServletRequest request) {
//
//		String jwt = RequestResponseUtil.getHeader(request, "authorization");
//		String appId = RequestResponseUtil.getHeader(request, "appId");
//		return (request instanceof HttpServletRequest) && !StringUtils.isEmpty(jwt) && !StringUtils.isEmpty(appId);
//	}
//
//	private AuthenticationToken createApiInvokeToken(ServletRequest request) {
//		String token = request.getParameter("accessToken");
//		HttpServletRequest req = (HttpServletRequest) request;
//		String url = req.getServletPath();
//		String appId = JsonWebTokenUtil.parseJwt(token, JsonWebTokenUtil.SECRET_KEY).getAppId();
//		return new ApiInvokeToken(appId, token, url);
//	}
//
//	// 验证当前用户是否属于mappedValue任意一个角色
//	private boolean checkRoles(Subject subject, Object mappedValue) {
//		String[] rolesArray = (String[]) mappedValue;
//		return rolesArray == null || rolesArray.length == 0
//				|| Stream.of(rolesArray).anyMatch(role -> subject.hasRole(role.trim()));
//	}
//
//	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
//		this.redisTemplate = redisTemplate;
//	}
//
//	public void setApiInvokeRecordService(ApiInvokeRecordService apiInvokeRecordService) {
//		this.apiInvokeRecordService = apiInvokeRecordService;
//	}
//
//}
