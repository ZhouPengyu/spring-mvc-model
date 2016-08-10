package com.hm.his.module.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.dao.OrderAdditionAmtMapper;
import com.hm.his.module.order.dao.OrderItemChargeMapper;
import com.hm.his.module.order.dao.OrderItemListMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.order.model.OrderItemCharge;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.model.OrderItemType;
import com.hm.his.module.order.service.OrderAdditionAmtService;
import com.hm.his.module.outpatient.model.PatientAdditional;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月4日
 */
@Service
public class OrderAdditionAmtServiceImpl implements OrderAdditionAmtService {
	@Autowired
	OrderItemListMapper orderItemListMapper;
	@Autowired
	OrderAdditionAmtMapper orderAdditionAmtMapper;
	@Autowired
	OrderItemChargeMapper orderItemChargeMapper;
	@Autowired
	HospitalOrderMapper hospitalOrderMapper;

	@Override
	public OrderItemList<OrderAdditionAmt> createOrderAdditionAmts(String orderNo, List<PatientAdditional> additionalList) {
		if (orderNo == null || CollectionUtils.isEmpty(additionalList)) {
			return null;
		}
		// 创建订单附加费用单
		OrderItemList<OrderAdditionAmt> orderAdditionAmtList = new OrderItemList<>();
		orderAdditionAmtList.setChargeStatus(ChargeStatus.TOCHARGE);
		orderAdditionAmtList.setOrderNo(orderNo);
		orderAdditionAmtList.setType(OrderItemListType.ADDITION_AMT.getType());
		orderAdditionAmtList.setRecordId(additionalList.get(0).getRecordId());
		orderItemListMapper.insert(orderAdditionAmtList);

		List<OrderAdditionAmt> orderAdditionAmts = Lists.newArrayList();

		for (PatientAdditional patientAdditional : additionalList) {
			OrderAdditionAmt orderAdditionAmt = new OrderAdditionAmt();
			orderAdditionAmt.setAmtName(patientAdditional.getAdditionalName());
			orderAdditionAmt.setAdditionalId(patientAdditional.getAdditionalId());
			orderAdditionAmt.setOrderItemListId(orderAdditionAmtList.getId());
			orderAdditionAmt.setOrderNo(orderNo);
			orderAdditionAmt.setHospitalId(SessionUtils.getHospitalId());
			orderAdditionAmt.setRecordId(patientAdditional.getRecordId());
			// TODO
			orderAdditionAmt.setCount(1);
			orderAdditionAmt.setSalePrice(patientAdditional.getAdditionalPrice());
			orderAdditionAmts.add(orderAdditionAmt);
		}

		orderAdditionAmtMapper.insertList(orderAdditionAmts);
		// 创建订单附加费用项
		List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();
		final Double[] orderAdditionAmtReceivableAmt = {0d};
		orderAdditionAmts.stream().forEach(orderAdditionAmt -> {
			OrderItemCharge orderItemCharge = new OrderItemCharge();
			orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
			orderItemCharge.setCreater(SessionUtils.getDoctorId());
			orderItemCharge.setItemId(orderAdditionAmt.getId());
			orderItemCharge.setOrderNo(orderNo);
			orderItemCharge.setRecordId(orderAdditionAmt.getRecordId());
			orderItemCharge.setItemListId(orderAdditionAmtList.getId());
			orderItemCharge.setModifier(SessionUtils.getDoctorId());
			orderItemCharge.setReceivableAmt(orderAdditionAmt.getSalePrice());
			orderItemCharge.setItemType(OrderItemType.ADDITION_AMT.getType());
			orderItemCharges.add(orderItemCharge);
			if (orderItemCharge.getReceivableAmt() != null) {
				// orderAdditionAmtReceivableAmt +=
				// orderItemCharge.getReceivableAmt();
				orderAdditionAmtReceivableAmt[0] = AmtUtils.add(orderAdditionAmtReceivableAmt[0], orderItemCharge.getReceivableAmt());
			}
		});


		orderItemChargeMapper.insertList(orderItemCharges);
		orderAdditionAmtList.setReceivableAmt(orderAdditionAmtReceivableAmt[0]);
		orderItemListMapper.update(orderAdditionAmtList);
		return orderAdditionAmtList;
	}

	@Override
	public List<OrderAdditionAmt> getOrderAdditionAmtsByOrderItemListIdAndChargeStatus(Long orderItemListId, Integer chargeStatus) {
		List<OrderAdditionAmt> orderAdditionAmts = orderAdditionAmtMapper.selectByOrderItemListIdAndChargeStatus(orderItemListId, chargeStatus);
		return orderAdditionAmts;
	}

