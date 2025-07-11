package com.easyapi.jdbc.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.easyapi.jdbc.service.SqlMakerService;
import com.easyapi.jdbc.service.SqlMakerServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/6 7:12 下午
 * @Version 1.0
 */
@Configuration
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@EnableConfigurationProperties(DataSourceConfiguration.class)
public class DataSourceAutoConfig {

    @ConfigurationProperties(prefix = "spring.datasource.druid.first")
    @Bean(name = "first")
    @Primary
    @ConditionalOnClass(DruidDataSourceBuilder.class)
    public DataSource dsDefault(){
        return DruidDataSourceBuilder.create().build();
    }

    @ConfigurationProperties(prefix = "spring.datasource.druid.second")
    @Bean(name = "second")
    @ConditionalOnClass(DruidDataSourceBuilder.class)
    public DataSource dsSystem(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConditionalOnClass({DruidDataSourceBuilder.class, JdbcTemplate.class})
    public JdbcTemplate jdbcTemplateDefault(@Qualifier("first")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnClass({DruidDataSourceBuilder.class, JdbcTemplate.class})
    public JdbcTemplate jdbcTemplateSystem(@Qualifier("second")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnClass({DruidDataSourceBuilder.class, JdbcTemplate.class})
    public SqlMakerService sqlMakerService() {
        return new SqlMakerServiceImpl();
    }

}
