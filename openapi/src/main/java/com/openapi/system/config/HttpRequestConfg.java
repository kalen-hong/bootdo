package com.openapi.system.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * http请求处理
 * @author liy
 * @date 2019年11月16日 下午3:14:58
 * @discript
 *
 */
@Configuration
public class HttpRequestConfg {
	private static final String ALLOW_SPECIAL_CHAR="[]{}";
	@Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        fa.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", ALLOW_SPECIAL_CHAR));
        return fa;
    }
}
