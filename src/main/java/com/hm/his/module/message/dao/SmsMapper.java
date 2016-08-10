package com.hm.his.module.message.dao;

import com.hm.his.module.message.model.SmsConfig;
import com.hm.his.module.message.model.SmsRecord;
import com.hm.his.module.message.pojo.SmsPatient;
import com.hm.his.module.message.pojo.SmsRequest;

import java.util.List;

/**
 * @author SuShaohua
 * @date 2016/8/1 20:13
 * @description
 */
public interface SmsMapper {
    List<SmsPatient> getSmsPatientList(SmsRequest req);

    List<SmsPatient> getSmsPatientListCount(SmsRequest req);

    Integer sendSmsRecord(SmsRecord req);

    SmsConfig getSmsConfig(Long hospitalId);

    List<SmsRecord> getSendedSmsList(SmsRequest req);

    SmsRecord getLastSmsRecord(Long patientId);
    
    Integer getSendedSmsListCount(SmsRequest req);

    void insertSmsRecord(List<SmsRecord> smsRecordList);

    void updateSmsConfig(SmsConfig smsConfig);

    Integer insertSmsConfig(SmsConfig config);
}
