package com.hm.his.framework.cache.redis;


import com.hm.his.framework.utils.SystemConfig;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * redis的管理类
 *
 */
public class JedisManager {

	private ShardedJedisPool shardPool;

	class JedisConfig {
		private JedisConfig() {
			initaize();
		}

		/**
		 * 初始化redis服务
		 */
		private void initaize() {
			System.out.println("redis initaizing ...");

			JedisPoolConfig pConfig = new JedisPoolConfig();
			pConfig.setMaxIdle(SystemConfig.getIntProperty("redis.maxIdle"));
			pConfig.setMaxTotal(SystemConfig.getIntProperty("redis.maxTotal"));
			pConfig.setMinIdle(SystemConfig.getIntProperty("redis.minIdle"));
			pConfig.setTestOnReturn(true);//redis连接归还时检测可用服务的数量是否发生变化，如果发生变化就需要销毁这个连接
			int nodes = SystemConfig.getIntProperty("redis.cluster.count");
			// 单位:毫秒
			Integer poolTimeout = SystemConfig.getIntProperty("redis.pool.timeout");

			List<JedisShardInfo> jdinfoList = new ArrayList<JedisShardInfo>();
			for (int i = 0; i < nodes; i++) {

				String masterHost = SystemConfig.getProperty("redis.cluster.master." + (i + 1) + ".host");
				String masterPort = SystemConfig.getProperty("redis.cluster.master." + (i + 1) + ".port");
				String masterPasswd = SystemConfig.getProperty("redis.cluster.master." + (i + 1) + ".passwd");

//				JedisShardInfo jsi = new JedisShardInfo(masterHost, Integer.valueOf(masterPort), poolTimeout);
//				jsi.setPassword(masterPasswd);
				JedisShardInfo jsi = new JedisShardInfo(masterHost);
				System.out.println((i+1)+" redis host:"+masterHost+" port:"+masterPort+" passwd:"+masterPasswd);
				jdinfoList.add(jsi);
			}

			shardPool = new ShardedJedisPool(pConfig, jdinfoList);
			startRedisMonitor(shardPool);
			System.out.println("redis initialize success");
		}

		private void startRedisMonitor(ShardedJedisPool shardPool) {
			JedisFailMonitor failMonitor = new JedisFailMonitor(shardPool, 5000);
			failMonitor.start();
			System.out.println("redis-fail-monitor thread start...");
		}
	}

	private JedisManager() {
		new JedisConfig();
	}

	private static JedisManager instance = new JedisManager();

	public static JedisManager getInstance() {
		return instance;
	}

	public ShardedJedis getShardedJedis() {
		return shardPool.getResource();
	}

	/**
	 * 正常归还jedis
	 * 
	 * @param jedis
	 */
	public void returnSharedJedis(final ShardedJedis jedis) {

		shardPool.returnResource(jedis);
	}

	/**
	 * jedis出现异常的时候销毁jedis
	 * 
	 * @param jedis
	 */
	public void returnBrokenSharedJedis(final ShardedJedis jedis) {
		shardPool.returnBrokenResource(jedis);
	}
}
