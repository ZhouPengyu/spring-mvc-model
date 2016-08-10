package com.hm.his.module.order.service.tem;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.order.pojo.ChargeRequest;
import com.hm.his.module.order.pojo.GetOrdersRequest;
import com.hm.his.module.order.pojo.HospitalOrderDetailPojo;
import com.hm.his.module.order.pojo.HospitalOrderPojo;
import com.hm.his.module.order.pojo.HospitalOrderPojoList;
import com.hm.his.module.order.pojo.OrderAdditionAmtListPojo;
import com.hm.his.module.order.pojo.OrderExamListPojo;
import com.hm.his.module.order.pojo.OrderPrescriptionPojo;
import com.hm.his.module.order.pojo.SellDrugRecordPojo;

@Service
public class OrderServiceTem {

	public HospitalOrderPojoList getToChargeOrders(GetOrdersRequest req) {
		HospitalOrderPojoList re = new HospitalOrderPojoList();
		List<HospitalOrderPojo> orders = Lists.newArrayList();
		HospitalOrderPojo order1 = new HospitalOrderPojo();
		orders.add(order1);
		order1.setAge(20D);
		order1.setAgeType("岁");
		order1.setDiagnosis("糖尿病，高血压");
		order1.setDiagnosisDate(new Date());
		order1.setDoctorName("李医生");
		order1.setGender("男");
		order1.setOrderNo("2016030115381010123");
		order1.setPatientName("张三");
		order1.setReceivableAmt(128.23);
		HospitalOrderPojo order2 = new HospitalOrderPojo();
		orders.add(order2);
		order2.setAge(25D);
		order2.setAgeType("月");
		order2.setDiagnosis("小儿多动症");
		order2.setDiagnosisDate(new Date());
		order2.setDoctorName("王医生");
		order2.setGender("男");
		order2.setOrderNo("2016030115381010123");
		order2.setPatientName("哈仔");
		order2.setReceivableAmt(18888.23);
		re.setOrders(orders);
		re.setTotalPage(10);
		return re;
	}

	public HospitalOrderPojoList getChargedOrders(GetOrdersRequest req) {
		HospitalOrderPojoList re = new HospitalOrderPojoList();

		List<HospitalOrderPojo> orders = Lists.newArrayList();
		HospitalOrderPojo order1 = new HospitalOrderPojo();
		orders.add(order1);
		order1.setAge(56D);
		order1.setAgeType("岁");
		order1.setDiagnosis("糖尿病，高血压");
		order1.setDiagnosisDate(new Date());
		order1.setDoctorName("李医生");
		order1.setGender("男");
		order1.setOrderNo("2016030115381010123");
		order1.setPatientName("张三");
		order1.setReceivableAmt(128.23);
		order1.setActualAmt(200.23);
		order1.setOperateDate(new Date());
		order1.setOperatorName("小张");

		HospitalOrderPojo order2 = new HospitalOrderPojo();
		orders.add(order2);
		order2.setAge(25D);
		order2.setAgeType("月");
		order2.setDiagnosis("小儿多动症");
		order2.setDiagnosisDate(new Date());
		order2.setDoctorName("王医生");
		order2.setGender("男");
		order2.setOrderNo("2016030115381010123");
		order2.setPatientName("哈仔");
		order2.setReceivableAmt(18888.23);
		order2.setActualAmt(15000.23);
		order2.setOperateDate(new Date());
		order2.setOperatorName("小张");
		re.setOrders(orders);
		re.setTotalPage(10);
		return re;
	}

	public HospitalOrderPojoList getRefundedOrders(GetOrdersRequest req) {
		HospitalOrderPojoList re = new HospitalOrderPojoList();

		List<HospitalOrderPojo> orders = Lists.newArrayList();
		HospitalOrderPojo order1 = new HospitalOrderPojo();
		orders.add(order1);
		order1.setAge(56D);
		order1.setAgeType("岁");
		order1.setDiagnosis("糖尿病，高血压");
		order1.setDiagnosisDate(new Date());
		order1.setDoctorName("李医生");
		order1.setGender("男");
		order1.setOrderNo("2016030115381010123");
		order1.setPatientName("张三");
		order1.setRefundAmt(200.23);
		order1.setOperateDate(new Date());
		order1.setOperatorName("小张");

		HospitalOrderPojo order2 = new HospitalOrderPojo();
		orders.add(order2);
		order2.setAge(25D);
		order2.setAgeType("月");
		order2.setDiagnosis("小儿多动症");
		order2.setDiagnosisDate(new Date());
		order2.setDoctorName("王医生");
		order2.setGender("男");
		order2.setOrderNo("2016030115381010123");
		order2.setPatientName("哈仔");
		order2.setRefundAmt(15000.23);
		order2.setOperateDate(new Date());
		order2.setOperatorName("小张");
		re.setOrders(orders);
		re.setTotalPage(10);
		return re;
	}

