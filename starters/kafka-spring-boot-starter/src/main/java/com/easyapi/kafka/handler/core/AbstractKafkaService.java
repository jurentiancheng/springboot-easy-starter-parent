package com.easyapi.kafka.handler.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easyapi.kafka.handler.job.RetryJobTemplate;
import com.easyapi.kafka.handler.config.KafkaCustomProperties;
import com.easyapi.kafka.handler.ComsumerMessageHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @className: AbstractKafkaService
 * @description: kafka 消息推送实现
 * @Author: jurentiancheng
 * @date: 2021/6/9
 **/

@Slf4j
@Data
public abstract class AbstractKafkaService<T> implements IKafkaService<T> {

    private final static long timeoutDuration = 60L;

    public KafkaCustomProperties kafkaConfig;

    private KafkaProducer<String, String> producer;

    public String pushTopic;

    public List<String> consumerTopicList;

    private String groupId;

    private boolean consumerCommit;


    @Override
    public void push(T t) {
        if (!this.kafkaConfig.isKafkaEnabled()) {
            log.info("kafka producer is closed");
            return;
        }
        String topic  = this.pushTopic;
        String strPayload = JSON.toJSONString(t);
        log.info("push msg kafka-topic with:{}, message:{}", topic, strPayload);
        producer.send(new ProducerRecord<>(topic , strPayload));
        producer.flush();
    }


    @Override
    public void pop(ComsumerMessageHandler handler){
        if (!this.kafkaConfig.isKafkaEnabled() && CollectionUtils.isNotEmpty(consumerTopicList)) {
            log.info("kafka consumer is closed");
            return;
        }
        KafkaConsumer<String, String> consumer = kafkaConfig.defaultConsumer(groupId);
        if (consumer == null) {
            log.info("kafka consumer is closed");
            return;
        }
        try {
            RetryJobTemplate<JSONObject> job = new RetryJobTemplate<>();
            job.setThreadName("subscribe "+groupId+" kafka ");
            consumer.subscribe(consumerTopicList);
            Duration timeout = Duration.ofSeconds(timeoutDuration);
            job.start(() -> {
                ConsumerRecords<String, String> records = consumer.poll(timeout);
                if (records == null || records.isEmpty()) {
                    return null;
                }
                AtomicReference<JSONObject> json= new AtomicReference<>(new JSONObject());
                records.forEach(record ->{
                    json.set(JSONObject.parseObject(record.value()));
                });
                return json.get();
            }, data -> {
                if (data==null){
                    return;
                }
                log.info("event data {}",data.toJSONString());
                handler.handlerRequest(data);
            });
        } catch (Exception e){
            log.error("kafka consumer data error {} ",e.getMessage());
        }finally {
            if (!consumerCommit){
                consumer.commitAsync();
            }
            consumer.close();
        }
    }

    @Override
    public abstract boolean kafkaEnable();
}
