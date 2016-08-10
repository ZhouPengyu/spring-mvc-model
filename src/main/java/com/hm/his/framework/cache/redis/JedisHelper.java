package com.hm.his.framework.cache.redis;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.utils.Check;
import com.hm.his.framework.utils.ThreadPoolManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.*;

import java.util.*;
import java.util.Map.Entry;

public class JedisHelper {

	private static Logger logger = Logger.getLogger(JedisHelper.class);
	private static Cache jobCache = CacheManager.getInstance().getCache("redis_job_list");

	private static int sleepTime = 300; // 默认sleep时间 300毫秒
	private static int sleepMaxCount = 3; // 默认等待次数
	private static final int TIME_OUT = 10000;//毫秒
	private static final byte DIS_LOCK = 1;

	public static String zMinScore = "-inf"; // 有序集(Sorted set) 的score的最小值
	public static String zMaxScore = "+inf"; // 有序集(Sorted set) 的score的最大值

	private static JedisManager jm = JedisManager.getInstance();

	/**
	 * 根据传入的key值，往redis缓存中存放key值对应的缓存元素
	 * @param k key值
	 */
	public static void addjob(String k) {
		Element e = new Element(k, k);
		jobCache.put(e);
	}

	/**
	 * 根据传入的key值，从redis缓存中删除对应key值的缓存元素
	 * @param k key值
	 */
	public static void removeJob(String k) {
		jobCache.remove(k);
	}

