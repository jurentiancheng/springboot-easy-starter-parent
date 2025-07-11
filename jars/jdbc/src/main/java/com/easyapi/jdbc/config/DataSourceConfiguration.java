package com.easyapi.jdbc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: jurentiancheng
 * @Date: 2021/5/6 7:13 下午
 * @Version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.default")
public class DataSourceConfiguration {
    private String schema = "supreview_etl";

}
