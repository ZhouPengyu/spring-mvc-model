package com.hm.his.module.statistics.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.statistics.pojo.AdditionAmtStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.AdditionAmtStatisticsPojo;
import com.hm.his.module.statistics.pojo.DrugSaleStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.DrugSaleStatisticsPojo;
import com.hm.his.module.statistics.pojo.ExamStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.ExamStatisticsPojo;
import com.hm.his.module.statistics.pojo.OrderStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.OrderStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月1日
 */
@RestController
@RequestMapping("/statisticsTest")
public class StatisticsControllerTest {
	/**
	 * 
	 * @description 药品销售统计
	 * @date 2016年3月24日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/drugSaleStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String drugSaleStatistics(@RequestBody StatisticsRequest req) {
		DrugSaleStatisticsPojo body = new DrugSaleStatisticsPojo();
		body.setTotalPage(10);
		body.setTotalProfit(10D);
		body.setTotalSaleAmt(12d);
		List<DrugSaleStatisticsItemPojo> drugs = Lists.newArrayList();
		DrugSaleStatisticsItemPojo item = new DrugSaleStatisticsItemPojo();
		item.setCount(12D);
		item.setDrugId(1L);
		item.setDrugName("二甲双胍");
		item.setManufacturer("哈药六厂");
		item.setProfit(10.45D);
		item.setPurchaseAmt(12.40D);
		item.setSaleChannelName("处方开药");
		item.setSaleAmt(12D);
		item.setSaleUnit("盒");
		item.setSpecification("规格");
		drugs.add(item);
		body.setDrugs(drugs);
		return HisResponse.getInstance(body).toString();
	}

	@RequestMapping(value = "/exportDrugSaleStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String exportDrugSaleStatistics(@RequestBody StatisticsRequest req) {
		return HisResponse.getInstance().toString();
	}

	@RequestMapping(value = "/examStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String examStatistics(@RequestBody StatisticsRequest req) {
		ExamStatisticsPojo body = new ExamStatisticsPojo();
		body.setExamCount(100);
		List<ExamStatisticsItemPojo> exams = Lists.newArrayList();
		ExamStatisticsItemPojo pojo = new ExamStatisticsItemPojo();
		pojo.setCount(10);
		pojo.setExamName("血常规");
		pojo.setSaleAmt(12.45D);
		exams.add(pojo);
		body.setExams(exams);
		body.setTotalPage(10);
		body.setTotalSaleAmt(100.50D);
		return HisResponse.getInstance(body).toString();
	}

	@RequestMapping(value = "/exportExamStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String exportExamStatistics(@RequestBody StatisticsRequest req) {
		return HisResponse.getInstance().toString();
	}

	@RequestMapping(value = "/additionAmtStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String additionAmtStatistics(@RequestBody StatisticsRequest req) {
		AdditionAmtStatisticsPojo body = new AdditionAmtStatisticsPojo();
		body.setAdditionAmtCount(100);
		List<AdditionAmtStatisticsItemPojo> additionAmts = Lists.newArrayList();
		AdditionAmtStatisticsItemPojo pojo = new AdditionAmtStatisticsItemPojo();
		pojo.setAdditionAmtName("注射费");
		pojo.setCount(10);
		pojo.setSaleAmt(1000D);
		additionAmts.add(pojo);
		body.setAdditionAmts(additionAmts);
		body.setTotalPage(10);
		body.setTotalSaleAmt(89D);
		return HisResponse.getInstance(body).toString();
	}

	@RequestMapping(value = "/exportAdditionAmtStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String exportAdditionAmtStatistics(@RequestBody StatisticsRequest req) {
		return HisResponse.getInstance().toString();
	}

	@RequestMapping(value = "/orderStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String orderStatistics(@RequestBody StatisticsRequest req) {
		OrderStatisticsPojo body = new OrderStatisticsPojo();
		body.setGrossImpression(100);
		List<OrderStatisticsItemPojo> orders = Lists.newArrayList();
		OrderStatisticsItemPojo pojo = new OrderStatisticsItemPojo();
		pojo.setActualAmt(100.34D);
		pojo.setAge(10D);
		pojo.setAgeType("岁");
		pojo.setChargeDate(new Date());
		pojo.setDiagnosis("糖尿病");
		pojo.setDoctorName("李医生");
		pojo.setGender("男");
		pojo.setOrderNo("12121212");
		pojo.setOrderType(1);
		pojo.setPatientName("张三");
		pojo.setPhoneNo("13588886666");
		pojo.setReceivableAmt(65.88D);
		pojo.setRecordId(123L);
		pojo.setPatientId(1L);
		pojo.setSellDrugRecordId(12L);
		orders.add(pojo);
		body.setOrders(orders);
		body.setTotalActualAmt(100.80D);
		body.setTotalPage(1);
		body.setTotalReceivableAmt(190D);
		return HisResponse.getInstance(body).toString();
	}

	@RequestMapping(value = "/exportOrderStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String exportOrderStatistics(@RequestBody StatisticsRequest req) {
		return HisResponse.getInstance().toString();
	}
}
