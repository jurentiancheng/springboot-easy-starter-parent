package com.easyapi.kafka.handler.job;

@FunctionalInterface
public interface IRetryMethod {
	void process() throws Exception;
}
