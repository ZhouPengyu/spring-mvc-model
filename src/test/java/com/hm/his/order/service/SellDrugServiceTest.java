package com.hm.his.order.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.SellDrug;
import com.hm.his.module.order.model.SellDrugRecord;
import com.hm.his.module.order.pojo.SellDrugInfoPojo;
import com.hm.his.module.order.service.SellDrugService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class SellDrugServiceTest {
	@Autowired
	SellDrugService sellDrugService;

	@Test
	public void sellDrugRecord() {
		SellDrugRecord req = new SellDrugRecord();
		req.setAge(20D);
		req.setAgeType("岁");
		req.setGender(1);
		req.setPatientName("张三");
		req.setActualAmt(100.23);
		req.setIdCardNo("1246546464");
		List<SellDrug> drugs = Lists.newArrayList();
		SellDrug d = new SellDrug();
		d.setDrugId(19L);
		d.setDrugName("药名");
		d.setSalePrice(12.00);
		d.setSaleUnit("盒");
		d.setCount(1);
		d.setSpecification("规格");
		drugs.add(d);
		req.setDrugs(drugs);
		sellDrugService.sellDrugs(req);

	}

	@Test
	public void getSellDrugInfo() throws Exception {
		HospitalOrder req = new HospitalOrder();
		req.setOrderNo("120160401104056190");
		SellDrugInfoPojo pojo = sellDrugService.getSellDrugInfo(req);
		System.out.println(JSON.toJSONString(pojo));
	}
}
