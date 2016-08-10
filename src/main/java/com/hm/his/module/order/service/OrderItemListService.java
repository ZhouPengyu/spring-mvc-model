package com.hm.his.module.order.service;

import java.util.List;

import com.hm.his.module.order.model.OrderItemList;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月8日
 */
public interface OrderItemListService {
	/**
	 * 
	 * @description 更具订单号查询下面下的全部单
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	List<OrderItemList> getOrderItemListsByOrderNoAndChargeStatus(String orderNo, Integer chargeStatus);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param id
	 * @return
	 */
	OrderItemList  getOrderItemListById(Long id);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param recordId
	 * @param foreignId
	 * @return
	 */
	OrderItemList  getOrderItemListByRecordIdForeignId(Long recordId, Long foreignId);

	void deleteOrderItemList(OrderItemList orderItemList);

	void updateOrderItemListChargeSatus(Long id, Integer chargeStatus);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	List<OrderItemList> getOrderItemListsByOrderNo(String orderNo);
}
