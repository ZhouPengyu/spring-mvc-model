package com.hm.his.module.order.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.manage.model.HospitalExam;
import com.hm.his.module.manage.service.HospitalExamService;
import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.dao.OrderExamMapper;
import com.hm.his.module.order.dao.OrderItemChargeMapper;
import com.hm.his.module.order.dao.OrderItemListMapper;
import com.hm.his.module.order.model.*;
import com.hm.his.module.order.service.OrderExamService;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.statistics.pojo.NameRequst;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @description 订单检查项服务
 * @author lipeng
 * @date 2016年3月4日
 */
@Service
public class OrderExamServiceImpl implements OrderExamService {
	@Autowired(required = false)
	OrderItemListMapper orderItemListMapper;
	@Autowired(required = false)
	OrderExamMapper orderExamMapper;
	@Autowired(required = false)
	OrderItemChargeMapper orderItemChargeMapper;
	@Autowired(required = false)
	HospitalOrderMapper hospitalOrderMapper;
	@Autowired
	HospitalExamService hospitalExamService;

	@Override
	public OrderItemList<OrderExam> createOrderExams(String orderNo, List<PatientExam> patientExams) {
		if (orderNo == null || CollectionUtils.isEmpty(patientExams)) {
			return null;
		}
		OrderItemList<OrderExam> orderExamList = new OrderItemList<>();
		orderExamList.setOrderNo(orderNo);
		orderExamList.setType(OrderItemListType.EXAM.getType());
		orderExamList.setChargeStatus(ChargeStatus.TOCHARGE);
		orderExamList.setRecordId(patientExams.get(0).getRecordId());
		orderItemListMapper.insert(orderExamList);
		// 创建订单中的检查项
		Long orderExamListId = orderExamList.getId();
		// 计算检查单的应收金额
		Double examListReceivableAmt = 0d;
		List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();
		for (PatientExam exam : patientExams) {
			OrderExam orderExam = new OrderExam();
			orderExam.setOrderItemListId(orderExamListId);
			orderExam.setCount(1);
			orderExam.setExamId(exam.getExamId());
			orderExam.setDataSource(exam.getDataSource() == null ? null : exam.getDataSource().intValue());
			orderExam.setExamName(exam.getExamName());
			orderExam.setOrderNo(orderNo);
			orderExam.setHospitalId(SessionUtils.getHospitalId());
			orderExam.setRecordId(exam.getRecordId());
			orderExam.setSalePrice(exam.getPrice());
			orderExam.setPatientExamId(exam.getPatientExamId());
			orderExamMapper.insert(orderExam);

			OrderItemCharge orderItemCharge = new OrderItemCharge();
			orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
			orderItemCharge.setCreater(SessionUtils.getDoctorId());
			orderItemCharge.setItemId(orderExam.getId());
			orderItemCharge.setOrderNo(orderNo);
			orderItemCharge.setRecordId(orderExam.getRecordId());
			orderItemCharge.setItemListId(orderExamListId);
			orderItemCharge.setModifier(SessionUtils.getDoctorId());
			orderItemCharge.setReceivableAmt(exam.getPrice());
			orderItemCharge.setItemType(OrderItemType.EXAM.getType());
			orderItemCharges.add(orderItemCharge);
			if (orderItemCharge.getReceivableAmt() != null) {
				// examListReceivableAmt += orderItemCharge.getReceivableAmt();
				examListReceivableAmt = AmtUtils.add(examListReceivableAmt, orderItemCharge.getReceivableAmt());
			}
		}
		orderItemChargeMapper.insertList(orderItemCharges);
		orderExamList.setReceivableAmt(examListReceivableAmt);
		orderItemListMapper.update(orderExamList);
		return orderExamList;
	}

	@Override
	public List<OrderExam> getOrderExamsByOrderItemListIdAndChargeStatus(Long orderItemListId, Integer chargeStatus) {
		return orderExamMapper.selectByOrderItemListIdAndChargeStatus(orderItemListId, chargeStatus);
	}

