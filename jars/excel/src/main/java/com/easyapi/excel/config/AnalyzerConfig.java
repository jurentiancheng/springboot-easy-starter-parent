package com.easyapi.excel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "excel.analyzer")
@Data
public class AnalyzerConfig {
    /**
     * 大数据量时 每次解析的记录数
     */
    Integer batchSize = 5000;
}
