package com.hm.his.module.message.pojo;

import com.hm.his.framework.model.PageRequest;
import com.hm.his.framework.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author SuShaohua
 * @date 2016/7/28 16:32
 * @description
 */
public class MessageRequestPojo extends PageRequest {
    private String messageHead;
    /**
     * 0 未读
     * 1 已读
     */
    private Integer status;
    /**
     * 1 收件箱
     * 2 已删除
     * 3 星标通知
     */
    private Integer boxId;

    /**************************************************************************/
    private String messageBody;

    private Integer priority;

    private Long doctorId;

    private Integer messageId;

    public String getMessageHead() {
        return messageHead;
    }

    public void setMessageHead(String messageHead) {
        this.messageHead = messageHead;
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

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public static void handleMessageRequest(MessageRequestPojo req) {
        req.setDoctorId(SessionUtils.getDoctorId());
    }
}
