package com.hm.his.drug.dao;

import com.hm.his.module.drug.dao.HospitalDrugMapper;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class HospitalDrugDaoTest {
	@Autowired(required = false)
	HospitalDrugMapper dao;

	/*@Test
	public void insert() throws Exception {
		HospitalOrder hospitalOrder = new HospitalOrder();
		hospitalOrder.setOrderNo("2016030310422003112");
		hospitalOrder.setCreater(1L);
		hospitalOrder.setHospitalId(1L);
		hospitalOrder.setOrderType(HospitalOrder.TYPE_PATIENT_RECORD);
		hospitalOrder.setChargeStatus(ChargeStatus.TOCHARGE);
		hospitalOrder.setModifier(1L);
		hospitalOrder.setPatientId(12l);
		hospitalOrder.setPatientName("张三");
		hospitalOrder.setPatientRecordId(12L);
		hospitalOrder.setReceivableAmt(100.00);
		dao.insert(hospitalOrder);
	}*/

	@Test
	public void selectHospitalDrug() throws Exception {
		DrugRequest request = new DrugRequest();
		//request.setDrugName("克拉霉素片");
//		request.setDrugType(3);
		request.setStatus(1);
		request.setCurrentPage(1);
		request.setPageSize(15);
		List<HospitalDrug> drugs = dao.selectHospitalDrug(request);

		System.out.println(drugs);
	}
}
