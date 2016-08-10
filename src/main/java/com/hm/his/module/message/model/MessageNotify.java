package com.hm.his.module.message.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author SuShaohua
 * @date 2016/7/28 17:02
 * @description
 */
public class MessageNotify {
    private Long doctorId;
    private Integer messageId;
    private Integer priority;
    private Integer status;
    private Integer boxId;
    private Long hospitalId;
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createDate;

    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date modifyDate;

    private Integer flag;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
