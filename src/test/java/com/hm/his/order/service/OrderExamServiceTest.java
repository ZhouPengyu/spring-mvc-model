package com.hm.his.order.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hm.his.module.order.service.OrderExamService;
import com.hm.his.module.statistics.pojo.NameRequst;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class OrderExamServiceTest {
	@Autowired
	OrderExamService orderExamService;

	@Test
	public void searchOrderExamNames() throws Exception {
		NameRequst req = new NameRequst();
		req.setName("常规");
		List<String> list = orderExamService.searchOrderExamNames(req);
		System.out.println(JSON.toJSONString(list));
	}
}
