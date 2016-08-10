package com.hm.his.module.message.model;

import java.util.Date;

/**
 * @author SuShaohua
 * @date 2016/8/1 20:34
 * @description
 */
public class SmsConfig {
    private Integer id;
    private Long hospitalId;
    private String serviceNumber;
    private Integer smsUpperLimit;
    private Double surplusAmount;
    private Integer status;
    private String signature;
    private Date createDate;
    private Date modifyDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public Integer getSmsUpperLimit() {
        return smsUpperLimit;
    }

    public void setSmsUpperLimit(Integer smsUpperLimit) {
        this.smsUpperLimit = smsUpperLimit;
    }

    public Double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(Double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
