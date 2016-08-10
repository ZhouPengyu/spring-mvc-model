package com.hm.his.order.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.pojo.GetOrderListParam;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class HospitalOrderDaoTest {
	@Autowired
	HospitalOrderMapper dao;

	@Test
	public void insert() throws Exception {
		HospitalOrder hospitalOrder = new HospitalOrder();
		hospitalOrder.setOrderNo("201603031042202332");
		hospitalOrder.setCreater(1L);
		hospitalOrder.setHospitalId(1L);
		hospitalOrder.setOrderType(HospitalOrder.TYPE_PATIENT_RECORD);
		hospitalOrder.setChargeStatus(ChargeStatus.TOCHARGE);
		hospitalOrder.setModifier(1L);
		hospitalOrder.setPatientId(12l);
		hospitalOrder.setPatientName("张三");
		hospitalOrder.setPatientRecordId(12L);
		hospitalOrder.setReceivableAmt(100.00);
		hospitalOrder.setTotalReceivableAmt(100.20D);
		dao.insert(hospitalOrder);
	}

	@Test
	public void selectHospitalOrders() throws Exception {
		GetOrderListParam param = new GetOrderListParam();
		param.setDoctorId(1L);
		param.setOrderColumn("create_date");
		param.setHospitalId(1L);
		param.setPageSize(10);
		param.setChargeStatus(ChargeStatus.TOCHARGE);
		List<HospitalOrder> orders = dao.selectHospitalOrders(param);
		System.out.println(orders.size());
	}

	@Test
	public void selectByRecordId() throws Exception {
		HospitalOrder orders = dao.selectByRecordId(13000L);
		System.out.println(orders.getReceivableAmt());
	}
}
