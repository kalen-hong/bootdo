package com.openapi.system.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

import com.openapi.system.domain.UserDO;

/* *
 * @Author tomsun28
 * @Description 
 * @Date 18:00 2018/3/3
 */
@Component
public class PasswordMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
    	UserDO userDO=(UserDO) authenticationInfo.getPrincipals().getPrimaryPrincipal();
    	String credentialString=new String((char[])authenticationToken.getCredentials());
        return authenticationToken.getPrincipal().toString().equals(userDO.getUsername())
                && credentialString.equals(userDO.getPassword());
    }
}
