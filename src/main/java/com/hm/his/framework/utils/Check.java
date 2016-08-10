package com.hm.his.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 检查工具
 * 
 * @author zhn
 * 
 */
public class Check {
	/**
	 * 根据传入的集合，检查它是否为空
	 * @param value 集合
	 * @return true:表示空；false:不为空
	 */
	public static boolean isEmpty(Collection<? extends Object> value) {
		return (null == value) || (0 == value.size());
	}

	/**
	 * 根据传入的对象数组，检查它是否为空
	 * @param value 对象数组
	 * @return true:表示空；false:不为空
	 */
	public static boolean isEmpty(Object[] value) {
		return (null == value) || (0 == value.length);
	}

	/**
	 * 根据传入的int类型数组，检查它是否为空
	 * @param value int类型数组
	 * @return true:表示空；false:不为空
	 */
	public static boolean isEmpty(int[] value) {
		return (null == value) || (0 == value.length);
	}

	/**
	 * 根据传入的集合，检查它是否为空
	 * @param value 集合
	 * @return true:表示空；false:不为空
	 */
	public static boolean isEmpty(Map<? extends Object, ? extends Object> value) {
		return (null == value) || (0 == value.size());
	}

	/**
	 * 根据传入的字符串对象，检查字符串是否为空
	 * @param value 字符串对象
	 * @return true:表示空；false:不为空
	 */
	public static boolean isEmpty(String value) {
		return StringUtils.isEmpty(value);
	}

	/**
	 * 根据传入的字符串数组，检查它是否为空
	 * @param value 字符串数组
	 * @return true:表示空；false:不为空
	 */
	public static boolean isEmpty(String[] value) {
		return (null == value) || (0 == value.length);
	}

	/**
	 * 根据传入的StringBuilder对象，检查它是否为空
	 * @param value StringBuilder对象
	 * @return true:表示空；false:不为空
	 */
	public static boolean isEmpty(StringBuilder value) {
		return (null == value) || (0 == value.length());
	}

	/**
	 * 根据传入的集合对象，检查它是否不为空
	 * @param value 集合对象
	 * @return true:不为空；false:表示空
	 */
	public static boolean notEmpty(Collection<? extends Object> value) {
		return (null != value) && (0 < value.size());
	}

	/**
	 * 根据传入的对象数组，检查它是否不为空
	 * @param value 对象数组
	 * @return true:不为空；false:表示空
	 */
	public static boolean notEmpty(Object[] value) {
		return (null != value) && (0 < value.length);
	}

	/**
	 * 根据传入的int类型数组，检查它是否不为空
	 * @param value int 类型数组
	 * @return true:不为空；false:表示空
	 */
	public static boolean notEmpty(int[] value) {
		return (null != value) && (0 < value.length);
	}

	/**
	 * 根据传入的集合对象，检查它是否不为空
	 * @param value 集合对象
	 * @return true:不为空；false:表示空
	 */
	public static boolean notEmpty(Map<? extends Object, ? extends Object> value) {
		return (null != value) && (0 < value.size());
	}

	/**
	 * 根据传入的字符串对象，检查它是否不为空
	 * @param value 字符串对象
	 * @return true:不为空；false:表示空
	 */
	public static boolean notEmpty(String value) {
		return StringUtils.isNotEmpty(value);
	}

	/**
	 * 根据传入的字符串数组对象，检查它是否不为空
	 * @param value 字符串数组
	 * @return true:不为空；false:表示空
	 */
	public static boolean notEmpty(String[] value) {
		return (null != value) && (value.length > 0);
	}

	/**
	 * 根据传入的StringBuilder对象，检查它是否不为空
	 * @param value StringBuilder对象
	 * @return true:不为空；false:表示空
	 */
	public static boolean notEmpty(StringBuilder value) {
		return (null != value) && (value.length() > 0);
	}
}