	public void charge(ChargeRequest req) {

	}

	public HospitalOrderDetailPojo getToChargeOrderInfo(HospitalOrder req) {
		HospitalOrderDetailPojo order = new HospitalOrderDetailPojo();
		order.setAge(56D);
		order.setAgeType("岁");
		order.setDiagnosis("糖尿病，高血压");
		order.setDiagnosisDate(new Date());
		order.setDoctorName("李医生");
		order.setGender("男");
		order.setOrderNo("2016030115381010123");
		order.setPatientName("张三疯");
		/**
		 * 附加费用
		 */
		OrderAdditionAmtListPojo orderAdditionAmtList = new OrderAdditionAmtListPojo();
		orderAdditionAmtList.setOrderAdditionAmtListId(1L);
		List<OrderAdditionAmt> orderAdditionAmts = Lists.newArrayList();
		OrderAdditionAmt orderAdditionAmt1 = new OrderAdditionAmt();
		orderAdditionAmt1.setId(1L);
		orderAdditionAmt1.setAmtName("注射费");
		orderAdditionAmt1.setCount(1);
		orderAdditionAmt1.setSalePrice(12.50);
		orderAdditionAmt1.setReceivableAmt(12.50);
		OrderAdditionAmt orderAdditionAmt2 = new OrderAdditionAmt();
		orderAdditionAmt2.setId(2L);
		orderAdditionAmt2.setAmtName("其他费用");
		orderAdditionAmt2.setCount(2);
		orderAdditionAmt2.setSalePrice(15.00);
		orderAdditionAmt2.setReceivableAmt(30.00);
		orderAdditionAmts.add(orderAdditionAmt1);
		orderAdditionAmts.add(orderAdditionAmt2);
		orderAdditionAmtList.setOrderAdditionAmts(orderAdditionAmts);
		order.setOrderAdditionAmtList(orderAdditionAmtList);

		/**
		 *  检查治疗
		 */
		OrderExamListPojo orderExamList = new OrderExamListPojo();
		orderExamList.setOrderExamListId(10L);
		List<OrderExam> orderExams = Lists.newArrayList();
		OrderExam orderExam1 = new OrderExam();
		orderExam1.setId(2L);
		orderExam1.setExamName("血常规");
		orderExam1.setCount(1);
		orderExam1.setSalePrice(40.00);
		orderExam1.setReceivableAmt(40.00);
		orderExams.add(orderExam1);

		OrderExam orderExam2 = new OrderExam();
		orderExam2.setId(10L);
		orderExam2.setExamName("尿常规");
		orderExam2.setCount(1);
		orderExam2.setSalePrice(500.00);
		orderExam2.setReceivableAmt(500.00);
		orderExams.add(orderExam2);

		orderExamList.setOrderExams(orderExams);
		order.setOrderExamList(orderExamList);

		/**
		 *  处方列表
		 */
		List<OrderPrescriptionPojo> orderPrescriptions = Lists.newArrayList();
		OrderPrescriptionPojo prescription = new OrderPrescriptionPojo();
		prescription.setOrderPrescriptionId(3L);
		List<OrderDrug> orderDrugs = Lists.newArrayList();
		OrderDrug drug1 = new OrderDrug();
		drug1.setId(20L);
		drug1.setCount(20+"");
		drug1.setSalePrice(12.50);
		drug1.setSaleUnit("片");
		drug1.setReceivableAmt(250.00);
		drug1.setDrugName("感冒颗粒");
		orderDrugs.add(drug1);
		OrderDrug drug2 = new OrderDrug();
		drug2.setId(1L);
		drug2.setCount(3+"");
		drug2.setSalePrice(10.00);
		drug2.setSaleUnit("盒");
		drug2.setReceivableAmt(30.00);
		drug2.setDrugName("感康");
		orderDrugs.add(drug2);
		prescription.setOrderDrugs(orderDrugs);
		order.setOrderPrescriptions(orderPrescriptions);

		return order;
	}

