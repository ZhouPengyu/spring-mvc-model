package com.hm.his.order.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.hm.his.module.order.pojo.ChargeRequest;
import com.hm.his.module.order.service.ChargeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class ChargeServiceTest {
	@Autowired
	ChargeService chargeService;

	@Test
	public void charge() {
		ChargeRequest req = new ChargeRequest();
		req.setOrderNo("120160321172644318");
		List<Long> orderPrescriptionIds = Lists.newArrayList();
		orderPrescriptionIds.add(558L);
		orderPrescriptionIds.add(559L);
		req.setOrderPrescriptionIds(orderPrescriptionIds);
		req.setActualAmt(250.50);
		chargeService.charge(req);

	}
}
