package com.hm.his.module.message.pojo;

import java.util.List;

/**
 * @author SuShaohua
 * @date 2016/8/1 20:39
 * @description
 */
public class SmsResponse {
    List<SmsPatient> patientList;
    String signature;
    Integer totalSmsAmt;
    Integer surplusSmsAmt;
    Integer totalPage;

    public List<SmsPatient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<SmsPatient> patientList) {
        this.patientList = patientList;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getTotalSmsAmt() {
        return totalSmsAmt;
    }

    public void setTotalSmsAmt(Integer totalSmsAmt) {
        this.totalSmsAmt = totalSmsAmt;
    }

    public Integer getSurplusSmsAmt() {
        return surplusSmsAmt;
    }

    public void setSurplusSmsAmt(Integer surplusSmsAmt) {
        this.surplusSmsAmt = surplusSmsAmt;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
