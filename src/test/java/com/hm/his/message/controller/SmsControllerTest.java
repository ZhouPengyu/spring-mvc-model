package com.hm.his.message.controller;

import com.google.common.collect.Lists;
import com.hm.his.BaseControllerTest;
import com.hm.his.module.message.pojo.SmsRequest;
import org.junit.Test;

import java.util.List;

/**
 * @author SuShaohua
 * @date 2016/8/3 10:45
 * @description
 */
public class SmsControllerTest extends BaseControllerTest{
    @Test
    public void getSmsUserList(){
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setCurrentPage(1);
        smsRequest.setStartDate("2016-04-01");
        smsRequest.setEndDate("2016-08-14");
        smsRequest.setCondition("");
        super.testRole("/message/getSmsUserList", smsRequest);
    }

    @Test
    public void getSendedSmsList(){
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setStartDate("2016-06-14");
        smsRequest.setEndDate("2016-09-14");
        smsRequest.setCondition("");
        smsRequest.setSendStatus(null);
        smsRequest.setCurrentPage(1);
        super.testRole("/message/getSendedSmsList", smsRequest);
    }

    @Test
    public void sndSms(){
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setContent("您已预约周六牙科门诊，请按时就诊");
        List<Long> patientIdList = Lists.newArrayList(2113l);
        smsRequest.setPatientIdList(patientIdList);
        smsRequest.setSignature("惠每云诊所");
        super.testRole("/message/sendSms", smsRequest);
    }
}
