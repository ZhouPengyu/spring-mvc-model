package com.hm.his.order.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.his.module.order.dao.OrderItemListMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemListType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class OrderItemListMapperTest {
	@Autowired(required = false)
	OrderItemListMapper dao;
	String orderNo = "";

	@Test
	public void insert() throws Exception {
		// 2.创建订单中的检查单对象
		OrderItemList examList = new OrderItemList();
		examList.setForeignId(1L);
		examList.setOrderNo(orderNo);
		examList.setRecordId(1L);
		examList.setType(OrderItemListType.EXAM.getType());
		examList.setChargeStatus(ChargeStatus.TOCHARGE);
		dao.insert(examList);
	}
}
