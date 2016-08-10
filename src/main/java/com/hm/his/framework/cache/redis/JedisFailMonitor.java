package com.hm.his.framework.cache.redis;

import redis.clients.jedis.ShardedJedisPool;

public class JedisFailMonitor extends Thread {

	private static final int DEFAULT_INTERVAL = 3 * 1000;

	private final ShardedJedisPool shardPool;
	private long interval = DEFAULT_INTERVAL; // every 3 seconds]
	private volatile boolean stopMonitor = false;
	private boolean running;

	public JedisFailMonitor(ShardedJedisPool shardPool, long interval) {
		super();
		this.shardPool = shardPool;
		this.interval = interval;
	}

	public JedisFailMonitor(ShardedJedisPool shardPool) {
		super();
		this.shardPool = shardPool;
	}

	public void stopMonitor() {
		this.stopMonitor = true;
	}

	@Override
	public void run() {
		this.running = true;
		while (!this.stopMonitor) {
			try {
				Thread.sleep(interval);
				shardPool.selfMonitor();
				shardPool.selfEject();
				shardPool.selfResume();
			} catch (Exception e) {
				System.out.println(e);
				break;
			}
		}
		this.running = false;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public boolean isStopMonitor() {
		return stopMonitor;
	}

	public void setStopMonitor(boolean stopMonitor) {
		this.stopMonitor = stopMonitor;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}