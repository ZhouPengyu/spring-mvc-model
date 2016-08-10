package com.hm.his.outpatient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hm.his.module.outpatient.dao.PatientMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })
public class PatientDaoTest {
	@Autowired
	PatientMapper dao;

	@Test
	public void testName() throws Exception {
		System.out.println("开始---------------------------");
		long start=System.currentTimeMillis();
		long end=System.currentTimeMillis();
		System.out.println((end-start));
		
	}
}