	@Override
	public void deleteOrderExam(Long id) {
		orderExamMapper.delete(id);
		orderItemChargeMapper.deleteByItemIdAndItemType(id, OrderItemType.EXAM.getType());
	}

	@Override
	public OrderExam getOrderExamByRecordIdAndPatientExamId(Long recordId, Long patientExamId) {
		return orderExamMapper.selectByRecordIdAndPatientExamId(recordId, patientExamId);
	}

	@Override
	public void deleteOrderExamByRecordIdAndPatientExamId(Long recordId, Long patientExamId) {
		OrderExam orderExam = orderExamMapper.selectByRecordIdAndPatientExamId(recordId, patientExamId);
		if (orderExam == null) {
			return;
		}
		Long orderExamId = orderExam.getId();
		this.deleteOrderExam(orderExamId);

	}

	@Override
	public OrderExam saveOrderExam(Long recordId, PatientExam exam) {
		if (recordId == null || exam == null) {
			return null;
		}
		OrderItemList orderItemList = this.selectOrderExamListByRecordId(recordId);
		if (orderItemList == null) {
			return null;
		}
		Long orderExamListId = orderItemList.getId();
		String orderNo = orderItemList.getOrderNo();
		OrderExam orderExam = new OrderExam();
		orderExam.setOrderItemListId(orderExamListId);
		Integer count = null == LangUtils.getInteger(exam.getTotal()) ? 1 : LangUtils.getInteger(exam.getTotal());
		orderExam.setCount(count);
		orderExam.setExamId(exam.getExamId());
		orderExam.setDataSource(exam.getDataSource() == null ? null : exam.getDataSource().intValue());
		orderExam.setExamName(exam.getExamName());
		orderExam.setOrderNo(orderNo);
		orderExam.setHospitalId(SessionUtils.getHospitalId());
		orderExam.setRecordId(exam.getRecordId());
		orderExam.setSalePrice(exam.getPrice());
		orderExam.setPatientExamId(exam.getPatientExamId());

		Map<String, Object> requestParams = new HashedMap();
		Double cost = 0D;
		if (null != exam.getExamId() && 0 == exam.getDataSource()){
			requestParams.put("examId", exam.getExamId());
			try {
				cost = hospitalExamService.searchHospitalExam(requestParams).get(0).getExamCost();
			} catch (Exception e) {
			}
		}
		orderExam.setCost(cost);
		orderExamMapper.insert(orderExam);

		OrderItemCharge orderItemCharge = new OrderItemCharge();
		orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
		orderItemCharge.setCreater(SessionUtils.getDoctorId());
		orderItemCharge.setItemId(orderExam.getId());
		orderItemCharge.setRecordId(orderExam.getRecordId());
		orderItemCharge.setOrderNo(orderNo);
		orderItemCharge.setItemListId(orderExamListId);
		orderItemCharge.setModifier(SessionUtils.getDoctorId());
		Double receivableAmt = AmtUtils.multiply(exam.getPrice(), count);
		orderItemCharge.setReceivableAmt(receivableAmt);
		orderItemCharge.setItemType(OrderItemType.EXAM.getType());
		orderItemChargeMapper.insert(orderItemCharge);

		orderExam.setChargeStatus(ChargeStatus.TOCHARGE);
		orderExam.setReceivableAmt(receivableAmt);
		return orderExam;
	}

