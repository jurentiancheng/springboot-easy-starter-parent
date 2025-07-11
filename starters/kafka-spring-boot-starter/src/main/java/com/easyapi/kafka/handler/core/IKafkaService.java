package com.easyapi.kafka.handler.core;


import com.easyapi.kafka.handler.ComsumerMessageHandler;

/**
 * @className: IKafkaService
 * @description: kafka实现接口
 * @Author: jurentiancheng
 * @date: 2021/6/9
 **/

public interface IKafkaService<T> {

    /**
     * 推送消息
     * @param t
     */
    void push(T t);

    /**
     * 消费消息
     * @param handler
     */
    void pop(ComsumerMessageHandler handler);

    /**
     * 判断kafka是否可用
     * @return
     */
    boolean kafkaEnable();

}
