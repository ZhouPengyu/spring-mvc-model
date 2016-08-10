package com.hm.his.framework.cache.redis;

/**
 * redis缓存的接口类
 *
 */
public class CacheCaller<T> {

	/**
	 * 回调方法
	 * @return 泛型对象（根据创建这个对象时传入的对象类型，则返回对应的对象类型）
	 */
	public T call() {
		return null;
	}

	/**
	 * 回调方法
	 * @param index
	 * @return 泛型对象（根据创建这个对象时传入的对象类型，则返回对应的对象类型）
	 */
	public T call(int index) {
		return null;
	}
}
