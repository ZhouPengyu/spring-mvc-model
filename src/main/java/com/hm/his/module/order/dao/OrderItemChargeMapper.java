package com.hm.his.module.order.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.order.model.OrderItemCharge;
import com.hm.his.module.order.pojo.OrderItemChargeReceivableAmtPojo;

public interface OrderItemChargeMapper {
	void delete(Long id);

	void insert(OrderItemCharge orderItemCharge);

	List<OrderItemCharge> selectAll();

	void insertList(List<OrderItemCharge> orderItemCharges);

	OrderItemCharge selectById(Long id);

	void update(OrderItemCharge orderItemCharge);

	List<OrderItemCharge> selectOrderItemChargesByItemListIdAndChargeStatus(@Param("itemListId") Long itemListId,
			@Param("chargeStatus") Integer chargeStatus);

	void updateOrderItemChargeStatusByItemListId(@Param("itemListId") Long itemListId,
			@Param("chargeStatus") Integer chargeStatus);

	List<OrderItemCharge> selectOrderItemChargesByItemIds(@Param("itemIds") List<Long> itemIds);

	void addChargeStatusToOrderItemCharges(@Param("itemIds") List<Long> itemIds,
			@Param("chargeStatus") Integer chargeStatus);

	List<OrderItemCharge> selectByItemListId(@Param("itemListId") Long itemListId);

	void deleteByItemIdAndItemType(@Param("itemId") Long itemId, @Param("itemType") int itemType);

	OrderItemCharge selectByItemIdAndItemType(@Param("itemId") Long itemId, @Param("itemType") int itemType);

	void deleteByItemListId(@Param("itemListId") Long itemListId);

	/**
	 * 
	 * @description 更新项的收费员和收费时间
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param date
	 */
	void updateChargeOperatorAndDateByItemListId(@Param("itemListId") Long itemListId,
			@Param("chargeOperator") Long chargeOperator, @Param("chargeDate") Date chargeDate);

	/**
	 * 
	 * @description 更新项的退费员和退费时间
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param date
	 */
	void updateRefundOperatorAndDateByItemIds(@Param("itemIds") List<Long> itemIds,
			@Param("refundOperator") Long refundOperator, @Param("refundDate") Date refundDate);

	/**
	 * 
	 * @description 获取指定订单的退费项目的金额合计
	 * @date 2016年4月5日
	 * @author lipeng
	 * @param orderNo
	 * @param i
	 * @return
	 */
	Double selectReceivableAmtSumByOrderNoAndChargeStatus(@Param("orderNo") String orderNo,
			@Param("chargeStatus") Integer chargeStatus);

	List<OrderItemChargeReceivableAmtPojo> selectReceivableAmtSumByOrderNosAndChargeStatus(
			@Param("orderNos") List<String> orderNos, @Param("chargeStatus") Integer chargeStatus);

	/**
	 * 
	 * @description 更具id更新状态
	 * @date 2016年4月6日
	 * @author lipeng
	 * @param ids
	 */
	void updateOrderItemChargeStatusByIds(@Param("chargeStatus") Integer chargeStatus, @Param("ids") List<Long> ids);

}