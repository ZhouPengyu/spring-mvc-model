package com.hm.his.module.message.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author SuShaohua
 * @date 2016/7/28 16:05
 * @description
 */
public class Message {

    private Integer messageId;

    private String messageHead;

    private String messageBody;

    private Long creater;

    private Long hospitalId;

    private Integer url;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;

    private Integer flag;

    /************************************************************/
    /**
     * 1 普通通知
     * 2 置顶通知
     */
    private Integer priority;
    /**
     * 0 未读
     * 1 已读
     */
    private Integer status;

    private Integer boxId;

    private Integer count;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getMessageHead() {
        return messageHead;
    }

    public void setMessageHead(String messageHead) {
        this.messageHead = messageHead;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getUrl() {
        return url;
    }

    public void setUrl(Integer url) {
        this.url = url;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
