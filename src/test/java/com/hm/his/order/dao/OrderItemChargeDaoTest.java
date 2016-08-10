package com.hm.his.order.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.his.module.order.dao.OrderItemChargeMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.OrderItemCharge;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class OrderItemChargeDaoTest {
	@Autowired
	OrderItemChargeMapper dao;

	public void initData() throws Exception {
		// List<OrderItemCharge> list = dao.selectAll();
		// for (OrderItemCharge item : list) {
		// item.setRefundDate(item.getModifyDate());
		// dao.update(item);
		// }
		// System.out.println(list.size());
	}

	@Test
	public void selectReceivableAmtSumByOrderNoAndChargeStatus() throws Exception {
		Integer chargeStatus = ChargeStatus.REFUND | ChargeStatus.CHARGED;
		Double re = dao.selectReceivableAmtSumByOrderNoAndChargeStatus("120160412142528313", chargeStatus);
		System.out.println(re);
	}
}
