package com.hm.his.outpatient;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.hm.his.BaseControllerTest;
import com.hm.his.module.outpatient.model.Patient;

public class PatientControllerTest  extends BaseControllerTest {

    @Test
    public void Login() throws Exception {
    	Patient patient = new Patient();
    	patient.setPatientId(9926l);
    	patient.setPatientName("zpys");
    	patient.setIdCardNo("cccccccccccccccc");
    	patient.setAge(23D);
    	patient.setAgeType("岁");
        super.testRole("outpatient/savePatientInfo", JSON.toJSONString(patient));
    }
    
    @Test
    public void getPatientRecords() throws Exception {
    	Patient patient = new Patient();
    	patient.setPatientId(10531l);
        super.testRole("outpatient/getPatientRecords", JSON.toJSONString(patient));
    }

}
