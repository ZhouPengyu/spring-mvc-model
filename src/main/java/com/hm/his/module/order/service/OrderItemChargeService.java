package com.hm.his.module.order.service;

import java.util.List;

import com.hm.his.module.order.model.OrderItemCharge;

public interface OrderItemChargeService {
	/**
	 * 
	 * @description 根据单的id和状态查询OrderItemCharge
	 * @date 2016年3月12日
	 * @author lipeng
	 * @param orderPrescriptionId
	 * @param tocharge
	 * @return
	 */
	List<OrderItemCharge> getOrderItemChargesByItemListIdAndChargeStatus(Long itemListId, Integer chargeStatus);

	/**
	 * 
	 * @description 将单下面全部的项的状态改为chargeStatus
	 * @date 2016年4月6日
	 * @author lipeng
	 * @param itemListId
	 * @param chargeStatus
	 */
	void updateOrderItemChargeStatusByItemListId(Long itemListId, Integer chargeStatus);

	List<OrderItemCharge> getOrderItemChargesByItemIds(List<Long> itemIds);

	void addChargeStatusToOrderItemCharges(List<Long> itemIds, Integer chargeStatus);

	/**
	 * 
	 * @description 更新项的收费员和收费时间
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param itemListId
	 */
	void updateChargeOperatorAndDateByItemListId(Long itemListId);

	/**
	 * 
	 * @description 更新项的退费员和退费时间
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param itemIds
	 */
	void updateRefundOperatorAndDateByItemIds(List<Long> itemIds);

	/**
	 * 
	 * @description 更新项的状态
	 * @date 2016年4月6日
	 * @author lipeng
	 * @param orderItemCharges
	 */
	void updateOrderItemChargeStatus(List<OrderItemCharge> orderItemCharges ,Integer chargeStatus);
}
