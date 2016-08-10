package com.hm.his.module.message.controller;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.module.message.model.Message;
import com.hm.his.module.message.pojo.MessageRequestPojo;
import com.hm.his.module.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author SuShaohua
 * @date 2016/7/28 17:54
 * @description
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    /**
     * @param
     * @description 获取消息列表
     * @author SuShaohua
     * @date 2016-07-29 12:37
     */
    @RequestMapping(value = "/getMessageList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getMessageList(@RequestBody MessageRequestPojo req) {
        HisResponse hisResponse = new HisResponse();
        Map<String, Object> body = messageService.getMessageList(req);
        hisResponse.setBody(body);
        return hisResponse.toString();
    }

    /**
     * @param
     * @description 给消息加星标或者取消星标
     * @author SuShaohua
     * @date 2016-07-29 12:37
     */
    @RequestMapping(value = "/asterisk", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String asterisk(@RequestBody Map<String, Integer> req) {
        HisResponse hisResponse = new HisResponse();
        Integer messageId = LangUtils.getInteger(req.get("messageId"));
        Integer result = messageService.updateMessageAsterisk(messageId);
        hisResponse.setBody(result);
        return hisResponse.toString();
    }

    /**
     * @param
     * @description 用户删除消息
     * @author SuShaohua
     * @date 2016-07-29 12:36
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestBody MessageRequestPojo req) {
        HisResponse hisResponse = new HisResponse();
        Integer result = messageService.deleteMessage(req);
        hisResponse.setBody(result);
        return hisResponse.toString();
    }

    /**
     * @param
     * @description 查看消息详情
     * @author SuShaohua
     * @date 2016-07-29 12:36
     */
    @RequestMapping(value = "/messageDetail", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String messageDetail(@RequestBody Map<String, Integer> req) {
        HisResponse hisResponse = new HisResponse();
        Integer messageId = LangUtils.getInteger(req.get("messageId"));
        Message message = messageService.getMessageDetail(messageId);
        hisResponse.setBody(message);
        return hisResponse.toString();
    }

    /**
     * @param
     * @description 全部标为已读
     * @author SuShaohua
     * @date 2016-08-01 16:48
     */
    @RequestMapping(value = "/allToRead", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String allToRead(@RequestBody Map<String, Integer> req) {
        HisResponse hisResponse = new HisResponse();
        Integer boxId = LangUtils.getInteger(req.get("boxId"));
        Integer result = messageService.allToRead(boxId);
        hisResponse.setBody(result);
        return hisResponse.toString();
    }

    /**
     * @param
     * @description 发送消息
     * @author SuShaohua
     * @date 2016-07-29 12:36
     */
    @RequestMapping(value = "/saveAndSendMessage", produces = "application/json;charset=UTF-8", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String saveAndSendMessage(@RequestBody MessageRequestPojo req) {
        HisResponse hisResponse = new HisResponse();
        Integer result = messageService.saveAndSendMessage(req);
        hisResponse.setBody(result);
        return hisResponse.toString();
    }

    /**
     * @description 消息数量分类统计
     * @author SuShaohua
     * @date 2016-08-10 14:13
     * @param
    */
    @RequestMapping(value = "/getMessageCount", produces = "application/json;charset=UTF-8", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getMessageCount(){
        HisResponse hisResponse = new HisResponse();
        Map<String, Integer> result = messageService.getMessageCount();
        hisResponse.setBody(result);
        return hisResponse.toString();
    }
}
