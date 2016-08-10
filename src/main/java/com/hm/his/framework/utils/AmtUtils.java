package com.hm.his.framework.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 
 * @description 金额操作工具
 * @author lipeng
 * @date 2016年3月14日
 */
public class AmtUtils {
	/**
	 * 
	 * @description 加法
	 * @date 2016年3月14日
	 * @author lipeng
	 * @param amt1
	 * @param v2
	 * @return
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		double re = b1.add(b2).doubleValue();
		return decimalFormat(re);
	}

	/**
	 * 
	 * @description 减法
	 * @date 2016年3月14日
	 * @author lipeng
	 * @param amt1
	 * @param amt2
	 * @return
	 */
	public static double subtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		double re = b1.subtract(b2).doubleValue();
		return decimalFormat(re);
	}

	/**
	 * 
	 * @description 乘法
	 * @date 2016年3月14日
	 * @author lipeng
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double multiply(double v1, Integer v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(v2);
		double re = b1.multiply(b2).doubleValue();
		return Double.valueOf(decimalFormat(re));
	}

	/**
	 * @description 乘法
	 * @date 2016年3月31日
	 * @author lipeng
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double multiply(double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(v2);
		double re = b1.multiply(b2).doubleValue();
		return Double.valueOf(decimalFormat(re));
	}

	/**
	 * 
	 * @description 保留两位小数并四舍五入
	 * @date 2016年3月31日
	 * @author lipeng
	 * @param value
	 * @return
	 */
	public static double decimalFormat(double value) {
		return Double.valueOf(new DecimalFormat("0.00").format(value));
	}

	public static void main(String[] args) {
		System.out.println(decimalFormat(100));
		Double a = null;
		Double b = null;
		Double c = AmtUtils.add(a, b);
		System.out.println(c);
	}

}
