package com.easyapi.kafka.handler.job;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于有循环处理的job，包含错误重试，延迟等待，线程退出等操作
 * 处理数据中有sleep处理的不适用，每次循环后有sleep的适用
 */
@Slf4j
public class RetryJobTemplate<T> {
	private String threadName;
	// 以下均为微秒秒单位
	// 每次循环等待时间
	private int loopSleep = 0;
	// 错误等待时间
	private int errTimeout = 10000;
	// 以下均为次数单位
	private int errLimit = 30;
	// 每次重试是否增加sleep时间
	private int sleepMulti = 1;

	public RetryJobTemplate() {
	}

	public RetryJobTemplate(String threadName) {
		this.threadName = threadName;
	}
	
	/**
	 * 同步客流预警
	 */
	public final void start(IGetData<T> get) {
	    this.start(get, null);
	}
	
	public final void startExt(IGetDataExt<T> get) {
	    this.startExt(get, null);
	}
	
	public final void start(IGetData<T> get, IProcessData<T> processe) {
		this.startExt((index) -> {
			return get.getData();
		}, processe);
	}
	
	public final void startExt(IGetDataExt<T> get, IProcessData<T> processe) {
		try {
			int index = 0;
			while (true) {
				int failCount = 0;
				try {
					if (Thread.currentThread().isInterrupted()) {
						log.info("safely exit thread:{}", threadName);
						break;
					}

					log.info("job loop start:{}", threadName);
					T data = get.getData(index++);
					if (data != null && processe != null) {
						log.info("job get data:{}, {}", threadName, JSON.toJSONString(data));
						processe.processData(data);
					}
					
					failCount = 0;
					log.info("job loop end:{}", threadName);
				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						throw (InterruptedException)e;
					}
					
					log.error("Exception:{}, {}, {}", threadName, failCount, e);
					if (failCount < errLimit) {
						failCount++;
					}
					if (sleepMulti == 0) {
						Thread.sleep(errTimeout);
					} else if (sleepMulti > 0){
						Thread.sleep(errTimeout * failCount * sleepMulti);
					}
				}
				if (loopSleep > 0) {
					Thread.sleep(loopSleep);
				}
			}
		} catch (InterruptedException e) {
			log.info("exception : {}, {}", threadName, e);
		}
	}
	
	public int getErrTimeout() {
		return errTimeout;
	}

	public void setErrTimeout(int errTimeout) {
		this.errTimeout = errTimeout;
	}

	public int getErrLimit() {
		return errLimit;
	}

	public void setErrLimit(int errLimit) {
		this.errLimit = errLimit;
	}
	
	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public int getLoopSleep() {
		return loopSleep;
	}

	public void setLoopSleep(int loopSleep) {
		this.loopSleep = loopSleep;
	}
	
	public int getSleepMulti() {
		return sleepMulti;
	}

	public void setSleepMulti(int sleepMulti) {
		this.sleepMulti = sleepMulti;
	}
}
