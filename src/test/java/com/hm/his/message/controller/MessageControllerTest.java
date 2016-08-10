package com.hm.his.message.controller;

import com.google.common.collect.Maps;
import com.hm.his.BaseControllerTest;
import com.hm.his.module.message.pojo.MessageRequestPojo;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * @author SuShaohua
 * @date 2016/7/29 10:40
 * @description
 */
public class MessageControllerTest extends BaseControllerTest {
    @Test
    public void saveAndSendMessage() {
        MessageRequestPojo req = new MessageRequestPojo();
        req.setMessageHead("欢迎使用惠每云诊所系统2");
        req.setMessageBody("dfdre惠每会二米dfdfdfdf=所系统,欢迎使用惠每云诊所系统,欢迎使用惠每云诊所系统,欢迎使用惠每云诊所系统,欢迎使用惠每云诊所系统");
        req.setPriority(1);
        super.testRole("message/saveAndSendMessage", req);
    }

    @Test
    public void messageDetail() {
        Map<String, Integer> req = Maps.newHashMap();
        req.put("messageId", 10);
        super.testRole("message/messageDetail", req);
    }

    @Test
    public void delete() {
        Map<String, Object> req = Maps.newHashMap();
        req.put("messageIdList", Arrays.asList(10, 11));
        super.testRole("message/delete", req);
    }

    @Test
    public void asterisk(){
        Map<String, Integer> req = Maps.newHashMap();
        req.put("messageId", 10);
        super.testRole("message/asterisk", req);
    }

    @Test
    public void messageList(){
        MessageRequestPojo req = new MessageRequestPojo();
        req.setStatus(null);
        req.setMessageHead("");
        req.setBoxId(0);
        super.testRole("message/getMessageList", req);
    }
}
