package com.hm.his.statistics.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hm.his.module.statistics.pojo.AdditionAmtStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.service.AdditionAmtStatisticsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class AdditionAmtStatisticsServiceTest {
	@Autowired
	AdditionAmtStatisticsService additionAmtStatisticsService;

	@Test
	public void additionAmtStatistics() throws Exception {
		StatisticsRequest req = new StatisticsRequest();
		AdditionAmtStatisticsPojo pojo = additionAmtStatisticsService.additionAmtStatistics(req);
		System.out.println(JSON.toJSONString(pojo));
	}
}
