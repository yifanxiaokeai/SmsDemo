package com.jiangyu.common.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: 线程池 </p>
 * <p>Description: 
 * 提供获取线程池，并支持现场管理。
 * 最大支持线程数10条，核心线程数5条.
 * 若当前线程小于核心线程数，则不用进行排队，直接开启线程。
 * 若当前线程大于5条，小于10条，则剩余线程进入队列进行等待。
 * </p>
 */
public class ThreadPool {
	private static ThreadPool threadPool;
	private static ThreadPoolExecutor threadPoolExecutor;
	private int CORE_SIZE = 5; // 核心線程數
	private int MAX_SIZE = 10; // 最大線程數
	private long KEEPLIVETIME = 60; // 空閒時間
	private TimeUnit TIMEUNIT = TimeUnit.SECONDS;

	public static ThreadPool newInstance() {
		if (threadPool == null) {
			threadPool = new ThreadPool();
		}

		return threadPool;
	}

	private ThreadPool() {
		threadPoolExecutor = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEPLIVETIME, TIMEUNIT,
				new LinkedBlockingQueue<Runnable>());
	}

	public void execute(Runnable task) {
		threadPoolExecutor.submit(task);
	}

	public void stopAll() {
		threadPoolExecutor.shutdownNow();
	}
}