	public HospitalOrderDetailPojo getChargedOrderInfo(HospitalOrder req) {
		HospitalOrderDetailPojo order = new HospitalOrderDetailPojo();
		order.setAge(56D);
		order.setAgeType("岁");
		order.setDiagnosis("糖尿病，高血压");
		order.setDiagnosisDate(new Date());
		order.setDoctorName("李医生");
		order.setGender("男");
		order.setOrderNo("2016030115381010123");
		order.setPatientName("张三疯");
		order.setReceivableAmt(1000.00);
		order.setActualAmt(1000.00);
		/**
		 * 附加费用
		 */
		OrderAdditionAmtListPojo orderAdditionAmtList = new OrderAdditionAmtListPojo();
		orderAdditionAmtList.setOrderAdditionAmtListId(1L);
		List<OrderAdditionAmt> orderAdditionAmts = Lists.newArrayList();
		OrderAdditionAmt orderAdditionAmt1 = new OrderAdditionAmt();
		orderAdditionAmt1.setId(1L);
		orderAdditionAmt1.setAmtName("注射费");
		orderAdditionAmt1.setCount(1);
		orderAdditionAmt1.setSalePrice(12.50);
		orderAdditionAmt1.setReceivableAmt(12.50);
		OrderAdditionAmt orderAdditionAmt2 = new OrderAdditionAmt();
		orderAdditionAmt2.setId(2L);
		orderAdditionAmt2.setAmtName("其他费用");
		orderAdditionAmt2.setCount(2);
		orderAdditionAmt2.setSalePrice(15.00);
		orderAdditionAmt2.setReceivableAmt(30.00);
		orderAdditionAmts.add(orderAdditionAmt1);
		orderAdditionAmts.add(orderAdditionAmt2);
		orderAdditionAmtList.setOrderAdditionAmts(orderAdditionAmts);
		order.setOrderAdditionAmtList(orderAdditionAmtList);

		/**
		 *  检查治疗
		 */
		OrderExamListPojo orderExamList = new OrderExamListPojo();
		orderExamList.setOrderExamListId(10L);
		List<OrderExam> orderExams = Lists.newArrayList();
		OrderExam orderExam1 = new OrderExam();
		orderExam1.setId(2L);
		orderExam1.setExamName("血常规");
		orderExam1.setCount(1);
		orderExam1.setSalePrice(40.00);
		orderExam1.setReceivableAmt(40.00);
		orderExams.add(orderExam1);

		OrderExam orderExam2 = new OrderExam();
		orderExam2.setId(10L);
		orderExam2.setExamName("尿常规");
		orderExam2.setCount(1);
		orderExam2.setSalePrice(500.00);
		orderExam2.setReceivableAmt(500.00);
		orderExams.add(orderExam2);

		orderExamList.setOrderExams(orderExams);
		order.setOrderExamList(orderExamList);

		/**
		 *  处方列表
		 */
		List<OrderPrescriptionPojo> orderPrescriptions = Lists.newArrayList();
		OrderPrescriptionPojo prescription = new OrderPrescriptionPojo();
		prescription.setOrderPrescriptionId(3L);
		List<OrderDrug> orderDrugs = Lists.newArrayList();
		OrderDrug drug1 = new OrderDrug();
		drug1.setId(20L);
		drug1.setCount(20+"");
		drug1.setSalePrice(12.50);
		drug1.setSaleUnit("片");
		drug1.setReceivableAmt(250.00);
		drug1.setDrugName("感冒颗粒");
		orderDrugs.add(drug1);
		OrderDrug drug2 = new OrderDrug();
		drug2.setId(1L);
		drug2.setCount(3+"");
		drug2.setSalePrice(10.00);
		drug2.setSaleUnit("盒");
		drug2.setReceivableAmt(30.00);
		drug2.setDrugName("感康");
		orderDrugs.add(drug2);
		prescription.setOrderDrugs(orderDrugs);
		order.setOrderPrescriptions(orderPrescriptions);

		/**
		 * 直接售药
		 */
		SellDrugRecordPojo sellDrugRecordPojo = new SellDrugRecordPojo();
		sellDrugRecordPojo.setSellDrugRecordId(1L);
		List<OrderDrug> orderDrugs2 = Lists.newArrayList();
		OrderDrug drug3 = new OrderDrug();
		drug3.setId(7L);
		drug3.setCount(2+"");
		drug3.setSalePrice(7.00);
		drug3.setSaleUnit("片");
		drug3.setReceivableAmt(14.00);
		drug3.setDrugName("药片");
		orderDrugs2.add(drug3);
		OrderDrug drug4 = new OrderDrug();
		drug4.setId(8L);
		drug4.setCount(4+"");
		drug4.setSalePrice(8.00);
		drug4.setSaleUnit("盒");
		drug4.setReceivableAmt(32.00);
		drug4.setDrugName("达克宁");
		orderDrugs2.add(drug4);
		sellDrugRecordPojo.setOrderDrugs(orderDrugs2);

		order.setSellDrugRecord(sellDrugRecordPojo);
		return order;
	}

