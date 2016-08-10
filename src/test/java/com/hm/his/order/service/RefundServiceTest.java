package com.hm.his.order.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.hm.his.module.order.pojo.PrescriptionAndItemIds;
import com.hm.his.module.order.pojo.RefundRequest;
import com.hm.his.module.order.pojo.SellDrugRecordAndItemIds;
import com.hm.his.module.order.service.RefundService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class RefundServiceTest {
	@Autowired
	RefundService refundService;

	@Test
	public void refund() {
		RefundRequest req = new RefundRequest();
		req.setOrderNo("120160321105857275");
		List<PrescriptionAndItemIds> orderPrescriptions = Lists.newArrayList();
		PrescriptionAndItemIds orderPrescriptionPojo = new PrescriptionAndItemIds();
		orderPrescriptionPojo.setOrderPrescriptionId(339L);
		List<Long> orderDrugIds = Lists.newArrayList();
		orderDrugIds.add(133L);
		orderPrescriptionPojo.setOrderDrugIds(orderDrugIds);
		orderPrescriptions.add(orderPrescriptionPojo);
		// PrescriptionAndItemIds orderPrescriptionPojo1 = new
		// PrescriptionAndItemIds();
		// orderPrescriptionPojo1.setOrderPrescriptionId(340L);
		// List<Long> orderDrugIds1 = Lists.newArrayList();
		// orderDrugIds1.add(134L);
		// orderPrescriptionPojo1.setOrderDrugIds(orderDrugIds1);
		// orderPrescriptions.add(orderPrescriptionPojo1);
		// PrescriptionAndItemIds orderPrescriptionPojo1 = new
		// PrescriptionAndItemIds();
		// orderPrescriptionPojo1.setOrderPrescriptionId(28L);
		// orderPrescriptions.add(orderPrescriptionPojo1);
		req.setOrderPrescriptions(orderPrescriptions);
		req.setRefundAmt(90D);
		refundService.refund(req);

	}

	@Test
	public void refundSellDrug() {
		RefundRequest req = new RefundRequest();
		req.setOrderNo("120160321105857275");
		req.setRefundAmt(90D);
		SellDrugRecordAndItemIds sellDrugRecord = new SellDrugRecordAndItemIds();
		sellDrugRecord.setSellDrugRecordId(450L);
		List<Long> orderDrugIds = Lists.newArrayList();
		orderDrugIds.add(224L);
		sellDrugRecord.setOrderDrugIds(orderDrugIds);
		req.setSellDrugRecord(sellDrugRecord);
		refundService.refund(req);

	}
}
