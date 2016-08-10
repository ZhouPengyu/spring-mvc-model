package com.hm.his.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件，映射类
 * 不支持动态修改配置文件，即改配置文件需要重启容器加载。
 * @author lijunwei
 *
 */
public class SystemConfig extends PropertyPlaceholderConfigurer {

	private static Map<String, String> ctxPropertiesMap;

	/**
	 * 更加传入的参数，对Properties对象中的属性进行解析，并保存到Map对象中
	 * @param beanFactoryToProcess 实体创建工厂类
	 * @param props 属性对象
	 */
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		ctxPropertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
	}

	/**
	 * 通过传入的属性名，获取对应的属性值(字符串对象)
	 * @param name 属性名
	 * @return 属性值
	 */
	public static String getProperty(String name) {
		return ctxPropertiesMap.get(name);
	}

	/**
	 * 通过传入的属性名，获取对应的属性值(长整型值)
	 * @param proName 属性名
	 * @return 属性值
	 */
	public static long getLongProperty(String proName) {
		String strValue = ctxPropertiesMap.get(proName);
		if (strValue != null && !strValue.trim().equals("")) {
			long longValue = Long.parseLong(strValue.trim());
			return longValue;
		} else {
			return 0;
		}
	}

	/**
	 * 通过传入的属性名，获取对应的属性值，如果属性值不存在，就返回传入的默认值defaultLongValue(长整型值)
	 * @param proName 属性名
	 * @param defaultLongValue 默认值
	 * @return 属性值
	 */
	public static long getLongProperty(String proName, long defaultLongValue) {
		String strValue = ctxPropertiesMap.get(proName);
		if (strValue == null) {
			return defaultLongValue;
		} else {
			if (!strValue.trim().equals("")) {
				long longValue = Long.parseLong(strValue.trim());
				return longValue;
			} else {
				return defaultLongValue;
			}
		}
	}

	/**
	 * 通过传入的属性名，获取对应的属性值(整型值)
	 * @param proName 属性名
	 * @return 属性值
	 */
	public static int getIntProperty(String proName) {
		String strValue = ctxPropertiesMap.get(proName);
		if (strValue != null && !strValue.trim().equals("")) {
			int intValue = Integer.parseInt(strValue.trim());
			return intValue;
		} else {
			return 0;
		}
	}

	/**
	 * 通过传入的属性名，获取对应的属性值，如果属性值不存在，就返回传入的默认值defaultLongValue(整型值)
	 * @param proName 属性名
	 * @param defaultLongValue 默认值
	 * @return 属性值
	 */
	public static int getIntProperty(String proName, int defaultInfValue) {
		String strValue = ctxPropertiesMap.get(proName);
		if (strValue == null) {
			return defaultInfValue;
		} else {
			if (!strValue.trim().equals("")) {
				int intValue = Integer.parseInt(strValue.trim());
				return intValue;
			} else {
				return defaultInfValue;
			}
		}
	}

}
