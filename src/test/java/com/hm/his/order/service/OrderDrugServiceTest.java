package com.hm.his.order.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.statistics.pojo.NameRequst;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class OrderDrugServiceTest {
	@Autowired
	OrderDrugService orderDrugService;

	@Test
	public void searchOrderDrugNames() throws Exception {
		NameRequst req = new NameRequst();
		req.setName("阿莫西林");
		List<String> list=orderDrugService.searchOrderDrugNames(req);
		System.out.println(JSON.toJSONString(list));
	}
	@Test
	public void hasOrderByDrugIdAndDataSource() throws Exception {
		boolean re=orderDrugService.hasOrderByDrugIdAndDataSource(1222L, 0);
		System.out.println(re);
	}
}
