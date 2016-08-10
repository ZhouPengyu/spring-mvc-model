package com.hm.his.framework.cache.redis;

/**
 * redisLoader接口
 * 用来实现redis服务高并发的接口类
 */
public interface RedisLoader {
	void doJob();
}
