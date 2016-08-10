package com.hm.his.module.message.dao;

import com.hm.his.module.message.model.Message;
import com.hm.his.module.message.model.MessageNotify;
import com.hm.his.module.message.pojo.MessageRequestPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author SuShaohua
 * @date 2016/7/28 16:16
 * @description
 */
public interface MessageMapper {
    Message findMessageById(Integer messageId);

    Integer updateMessageAsterisk(@Param("messageId") Integer messageId, @Param("doctorId") Long doctorId);

    Integer deleteMessage(MessageRequestPojo req);

    Message queryMessageStatus(@Param("messageId") Integer messageId, @Param("doctorId") Long doctorId);

    List<Message> queryMessageList(MessageRequestPojo requestPojo);

    Integer queryMessageListCount(MessageRequestPojo req);

    Integer saveMessage(Message message);

    Integer sendMessage(List<MessageNotify> notifyList);

    Integer updateMessageStatus(@Param("messageId") Integer messageId, @Param("doctorId") Long doctorId);

    Integer allToRead(Map<String, Object> param);

    List<Message> queryReadCount(Map<String, Object> param);
}
