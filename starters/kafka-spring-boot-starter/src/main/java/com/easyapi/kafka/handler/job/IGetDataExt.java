package com.easyapi.kafka.handler.job;

import java.io.IOException;
import java.text.ParseException;

@FunctionalInterface
public interface IGetDataExt<T> {
	T getData(int index) throws IOException, ParseException, InterruptedException;
}
