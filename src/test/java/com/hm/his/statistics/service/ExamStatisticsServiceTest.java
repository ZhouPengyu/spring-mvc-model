package com.hm.his.statistics.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hm.his.module.statistics.pojo.ExamStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.service.ExamStatisticsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class ExamStatisticsServiceTest {
	@Autowired
	ExamStatisticsService examStatisticsService;

	@Test
	public void examStatistics() throws Exception {
		StatisticsRequest req = new StatisticsRequest();
		req.setExamName("常规");
		ExamStatisticsPojo pojo = examStatisticsService.examStatistics(req);
		System.out.println(JSON.toJSONString(pojo));
	}
}
