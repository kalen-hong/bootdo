package com.openapi.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class MybatisConfig {
	@Primary
	@Bean(name="openapiFactoryBean")
	public SqlSessionFactoryBean openapiFactoryBean(@Qualifier("openapiDataSource")DataSource dataSource) throws IOException {
		SqlSessionFactoryBean factoryBean=new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().
				getResources("classpath:mybatis/openapi/**/*.xml"));
		return factoryBean;
	}
	
	@Bean
	public MapperScannerConfigurer openapiConfigurer() {
		MapperScannerConfigurer configurer=new MapperScannerConfigurer();
		configurer.setBasePackage("com.openapi.*.dao");
		configurer.setSqlSessionFactoryBeanName("openapiFactoryBean");
		return configurer;
	}
	
	@Bean(name="testFactoryBean")
	public SqlSessionFactoryBean testFactoryBean(@Qualifier("testDataSource")DataSource dataSource) throws IOException {
		SqlSessionFactoryBean factoryBean=new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().
				getResources("classpath:mybatis/test/*.xml"));
		return factoryBean;
	}
	
	@Bean
	public MapperScannerConfigurer testConfigurer() {
		MapperScannerConfigurer configurer=new MapperScannerConfigurer();
		configurer.setBasePackage("com.test.dao");
		configurer.setSqlSessionFactoryBeanName("testFactoryBean");
		return configurer;
	}
}
