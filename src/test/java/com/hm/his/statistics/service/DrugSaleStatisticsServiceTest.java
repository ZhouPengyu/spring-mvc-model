package com.hm.his.statistics.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hm.his.module.statistics.pojo.DrugSaleStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.service.DrugSaleStatisticsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-conf.xml" })
public class DrugSaleStatisticsServiceTest {
	@Autowired
	DrugSaleStatisticsService drugSaleStatisticsService;

	@Test
	public void drugSaleStatistics() throws Exception {
		StatisticsRequest req = new StatisticsRequest();
		req.setStartDate("2016-03-05");
		req.setEndDate("2016-07-16");
		//req.setDrugName("阿莫西林胶囊");
	//	req.setSaleChannel(SaleChannel.Sell.getType());
//		req.setCurrentPage(1);
//		req.setPageSize(2);
		DrugSaleStatisticsPojo pojo = drugSaleStatisticsService.drugSaleStatistics(req);
		System.out.println(JSON.toJSONString(pojo));
	}
}
