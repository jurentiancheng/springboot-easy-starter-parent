package com.easyapi.kafka.handler.job;

import java.io.IOException;
import java.text.ParseException;

@FunctionalInterface
public interface IGetData<T> {
	T getData() throws IOException, ParseException, InterruptedException;
}
