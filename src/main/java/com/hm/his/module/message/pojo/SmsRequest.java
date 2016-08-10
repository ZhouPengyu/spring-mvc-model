package com.hm.his.module.message.pojo;

import com.hm.his.framework.model.PageRequest;
import com.hm.his.framework.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author SuShaohua
 * @date 2016/8/1 20:32
 * @description
 */
public class SmsRequest extends PageRequest {
    private String startDate;
    private String endDate;
    private String condition;
    private Integer sendStatus;
    private Long hospitalId;

    private String content;
    private List<Long> patientIdList;
    private String signature;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getPatientIdList() {
        return patientIdList;
    }

    public void setPatientIdList(List<Long> patientIdList) {
        this.patientIdList = patientIdList;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public static void handleSmsRequest(SmsRequest req) {
        if (null == req.getHospitalId()) {
            req.setHospitalId(SessionUtils.getHospitalId());
        }
        if (StringUtils.isEmpty(req.getStartDate()) && StringUtils.isEmpty(req.getEndDate())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            req.setEndDate(sdf.format(calendar.getTime()) + " 23:59:59");
            calendar.set(Calendar.DATE, 1);
            req.setStartDate(sdf.format(calendar.getTime()) + " 00:00:00");
        } else {
            req.setStartDate(req.getStartDate() + " 00:00:00");
            req.setEndDate(req.getEndDate() + " 23:59:59");
        }
    }
}
