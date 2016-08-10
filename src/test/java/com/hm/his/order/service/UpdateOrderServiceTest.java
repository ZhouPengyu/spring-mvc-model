package com.hm.his.order.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.order.service.UpdateOrderService;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientChineseDrugPieces;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class UpdateOrderServiceTest {
	@Autowired
	UpdateOrderService updateOrderService;
	@Autowired
	OrderService orderService;

	@Test
	public void createHospitalOrder() throws Exception {
		PatientInquiryRequest patientRecord = new PatientInquiryRequest();
		patientRecord.setRecordId(2L);
		orderService.createHospitalOrder(patientRecord);
	}

	/**
	 * 
	 * @description 添加附加费用单
	 * @date 2016年3月14日
	 * @author lipeng
	 * @throws Exception
	 */
	@Test
	public void addOrderAdditionAmtList() throws Exception {
		Long recordId = 1L;
		List<PatientAdditional> patientAdditionals = Lists.newArrayList();
		PatientAdditional patientAdditional = new PatientAdditional();
		patientAdditional.setAdditionalId(2L);
		patientAdditional.setAdditionalName("A费");
		patientAdditional.setAdditionalPrice(20D);
		patientAdditional.setRecordId(1L);
		patientAdditionals.add(patientAdditional);
		updateOrderService.addOrderAdditionAmtList(recordId, patientAdditionals);
	}

	/**
	 * 
	 * @description 添加附加费用单
	 * @date 2016年3月14日
	 * @author lipeng
	 * @throws Exception
	 */
	@Test
	public void addOrderExamList() throws Exception {
		Long recordId = 1L;
		List<PatientExam> patientExams = Lists.newArrayList();
		PatientExam exam = new PatientExam();
		exam.setPatientExamId(3L);
		exam.setExamId(1L);
		exam.setExamName("sss");
		exam.setPrice(30D);
		exam.setRecordId(1L);
		patientExams.add(exam);
		updateOrderService.addOrderExamList(recordId, patientExams);
	}

	@Test
	public void deleteRecordItem() throws Exception {
		Long recordId = 1L;
		updateOrderService.deleteRecordItem(recordId, 1L, OrderItemListType.EXAM);
	}

	@Test
	public void deleteAllRecordItems() throws Exception {
		Long recordId = 1L;
		updateOrderService.deleteAllRecordItems(recordId, OrderItemListType.CHINESE_PRESCRIPTION);
	}

	@Test
	public void saveOrderAdditionAmt() throws Exception {
		Long recordId = 1L;
		PatientAdditional patientAdditional = new PatientAdditional();
		patientAdditional.setAdditionalId(2L);
		patientAdditional.setAdditionalName("A费");
		patientAdditional.setAdditionalPrice(20D);
		patientAdditional.setRecordId(1L);
		updateOrderService.saveOrderAdditionAmt(recordId, patientAdditional);
	}

	@Test
	public void saveOrderChinesePrescription() throws Exception {
		Long recordId = 1L;
		PatientChineseDrug patientChineseDrug = new PatientChineseDrug();
		patientChineseDrug.setPrescription(2L);
		patientChineseDrug.setTotalDosage(3L);
		patientChineseDrug.setRecordId(1L);
		List<PatientChineseDrugPieces> decoctionPiecesList = Lists.newArrayList();
		PatientChineseDrugPieces patientChineseDrugPieces = new PatientChineseDrugPieces();
		patientChineseDrugPieces.setUnivalence(5.00D);
		patientChineseDrugPieces.setDrugId(5L);
		patientChineseDrugPieces.setDrugName("wuahahha");
		patientChineseDrugPieces.setUnit("两");
		patientChineseDrugPieces.setValue(5D);
		patientChineseDrugPieces.setDataSource(1L);
		decoctionPiecesList.add(patientChineseDrugPieces);
		patientChineseDrug.setDecoctionPiecesList(decoctionPiecesList);
		updateOrderService.saveOrderChinesePrescription(recordId, patientChineseDrug);
	}

	@Test
	public void saveOrderExam() throws Exception {
		Long recordId = 2L;
		PatientExam exam = new PatientExam();
		exam.setPatientExamId(4L);
		exam.setExamId(4L);
		exam.setExamName("eeeee");
		exam.setPrice(50D);
		exam.setRecordId(recordId);
		updateOrderService.saveOrderExam(recordId, exam);
	}

	@Test
	public void saveOrderPatentPrescription() throws Exception {
		Long recordId = 1L;
		List<PatientDrug> patientDrugs = Lists.newArrayList();
		PatientDrug patientDrug = new PatientDrug();
		patientDrug.setDrugId(1L);
		patientDrug.setDrugName("片片");
		patientDrug.setTotalDosage(5D);
		patientDrug.setUnivalence(6d);
		patientDrug.setTotalDosageUnit("片");
		patientDrug.setPrescription(1L);
		patientDrug.setDataSource(1L);
		patientDrug.setRecordId(1L);
		patientDrugs.add(patientDrug);
		updateOrderService.saveOrderPatentPrescription(recordId, patientDrugs);
	}

}
