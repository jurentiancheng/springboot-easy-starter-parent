package com.easyapi.kafka.handler.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Properties;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(KafkaClient.class)
@EnableConfigurationProperties(KafkaCustomProperties.class)
public class KafkaCustomAutoConfiguration {

    /**
     * 服务器地址
     */
    @Resource
    private KafkaCustomProperties config;

    private KafkaProducer<String, String> producer;

    /**
     * producer是线程安全，可以使用bean注册
     *
     * @return
     */
    @Bean
    public KafkaProducer<String, String> producer() {
        if (!config.isKafkaEnabled()) {
            return null;
        }
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getServers());
        props.put("enable.idempotence", true);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<String, String>(props);
        return this.producer;
    }

    @PreDestroy
    public void destroy() {
        if (this.producer != null) {
            this.producer.flush();
            this.producer.close();
            log.info("producer closed");
        }
    }
}
