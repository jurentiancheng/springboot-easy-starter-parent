package com.easyapi.kafka.handler;

/**
 * @className: ComsumerMessageHandler
 * @description: 消费者消息处理方法
 * @Author: jurentiancheng
 * @date: 2021/6/27
 **/

public interface ComsumerMessageHandler<T> {

     void handlerRequest(T t);
}