	public HospitalOrderDetailPojo getRefundedOrderInfo(HospitalOrder req) {
		HospitalOrderDetailPojo order = new HospitalOrderDetailPojo();
		order.setAge(56D);
		order.setAgeType("岁");
		order.setDiagnosis("糖尿病，高血压");
		order.setDiagnosisDate(new Date());
		order.setDoctorName("李医生");
		order.setGender("男");
		order.setOrderNo("2016030115381010123");
		order.setPatientName("张三疯");
		order.setRefundAmt(1000.00);
		/**
		 * 附加费用
		 */
		OrderAdditionAmtListPojo orderAdditionAmtList = new OrderAdditionAmtListPojo();
		orderAdditionAmtList.setOrderAdditionAmtListId(1L);
		List<OrderAdditionAmt> orderAdditionAmts = Lists.newArrayList();
		OrderAdditionAmt orderAdditionAmt1 = new OrderAdditionAmt();
		orderAdditionAmt1.setId(1L);
		orderAdditionAmt1.setAmtName("注射费");
		orderAdditionAmt1.setCount(1);
		orderAdditionAmt1.setSalePrice(12.50);
		orderAdditionAmt1.setReceivableAmt(12.50);
		OrderAdditionAmt orderAdditionAmt2 = new OrderAdditionAmt();
		orderAdditionAmt2.setId(2L);
		orderAdditionAmt2.setAmtName("其他费用");
		orderAdditionAmt2.setCount(2);
		orderAdditionAmt2.setSalePrice(15.00);
		orderAdditionAmt2.setReceivableAmt(30.00);
		orderAdditionAmts.add(orderAdditionAmt1);
		orderAdditionAmts.add(orderAdditionAmt2);
		orderAdditionAmtList.setOrderAdditionAmts(orderAdditionAmts);
		order.setOrderAdditionAmtList(orderAdditionAmtList);

		/**
		 *  检查治疗
		 */
		OrderExamListPojo orderExamList = new OrderExamListPojo();
		orderExamList.setOrderExamListId(10L);
		List<OrderExam> orderExams = Lists.newArrayList();
		OrderExam orderExam1 = new OrderExam();
		orderExam1.setId(2L);
		orderExam1.setExamName("血常规");
		orderExam1.setCount(1);
		orderExam1.setSalePrice(40.00);
		orderExam1.setReceivableAmt(40.00);
		orderExams.add(orderExam1);

		OrderExam orderExam2 = new OrderExam();
		orderExam2.setId(10L);
		orderExam2.setExamName("尿常规");
		orderExam2.setCount(1);
		orderExam2.setSalePrice(500.00);
		orderExam2.setReceivableAmt(500.00);
		orderExams.add(orderExam2);

		orderExamList.setOrderExams(orderExams);
		order.setOrderExamList(orderExamList);

		/**
		 *  处方列表
		 */
		List<OrderPrescriptionPojo> orderPrescriptions = Lists.newArrayList();
		OrderPrescriptionPojo prescription = new OrderPrescriptionPojo();
		prescription.setOrderPrescriptionId(3L);
		List<OrderDrug> orderDrugs = Lists.newArrayList();
		OrderDrug drug1 = new OrderDrug();
		drug1.setId(20L);
		drug1.setCount(20+"");
		drug1.setSalePrice(12.50);
		drug1.setSaleUnit("片");
		drug1.setReceivableAmt(250.00);
		drug1.setDrugName("感冒颗粒");
		orderDrugs.add(drug1);
		OrderDrug drug2 = new OrderDrug();
		drug2.setId(1L);
		drug2.setCount(3+"");
		drug2.setSalePrice(10.00);
		drug2.setSaleUnit("盒");
		drug2.setReceivableAmt(30.00);
		drug2.setDrugName("感康");
		orderDrugs.add(drug2);
		prescription.setOrderDrugs(orderDrugs);
		order.setOrderPrescriptions(orderPrescriptions);

		/**
		 * 直接售药
		 */
		SellDrugRecordPojo sellDrugRecordPojo = new SellDrugRecordPojo();
		sellDrugRecordPojo.setSellDrugRecordId(1L);
		List<OrderDrug> orderDrugs2 = Lists.newArrayList();
		OrderDrug drug3 = new OrderDrug();
		drug3.setId(7L);
		drug3.setCount(2+"");
		drug3.setSalePrice(7.00);
		drug3.setSaleUnit("片");
		drug3.setReceivableAmt(14.00);
		drug3.setDrugName("药片");
		orderDrugs2.add(drug3);
		OrderDrug drug4 = new OrderDrug();
		drug4.setId(8L);
		drug4.setCount(4+"");
		drug4.setSalePrice(8.00);
		drug4.setSaleUnit("盒");
		drug4.setReceivableAmt(32.00);
		drug4.setDrugName("达克宁");
		orderDrugs2.add(drug4);
		sellDrugRecordPojo.setOrderDrugs(orderDrugs2);

		order.setSellDrugRecord(sellDrugRecordPojo);

		return order;
	}

}
