package com.hm.his.framework.utils;

import org.apache.commons.lang3.StringUtils;

import com.hm.his.module.outpatient.model.PatientInquiry;

/**
 * Created by wangjialin on 15/12/22.
 */
public class PatientRecordUtils {
    public static String getRecordAbstract(PatientInquiry patientInquiry){
        String result = "";
        if (StringUtils.isNotEmpty(patientInquiry.getSymptom())){
            result = patientInquiry.getSymptom();
        } else if (StringUtils.isNotEmpty(patientInquiry.getPresentIllness())){
            result = patientInquiry.getPresentIllness();
        } else if (StringUtils.isNotEmpty(patientInquiry.getPhysExam())){
            result = patientInquiry.getPhysExam();
        } else {
            result = "该病历未录入症状、现病史或查体。";
        }
        return result;
    }
}
