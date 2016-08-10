package com.hm.his.order.controller;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.hm.his.BaseControllerTest;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.SellDrug;
import com.hm.his.module.order.model.SellDrugRecord;
import com.hm.his.module.order.pojo.ChargeRequest;
import com.hm.his.module.order.pojo.GetOrdersRequest;
import com.hm.his.module.order.pojo.NameRequst;
import com.hm.his.module.order.pojo.PrescriptionAndItemIds;
import com.hm.his.module.order.pojo.RefundRequest;

public class OrderControllerTest extends BaseControllerTest {
	String orderNo = "120160317144142812";

	@Test
	public void getToChargeOrders() {
		GetOrdersRequest req = new GetOrdersRequest();
		req.setCurrentPage(1);
		req.setPageSize(10);
		req.setStartDate("2016-3-16");
		super.testRole("order/getToChargeOrders", req);
	}

	@Test
	public void getChargedOrders() {
		GetOrdersRequest req = new GetOrdersRequest();
		req.setCurrentPage(1);
		req.setPageSize(10);
		req.setStartDate("2016-10-10");
		req.setEndDate("2016-10-11");
		super.testRole("order/getChargedOrders", req);
	}

	@Test
	public void getRefundedOrders() {
		GetOrdersRequest req = new GetOrdersRequest();
		req.setCurrentPage(1);
		req.setPageSize(10);
		req.setStartDate("2016-10-10");
		req.setEndDate("2016-10-11");

		super.testRole("order/getRefundedOrders", req);
	}

	@Test
	public void getToChargeOrderInfo() {
		HospitalOrder req = new HospitalOrder();
		req.setOrderNo(orderNo);
		super.testRole("order/getToChargeOrderInfo", req);
	}

	@Test
	public void getChargedOrderInfo() {
		HospitalOrder req = new HospitalOrder();
		req.setOrderNo(orderNo);
		super.testRole("order/getChargedOrderInfo", req);
	}

	@Test
	public void getRefundedOrderInfo() {
		HospitalOrder req = new HospitalOrder();
		req.setOrderNo(orderNo);
		super.testRole("order/getRefundedOrderInfo", req);
	}

	@Test
	public void charge() {
		ChargeRequest req = new ChargeRequest();
		req.setOrderNo(orderNo);
		req.setActualAmt(250.50);
		// req.setOrderNo(orderNo);
		// req.setActualAmt(120.23);
		// OrderAdditionAmtListAndItemIds orderAdditionAmtList = new
		// OrderAdditionAmtListAndItemIds();
		// orderAdditionAmtList.setOrderAdditionAmtListId(1L);
		// orderAdditionAmtList.setOrderAdditionAmtIds(Arrays.asList(1L, 2L);
		// req.setOrderAdditionAmtList(orderAdditionAmtList);
		//
		// OrderExamListAndItemIds orderExamList = new
		// OrderExamListAndItemIds();
		// orderExamList.setOrderExamListId(1L);
		// orderExamList.setOrderExamIds(Arrays.asList(1L);
		// req.setOrderExamList(orderExamList);
		//
		// List<PrescriptionAndItemIds> orderPrescriptions =
		// Lists.newArrayList();
		// PrescriptionAndItemIds prescriptionAndItemIds = new
		// PrescriptionAndItemIds();
		// prescriptionAndItemIds.setOrderPrescriptionId(1L);
		// prescriptionAndItemIds.setOrderDrugIds(Arrays.asList(1L);
		// req.setOrderPrescriptions(orderPrescriptions);
		//
		// SellDrugRecordAndItemIds sellDrugRecord = new
		// SellDrugRecordAndItemIds();
		// sellDrugRecord.setSellDrugRecordId(1L);
		// sellDrugRecord.setOrderDrugIds(Arrays.asList(1L, 2L);
		// req.setSellDrugRecord(sellDrugRecord);
		super.testRole("order/charge", req);
	}

