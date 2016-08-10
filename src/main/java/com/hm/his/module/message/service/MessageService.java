package com.hm.his.module.message.service;

import com.hm.his.module.message.model.Message;
import com.hm.his.module.message.pojo.MessageRequestPojo;

import java.util.Map;

/**
 * @author SuShaohua
 * @date 2016/7/28 17:56
 * @description
 */
public interface MessageService {
    Map<String, Object> getMessageList(MessageRequestPojo req);

    Integer updateMessageAsterisk(Integer messageId);

    Integer deleteMessage(MessageRequestPojo req);

    Message getMessageDetail(Integer messageId);

    Integer saveAndSendMessage(MessageRequestPojo req);

    Integer allToRead(Integer boxId);

    Map<String,Integer> getMessageCount();
}