	@Override
	public OrderItemList<OrderExam> saveOrderExams(Long recordId, List<PatientExam> patientExams) {
		if (recordId == null || CollectionUtils.isEmpty(patientExams)) {
			return null;
		}
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		if (hospitalOrder == null) {
			return null;
		}
		String orderNo = hospitalOrder.getOrderNo();
		OrderItemList<OrderExam> orderExamList = new OrderItemList<>();
		orderExamList.setOrderNo(orderNo);
		orderExamList.setRecordId(recordId);
		orderExamList.setType(OrderItemListType.EXAM.getType());
		orderExamList.setChargeStatus(ChargeStatus.TOCHARGE);
		orderItemListMapper.insert(orderExamList);
		// 创建订单中的检查项
		Long orderExamListId = orderExamList.getId();
		// 计算检查单的应收金额
		Double examListReceivableAmt = 0d;
		List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();
		for (PatientExam exam : patientExams) {
			OrderExam orderExam = new OrderExam();
			orderExam.setOrderItemListId(orderExamListId);
			Integer count = null == LangUtils.getInteger(exam.getTotal()) ? 1 : LangUtils.getInteger(exam.getTotal());
			orderExam.setCount(count);
			orderExam.setExamId(exam.getExamId());
			orderExam.setDataSource(exam.getDataSource() == null ? null : exam.getDataSource().intValue());
			orderExam.setExamName(exam.getExamName());
			orderExam.setOrderNo(orderNo);
			orderExam.setHospitalId(SessionUtils.getHospitalId());
			orderExam.setRecordId(exam.getRecordId());
			orderExam.setSalePrice(exam.getPrice());
			orderExam.setPatientExamId(exam.getPatientExamId());
			Map<String, Object> requestParams = new HashedMap();
			Double cost = 0D;
			if (null != exam.getExamId() && 0 == exam.getDataSource()){
				requestParams.put("examId", exam.getExamId());
				try {
					cost = hospitalExamService.searchHospitalExam(requestParams).get(0).getExamCost();
				} catch (Exception e) {
				}
			}
			orderExam.setCost(cost);
			orderExamMapper.insert(orderExam);

			OrderItemCharge orderItemCharge = new OrderItemCharge();
			orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
			orderItemCharge.setCreater(SessionUtils.getDoctorId());
			orderItemCharge.setItemId(orderExam.getId());
			orderItemCharge.setOrderNo(orderNo);
			orderItemCharge.setRecordId(orderExam.getRecordId());
			orderItemCharge.setItemListId(orderExamListId);
			orderItemCharge.setModifier(SessionUtils.getDoctorId());
			orderItemCharge.setReceivableAmt(AmtUtils.multiply(exam.getPrice(), count));
			orderItemCharge.setItemType(OrderItemType.EXAM.getType());
			orderItemCharges.add(orderItemCharge);
			if (orderItemCharge.getReceivableAmt() != null) {
				// examListReceivableAmt += orderItemCharge.getReceivableAmt();
				examListReceivableAmt = AmtUtils.add(examListReceivableAmt, orderItemCharge.getReceivableAmt());
			}
		}
		orderItemChargeMapper.insertList(orderItemCharges);
		orderExamList.setReceivableAmt(examListReceivableAmt);
		orderItemListMapper.update(orderExamList);
		return orderExamList;
	}

