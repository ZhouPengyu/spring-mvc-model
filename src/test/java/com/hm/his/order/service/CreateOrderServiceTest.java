package com.hm.his.order.service;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class CreateOrderServiceTest {
	@Autowired
	OrderService orderService;

	@Test
	public void createOrderNo() {
		String s = orderService.createOrderNo();
		System.out.println(s);
	}

	@Test
	public void createOrder() throws Exception {
		PatientInquiryRequest patientRecord = new PatientInquiryRequest();
		// 患者id
		patientRecord.setPatientId(1L);
		// 病历id
		patientRecord.setRecordId(1L);
		patientRecord.setPatientName("pengge");
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
		patientDrug.setDrugName("板蓝根颗粒");
		patientDrug.setTotalDosage(10D);
		patientDrug.setUnivalence(10d);
		patientDrug.setTotalDosageUnit("片");
		patientDrug.setPrescription(1L);
		patientDrug.setDataSource(0L);
		patientDrug.setRecordId(1L);
		patientDrugs.add(patientDrug);
		PatientDrug patientDrug1 = new PatientDrug();
		patientDrug1.setDrugId(3L);
		patientDrug1.setDrugName("盐酸伪麻黄碱缓释片");
		patientDrug1.setTotalDosage(10D);
		patientDrug1.setUnivalence(10d);
		patientDrug1.setTotalDosageUnit("片");
		patientDrug1.setPrescription(1L);
		patientDrug1.setDataSource(0L);
		patientDrug1.setRecordId(1L);
		// patientDrugs.add(patientDrug1);
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
		patientChineseDrugPieces.setDrugId(17L);
		patientChineseDrugPieces.setDrugName("香砂养胃丸");
		patientChineseDrugPieces.setUnit("两");
		patientChineseDrugPieces.setValue(5D);
		patientChineseDrugPieces.setDataSource(0L);
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
}
