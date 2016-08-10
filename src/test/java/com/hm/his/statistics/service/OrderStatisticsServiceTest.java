package com.hm.his.statistics.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hm.his.module.statistics.pojo.OrderStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.service.OrderStatisticsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-conf.xml" })
public class OrderStatisticsServiceTest {
	@Autowired
	OrderStatisticsService orderStatisticsService;

	@Test
	public void orderStatistics() throws Exception {
		StatisticsRequest req = new StatisticsRequest();
		req.setStartDate("2016-04-01");
		req.setEndDate("2016-07-17");
		System.out.println(JSON.toJSONString(req));
		OrderStatisticsPojo pojo = orderStatisticsService.orderStatistics(req);
		System.out.println(JSON.toJSONString(pojo));
	}
}
