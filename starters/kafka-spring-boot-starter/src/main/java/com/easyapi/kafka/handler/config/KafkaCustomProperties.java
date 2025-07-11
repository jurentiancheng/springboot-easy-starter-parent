package com.easyapi.kafka.handler.config;

import lombok.Data;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Properties;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaCustomProperties {

    /**
     * 服务器地址
     */
    private String servers;
    private String autoOffsetReset = "latest";
    private Boolean enableAutoCommit = true;
    private Properties defaultProperties;

    public boolean isKafkaEnabled() {
        return !StringUtils.isEmpty(servers);
    }

    /**
     * consumer不是线程安全，不可以使用@Bean注册，每次新生成
     * @return
     */
    public KafkaConsumer<String, String> defaultConsumer(String groupId) {
        return consumer(groupId, autoOffsetReset, enableAutoCommit);
    }

    public KafkaConsumer<String, String> consumer(String groupId, String autoOffsetReset, Boolean autoCommit) {
        if (!isKafkaEnabled()) {
            return null;
        }
        Properties props = Optional.ofNullable(defaultProperties).map(properties -> new Properties(defaultProperties)).orElse(new Properties());
        props.put("bootstrap.servers", servers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", autoCommit);
        props.put("auto.offset.reset", autoOffsetReset);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<>(props);
    }
}
