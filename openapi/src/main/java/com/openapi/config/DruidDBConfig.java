package com.openapi.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * 数据库配置
 * 
 * @author liy
 * @date 2019年10月19日 下午6:02:17
 * @discript
 *
 */
@Configuration
public class DruidDBConfig {

	@ConfigurationProperties(prefix = "spring.datasource.openapi")
	@Bean(name = "openapiDataSource", initMethod = "init", destroyMethod = "close") // 声明其为Bean实例
	@Primary // 在同样的DataSource中，首先使用被标注的DataSource
	public DataSource openapiDataSource() {
		return new DruidDataSource();
	}
	
	@Primary
	@Bean(name = "openapiTransactionManager")
	public PlatformTransactionManager openapiTransactionManager(@Qualifier("openapiDataSource")DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
	}

	@ConfigurationProperties(prefix = "spring.datasource.test")
	@Bean(name = "testDataSource", initMethod = "init", destroyMethod = "close") // 声明其为Bean实例
	public DataSource testDataSource() {
		return new DruidDataSource();
	}
	
	@Bean(name = "testTransactionManager")
	public PlatformTransactionManager testTransactionManager(@Qualifier("testDataSource")DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		reg.addInitParameter("allow", ""); // 白名单
		return reg;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		filterRegistrationBean.addInitParameter("profileEnable", "true");
		filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
		filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
		filterRegistrationBean.addInitParameter("DruidWebStatFilter", "/*");
		return filterRegistrationBean;
	}
}
