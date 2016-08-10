package com.hm.his.order.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hm.his.module.order.dao.OrderAdditionAmtMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.OrderAdditionAmt;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class OrderAdditionAmtMapperTest {
	@Autowired
	OrderAdditionAmtMapper dao;

	@Test
	public void selectByOrderItemListIdAndChargeStatus() {
		List<OrderAdditionAmt> list = dao.selectByOrderItemListIdAndChargeStatus(29L, ChargeStatus.TOCHARGE);
		System.out.println(JSON.toJSONString(list));
	}

	@Test
	public void selectById() {
		OrderAdditionAmt amt = dao.selectById(6L);
		System.out.println(JSON.toJSONString(amt));
	}
}
