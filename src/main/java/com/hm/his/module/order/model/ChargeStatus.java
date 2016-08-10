package com.hm.his.module.order.model;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @description
 * @author lipeng 訂單費用情況
 * @date 2016年2月27日
 */
public class ChargeStatus {
	/**
	 *  费用状态：包含待收费内容
	 */
	public static final int TOCHARGE = 0b001; // 1
	/**
	 *  费用状态：包含已收费内容
	 */
	public static final int CHARGED = 0b010;// 2
	/**
	 *  费用状态：包含退费内容
	 */
	public static final int REFUND = 0b100;// 4
	/**
	 *  费用状态：包含待收费内容和已收费内容
	 */
	public static final int TOCHARGE_CHARGED = 0b011;// 3
	/**
	 *  费用状态：包含待收费内容和退费内容
	 */
	public static final int TOCHARGE_REFUND = 0b101;// 5
	/**
	 *  费用状态：包含已收费内容和退费内容
	 */
	public static final int CHARGED_REFUND = 0b110;// 6
	/**
	 *  费用状态：包含待收费内容，已收费内容和退费内容
	 */
	public static final int TOCHARGE_CHARGED_REFUND = 0b111;// 7

	public static void main(String[] args) {
		// a:011 a:011 a:111 a:111
		// b:001 b:011 b:011 b:011
		// c:001 c:001 c:001 c:011
		int a = 0b011, b = 0b001, c = 0b001;
		// int a =0b011, b = 0b011, c = 0b001;
		// int a =0b111, b = 0b011, c = 0b001;
		// int a =0b111, b = 0b011, c = 0b011;
		int re = (a & CHARGED | a & REFUND) & (b & CHARGED | b & REFUND) & (c & CHARGED | c & REFUND);
		// 遍历子项，如果子项全部包含已收费和已退费，说明订单已经不包括待收费的项，那么 订单要移除待收费状态。
		System.out.println(re > 0);
		// 移除一个状态
		System.out.println(TOCHARGE_CHARGED_REFUND & (~TOCHARGE));
		System.out.println(ChargeStatus.REFUND | ChargeStatus.CHARGED);
	}

	/**
	 * 
	 * @description 从src状态中移除status状态
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param src
	 * @param status ,status参数只能是基础状态，即只有1位位1的状态
	 * @return
	 */
	public static int removeStatus(int src, int status) {
		return src & (~status);
	}

	/**
	 * 
	 * @description 给src状态添加status状态,status参数只能是基础状态，即只有1位位1的状态
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param src
	 * @param status
	 * @return
	 */
	public static int addStatus(int src, int status) {
		return src | status;
	}

	/**
	 * 
	 * @description 判断src是否包含status状态,status参数只能是基础状态，即只有1位位1的状态
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param src
	 * @param status
	 * @return
	 */
	public static boolean hasStatus(int src, int status) {
		return (src & status) == status ? true : false;
	}

	/**
	 * 
	 * @description 如果status中的全部状态都不包含待收费了，那么移除src中的待收费状态
	 * 用于计算订单状态以及订单中单的状态
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param orderStatus
	 * @param status
	 * @return
	 */
	public static int removeToChargedStatusWhenAllCharged(int orderStatus, List<Integer> statusList) {
		if (CollectionUtils.isEmpty(statusList)) {
			return orderStatus;
		}
		boolean hasToCharged = false;
		for (Integer status : statusList) {
			if (hasStatus(status, TOCHARGE)) {
				// 如果其中一项含待收费，那么订单就应该包含待收费
				hasToCharged = true;
				break;
			}
		}
		// 没有待收费
		if (!hasToCharged) {
			orderStatus = removeStatus(orderStatus, TOCHARGE);
		}
		return orderStatus;
	}

	/**
	 * 
	 * @description 将statusList全部状态进行或运算
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param statusList
	 * @return
	 */
	public static Integer addAll(List<Integer> statusList) {
		if (CollectionUtils.isEmpty(statusList)) {
			return null;
		}
		Integer re = null;
		for (Integer status : statusList) {
			if (re == null) {
				re = status;
			} else {
				re = re | status;
			}

		}
		return re;
	}

}
