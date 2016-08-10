package com.hm.his.module.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderItemList;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月9日
 */
public interface OrderItemListMapper {

	void insert(OrderItemList orderItemList);

	void insertList(List<OrderItemList> orderItemLists);

	void delete(Long id);

	OrderItemList selectById(Long id);

	void update(OrderItemList orderItemList);

	/**
	 * 
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	List<OrderItemList> selectByOrderNoAndChargeStatus(@Param("orderNo") String orderNo, @Param("chargeStatus") Integer chargeStatus);

	/**
	 * 
	 * @description
	 * @date 2016年3月12日
	 * @author lipeng
	 * @param id
	 * @param chargeStatus
	 */
	void updateOrderItemListChargeSatus(@Param("id") Long id, @Param("chargeStatus") Integer chargeStatus);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	List<OrderItemList> selectByOrderNo(@Param("orderNo") String orderNo);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param recordId
	 * @return
	 */
	List<OrderItemList> selectByRecordId(@Param("recordId") Long recordId);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param orderNo
	 * @param id
	 * @return
	 */
	OrderItemList selectByOrderNoAndForeignId(@Param("orderNo") String orderNo, @Param("foreignId") Long foreignId);

	List<OrderItemList> selectByRecordIdAndType(@Param("recordId") Long recordId, @Param("type") Integer type);

	OrderItemList selectByRecordIdAndForeignId(@Param("recordId") Long recordId, @Param("foreignId") Long foreignId);
}