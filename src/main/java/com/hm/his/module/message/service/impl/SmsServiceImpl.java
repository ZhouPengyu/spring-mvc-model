package com.hm.his.module.message.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.ChineseToEnglish;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.framework.utils.SmsCaptchaUtil;
import com.hm.his.module.message.dao.SmsMapper;
import com.hm.his.module.message.model.SmsConfig;
import com.hm.his.module.message.model.SmsRecord;
import com.hm.his.module.message.pojo.SmsPatient;
import com.hm.his.module.message.pojo.SmsRequest;
import com.hm.his.module.message.service.SmsService;
import com.hm.his.module.outpatient.model.Patient;
import com.hm.his.module.outpatient.model.PatientDiagnosis;
import com.hm.his.module.outpatient.model.PatientInquiry;
import com.hm.his.module.outpatient.service.PatientDiagnosisService;
import com.hm.his.module.outpatient.service.PatientInquiryService;
import com.hm.his.module.outpatient.service.PatientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author SuShaohua
 * @date 2016/8/2 10:21
 * @description
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    PatientService patientService;
    @Autowired
    PatientInquiryService patientInquiryService;
    @Autowired
    PatientDiagnosisService patientDiagnosisService;
    @Autowired(required = false)
    SmsMapper smsMapper;

    @Override
    public List<SmsPatient> getSmsPatientList(SmsRequest req) throws Exception {
        SmsRequest.handleSmsRequest(req);
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("hospitalId", SessionUtils.getHospitalId());
        parameterMap.put("patientConditions", req.getCondition());
        parameterMap.put("diagnosisDateStart", req.getStartDate());
        parameterMap.put("diagnosisDate", req.getEndDate());
        parameterMap.put("startRecord", req.getStartRecord());
        parameterMap.put("pageSize", req.getPageSize());
        String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        String reg = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        String phoneReg = "^[1][34578][0-9]{9}$";
        parameterMap.put("phoneReg", phoneReg);
        List<Patient> patientList = patientService.searchPatientWithValidPhone(parameterMap);
        List<SmsPatient> smsPatientList = new ArrayList<>();
        for (Patient patient : patientList) {
            SmsPatient smsPatient = new SmsPatient();
            PatientInquiry patientInquiry = patientInquiryService.getInquiryNewByPatientId(patient.getPatientId());
            SmsRecord smsRecord = smsMapper.getLastSmsRecord(patient.getPatientId());
            String diagnosis = "";
            if (patientInquiry != null) {
                List<PatientDiagnosis> patientDiagnosisList = patientDiagnosisService.getDiagnosisByRecordId(patientInquiry.getRecordId());
                List<String> diagnosisList = patientDiagnosisList.stream().map(patientDiagnosis -> {
                    return patientDiagnosis.getDiseaseName();
                }).collect(Collectors.toList());
                diagnosis = String.join("，", diagnosisList);
                String modifyDate = patientInquiry.getModifyDate();
                Integer endIndex = -1 == modifyDate.indexOf(" ") ? modifyDate.length() : modifyDate.indexOf(" ");
                smsPatient.setLastVisitTime(modifyDate.substring(0, endIndex));
//                String diagnosisDate = patient.getDiagnosisDate();
//                Integer endIndex = -1 == diagnosisDate.indexOf(" ") ? diagnosisDate.length() : diagnosisDate.indexOf(" ");
//                smsPatient.setLastVisitTime(diagnosisDate.substring(0, endIndex));
            }
            smsPatient.setDiagnosis(diagnosis);
            smsPatient.setPatientId(patient.getPatientId());
            smsPatient.setPatientName(patient.getPatientName());
            smsPatient.setGender(LangUtils.getInteger(patient.getGender()));
            smsPatient.setAge(patient.getAge());
            smsPatient.setAgeType(patient.getAgeType());
            smsPatient.setPhoneNo(patient.getPhoneNo());
            smsPatient.setLastSendTime(null == smsRecord ? null : smsRecord.getModifyDate());
            if (StringUtils.isEmpty(patient.getAgeType())) {
                //得到当前的年份
                String cYear = DateUtil.formatDate(new Date()).substring(0, 4);
                //得到生日年份
                String birthYear = patient.getBirthday().substring(0, 4);
                int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
                patient.setAge(Double.valueOf(age));
                smsPatient.setAge(patient.getAge());
                smsPatient.setAgeType("岁");
            }
            smsPatientList.add(smsPatient);
            // TODO: 2016/8/2  直接售药用户未合并, 考虑修改直接售药的接口
        }
        return smsPatientList;
    }

    @Override
    public SmsConfig getHospitalSmsConfig(Long hospitalId) {
        SmsConfig smsConfig = new SmsConfig();
        if (null != hospitalId) {
            smsConfig = smsMapper.getSmsConfig(hospitalId);
        }
        if (null == smsConfig){
            SmsConfig config = new SmsConfig();
            config.setHospitalId(SessionUtils.getHospitalId());
            config.setSmsUpperLimit(300);
            config.setSurplusAmount(300D);
            config.setStatus(1);
            insertSmsConfig(config);
            return config;
        }else
            return smsConfig;
    }

    private Integer insertSmsConfig(SmsConfig config) {
        return smsMapper.insertSmsConfig(config);
    }

    @Override
    public Integer getSmsPatientListCount(SmsRequest req) {
        SmsRequest.handleSmsRequest(req);
        Integer result = 0;
        try {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("hospitalId", SessionUtils.getHospitalId());
            parameterMap.put("patientConditions", req.getCondition());
            parameterMap.put("diagnosisDateStart", req.getStartDate());
            parameterMap.put("diagnosisDate", req.getEndDate());
            String phoneReg = "^[1][34578][0-9]{9}$";
            parameterMap.put("phoneReg", phoneReg);
            result = patientService.searchPatientWithValidPhoneCount(parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<SmsPatient> getSendedSmsList(SmsRequest req) {
        SmsRequest.handleSmsRequest(req);
        List<SmsRecord> smsRecordList = smsMapper.getSendedSmsList(req);
        Map<Long, Patient> patientMap = Maps.newHashMap();
        List<Long> patientIdList = smsRecordList.stream().map(smsRecord -> {
            return smsRecord.getRecipient();
        }).collect(Collectors.toList());
        List<Patient> patientList = Lists.newArrayList();
        if (null != patientIdList && patientIdList.size() > 0) {
            patientList = patientService.findPatientListByIds(patientIdList);
        }
        if (null != patientList && patientList.size() > 0) {
            for (Patient patient : patientList) {
                patientMap.put(patient.getPatientId(), patient);
            }
        }
        List<SmsPatient> smsPatientList = Lists.newArrayList();
        for (SmsRecord smsRecord : smsRecordList) {
            SmsPatient smsPatient = new SmsPatient();
            smsPatient.setSendStatus(smsRecord.getStatus());
            smsPatient.setSmsContent(smsRecord.getContent());
            smsPatient.setLastSendTime(smsRecord.getModifyDate());
            smsPatient.setOperator(smsRecord.getOperator());

            Patient patient = patientMap.get(smsRecord.getRecipient());
            smsPatient.setAge(patient.getAge());
            smsPatient.setAgeType(patient.getAgeType());
            smsPatient.setGender(LangUtils.getInteger(patient.getGender()));
            smsPatient.setPatientName(patient.getPatientName());
            smsPatient.setPhoneNo(patient.getPhoneNo());
            if (StringUtils.isEmpty(patient.getAgeType())) {
                //得到当前的年份
                String cYear = DateUtil.formatDate(new Date()).substring(0, 4);
                //得到生日年份
                String birthYear = patient.getBirthday().substring(0, 4);
                int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
                patient.setAge(Double.valueOf(age));
                smsPatient.setAge(patient.getAge());
                smsPatient.setAgeType("岁");
            }
            smsPatientList.add(smsPatient);
        }
        return smsPatientList;
    }

    @Override
    public Integer getSendedSmsListCount(SmsRequest req) {
        SmsRequest.handleSmsRequest(req);
        Integer result = 0;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        result = smsMapper.getSendedSmsListCount(req);
        return result;
    }

    @Override
    public Integer sendSms(SmsRequest req) {
        SmsRequest.handleSmsRequest(req);
        Integer success = 0;
        String content = req.getContent();
        String signature = req.getSignature();
        List<Long> patientIdList = req.getPatientIdList();
        List<Patient> patientList = patientService.findPatientListByIds(patientIdList);
        SmsConfig smsConfig = getHospitalSmsConfig(SessionUtils.getHospitalId());
        Double surplusAmount = null == smsConfig.getSurplusAmount() ? 0D : LangUtils.getDouble(smsConfig.getSurplusAmount());
        List<SmsRecord> smsRecordList = Lists.newArrayList();
        if (null != patientList && patientList.size() > 0) {
            for (Patient patient : patientList) {
                String hospitalPhoneNo = patient.getPhoneNo();
                String smsDetail = content + "【" + signature + "】";
                Double billAmount = smsDetail.length() < 64 ? 1D : (Math.ceil(smsDetail.length() / 60D));
                Integer status = 0;
                if (billAmount <= surplusAmount) {
                    status = 1 == SmsCaptchaUtil.sendSms(hospitalPhoneNo, smsDetail) ? 1 : 2;
                    if (1 == status){
                        surplusAmount = AmtUtils.subtract(surplusAmount, billAmount);
                        success++;
                    }
                } else {
                    status = 2;
                }
                SmsRecord smsRecord = new SmsRecord();
                smsRecord.setSender(SessionUtils.getDoctorId());
                smsRecord.setSmsType(3);
                smsRecord.setContent(content);
                smsRecord.setBillAmount(billAmount);
                smsRecord.setRecipient(patient.getPatientId());
                smsRecord.setPhoneNo(hospitalPhoneNo);
                smsRecord.setStatus(status);
                smsRecord.setSignature(signature);
                smsRecord.setRecipientName(patient.getPatientName());
                smsRecord.setRecipientNamePinyin(ChineseToEnglish.getPingYin(patient.getPatientName()));
                smsRecordList.add(smsRecord);
            }
            smsMapper.insertSmsRecord(smsRecordList);
            smsConfig.setSignature(signature);
            smsConfig.setSurplusAmount(surplusAmount);
            smsMapper.updateSmsConfig(smsConfig);
        }
        return success;
    }
}






















