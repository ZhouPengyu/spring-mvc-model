package com.hm.his.order.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.pojo.GetOrdersRequest;
import com.hm.his.module.order.pojo.HospitalOrderDetailPojo;
import com.hm.his.module.order.pojo.HospitalOrderPojoList;
import com.hm.his.module.order.pojo.SaleChannel;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class OrderServiceTest {
	@Autowired
	OrderService orderService;
	@Autowired
	DrugService drugService;

	String orderNo="120160323165324442";
	@Test
	public void createOrderNo() {
		String s = orderService.createOrderNo();
		System.out.println(s);
	}

	@Test
	public void getToChargeOrders() {
		GetOrdersRequest req = new GetOrdersRequest();
		req.setCurrentPage(1);
//		req.setPatientName("张三");
		req.setStartDate("2016-03-22");
		req.setEndDate("2016-03-22");
		req.setPageSize(50);
		System.out.println(JSON.toJSONString(req));
		HospitalOrderPojoList pojo = orderService.getToChargeOrders(req);
		System.out.println(JSON.toJSONString(pojo));
	}
	@Test
	public void getChargedOrders() {
		GetOrdersRequest req = new GetOrdersRequest();
		req.setCurrentPage(1);
//		req.setPatientName("张三");
		req.setPageSize(10);
		HospitalOrderPojoList pojo = orderService.getChargedOrders(req);
		System.out.println(JSON.toJSONString(pojo));
	}
	@Test
	public void getRefundOrders() {
		GetOrdersRequest req = new GetOrdersRequest();
		req.setCurrentPage(1);
//		req.setPatientName("张三");
		req.setPageSize(10);
		HospitalOrderPojoList pojo = orderService.getRefundedOrders(req);
		System.out.println(JSON.toJSONString(pojo));
	}

	@Test
	public void getToChargeOrderInfo() {
		GetOrdersRequest req = new GetOrdersRequest();
		req.setOrderNo(orderNo);
		HospitalOrderDetailPojo pojo = orderService.getToChargeOrderInfo(req);
		System.out.println(JSON.toJSONString(pojo));
	}
	@Test
	public void getChargedOrderInfo() {
		GetOrdersRequest req = new GetOrdersRequest();
		req.setOrderNo(orderNo);
		HospitalOrderDetailPojo pojo = orderService.getChargedOrderInfo(req);
		System.out.println(JSON.toJSONString(pojo));
	}
	@Test
	public void getRefundOrderInfo() {
		HospitalOrder req = new HospitalOrder();
		req.setOrderNo(orderNo);
		HospitalOrderDetailPojo pojo = orderService.getRefundedOrderInfo(req);
		System.out.println(JSON.toJSONString(pojo));
	}

	@Test
	public void createOrder() throws Exception {
		PatientInquiryRequest patientRecord = new PatientInquiryRequest();
		// 患者id
		patientRecord.setPatientId(1L);
		// 病历id
		patientRecord.setRecordId(1L);
		// 检查
		List<PatientExam> examList = Lists.newArrayList();
		PatientExam patientExam = new PatientExam();
		patientExam.setExamId(1L);
		patientExam.setExamName("血常规");
		patientExam.setPrice(10.00D);
		patientExam.setUnit("单位");
		patientExam.setPatientExamId(1L);
		patientExam.setRecordId(1L);
		examList.add(patientExam);
		patientRecord.setExamList(examList);
		// 西药处方
		List<PatientDrug> patientDrugs = Lists.newArrayList();
		PatientDrug patientDrug = new PatientDrug();
		patientDrug.setDrugId(1L);
		patientDrug.setDrugName("二甲双胍片");
		patientDrug.setTotalDosage(10D);
		patientDrug.setUnivalence(10d);
		patientDrug.setTotalDosageUnit("片");
		patientDrug.setPrescription(1L);
		patientDrug.setDataSource(1L);
		patientDrug.setRecordId(1L);
		patientDrugs.add(patientDrug);
		Map<String, Object> map = Maps.newHashMap();
		map.put("patentDrugList", patientDrugs);
		List<Map<String, Object>> patientDrugMapList = Lists.newArrayList();
		patientDrugMapList.add(map);
		patientRecord.setPatentPrescriptionList(patientDrugMapList);
		// 中药处方
		List<PatientChineseDrug> chinesePrescriptionList = Lists.newArrayList();
		PatientChineseDrug patientChineseDrug = new PatientChineseDrug();
		patientChineseDrug.setPrescription(2L);
		patientChineseDrug.setTotalDosage(5L);
		patientChineseDrug.setRecordId(1L);
		List<PatientChineseDrugPieces> decoctionPiecesList = Lists.newArrayList();
		PatientChineseDrugPieces patientChineseDrugPieces = new PatientChineseDrugPieces();
		patientChineseDrugPieces.setUnivalence(20.00D);
		patientChineseDrugPieces.setDrugId(3L);
		patientChineseDrugPieces.setDrugName("阿胶");
		patientChineseDrugPieces.setUnit("两");
		patientChineseDrugPieces.setValue(5D);
		patientChineseDrugPieces.setDataSource(1L);
		decoctionPiecesList.add(patientChineseDrugPieces);
		patientChineseDrug.setDecoctionPiecesList(decoctionPiecesList);
		chinesePrescriptionList.add(patientChineseDrug);
		patientRecord.setChinesePrescriptionList(chinesePrescriptionList);
		// 附加费用
		List<PatientAdditional> additionalList = Lists.newArrayList();
		PatientAdditional patientAdditional = new PatientAdditional();
		patientAdditional.setAdditionalId(1L);
		patientAdditional.setRecordId(1L);
		patientAdditional.setAdditionalName("注射费");
		patientAdditional.setAdditionalPrice(100.00d);
		additionalList.add(patientAdditional);
		patientRecord.setAdditionalList(additionalList);
		orderService.createOrder(patientRecord);
	}
	@Test
	public void searchDrugSaleWayById() throws Exception {
		List<SaleWayPojo> list= drugService.searchDrugSaleWayById(SaleChannel.Prescription.getType(), 9L);
		System.out.println(list.size());
	}
	
	@Test
	public void hasOrderByDoctorId() throws Exception {
		boolean re=orderService.hasOrderByDoctorId(205L);
		System.out.println(re);
	}
}
