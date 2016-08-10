package com.hm.his.order.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.service.GetIsChargedService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })public class GetIsChargedServiceTest {
	@Autowired
	GetIsChargedService getIsChargedService;
	@Test
	public void isCharged() throws Exception {
		int a=getIsChargedService.isCharged(16765L, 2532L,OrderItemListType.EXAM );
		System.out.println(a);
	}
}
