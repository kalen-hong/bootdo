package com.openapi.system.shiro.realm;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openapi.system.exception.ApiAuthenticationException;

/* *
 * @Author tomsun28
 * @Description 
 * @Date 21:15 2018/3/3
 */
public class AModularRealmAuthenticator extends ModularRealmAuthenticator {
	private static final Logger log = LoggerFactory.getLogger(AModularRealmAuthenticator.class);
	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {

		assertRealmsConfigured();
		List<Realm> realms = this.getRealms().stream().filter(realm -> {
			return realm.supports(authenticationToken);
		}).collect(toList());
		return realms.size() == 1 ? this.doSingleRealmAuthentication(realms.iterator().next(), authenticationToken)
				: this.doMultiRealmAuthentication(realms, authenticationToken);

	}
	
	/**
	 * 重写的目的是，多数据源realm认证时,抛出的shiroException被shiro自动捕捉
	 */
	@Override
	protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {

		AuthenticationStrategy strategy = getAuthenticationStrategy();

		AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

		if (log.isTraceEnabled()) {
			log.trace("Iterating through {} realms for PAM authentication", realms.size());
		}

		for (Realm realm : realms) {

			aggregate = strategy.beforeAttempt(realm, token, aggregate);

			if (realm.supports(token)) {

				log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);

				AuthenticationInfo info = null;
				Throwable t = null;
				try {
					info = realm.getAuthenticationInfo(token);
				}  catch (ApiAuthenticationException e) {
					throw e;
				} catch (Throwable throwable) {
					t = throwable;
					if (log.isWarnEnabled()) {
						String msg = "Realm [" + realm
								+ "] threw an exception during a multi-realm authentication attempt:";
						log.warn(msg, t);
					}
				}

				aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);

			} else {
				log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
			}
		}

		aggregate = strategy.afterAllAttempts(token, aggregate);

		return aggregate;
	}
}
