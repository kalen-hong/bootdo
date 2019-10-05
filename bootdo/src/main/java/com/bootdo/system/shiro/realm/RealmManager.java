package com.bootdo.system.shiro.realm;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bootdo.system.shiro.matcher.JwtMatcher;
import com.bootdo.system.shiro.matcher.PasswordMatcher;
import com.bootdo.system.shiro.token.ApiInvokeToken;

/* *
 * @Author tomsun28
 * @Description realm管理器
 * @Date 17:52 2018/3/3
 */
@Component
public class RealmManager {

    private PasswordMatcher passwordMatcher;
    private JwtMatcher jwtMatcher;
    @Autowired
    public RealmManager(PasswordMatcher passwordMatcher,JwtMatcher jwtMatcher) {
        this.passwordMatcher = passwordMatcher;
        this.jwtMatcher = jwtMatcher;
    }

    public List<Realm> initGetRealm() {
        List<Realm> realmList = new LinkedList<>();
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(passwordMatcher);
        userRealm.setAuthenticationTokenClass(UsernamePasswordToken.class);
        realmList.add(userRealm);
        // ----- jwt
        ApiInvokeRealm apiRealm = new ApiInvokeRealm();
        apiRealm.setCredentialsMatcher(jwtMatcher);
        apiRealm.setAuthenticationTokenClass(ApiInvokeToken.class);
        realmList.add(apiRealm);
        return Collections.unmodifiableList(realmList);
    }
}
