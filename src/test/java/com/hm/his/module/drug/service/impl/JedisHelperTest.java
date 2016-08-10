package com.hm.his.module.drug.service.impl;


import com.hm.his.framework.cache.redis.CacheCaller;
import com.hm.his.framework.cache.redis.JedisHelper;
import com.hm.his.framework.cache.redis.RedisLoader;
import com.hm.his.framework.utils.Check;
import com.hm.his.framework.utils.ThreadPoolManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-conf.xml" })
public class JedisHelperTest {
	Log log = LogFactory.getLog(JedisHelperTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDelStartWith(){
		JedisHelper.delKeysLike("Login_getFarmeByDoctorId");
	}

	@Test
	public void testZrevrangeByLong() {
		String key;
		long max;
		long min;
		int offset;
		int count;
		Map<Integer, Double> actual;

		key = "JedisHelperTest_testZrevrange";
		JedisHelper.zrem(key, 1);
		JedisHelper.zrem(key, 3);
		JedisHelper.zrem(key, 4);

		max = 1000L;
		min = 300L;
		offset = 0;
		count = 1;
		actual = JedisHelper.zrevrangeByScore(Integer.class, key, max, min, offset, count);
		printResult(actual);

		JedisHelper.zadd(key, 100, 1);
		actual = JedisHelper.zrevrangeByScore(Integer.class, key, max, min, offset, count);
		printResult(actual);

		JedisHelper.zadd(key, 300, 3);
		actual = JedisHelper.zrevrangeByScore(Integer.class, key, max, min, offset, count);
		printResult(actual);

		JedisHelper.zadd(key, 400, 4);
		actual = JedisHelper.zrevrangeByScore(Integer.class, key, max, min, offset, count);
		printResult(actual);
	}

	@Test
	public void testZrevrangeByString() {
		String key;
		String max;
		String min;
		int offset;
		int count;
		Map<Integer, Double> actual;

		key = "JedisHelperTest_testZrevrange";
		JedisHelper.zrem(key, 1);
		JedisHelper.zrem(key, 3);
		JedisHelper.zrem(key, 4);

		max = "+inf";
		min = "300";
		offset = 0;
		count = 1;
		actual = JedisHelper.zrevrangeByScore(Integer.class, key, max, min, offset, count);
		printResult(actual);

		JedisHelper.zadd(key, 100, 1);
		actual = JedisHelper.zrevrangeByScore(Integer.class, key, max, min, offset, count);
		printResult(actual);

		JedisHelper.zadd(key, 300, 3);
		actual = JedisHelper.zrevrangeByScore(Integer.class, key, max, min, offset, count);
		printResult(actual);

		JedisHelper.zadd(key, 400, 4);
		actual = JedisHelper.zrevrangeByScore(Integer.class, key, max, min, offset, count);
		printResult(actual);
	}

	private void printResult(Map<Integer, Double> result) {
		if (Check.notEmpty(result)) {
			System.out.println("------------------ print result (" + result.size() + ")------------------");
			for (Entry<Integer, Double> entry : result.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		} else {
			System.out.println("------------------ print result (0)------------------");
			System.out.println("this is empty list or set");
		}
	}

	@Test
	public void testIncre() {
		String key = "test_incre_key";
		int startNum = 1000;
		int result = JedisHelper.incrby(key, startNum).intValue();
		JedisHelper.expire(key, 5);
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
		}
		System.out.println(JedisHelper.exists(key));
		System.out.println(result);
		System.out.println(JedisHelper.incr(key));
		System.out.println(JedisHelper.get(Integer.class, key));

	}

	@Test
	public void testSetNx23() throws InterruptedException {
		JedisHelper.loadDataToRedis("key111", new RedisLoader() {
			@Override
			public void doJob() {
				System.out.println(Thread.currentThread().getName() + "job1 start....");
				try {
					TimeUnit.SECONDS.sleep(18);
				} catch (InterruptedException e) {
				}
				System.out.println(Thread.currentThread().getName() + "job1 end....");
			}

		});
		JedisHelper.loadDataToRedis("key111", new RedisLoader() {
			@Override
			public void doJob() {
				System.out.println(Thread.currentThread().getName() + "job2 start....");
				try {
					TimeUnit.SECONDS.sleep(18);
				} catch (InterruptedException e) {
				}
				System.out.println(Thread.currentThread().getName() + "job2 end....");
			}

		});
		TimeUnit.SECONDS.sleep(12);
		JedisHelper.loadDataToRedis("key111", new RedisLoader() {
			@Override
			public void doJob() {
				System.out.println(Thread.currentThread().getName() + "job3 start....");
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
				}
				System.out.println(Thread.currentThread().getName() + "job3 end....");
			}

		});
		JedisHelper.loadDataToRedis("key111", new RedisLoader() {
			@Override
			public void doJob() {
				System.out.println(Thread.currentThread().getName() + "job4 start....");
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
				}
				System.out.println(Thread.currentThread().getName() + "job4 end....");
			}

		});
		TimeUnit.SECONDS.sleep(1);
		JedisHelper.loadDataToRedis("key111", new RedisLoader() {
			@Override
			public void doJob() {
				System.out.println(Thread.currentThread().getName() + "job5 start....");
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
				}
				System.out.println(Thread.currentThread().getName() + "job5 end....");
			}

		});
		System.out.println(1);
	}

	@Test
	public void testSetNx232() throws InterruptedException {
		JedisHelper.loadDataToRedis("key111", new RedisLoader() {
			@Override
			public void doJob() {
				System.out.println(Thread.currentThread().getName() + "job1 start....");
				try {
					TimeUnit.SECONDS.sleep(4);
				} catch (InterruptedException e) {
				}
				System.out.println(Thread.currentThread().getName() + "job1 end....");
			}

		});
		System.out.println(1);
	}

	@Test
	public void getD() throws InterruptedException {
		getDDD();
		getDDD();
		getDDD();
		getDDD();
		getDDD();
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
		}
		getDDD2();
		getDDD2();

		try {
			Thread.sleep(225000);
		} catch (InterruptedException e) {
		}
		System.out.println(1);
	}

	private void getDDD() {
		ThreadPoolManager.es.execute(new Runnable() {
			@Override
			public void run() {
				Integer result = JedisHelper.get(Integer.class, "testkey", 100000, new CacheCaller<Integer>() {
					@Override
					public Integer call() {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
						}
						throw new RuntimeException("test msg");
					}
				});
				System.out.println(Thread.currentThread().getName() + "get result:" + result);
			}
		});
	}

	private void getDDD2() {
		ThreadPoolManager.es.execute(new Runnable() {
			@Override
			public void run() {
				Integer result = JedisHelper.get(Integer.class, "testkey", 100000, new CacheCaller<Integer>() {
					@Override
					public Integer call() {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
						}
						return 1024;
					}
				});
				System.out.println(Thread.currentThread().getName() + "get result:" + result);
			}
		});
	}
}