	@Override
	public OrderItemList<OrderExam> firstSaveOrderExams(Long recordId, List<PatientExam> patientExams, HospitalOrder hospitalOrder) {
		if (recordId == null || CollectionUtils.isEmpty(patientExams)) {
			return null;
		}
		String orderNo = hospitalOrder.getOrderNo();
		OrderItemList<OrderExam> orderExamList = new OrderItemList<>();
		orderExamList.setOrderNo(orderNo);
		orderExamList.setRecordId(recordId);
		orderExamList.setType(OrderItemListType.EXAM.getType());
		orderExamList.setChargeStatus(ChargeStatus.TOCHARGE);
		orderItemListMapper.insert(orderExamList);
		// 创建订单中的检查项
		Long orderExamListId = orderExamList.getId();

		//将 诊所自己的检查项查出价格来
		List<Long> examIds = patientExams.stream().filter(paExam -> paExam.getDataSource()==0 && paExam.getExamId() !=null).map(PatientExam::getExamId).collect(Collectors.toList());
		Map<Long,Double> examCostMap = Maps.newHashMap();
		try {
			List<HospitalExam> hospitalExams = hospitalExamService.searchHospitalExamByIds(examIds);
			if(CollectionUtils.isNotEmpty(hospitalExams)){
				hospitalExams.stream().forEach(hospitalExam -> {
					examCostMap.put(hospitalExam.getExamId(),hospitalExam.getExamCost());
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<OrderExam> orderExams = Lists.newArrayList();
		for (PatientExam exam : patientExams) {
			OrderExam orderExam = new OrderExam();
			orderExam.setOrderItemListId(orderExamListId);
			Integer count = null == LangUtils.getInteger(exam.getTotal()) ? 1 : LangUtils.getInteger(exam.getTotal());
			orderExam.setCount(count);
			orderExam.setExamId(exam.getExamId());
			orderExam.setDataSource(exam.getDataSource() == null ? null : exam.getDataSource().intValue());
			orderExam.setExamName(exam.getExamName());
			orderExam.setOrderNo(orderNo);
			orderExam.setHospitalId(SessionUtils.getHospitalId());
			orderExam.setRecordId(exam.getRecordId());
			orderExam.setSalePrice(exam.getPrice());
			orderExam.setPatientExamId(exam.getPatientExamId());
			Double cost = 0D;
			if (null != exam.getExamId() && 0 == exam.getDataSource()){
				cost = examCostMap.get(exam.getExamId());
				cost =  cost != null ? cost : 0D;
			}
			orderExam.setCost(cost);
			orderExams.add(orderExam);
		}
		orderExamMapper.insertList(orderExams);
		// 计算检查单的应收金额
		final Double[] examListReceivableAmt = {0d};
		List<OrderItemCharge> orderItemCharges = new ArrayList<OrderItemCharge>();

		orderExams.stream().forEach(orderExam -> {
			OrderItemCharge orderItemCharge = new OrderItemCharge();
			orderItemCharge.setChargeStatus(ChargeStatus.TOCHARGE);
			orderItemCharge.setCreater(SessionUtils.getDoctorId());
			orderItemCharge.setItemId(orderExam.getId());
			orderItemCharge.setOrderNo(orderNo);
			orderItemCharge.setRecordId(orderExam.getRecordId());
			orderItemCharge.setItemListId(orderExamListId);
			orderItemCharge.setModifier(SessionUtils.getDoctorId());
			orderItemCharge.setReceivableAmt(AmtUtils.multiply(orderExam.getSalePrice(), orderExam.getCount()));
			orderItemCharge.setItemType(OrderItemType.EXAM.getType());
			orderItemCharges.add(orderItemCharge);
			if (orderItemCharge.getReceivableAmt() != null) {
				// examListReceivableAmt += orderItemCharge.getReceivableAmt();
				examListReceivableAmt[0] = AmtUtils.add(examListReceivableAmt[0], orderItemCharge.getReceivableAmt());
			}
		});


		orderItemChargeMapper.insertList(orderItemCharges);
		orderExamList.setReceivableAmt(examListReceivableAmt[0]);
		orderItemListMapper.update(orderExamList);
		return orderExamList;
	}

	@Override
	public void deleteOrderExamsByOrderItemListId(Long orderExamListId) {
		orderExamMapper.deleteByOrderItemListId(orderExamListId);
		orderItemChargeMapper.deleteByItemListId(orderExamListId);
	}

	@Override
	public OrderItemList selectOrderExamListByRecordId(Long recordId) {
		List<OrderItemList> list = orderItemListMapper.selectByRecordIdAndType(recordId, OrderItemListType.EXAM.getType());
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public List<String> searchOrderExamNames(NameRequst req) {
		req.setCurrentPage(1);
		req.setPageSize(5);
		req.setHospitalId(SessionUtils.getHospitalId());
		return orderExamMapper.searchOrderExamNames(req);
	}

}
