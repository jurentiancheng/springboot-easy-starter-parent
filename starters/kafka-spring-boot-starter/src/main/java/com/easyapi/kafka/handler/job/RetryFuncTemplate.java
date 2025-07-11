package com.easyapi.kafka.handler.job;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于有重试需求的远程调用，防止redis，数据库，远程接口重启造成的数据丢失
 */
@Slf4j
public class RetryFuncTemplate {
	private String threadName;
	// 以下均为微秒秒单位
	// 重试等待时间
	private int retryTimeout = 5000;
	
	// 以下均为次数单位
	private int retryLimit = 30;

	// 每次重试是否增加sleep时间
	private int sleepMulti = 2;
	
	public RetryFuncTemplate() {
	}

	public RetryFuncTemplate(String threadName) {
		this.threadName = threadName;
	}
	
	/**
	 * 
	 * @param process
	 * @throws Exception
	 */
	public void processData(IRetryMethod process) throws Exception {
		int failCount = 0;
		while (true) {
			try {
				process.process();
				break;
			} catch (Exception e) {
				if (e instanceof InterruptedException) {
					throw e;
				}
				failCount++;
				if (failCount < retryLimit) {
					log.error("processData error retry: {}, {}, {}", threadName, failCount, e);
					if(sleepMulti == 0) {
						Thread.sleep(retryTimeout);
					} else if (sleepMulti > 0) {
						Thread.sleep(retryTimeout * failCount * sleepMulti);
					}
					continue;
				} else {
					throw e;
				}
			}
		}
	}
	
	public int getRetryTimeout() {
		return retryTimeout;
	}

	public void setRetryTimeout(int retryTimeout) {
		this.retryTimeout = retryTimeout;
	}

	public int getRetryLimit() {
		return retryLimit;
	}

	public void setRetryLimit(int retryLimit) {
		this.retryLimit = retryLimit;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public void setSleepMulti (int sleepMulti) {this.sleepMulti = sleepMulti;}

	public int getSleepMulti () {
		return this.sleepMulti;
	}
}
