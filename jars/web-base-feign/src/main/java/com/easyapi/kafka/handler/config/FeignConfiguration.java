package com.easyapi.kafka.handler.config;

import com.easyapi.basefeign.encoder.BeanQueryMapEncoder;
import feign.Feign;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @className: FeginConfiguration
 * @description: 配置类
 * @Author: jurentiancheng
 * @date: 2021/7/12
 **/
@Configuration
public class FeignConfiguration {


    /**
     * @Description 替换解析queryMap的类，实现父类中变量的映射
     */
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .queryMapEncoder(new BeanQueryMapEncoder())
                .retryer(Retryer.NEVER_RETRY);
    }



}
