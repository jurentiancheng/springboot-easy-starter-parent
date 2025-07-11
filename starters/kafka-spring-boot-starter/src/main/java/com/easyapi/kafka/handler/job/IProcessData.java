package com.easyapi.kafka.handler.job;

@FunctionalInterface
public interface IProcessData<T> {
	void processData(T data) throws Exception;
}
