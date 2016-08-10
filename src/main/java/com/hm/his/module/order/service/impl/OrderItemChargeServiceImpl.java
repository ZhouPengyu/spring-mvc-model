package com.hm.his.module.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.order.dao.OrderItemChargeMapper;
import com.hm.his.module.order.model.OrderItemCharge;
import com.hm.his.module.order.service.OrderItemChargeService;

@Service
public class OrderItemChargeServiceImpl implements OrderItemChargeService {
	@Autowired
	OrderItemChargeMapper orderItemChargeMapper;

	@Override
	public List<OrderItemCharge> getOrderItemChargesByItemListIdAndChargeStatus(Long itemListId, Integer chargeStatus) {
		return orderItemChargeMapper.selectOrderItemChargesByItemListIdAndChargeStatus(itemListId, chargeStatus);
	}

	@Override
	public void updateOrderItemChargeStatusByItemListId(Long itemListId, Integer chargeStatus) {
		orderItemChargeMapper.updateOrderItemChargeStatusByItemListId(itemListId, chargeStatus);
	}

	@Override
	public List<OrderItemCharge> getOrderItemChargesByItemIds(List<Long> itemIds) {
		return orderItemChargeMapper.selectOrderItemChargesByItemIds(itemIds);
	}

	@Override
	public void addChargeStatusToOrderItemCharges(List<Long> itemIds, Integer chargeStatus) {
		orderItemChargeMapper.addChargeStatusToOrderItemCharges(itemIds, chargeStatus);
	}

	@Override
	public void updateChargeOperatorAndDateByItemListId(Long itemListId) {
		orderItemChargeMapper.updateChargeOperatorAndDateByItemListId(itemListId, SessionUtils.getDoctorId(), new Date());
	}

	@Override
	public void updateRefundOperatorAndDateByItemIds(List<Long> itemIds) {
		orderItemChargeMapper.updateRefundOperatorAndDateByItemIds(itemIds, SessionUtils.getDoctorId(), new Date());
	}

	@Override
	public void updateOrderItemChargeStatus(List<OrderItemCharge> orderItemCharges, Integer chargeStatus) {
		if (CollectionUtils.isEmpty(orderItemCharges)) {
			return;
		}
		List<Long> ids = orderItemCharges.stream().map(OrderItemCharge::getId).collect(Collectors.toList());
		orderItemChargeMapper.updateOrderItemChargeStatusByIds(chargeStatus, ids);
	}

}
