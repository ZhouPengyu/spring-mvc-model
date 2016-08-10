package com.hm.his.order.controller;

import org.junit.Test;

import com.hm.his.BaseControllerTest;
import com.hm.his.module.statistics.pojo.StatisticsRequest;

public class StatisticsControllerTest extends BaseControllerTest {
	String baseUrl = "/statistics";

	@Test
	public void drugSaleStatistics() {
		StatisticsRequest req = new StatisticsRequest();
		req.setStartDate("2016-03-01");
		req.setEndDate("2016-03-12");
		req.setDrugType(1);
		req.setDrugName("二甲双胍");
		req.setSaleChannel(1);
		req.setCurrentPage(1);
		req.setPageSize(50);
		super.testRole(baseUrl + "/drugSaleStatistics", req);
	}

	@Test
	public void examStatistics() {
		StatisticsRequest req = new StatisticsRequest();
		req.setStartDate("2016-03-01");
		req.setEndDate("2016-03-12");
		req.setExamName("血常规");
		req.setCurrentPage(1);
		req.setPageSize(10);

		super.testRole(baseUrl + "/examStatistics", req);
	}

	@Test
	public void additionAmtStatistics() {
		StatisticsRequest req = new StatisticsRequest();
		req.setStartDate("2016-03-01");
		req.setEndDate("2016-03-12");
		req.setAdditionAmtName("注射费");
		req.setCurrentPage(1);
		req.setPageSize(10);
		super.testRole(baseUrl + "/additionAmtStatistics", req);
	}

	@Test
	public void orderStatistics() {
		StatisticsRequest req = new StatisticsRequest();
		req.setStartDate("2016-03-01");
		req.setEndDate("2016-03-12");
		req.setPatientName("张三");
		req.setPhoneNo("13488886666");
		req.setCurrentPage(1);
		req.setPageSize(10);
		super.testRole(baseUrl + "/orderStatistics", req);
	}

	@Test
	public void exportDrugSaleStatistics() {
		StatisticsRequest req = new StatisticsRequest();
		req.setStartDate("2016-04-06");
		req.setEndDate("2016-04-06");
		super.testRole(baseUrl + "/exportDrugSaleStatistics", req);
	}
}
