package com.hm.his.framework.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * @author lijunwei
 *
 */
public class ThreadPoolManager {

	private ThreadPoolManager() {
	}

	public static ExecutorService es = Executors.newFixedThreadPool(15);
	public static ExecutorService timeline = Executors.newFixedThreadPool(15);
}
