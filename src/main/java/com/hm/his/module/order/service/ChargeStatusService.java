package com.hm.his.module.order.service;

import com.hm.his.module.order.model.OrderItemType;

public interface ChargeStatusService {
	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param orderNo
	 */
	void updateHospitalOrderChargeStatus(String orderNo);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param recordId
	 */
	void updateHospitalOrderChargeStatus(Long recordId);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param itemListId
	 */
	void updateOrderItemListChargeStatus(Long itemListId);
	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param itemId
	 */
	void updateOrderItemListChargeStatusByOrderItemIdAndType(Long itemId,OrderItemType type);
}
