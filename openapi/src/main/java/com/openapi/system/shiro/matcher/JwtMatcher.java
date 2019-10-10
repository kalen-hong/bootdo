package com.openapi.system.shiro.matcher;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;


/**
 * 
 * @author liy
 * @date 2019年10月7日 下午6:14:46
 * @discript
 *
 */
@Component
public class JwtMatcher implements CredentialsMatcher {


    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        return true;
    }
}
