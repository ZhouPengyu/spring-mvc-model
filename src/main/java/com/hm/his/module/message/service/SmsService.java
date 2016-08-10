package com.hm.his.module.message.service;

import com.hm.his.module.message.model.SmsConfig;
import com.hm.his.module.message.pojo.SmsPatient;
import com.hm.his.module.message.pojo.SmsRequest;

import java.util.List;

/**
 * @author SuShaohua
 * @date 2016/8/2 10:19
 * @description
 */
public interface SmsService {
    List<SmsPatient> getSmsPatientList(SmsRequest req) throws Exception;

    SmsConfig getHospitalSmsConfig(Long hospitalId);

    Integer getSmsPatientListCount(SmsRequest req);

    List<SmsPatient> getSendedSmsList(SmsRequest req);

    Integer getSendedSmsListCount(SmsRequest req);

    Integer sendSms(SmsRequest req);
}