	@Test
	public void refund() {
		RefundRequest req = new RefundRequest();
		req.setOrderNo(orderNo);
		List<PrescriptionAndItemIds> orderPrescriptions = Lists.newArrayList();
		PrescriptionAndItemIds orderPrescriptionPojo = new PrescriptionAndItemIds();
		orderPrescriptionPojo.setOrderPrescriptionId(160L);
		List<Long> orderDrugIds = Lists.newArrayList();
		orderDrugIds.add(51L);
		orderPrescriptionPojo.setOrderDrugIds(orderDrugIds);
		orderPrescriptions.add(orderPrescriptionPojo);
		// PrescriptionAndItemIds orderPrescriptionPojo1 = new
		// PrescriptionAndItemIds();
		// orderPrescriptionPojo1.setOrderPrescriptionId(28L);
		// orderPrescriptions.add(orderPrescriptionPojo1);
		req.setOrderPrescriptions(orderPrescriptions);
		req.setRefundAmt(90D);
		// RefundRequest req = new RefundRequest();
		// OrderAdditionAmtListAndItemIds orderAdditionAmtList = new
		// OrderAdditionAmtListAndItemIds();
		// orderAdditionAmtList.setOrderAdditionAmtListId(2L);
		// List<Long> orderAdditionAmtIds = Arrays.asList(1L, 2L);
		// orderAdditionAmtList.setOrderAdditionAmtIds(orderAdditionAmtIds);
		// req.setOrderAdditionAmtList(orderAdditionAmtList);
		// OrderExamListAndItemIds orderExamList = new
		// OrderExamListAndItemIds();
		// orderExamList.setOrderExamListId(5L);
		// orderExamList.setOrderExamIds(Arrays.asList(12L, 45L);
		// req.setOrderExamList(orderExamList);
		//
		// req.setOrderNo(orderNo);
		// List<PrescriptionAndItemIds> orderPrescriptions =
		// Lists.newArrayList();
		// PrescriptionAndItemIds orderPrescriptionPojo = new
		// PrescriptionAndItemIds();
		// orderPrescriptionPojo.setOrderPrescriptionId(3L);
		// orderPrescriptionPojo.setOrderDrugIds(Arrays.asList(5L, 8L);
		// req.setOrderPrescriptions(orderPrescriptions);
		//
		// req.setRefundAmt(250.45);
		// SellDrugRecordAndItemIds sellDrugRecord = new
		// SellDrugRecordAndItemIds();
		// sellDrugRecord.setSellDrugRecordId(3L);
		// sellDrugRecord.setOrderDrugIds(Arrays.asList(7L, 34L);
		// req.setSellDrugRecord(sellDrugRecord);
		super.testRole("order/refund", req);
	}

	@Test
	public void sellDrugs() {
		SellDrugRecord req = new SellDrugRecord();
		req.setAge(20D);
		req.setAgeType("岁");
		req.setGender(1);
		req.setPatientName("张三");
		req.setActualAmt(100.23);
		req.setCreater(SessionUtils.getDoctorId());
		req.setIdCardNo("1246546464");
		List<SellDrug> drugs = Lists.newArrayList();
		SellDrug d = new SellDrug();
		d.setDrugId(19L);
		d.setDrugName("药名");
		d.setSalePrice(12.00);
		d.setSaleUnit("盒");
		d.setCount(1);
		d.setSpecification("规格");
		drugs.add(d);
		req.setDrugs(drugs);
		super.testRole("order/sellDrugs", req);
	}

	@Test
	public void searchOrderExamNames() throws Exception {
		NameRequst req = new NameRequst();
		req.setName("常规");
		super.testRole("order/searchOrderExamNames", req);
	}

	@Test
	public void searchOrderDrugNames() throws Exception {
		NameRequst req = new NameRequst();
		req.setName("阿莫西林");
		super.testRole("order/searchOrderDrugNames", req);
	}

	@Test
	public void getSellDrugInfo() throws Exception {
		HospitalOrder req = new HospitalOrder();
		req.setOrderNo("120160401104056190");
		super.testRole("order/getSellDrugInfo", req);
	}

}
