package com.hm.his.module.manage.service.impl;

import com.hm.his.module.manage.model.HospitalExam;
import com.hm.his.module.manage.service.HospitalExamService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/16
 * Time: 14:32
 * CopyRight:HuiMei Engine
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config.xml", "classpath:mybatis.xml" })

public class HospitalExamServiceImplTest {

    @Autowired
    HospitalExamService hospitalExamService;

    @Test
    public void testSearchHospitalExam() throws Exception {
        HospitalExam hospitalExam = new HospitalExam();
        hospitalExam.setHospitalId(1L);
        List<HospitalExam> hospitalExams = hospitalExamService.searchHospitalExam(null);
        System.out.println(hospitalExams);
    }
}