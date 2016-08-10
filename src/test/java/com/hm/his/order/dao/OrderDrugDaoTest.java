package com.hm.his.order.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.dao.OrderAdditionAmtMapper;
import com.hm.his.module.order.dao.OrderDrugMapper;
import com.hm.his.module.order.dao.OrderExamMapper;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.statistics.pojo.StatisticsRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class OrderDrugDaoTest {
	@Autowired
	OrderDrugMapper orderDrugMapper;
	@Autowired
	OrderExamMapper orderExamMapper;
	@Autowired
	OrderAdditionAmtMapper orderAdditionAmtMapper;
	@Autowired
	HospitalOrderMapper hospitalOrderMapper;

	@Test
	public void insert() throws Exception {
		OrderDrug orderDrug = new OrderDrug();
		orderDrug.setChargeStatus(1);
		orderDrug.setDrugId(12L);
		orderDrug.setOrderNo("123");
		orderDrug.setHospitalId(1L);
		orderDrug.setDrugName("药");
		orderDrug.setManufacturer("");
		orderDrug.setSalePrice(12D);
		orderDrug.setSaleUnit("盒");
		orderDrug.setSpecification("规格");
		OrderDrug orderDrug1 = new OrderDrug();
		orderDrug1.setChargeStatus(1);
		orderDrug1.setDrugId(12L);
		orderDrug1.setOrderNo("123");
		orderDrug.setHospitalId(1L);
		orderDrug1.setDrugName("药");
		orderDrug1.setManufacturer("");
		orderDrug1.setSalePrice(12D);
		orderDrug1.setSaleUnit("盒");
		orderDrug1.setSpecification("规格");

		List<OrderDrug> list = Lists.newArrayList();
		list.add(orderDrug1);
		list.add(orderDrug);

		orderDrugMapper.insertList(list);
		System.out.println(list.get(0).getId());
	}

	@Test
	public void selectById() throws Exception {
		OrderDrug orderDrug = orderDrugMapper.selectById(1170L);
		System.out.println(orderDrug.getDrugName() + "," + orderDrug.getReceivableAmt());

	}

	/**
	 * 
	 * @description 初始化hospitalId
	 * @date 2016年3月29日
	 * @author lipeng
	 * @throws Exception
	 */
	@Test
	public void initData() throws Exception {
		List<OrderDrug> orderDrugs = orderDrugMapper.selectAll();
		for (OrderDrug orderDrug : orderDrugs) {
			HospitalOrder order = hospitalOrderMapper.selectByOrderNo(orderDrug.getOrderNo());
			if (order != null) {
				orderDrug.setHospitalId(order.getHospitalId());
				orderDrugMapper.update(orderDrug);
			}
		}
		List<OrderExam> orderExams = orderExamMapper.selectAll();
		for (OrderExam orderExam : orderExams) {
			HospitalOrder order = hospitalOrderMapper.selectByOrderNo(orderExam.getOrderNo());
			if (order != null) {
				orderExam.setHospitalId(order.getHospitalId());
				orderExamMapper.update(orderExam);
			}
		}
		List<OrderAdditionAmt> orderAdditionAmts = orderAdditionAmtMapper.selectAll();
		for (OrderAdditionAmt orderAdditionAmt : orderAdditionAmts) {
			HospitalOrder order = hospitalOrderMapper.selectByOrderNo(orderAdditionAmt.getOrderNo());
			if (order != null) {
				orderAdditionAmt.setHospitalId(order.getHospitalId());
				orderAdditionAmtMapper.update(orderAdditionAmt);
			}
		}

	}

	@Test
	public void selectByStatisticsRequest() throws Exception {
		StatisticsRequest req = new StatisticsRequest();
		req.setHospitalId(1L);
		req.setDrugName("片");
		List<OrderDrug> orderDrugs = orderDrugMapper.selectByStatisticsRequest(req);
		System.out.println(JSON.toJSONString(orderDrugs));
	}

}