	@Override
	public OrderItemList<OrderAdditionAmt> saveOrderAdditionAmts(Long recordId, List<PatientAdditional> additionalList) {
		if (recordId == null || CollectionUtils.isEmpty(additionalList)) {
			return null;
		}
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		if (hospitalOrder == null) {
			return null;
		}
		String orderNo = hospitalOrder.getOrderNo();
		return this.createOrderAdditionAmts(orderNo, additionalList);
	}

	@Override
	public OrderItemList<OrderAdditionAmt> firstSaveOrderAdditionAmts(Long recordId, List<PatientAdditional> additionalList, HospitalOrder hospitalOrder) {
		if (recordId == null || CollectionUtils.isEmpty(additionalList)) {
			return null;
		}
		if (hospitalOrder == null) {
			return null;
		}
		String orderNo = hospitalOrder.getOrderNo();
		return this.createOrderAdditionAmts(orderNo, additionalList);
	}

	@Override
	public OrderAdditionAmt getOrderAdditionAmtByRecordIdAndAdditionalId(Long recordId, Long additionalId) {
		return orderAdditionAmtMapper.selectByRecordIdAndAdditionalId(recordId, additionalId);
	}

	@Override
	public void deleteOrderAdditionAmt(Long id) {
		orderAdditionAmtMapper.delete(id);
		orderItemChargeMapper.deleteByItemIdAndItemType(id, OrderItemType.ADDITION_AMT.getType());
	}

	@Override
	public OrderAdditionAmt saveOrderAdditionAmt(Long recordId, PatientAdditional patientAdditional) {
		if (recordId == null || patientAdditional == null) {
			return null;
		}
		OrderItemList orderItemList = this.selectOrderAdditionAmtListByRecordId(recordId);
		if (orderItemList == null) {
			return null;
		}
		Long orderExamListId = orderItemList.getId();
		String orderNo = orderItemList.getOrderNo();
		OrderAdditionAmt orderAdditionAmt = new OrderAdditionAmt();
		orderAdditionAmt.setAmtName(patientAdditional.getAdditionalName());
		orderAdditionAmt.setAdditionalId(patientAdditional.getAdditionalId());
		orderAdditionAmt.setOrderItemListId(orderExamListId);
		orderAdditionAmt.setOrderNo(orderNo);
		orderAdditionAmt.setHospitalId(SessionUtils.getHospitalId());
		orderAdditionAmt.setRecordId(patientAdditional.getRecordId());
		// TODO
		orderAdditionAmt.setCount(1);
		orderAdditionAmt.setSalePrice(patientAdditional.getAdditionalPrice());
		orderAdditionAmtMapper.insert(orderAdditionAmt);

		OrderItemCharge orderItemCharge = new OrderItemCharge();
		orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
		orderItemCharge.setCreater(SessionUtils.getDoctorId());
		orderItemCharge.setItemId(orderAdditionAmt.getId());
		orderItemCharge.setOrderNo(orderNo);
		orderItemCharge.setRecordId(orderAdditionAmt.getRecordId());
		orderItemCharge.setItemListId(orderExamListId);
		orderItemCharge.setModifier(SessionUtils.getDoctorId());
		orderItemCharge.setReceivableAmt(orderAdditionAmt.getSalePrice());
		orderItemCharge.setItemType(OrderItemType.ADDITION_AMT.getType());
		orderItemChargeMapper.insert(orderItemCharge);

		orderAdditionAmt.setChargeStatus(ChargeStatus.TOCHARGE);
		orderAdditionAmt.setReceivableAmt(orderItemCharge.getReceivableAmt());
		return orderAdditionAmt;
	}

	@Override
	public void updateOrderAdditionAmtByListIdAndChargeStatus(Long orderAdditionAmtListId, Integer chargeStatus) {
		orderAdditionAmtMapper.updateOrderAdditionAmtByListIdAndChargeStatus(orderAdditionAmtListId, chargeStatus);
	}

	@Override
	public void deleteOrderAdditionAmtsByOrderItemListId(Long orderItemListId) {
		orderAdditionAmtMapper.deleteByOrderItemListId(orderItemListId);
		orderItemChargeMapper.deleteByItemListId(orderItemListId);
	}

	@Override
	public OrderItemList selectOrderAdditionAmtListByRecordId(Long recordId) {
		List<OrderItemList> list = orderItemListMapper.selectByRecordIdAndType(recordId, OrderItemListType.ADDITION_AMT.getType());
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

}