	/**
	 * 根据传入的key值，检查redis缓存中对应key值的缓存元素是否存在
	 * @param k key值
	 * @return true:存在; false:不存在
	 */
	public static boolean checkkey(String k) {
		Element e = jobCache.get(k);
		if (null != e) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *根据传入的key值和缓存对象，往redis缓存中添加key值指向的缓存元素
	 * @param key key值
	 * @param o 缓存对象
	 */
	public static void set(String key, Object o) {
		String json = JSON.toJSONString(o);

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.set(key, json);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的key值、过期时间和缓存对象，往redis缓存中存放key值指向的缓存元素
	 * @param key key值
	 * @param expires 缓存过期时间
	 * @param o 缓存对象
	 */
	public static void set(String key, int expires, Object o) {
		String json = JSON.toJSONString(o);

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Transaction t = jedis.getShard(key).multi();
			t.set(key, json);
			t.expire(key, expires);
			t.exec();

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的key值、缓存过期时间和缓存对象，检查对应key值指向的缓存元素是否已经存在<br>
	 * 如果存在，不做任何操作；否则，就往缓存中存放改key值的缓存元素
	 * @param key key值
	 * @param expires 缓存过期时间
	 * @param o 缓存对象
	 * @return int数值[0:操作失败; 1:操作成功]
	 */
	public static int setnx(String key, int expires, Object o) {
		ShardedJedis jedis = null;
		String value = JSON.toJSONString(o);
		Long result = 0L;
		try {
			jedis = jm.getShardedJedis();
			Transaction t = jedis.getShard(key).multi();
			t.expire(key, expires);
			Response<Long> response = t.setnx(key, value);
			t.exec();
			if (response != null) {
				result = response.get();
			}
			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0;
		}
		if (result == null) {
			return 0;
		}
		return result.intValue();
	}

	public static int setnx(String key, Object o) {
		ShardedJedis jedis = null;
		String value = JSON.toJSONString(o);
		Long result = 0L;
		try {
			jedis = jm.getShardedJedis();
			result = jedis.setnx(key, value);
			jm.returnSharedJedis(jedis);
			return result.intValue();
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0;
		}
	}

	/**
	 * 根据传入的对象类型、key值和缓存对象，从redis缓存中取出key值对应的缓存元素，并且为key值设置传入的新缓存元素
	 * @param clazz 对象类型
	 * @param key key值
	 * @param o 新的缓存元素
	 * @return 旧的缓存元素
	 */
	public static <T> T getSet(Class<T> clazz, String key, Object o) {
		ShardedJedis jedis = null;
		try {
			String value = null;
			if (!(o instanceof String)) {
				value = JSON.toJSONString(o);
			} else {
				value = (String) o;
			}
			jedis = jm.getShardedJedis();
			String json = jedis.getSet(key, value);
			jm.returnSharedJedis(jedis);

			if (StringUtils.isNotEmpty(json)) {
				T t = JSON.parseObject(json, clazz);
				return t;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return null;
		}
	}

	/**
	 * 根据传入的key值和redis回调对象，后台另行处理该redisLoader对象
	 * @param lockKey 加锁了的key值
	 * @param loader redisloader对象
	 */
	public static void loadDataToRedis(final String lockKey, final RedisLoader loader) {
		ThreadPoolManager.es.execute(new Runnable() {
			@Override
			public void run() {
				doJobWithRedisDistributedLock(lockKey, loader);
			}
		});
	}

	/**
	 * 工具方法，供loadDataToRedis()使用(该方法主要是为了解决redis缓存中高并发时的缓存数据错乱的)
	 * @param lockKey key值
	 * @param loader redisloader对象
	 */
	public static void doJobWithRedisDistributedLock(final String lockKey, final RedisLoader loader) {
		long timeStamp = getCurrentTime() + TIME_OUT + 1;
		int lock = setnx(lockKey, timeStamp);
		if (lock == 1) {
			JedisHelper.expire(lockKey, TIME_OUT / 1000);
			try {
				loader.doJob();
			} catch (Exception e) {
				logger.error("doJob异常：", e);
			} finally {
				if (getCurrentTime() < timeStamp) {
					del(lockKey);
				}
			}
			return;
		}
	}

	/**
	 * 使用分布锁解决只允许单任务执行问题，当多任务并行调用时，只有第一个获得锁任务可被执行，<br/>
	 * 后续被锁住任务无法执行，会等待，直到任务判断成功，或重试结束.<br/>
	 * 注意任务判断回调必须引入共享状态，如redis，memcache等<br/>
	 * @param lockKey key值
	 * @param job redisJob对象
	 * @return 任务是否执行成功
	 */
	public static boolean doJobUntilSuccess(String lockKey, RedisJob job) {
		long timeStamp = getCurrentTime() + TIME_OUT + 1;
		int lock = setnx(lockKey, timeStamp);
		if (lock == 1) {
			JedisHelper.expire(lockKey, TIME_OUT / 1000);
			try {
				job.doJob();
				return true;
			} catch (Exception e) {
				logger.error("doJob异常：", e);
				return false;
			} finally {
				if (getCurrentTime() < timeStamp) {
					del(lockKey);
				}
			}
		}

		try {
			int tryCount = sleepMaxCount;
			while (tryCount-- > 0) {
				Thread.sleep(sleepTime);

				boolean isSuccessful = job.isJobSuccessful();
				if (isSuccessful) {
					return true;
				}

			}
			return false;
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	private static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 根据传入的key值，判断redis缓存中对应的key值的缓存元素是否存在
	 * @param key key值
	 * @return true:存在; false:不存在
	 */
	public static boolean exists(String key) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			boolean result = jedis.exists(key);
			jm.returnSharedJedis(jedis);
			return result;
		} catch (Exception e) {
			//logger.error(e);
			System.out.println(e.getStackTrace());
			jm.returnBrokenSharedJedis(jedis);
			return false;
		}
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * @param key KEY值
	 * @param seconds 过期时间，单位：秒
	 */
	public static void expire(String key, int seconds) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.expire(key, seconds);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的对象类型和key值，获取redis缓存中中对应key值指向的缓存元素
	 * @param clazz 对象类型
	 * @param key key值
	 * @return clasz类型指向的缓存对象（泛型机制，下同）
	 */
	public static <T> T get(Class<T> clazz, String key) {
		String json = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			json = jedis.get(key);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}

		T t = JSON.parseObject(json, clazz);
		return t;
	}

	/**
	 * 根据传入的对象类型和key值，获取redis缓存中对应key值指向的缓存元素
	 * @param clazz 对象类型
	 * @param key key值
	 * @return clasz类型指向的缓存对象集合
	 */
	public static <T> List<T> getList(Class<T> clazz, String key) {
		String json = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			json = jedis.get(key);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}

		List<T> list = JSON.parseArray(json, clazz);
		return list;
	}

	/**
	 * 根据传入的对象类型、key值和缓存对象，从redis缓存中获取key值对应缓存元素<br>
	 * 如果key值对应的缓存元素存在，则返回；如果不存在，则从缓存对象中重新获取并保存到redis缓存中
	 * @param clazz 对象类型
	 * @param key key值
	 * @param expires 过期时间
	 * @param caller 缓存对象
	 * @return clasz类型对应的对象
	 */
	public static <T> T get(Class<T> clazz, String key, int expires, CacheCaller<T> caller) {
		if (key == null) {
			return null;
		}

		T value = JedisHelper.get(clazz, key);
		if (value != null) {
			return value;
		}
		String lockKey = "dis_lock_" + key;

		long timeStamp = getCurrentTime() + TIME_OUT + 1;
		int lock = setnx(lockKey, DIS_LOCK);
		if (lock != 1) {
			try {
				int tryCount = sleepMaxCount;
				while (tryCount-- > 0) {
					Thread.sleep(sleepTime);
					value = JedisHelper.get(clazz, key);
					if (null != value) {
						return value;
					}
				}
//				return caller.call();
			} catch (Exception e) {
				logger.error(e);
			}
		}
		//get lock
		JedisHelper.expire(lockKey, TIME_OUT / 1000);
		try {
			value = caller.call();
			if (value != null) {
				JedisHelper.set(key, expires, value);
			}
		} catch (Exception e) {
			logger.error("get call 异常", e);
		} finally {
			if (getCurrentTime() < timeStamp) {
				del(lockKey);
			}
		}
		return value;
	}

	/**
	 * 根据传入的对象类型、key值和缓存对象，从redis缓存中获取key值对应缓存元素<br>
	 * 如果key值对应的缓存元素存在，则返回；如果不存在，则从缓存对象中重新获取并保存到redis缓存中
	 * @param clazz 对象类型
	 * @param key key值
	 * @param expires 过期时间
	 * @param caller 缓存对象
	 * @return clasz类型对应的对象集合
	 */
	public static <T> List<T> getList(Class<T> clazz, String key, int expires, CacheCaller<List<T>> caller) {
		List<T> list = null;

		if (StringUtils.isNotEmpty(key)) {
			list = JedisHelper.getList(clazz, key);

			// 有缓存数据
			if (null != list) {
				return list;
			}
			// 没有缓存数据
			else {
				// 已经有任务在执行
				if (JedisHelper.checkkey(key)) {
					int c = sleepMaxCount;
					while (c > 0) {
						try {
							Thread.sleep(sleepTime);
						} catch (Exception e) {
							logger.error(e);
						}
						c--;
						list = JedisHelper.getList(clazz, key);
						if (null != list) {
							return list;
						}
					}
					return null;
				}
				// 没有任务在执行
				else {
					JedisHelper.addjob(key);
					try {
						list = caller.call();
						if (null != list) {
							JedisHelper.set(key, expires, list);
							return list;
						}
					} catch (Exception e) {
						logger.error(e);
					} finally {
						JedisHelper.removeJob(key);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据传入的key值，为redis缓存中该key值对应的缓存对象加1（该缓存对象必须是整数类型）<br>
	 * 如果key对应的缓存对象不存在，那就先初始化为0，然后再执行incr操作
	 * @param key key值
	 * @return 执行incr之后的缓存对象结果
	 */
	public static Long incr(String key) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Long l = jedis.incr(key);

			jm.returnSharedJedis(jedis);
			return l;
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0L;
		}
	}

	/**
	 * 根据传入的key值和指定的操作数，为redis缓存中该key值对应的缓存对象加increment（increment可以是负数）<br>
	 * 如果key对应的缓存对象不存在，那就先初始化为0，然后再执行incrby操作
	 * @param key key值
	 * @param increment 操作数
	 * @return 执行incr之后的缓存对象结果
	 */
	public static Long incrby(String key, int increment) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Long l = jedis.incrBy(key, increment);
			jm.returnSharedJedis(jedis);
			return l;
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0L;
		}
	}

	/**
	 * 根据传入的key值和指定的操作数，为redis缓存中该key值对应的缓存对象加increment（increment可以是负数）<br>
	 * 如果key对应的缓存对象不存在，那就先初始化为0，然后再执行incrby操作
	 * @param key key值
	 * @param increment 操作数
	 * @param expireSecond 过期时间
	 * @return 执行incr之后的缓存对象结果
	 */
	public static Long incrby(String key, int increment, int expireSecond) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Long l = jedis.incrBy(key, increment);
			jedis.expire(key, expireSecond);
			jm.returnSharedJedis(jedis);
			return l;
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0L;
		}
	}

	/**
	 * 根据传入的key值，为redis缓存中该key值对应的缓存对象进行减1操作(该缓存对象一定要是整数值)
	 * @param key key值
	 * @return 执行decr操作后的缓存结果
	 */
	public static Long decr(String key) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Long l = jedis.decr(key);

			jm.returnSharedJedis(jedis);
			return l;
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0L;
		}
	}

