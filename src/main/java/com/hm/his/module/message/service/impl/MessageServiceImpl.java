package com.hm.his.module.message.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.message.dao.MessageMapper;
import com.hm.his.module.message.model.Message;
import com.hm.his.module.message.model.MessageNotify;
import com.hm.his.module.message.pojo.MessageRequestPojo;
import com.hm.his.module.message.service.MessageService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author SuShaohua
 * @date 2016/7/28 18:01
 * @description
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired(required = false)
    MessageMapper messageMapper;
    @Autowired
    DoctorService doctorService;

    @Override
    public Map<String, Object> getMessageList(MessageRequestPojo req) {
        MessageRequestPojo.handleMessageRequest(req);
        Map<String, Object> map = Maps.newHashMap();
        if (null == req.getBoxId() || 0 == req.getBoxId()) {
            req.setBoxId(1);
            req.setStatus(0);
        }
        List<Message> messageList = messageMapper.queryMessageList(req);
        Map<Integer, List<Message>> messageListMap = messageList.stream().collect(Collectors.groupingBy(message -> {
            return 1 == message.getPriority() ? 1 : 2;
        }));
        map.put("messageList", messageList);
//        Map<String, Object> param= Maps.newHashMap();
//        param.put("doctorId", SessionUtils.getDoctorId());
//        param.put("status", 0);
//        List<Message> unreadCountList = messageMapper.queryReadCount(param);
//        Integer unreadInboxCount = unreadCountList.stream().filter(message -> null != message.getBoxId() && message.getBoxId().toString().matches("1\\d"))
//                .mapToInt(message -> {
//                    return message.getCount();
//                }).sum();
//        map.put("unreadInboxCount", null == unreadInboxCount ? 0 : unreadInboxCount);
        Integer totalSize = messageMapper.queryMessageListCount(req);
        int pageSize = req.getPageSize();
        map.put("totalPage", Math.ceil((double) totalSize / pageSize));
        return map;
    }

    @Override
    public Integer updateMessageAsterisk(Integer messageId) {
        Integer result = 0;
        Long doctorId = SessionUtils.getDoctorId();
        result = messageMapper.updateMessageAsterisk(messageId, doctorId);
        return result;
    }

    @Override
    public Integer deleteMessage(MessageRequestPojo req) {
        MessageRequestPojo.handleMessageRequest(req);
        if (null != req.getBoxId()  && 0 == req.getBoxId()) {
            req.setStatus(0);
            req.setBoxId(null);
        }
        Integer result = messageMapper.deleteMessage(req);
        return result;
    }


    @Override
    public Integer allToRead(Integer boxId) {
        Long doctorId = SessionUtils.getDoctorId();
        Map<String, Object> param = Maps.newHashMap();
        param.put("doctorId", doctorId);
        param.put("status", 1);
        param.put("boxId", 0 == boxId ? 1 : boxId);
        Integer result = messageMapper.allToRead(param);
        return result;
    }


    @Override
    public Message getMessageDetail(Integer messageId) {
        Long doctorId = SessionUtils.getDoctorId();
        messageMapper.updateMessageStatus(messageId, doctorId);
        Message message = messageMapper.findMessageById(messageId);
        return message;
    }

    @Override
    public Integer saveAndSendMessage(MessageRequestPojo req) {
        Message message = new Message();
        message.setMessageHead(req.getMessageHead());
        message.setMessageBody(req.getMessageBody());
        message.setCreater(SessionUtils.getDoctorId());
        message.setHospitalId(SessionUtils.getHospitalId());
        messageMapper.saveMessage(message);
        Integer messageId = message.getMessageId();

        Doctor doctor = new Doctor();
        List<Doctor> doctorList = doctorService.searchDoctor(doctor);
        List<MessageNotify> notifyList = Lists.newArrayList();
        Integer priority = req.getPriority();
        Integer boxId = 1 == priority ? 10 : 13;
        for (Doctor doc : doctorList) {
            MessageNotify notify = new MessageNotify();
            notify.setDoctorId(doc.getDoctorId());
            notify.setHospitalId(doc.getHospitalId());
            notify.setMessageId(messageId);
            notify.setBoxId(boxId);
            notify.setPriority(priority);
            notifyList.add(notify);
        }
        Integer result = messageMapper.sendMessage(notifyList);
        return result;
    }


    @Override
    public Map<String, Integer> getMessageCount() {
        Map<String, Integer> result = Maps.newHashMap();
        Map<String, Object> param= Maps.newHashMap();
        param.put("doctorId", SessionUtils.getDoctorId());
        List<Message> readCountList = messageMapper.queryReadCount(param);
        Integer unreadInboxCount = readCountList.stream()
                .filter(message -> 0 == message.getStatus())
                .filter(message -> null != message.getBoxId() && message.getBoxId().toString().matches("1\\d"))
                .mapToInt(message -> {
                    return message.getCount();
                }).sum();
        result.put("unreadInboxCount", null == unreadInboxCount ? 0 : unreadInboxCount);
        Integer totalCount = readCountList.stream()
                .filter(message -> null != message.getBoxId() && message.getBoxId().toString().matches("1\\d"))
                .mapToInt(message -> {
                    return message.getCount();
                }).sum();
        result.put("totalCount", null == totalCount ? 0 : totalCount);
        Integer asteriskCount = readCountList.stream()
                .filter(message -> null != message.getBoxId() && message.getBoxId().toString().matches("13"))
                .mapToInt(message -> {
                    return message.getCount();
                }).sum();
        result.put("asteriskCount", null == asteriskCount ? 0 : asteriskCount);
        return result;
    }

}
