package com.easyapi.minio.configure;

import com.easyapi.minio.service.FileService;
import com.easyapi.minio.service.impl.FileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(FileService.class)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {

    @Bean
    public FileService fileService(MinioProperties properties) {
        return  new FileServiceImpl(properties.getEndpoint(), properties.getOutEndpoint(), properties.getAccessKey(), properties.getSecretKey(), properties.getBucket(), properties.getBasePath(), properties.getStaticPath());
    }
}