	/**
	 * 根据传入的key值和指定的操作数，为redis缓存中该key值对应的缓存对象进行减decrease操作
	 * @param key key值
	 * @param increment 减操作数
	 * @return 执行decr操作后的缓存结果
	 */
	public static Long decrby(String key, int increment) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Long l = jedis.decrBy(key, increment);

			jm.returnSharedJedis(jedis);
			return l;
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0L;
		}
	}

	/**
	 * 根据传入的key值和指定的操作数，为redis缓存中该key值对应的缓存对象进行减decrease操作
	 * @param key key值
	 * @param increment 减操作数
	 * @param expireSecond 过期时间
	 * @return 执行decr操作后的缓存结果
	 */
	public static Long decrby(String key, int increment, int expireSecond) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Long l = jedis.decrBy(key, increment);
			jedis.expire(key, expireSecond);
			jm.returnSharedJedis(jedis);
			return l;
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0L;
		}
	}

	/**
	 * 根据传入的key值，删除redis缓存中对应的缓存对象
	 * @param key key值
	 */
	public static void del(String key) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.del(key);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的在 某字符开头的值 ，删除redis缓存中对应的缓存对象
	 * @param likeKey 某字符开头
	 */
	public static long delKeysLike(String likeKey) {

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Collection<Jedis> allShards = jedis.getAllShards();
			Iterator<Jedis> iters = allShards.iterator();
			long count = 0;
			while (iters.hasNext()){
				Jedis _jedis = iters.next();
				Set<String> keys = _jedis.keys(likeKey + "*");
				if(CollectionUtils.isNotEmpty(keys))
					count += _jedis.del(keys.toArray(new String[keys.size()]));
			}
			jm.returnSharedJedis(jedis);
			return count;
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
		return 0;
	}

	/**
	 * 根据传入的key值和对应的属性列表，删除redis缓存中的hash表类型的缓存元素
	 * @param key key值
	 * @param fields key值对应的属性列表
	 */
	public static void hdel(String key, String... fields) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.hdel(key, fields);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value<br>
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作<br>
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖
	 * @param key key值
	 * @param field 哈希表的域
	 * @param o 缓存值
	 */
	public static void hset(String key, String field, Object o) {
		String json = JSON.toJSONString(o);

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.hset(key, field, json);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 将哈希表 key 中的域hash<br>
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作<br>
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖
	 * @param key
	 * @param hash
	 */
	public static void hmset(String key, Map<String, String> hash) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.hmset(key, hash);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment<br>
	 * 增量也可以为负数，相当于对给定域进行减法操作。<br>
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。<br>
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。<br>
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。<br>
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。<br>
	 * @param key key值
	 * @param field 哈希表的域
	 * @param increment 增量
	 */
	public static Long hincrBy(String key, String field, long increment) {
		ShardedJedis jedis = null;
		Long value = null;
		try {
			jedis = jm.getShardedJedis();
			value = jedis.hincrBy(key, field, increment);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}

		return value;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value，并设置了一个缓存过期时间<br>
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作<br>
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖
	 * @param key key值
	 * @param field 哈希表的域
	 * @param expire 过期时间
	 * @param o 缓存值
	 */
	public static void hset(String key, String field, int expire, Object o) {
		String json = JSON.toJSONString(o);

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Transaction t = jedis.getShard(key).multi();
			t.hset(key, field, json);
			t.expire(key, expire);
			t.exec();

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的对象类型、key值和域值，获取哈希表中key值中域为field的缓存元素
	 * @param clazz 对象类型
	 * @param key key值
	 * @param field 域值
	 * @return 缓存元素
	 */
	public static <T> T hget(Class<T> clazz, String key, String field) {
		String json = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			json = jedis.hget(key, field);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}

		T t = JSON.parseObject(json, clazz);
		return t;
	}

	/**
	 * 根据传入的对象类型和哈希key值，获取哈希key值下的所有域对应的缓存元素
	 * @param valueClazz 对象类型
	 * @param key key值
	 * @return 缓存元素Map
	 */
	public static <T> Map<String, T> hgetAll(Class<T> valueClazz, String key) {
		Map<String, String> json = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			json = jedis.hgetAll(key);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}

		Map<String, T> map = new HashMap<String, T>();
		for (Entry<String, String> item : json.entrySet()) {
			T t = JSON.parseObject(item.getValue(), valueClazz);
			map.put(item.getKey(), t);
		}
		return map;
	}

	/**
	 * 根据传入的对象类型、key值、域值、过期时间和缓存对调对象，获取哈希表中key值中域为field的缓存元素<br>
	 * 如果对应的缓存元素存在，则返回；否则，就调用缓存对象重新获取数据并保存到缓存中去
	 * @param clazz 对象类型
	 * @param key key值
	 * @param field 域值
	 * @param expires 过期时间
	 * @param caller 缓存回调对象
	 * @return 缓存元素
	 */
	public static <T> T hget(Class<T> clazz, String key, String field, int expires, CacheCaller<T> caller) {
		T t = null;

		if (StringUtils.isNotEmpty(key)) {
			t = JedisHelper.hget(clazz, key, field);

			// 有缓存数据
			if (null != t) {
				return t;
			}
			// 没有缓存数据
			else {
				// 已经有任务在执行
				if (JedisHelper.checkkey(key)) {
					int c = sleepMaxCount;
					while (c > 0) {
						try {
							Thread.sleep(sleepTime);
						} catch (Exception e) {
							logger.error(e);
						}
						c--;
						t = JedisHelper.hget(clazz, key, field);
						if (null != t) {
							return t;
						}
					}
					return null;
				}
				// 没有任务在执行
				else {
					JedisHelper.addjob(key);
					try {
						t = caller.call();
						if (null != t) {
							JedisHelper.hset(key, field, expires, t);
							return t;
						}
					} catch (Exception e) {
						logger.error(e);
					} finally {
						JedisHelper.removeJob(key);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据传入的key值、过期时间和对象列表，往redis缓存中push进key对应的对象列表(从表头开始往列表中存放)
	 * @param key key值
	 * @param os 对象列表
	 */
	public static void lpush(String key, Object... os) {
		String[] jsons = new String[os.length];
		for (int i = 0; i < jsons.length; i++) {
			jsons[i] = JSON.toJSONString(os[i]);
			ShardedJedis jedis = null;
			try {
				jedis = jm.getShardedJedis();
				jedis.lpush(key, jsons[i]);

				jm.returnSharedJedis(jedis);
			} catch (Exception e) {
				logger.error(e);
				jm.returnBrokenSharedJedis(jedis);
			}
		}
	}

	/**
	 * 根据传入的key值、过期时间和对象列表，往redis缓存中push进key对应的对象列表(从表头开始往列表中存放)
	 * @param key key值
	 * @param expires 缓存过期时间
	 * @param os 对象列表
	 */
	public static void lpush(String key, int expires, Object... os) {
		String[] jsons = new String[os.length];
		for (int i = 0; i < jsons.length; i++) {
			jsons[i] = JSON.toJSONString(os[i]);
		}

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Transaction t = jedis.getShard(key).multi();
			for (String json : jsons) {
				t.lpush(key, json);
			}
			t.expire(key, expires);
			t.exec();

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的key值和对象列表，往redis缓存的列表类型中存放key对应的列表对象（从列表的尾部开始）
	 * @param key key值
	 * @param os 对象列表
	 */
	public static void rpush(String key, Object... os) {
		String[] jsons = new String[os.length];
		for (int i = 0; i < jsons.length; i++) {
			jsons[i] = JSON.toJSONString(os[i]);
			ShardedJedis jedis = null;
			try {
				jedis = jm.getShardedJedis();
				jedis.rpush(key, jsons[i]);

				jm.returnSharedJedis(jedis);
			} catch (Exception e) {
				logger.error(e);
				jm.returnBrokenSharedJedis(jedis);
			}
		}

	}

	/**
	 * 根据传入的key值和对象列表，往redis缓存的列表类型中存放key对应的列表对象（从列表的尾部开始）
	 * @param key key值
	 * @param list 对象列表
	 */
	public static void rpush(String key, List<? extends Object> list) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			for (Object obj : list) {
				jedis.rpush(key, obj.toString());
			}

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。<br>
	 * count 的值可以是以下几种：<br>
	 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。<br>
	 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。<br>
	 * count = 0 : 移除表中所有与 value 相等的值。<br>
	 * @param key key值
	 * @param count
	 * @param value 待移除的值
	 */
	public static void lrem(String key, long count, Object value) {
		ShardedJedis jedis = null;

		try {
			jedis = jm.getShardedJedis();
			String v = JSON.toJSONString(value);
			jedis.lrem(key, count, v);
			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);

		}
	}

	/**
	 * 根据传入的key值，获取列表的元素个数，即列表的长度
	 * @param key key值
	 * @return 列表的长度
	 */
	public static long llen(String key) {
		ShardedJedis jedis = null;

		try {
			jedis = jm.getShardedJedis();
			long length = jedis.llen(key);
			jm.returnSharedJedis(jedis);
			return length;
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return 0L;
		}
	}

	/**
	 * 根据传入的key值，对列表进行修剪，按照start指定的起始位置开始，修剪到end指定的结束位置
	 * @param key key值
	 * @param start 起始位置
	 * @param end 结束位置
	 */
	public static void ltrim(String key, long start, long end) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.ltrim(key, start, end);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
	 * @param clazz 对象类型
	 * @param key key值
	 * @param start 偏移量开始的位置
	 * @param end 偏移量结束的位置
	 * @return 获取的区间元素
	 */
	public static <T> List<T> lrange(Class<T> clazz, String key, long start, long end) {
		List<T> list = new ArrayList<T>();
		List<String> sList = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			sList = jedis.lrange(key, start, end);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}

		if (Check.notEmpty(sList)) {
			for (String str : sList) {
				T t = JSON.parseObject(str, clazz);
				list.add(t);
			}
		}
		return list;
	}

	/**
	 * 根据传入的key值，往指定的Index位置，插入value值
	 * @param key key值
	 * @param index 插入的位置
	 * @param value 插入的值
	 */
	public static void lset(String key, long index, Object value) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.lset(key, index, JSON.toJSONString(value));

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的key值，获取index指定的位置的缓存元素
	 * @param clazz 对象类型
	 * @param key key值
	 * @param index 指定的位置
	 * @return 缓存元素
	 */
	public static <T> T lindex(Class<T> clazz, String key, int index) {
		ShardedJedis jedis = null;
		String str = null;
		try {
			jedis = jm.getShardedJedis();
			str = jedis.lindex(key, index);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}

		return JSON.parseObject(str, clazz);
	}

	/**
	 * 返回有序集 key 中所有成员的数量
	 * @param key key值
	 * @return 有序集成员数量
	 */
	public static int zcount(String key) {
		int count = 0;
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			count = jedis.zcount(key, zMinScore, zMaxScore).intValue();
			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);

		}
		return count;
	}

	/**
	 * 根据传入的key值，往有序集中添加scoreObjects列表里面的对象，并且按照对象里面的score进行有序的排序
	 * @param key key值
	 * @param scoreObjects 带有score值的对象列表
	 */
	public static void zadd(String key, Map<? extends Object, Double> scoreObjects) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Transaction t = jedis.getShard(key).multi();
			for (Entry<? extends Object, Double> enrty : scoreObjects.entrySet()) {
				String json = JSON.toJSONString(enrty.getKey());
				Double score = enrty.getValue();
				t.zadd(key, score, json);
			}
			t.exec();

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的key值和过期时间，往有序集中添加scoreObjects列表里面的对象，并且按照对象里面的score进行有序的排序
	 * @param key key值
	 * @param expire 过期时间
	 * @param scoreObjects 带有score的对象集合
	 */
	public static void zadd(String key, int expire, Map<? extends Object, Double> scoreObjects) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			if(null != scoreObjects){
				Transaction t = jedis.getShard(key).multi();
				for (Entry<? extends Object, Double> enrty : scoreObjects.entrySet()) {
					String json = JSON.toJSONString(enrty.getKey());
					Double score = enrty.getValue();
					t.zadd(key, score, json);
				}
				t.expire(key, expire);
				t.exec();
	
				jm.returnSharedJedis(jedis);
			}
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的key值、过期时间和score值，往有序集中添加对应的对象o，并按照score自动进行排序
	 * @param key key值
	 * @param expire 过期时间
	 * @param score 排序的score值
	 * @param o 添加的缓存对象
	 */
	public static void zadd(String key, int expire, double score, Object o) {
		String json = JSON.toJSONString(o);

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.zadd(key, score, json);

			Transaction t = jedis.getShard(key).multi();
			t.zadd(key, score, json);
			t.expire(key, expire);
			t.exec();

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的key值score值，往有序集中添加对应的对象o，并按照score自动进行排序
	 * @param key key值
	 * @param score 排序的score值
	 * @param o 添加的缓存对象
	 */
	public static void zadd(String key, double score, Object o) {
		String json = JSON.toJSONString(o);

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.zadd(key, score, json);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 返回有序集 key 的基数
	 * @param key key值
	 * @return 基数值
	 */
	public static long zcard(String key) {
		long count = 0;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			count = jedis.zcard(key);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
		return count;
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量
	 * @param key key值
	 * @param min 最小值(可以等于)
	 * @param max 最大值(可以等于)
	 * @return 数量值
	 */
	public static long zcount(String key, String min, String max) {
		long count = 0;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			count = jedis.zcount(key, min, max);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
		return count;
	}

	/**
	 * 删除key中指定的成员o
	 * @param key key值
	 * @param o 待删除的成员对象
	 */
	public static void zrem(String key, Object o) {
		String json = JSON.toJSONString(o);
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.zrem(key, json);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 移除有序集 key 中，指定排名(rank)区间内的所有成员<br>
	 * 区间分别以下标参数 start 和 end 指出，包含 start 和 end 在内
	 * @param key key值
	 * @param start 起始下标
	 * @param end 结束下标
	 */
	public static void zremrangebyrank(String key, int start, int end) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.zremrangeByRank(key, start, end);
			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 用于判断member是否在zset列表中
	 * @param key zset缓存key值
	 * @param member zset列表中某条记录的索引
	 * @return true存在；false不存在
	 * @author lf
	 */
	public static boolean isMemberExists(String key, String member) {
		Double value = zscore(key, member);
		if (value == null) {
			return false;
		}
		return value > 0;
	}

	/**
	 * 获取key中member指向的成员的score值
	 * @param key key值
	 * @param member 成员
	 * @return 成员的score值
	 */
	public static Double zscore(String key, String member) {
		ShardedJedis jedis = null;
		Double score = null;
		try {
			jedis = jm.getShardedJedis();
			score = jedis.zscore(key, member);
			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
		return score;
	}

	/**
	 * 为有序集 key 的成员 o的 score 值加上增量 score
	 * @param key key值
	 * @param score 增量score
	 * @param o 成员
	 */
	public static void zincrby(String key, double score, Object o) {
		String json = JSON.toJSONString(o);

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			jedis.zincrby(key, score, json);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员<br>
	 * 其中成员的位置按 score 值递减(从大到小)来排列
	 * @param clazz 对象类型
	 * @param key key值
	 * @param start 区间起始下标
	 * @param end 区间结束下标
	 * @return 区间的成员对象
	 */
	public static <T> Map<T, Double> zrevrange(Class<T> clazz, String key, long start, long end) {
		Map<T, Double> map = new LinkedHashMap<T, Double>();
		Set<Tuple> set = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			set = jedis.zrevrangeWithScores(key, start, end);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return null;
		}

		for (Tuple tuple : set) {
			T t = JSON.parseObject(tuple.getElement(), clazz);
			Double score = tuple.getScore();
			map.put(t, score);
		}
		return map;
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员<br>
	 * 其中成员的位置按 score 值递减(从小到大)来排列
	 * @param clazz 对象类型
	 * @param key key值
	 * @param start 区间起始下标
	 * @param end 区间结束下标
	 * @return 区间的成员对象
	 */
	public static <T> Map<T, Double> zrange(Class<T> clazz, String key, long start, long end) {
		Map<T, Double> map = new LinkedHashMap<T, Double>();
		Set<Tuple> set = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			set = jedis.zrangeWithScores(key, start, end);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return null;
		}

		for (Tuple tuple : set) {
			T t = JSON.parseObject(tuple.getElement(), clazz);
			Double score = tuple.getScore();
			map.put(t, score);
		}
		return map;
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从大到小排序），从offset偏移量开始的count个缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double, String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @param offset 偏移量
	 * @param count 个数
	 * @return 获取的Map对象
	 */
	private static <T> Map<T, Double> zrevrangeByScoreCommon(Class<T> clazz, String key, Object maxScore,
			Object minScore, int offset, int count) {
		Map<T, Double> map = new LinkedHashMap<T, Double>();
		Set<Tuple> set = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			if (maxScore.getClass().equals(Long.class) && minScore.getClass().equals(Long.class)) {
				set = jedis.zrevrangeByScoreWithScores(key, Long.valueOf(maxScore.toString()),
						Long.valueOf(minScore.toString()), offset, count);
			} else if (maxScore.getClass().equals(Double.class) && minScore.getClass().equals(Double.class)) {
				set = jedis.zrevrangeByScoreWithScores(key, Double.valueOf(maxScore.toString()),
						Double.valueOf(minScore.toString()), offset, count);
			} else if (maxScore.getClass().equals(String.class) && minScore.getClass().equals(String.class)) {
				set = jedis.zrevrangeByScoreWithScores(key, maxScore.toString(), minScore.toString(), offset, count);
			}

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return null;
		}

		for (Tuple tuple : set) {
			T t = JSON.parseObject(tuple.getElement(), clazz);
			Double score = tuple.getScore();
			map.put(t, score);
		}
		return map;
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从大到小排序），从offset偏移量开始的count个缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double, String(当前对象类型)]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @param offset 偏移量
	 * @param count 个数
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrevrangeByScore(Class<T> clazz, String key, String max, String min, int offset,
			int count) {
		return zrevrangeByScoreCommon(clazz, key, max, min, offset, count);
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从大到小排序），从offset偏移量开始的count个缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double(当前对象类型), String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @param offset 偏移量
	 * @param count 个数
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrevrangeByScore(Class<T> clazz, String key, double max, double min, int offset,
			int count) {
		return zrevrangeByScoreCommon(clazz, key, max, min, offset, count);
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从大到小排序），从offset偏移量开始的count个缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long(当前对象类型), Double, String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @param offset 偏移量
	 * @param count 个数
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrevrangeByScore(Class<T> clazz, String key, long max, long min, int offset,
			int count) {
		return zrevrangeByScoreCommon(clazz, key, max, min, offset, count);
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从大到小排序）的所有缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double, String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @return 获取的Map对象
	 */
	private static <T> Map<T, Double> zrevrangeByScoreCommon(Class<T> clazz, String key, Object maxScore,
			Object minScore) {
		Map<T, Double> map = new LinkedHashMap<T, Double>();
		Set<Tuple> set = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			if (maxScore.getClass().equals(Long.class) && minScore.getClass().equals(Long.class)) {
				set = jedis.zrevrangeByScoreWithScores(key, Long.valueOf(maxScore.toString()),
						Long.valueOf(minScore.toString()));
			} else if (maxScore.getClass().equals(Double.class) && minScore.getClass().equals(Double.class)) {
				set = jedis.zrevrangeByScoreWithScores(key, Double.valueOf(maxScore.toString()),
						Double.valueOf(minScore.toString()));
			} else if (maxScore.getClass().equals(String.class) && minScore.getClass().equals(String.class)) {
				set = jedis.zrevrangeByScoreWithScores(key, maxScore.toString(), minScore.toString());
			}

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return null;
		}

		for (Tuple tuple : set) {
			T t = JSON.parseObject(tuple.getElement(), clazz);
			Double score = tuple.getScore();
			map.put(t, score);
		}
		return map;
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从大到小排序）的所有缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double, String(当前对象类型)]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrevrangeByScore(Class<T> clazz, String key, String max, String min) {
		return zrevrangeByScoreCommon(clazz, key, max, min);
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从大到小排序）的所有缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double(当前对象类型), String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrevrangeByScore(Class<T> clazz, String key, double max, double min) {
		return zrevrangeByScoreCommon(clazz, key, max, min);
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从大到小排序）的所有缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long(当前对象类型), Double, String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrevrangeByScore(Class<T> clazz, String key, long max, long min) {
		return zrevrangeByScoreCommon(clazz, key, max, min);
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从小到大排序），从offset偏移量开始的count个缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double, String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @param offset 偏移量
	 * @param count 个数
	 * @return 获取的Map对象
	 */
	private static <T> Map<T, Double> zrangeByScoreCommon(Class<T> clazz, String key, Object maxScore, Object minScore,
			int offset, int count) {

		Map<T, Double> map = new LinkedHashMap<T, Double>();
		Set<Tuple> set = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			if (maxScore.getClass().equals(Long.class) && minScore.getClass().equals(Long.class)) {
				set = jedis.zrangeByScoreWithScores(key, Long.valueOf(minScore.toString()),
						Long.valueOf(maxScore.toString()), offset, count);
			} else if (maxScore.getClass().equals(Double.class) && minScore.getClass().equals(Double.class)) {
				set = jedis.zrangeByScoreWithScores(key, Double.valueOf(minScore.toString()),
						Double.valueOf(maxScore.toString()), offset, count);
			} else if (maxScore.getClass().equals(String.class) && minScore.getClass().equals(String.class)) {
				set = jedis.zrangeByScoreWithScores(key, minScore.toString(), maxScore.toString(), offset, count);
			}

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
			return null;
		}

		for (Tuple tuple : set) {
			T t = JSON.parseObject(tuple.getElement(), clazz);
			Double score = tuple.getScore();
			map.put(t, score);
		}
		return map;
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从小到大排序），从offset偏移量开始的count个缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double, String(当前对象类型)]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @param offset 偏移量
	 * @param count 个数
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrangeByScore(Class<T> clazz, String key, String max, String min, int offset,
			int count) {
		return zrangeByScoreCommon(clazz, key, max, min, offset, count);
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从小到大排序），从offset偏移量开始的count个缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long, Double(当前对象类型), String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @param offset 偏移量
	 * @param count 个数
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrangeByScore(Class<T> clazz, String key, double max, double min, int offset,
			int count) {
		return zrangeByScoreCommon(clazz, key, max, min, offset, count);
	}

	/**
	 * 根据传入的key值，获取maxScore和minScore区间内（按照从小到大排序），从offset偏移量开始的count个缓存元素
	 * @param clazz 对象 类型
	 * @param key key值
	 * @param maxScore 最大值对象，包括[Long(当前对象类型), Double, String]三个类型情况处理
	 * @param minScore 最小值对象，同上
	 * @param offset 偏移量
	 * @param count 个数
	 * @return 获取的Map对象
	 */
	public static <T> Map<T, Double> zrangeByScore(Class<T> clazz, String key, long max, long min, int offset, int count) {
		return zrangeByScoreCommon(clazz, key, max, min, offset, count);
	}

	/**
	 * 查找所有符合给定模式 pattern 的 key
	 * @param key key模式pattern
	 * @return 所有符合pattern的key集合
	 */
	public static Set<String> keys(String key) {
		System.out.println("keys() start:" + new Date());
		Set<String> result = new HashSet<String>();
		if (StringUtils.isBlank(key)) {
			return result;
		}
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Iterator<Jedis> it = jedis.getAllShards().iterator();
			while (it.hasNext()) {
				Jedis j = it.next();
				Set<String> r = j.keys(key);
				if (r != null) {
					result.addAll(r);
				}
			}
			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			jm.returnBrokenSharedJedis(jedis);
		}
		System.out.println("keys end:" + new Date());
		return result;
	}

	/**
	 * 根据传入的key值、过期时间和缓存对象(页面缓存)，往redis中存放改key指向的元素（页面缓存存取专用）
	 * @param key key值
	 * @param expires 过期时间
	 * @param o 缓存的页面内容
	 */
	public static void setPage(String key, int expires, String o) {
		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			Transaction t = jedis.getShard(key).multi();
			t.set(key, o);
			t.expire(key, expires);
			t.exec();

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
	}

	/**
	 * 根据传入的Key值，获取对应的缓存对象（页面缓存存取专用）
	 * @param key key值
	 * @return 页面缓存内容
	 */
	public static String getPage(String key) {
		String o = null;

		ShardedJedis jedis = null;
		try {
			jedis = jm.getShardedJedis();
			o = jedis.get(key);

			jm.returnSharedJedis(jedis);
		} catch (Exception e) {
			logger.error(e);
			jm.returnBrokenSharedJedis(jedis);
		}
		return o;
	}

	/** 页面缓存存取专用*/
	/**
	 * 根据传入的key值、过期时间和缓存回调对象，往redis中获取key指向的缓存元素<br>
	 * 如果缓存中存在，则返回缓存内容；否则，就重新从数据库中获取，并保存到缓存中
	 * @param key key值
	 * @param expires 过期时间
	 * @param caller 缓存回调对象
	 * @return 获取的页面内容
	 */
	public static String getPage(String key, int expires, CacheCaller<String> caller) {
		String t = null;

		if (StringUtils.isNotEmpty(key)) {
			t = JedisHelper.getPage(key);

			// 有缓存数据
			if (null != t) {
				return t;
			}
			// 没有缓存数据
			else {
				// 已经有任务在执行
				if (JedisHelper.checkkey(key)) {
					int c = sleepMaxCount;
					while (c > 0) {
						try {
							Thread.sleep(sleepTime);
						} catch (Exception e) {
							logger.error(e);
						}
						c--;
						t = JedisHelper.getPage(key);
						if (null != t) {
							return t;
						}
					}
					return null;
				}
				// 没有任务在执行
				else {
					JedisHelper.addjob(key);
					try {
						t = caller.call();
						if (null != t) {
							JedisHelper.setPage(key, expires, t);
							return t;
						}
					} catch (Exception e) {
						logger.error(e);
					} finally {
						JedisHelper.removeJob(key);
					}
				}
			}
		}
		return null;
	}

}
