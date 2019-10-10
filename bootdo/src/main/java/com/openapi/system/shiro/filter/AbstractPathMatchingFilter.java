package com.openapi.system.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * 重写过滤链路径匹配规则
 * @author liy
 * @date 2019年10月5日 下午10:11:07
 * @discript
 *
 */
public abstract class AbstractPathMatchingFilter extends AccessControlFilter {

    public AbstractPathMatchingFilter() {

    }

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = this.getPathWithinApplication(request);
        // path: url==method eg: http://api/menu==GET   需要解析出path中的url和httpMethod
        String[] strings = path.split("==");
        if (strings.length <= 1) {
            // 分割出来只有URL
            return this.pathsMatch(strings[0], requestURI);
        } else {
            // 分割出url+httpMethod,判断httpMethod和request请求的method是否一致,不一致直接false
            String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();
            return httpMethod.equals(strings[1].toUpperCase()) && this.pathsMatch(strings[0], requestURI);
        }
    }


    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    protected abstract boolean isAccessAllowed(ServletRequest var1, ServletResponse var2, Object var3) throws Exception;
    
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return this.onAccessDenied(request, response);
    }

    protected abstract boolean onAccessDenied(ServletRequest var1, ServletResponse var2) throws Exception;

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return this.isAccessAllowed(request, response, mappedValue) || this.onAccessDenied(request, response, mappedValue);
    }



    protected void saveRequest(ServletRequest request) {
        WebUtils.saveRequest(request);
    }



}