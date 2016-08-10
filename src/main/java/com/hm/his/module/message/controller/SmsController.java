package com.hm.his.module.message.controller;

import com.google.common.collect.Maps;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.message.model.SmsConfig;
import com.hm.his.module.message.pojo.SmsPatient;
import com.hm.his.module.message.pojo.SmsRequest;
import com.hm.his.module.message.service.SmsService;
import com.hm.his.module.user.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author SuShaohua
 * @date 2016/8/1 20:12
 * @description
 */
@RestController
@RequestMapping("/message")
public class SmsController {
    @Autowired
    SmsService smsService;
    @Autowired
    HospitalService hospitalService;

    @RequestMapping(value = "/getSmsUserList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getSmsUserList(@RequestBody SmsRequest req) {
        HisResponse hisResponse = new HisResponse();
        Map<String, Object> result = Maps.newHashMap();
        Long hospitalId = SessionUtils.getHospitalId();
        SmsConfig smsConfig = smsService.getHospitalSmsConfig(hospitalId);
        try {
            List<SmsPatient> patientList = smsService.getSmsPatientList(req);
            result.put("patientList", patientList);
            String signature = smsConfig.getSignature();
            if (null == signature) {
                signature = hospitalService.getHospitalById(hospitalId).getHospitalName();
            }
            result.put("signature", signature);
            result.put("totalSmsAmt", smsConfig.getSmsUpperLimit());
            result.put("surplusSmsAmt", smsConfig.getSurplusAmount());
            Integer totalSize = smsService.getSmsPatientListCount(req);
            result.put("totalPage", Math.ceil((double) totalSize / req.getPageSize()));
            hisResponse.setBody(result);
        } catch (Exception e) {
            hisResponse.setErrorCode(500);
            e.printStackTrace();
        }
        return hisResponse.toString();
    }

    @RequestMapping(value = "/getSendedSmsList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getSendedSmsList(@RequestBody SmsRequest req) {
        HisResponse hisResponse = new HisResponse();
        Map<String, Object> result = Maps.newHashMap();
        Long hospitalId = SessionUtils.getHospitalId();
        SmsConfig smsConfig = smsService.getHospitalSmsConfig(hospitalId);
        try {
            List<SmsPatient> patientList = smsService.getSendedSmsList(req);
            result.put("patientList", patientList);
            result.put("totalSmsAmt", smsConfig.getSmsUpperLimit());
            result.put("surplusSmsAmt", smsConfig.getSurplusAmount());
            Integer totalSize = smsService.getSendedSmsListCount(req);
            result.put("totalPage", Math.ceil((double) totalSize / req.getPageSize()));
            hisResponse.setBody(result);
        } catch (Exception e) {
            hisResponse.setErrorCode(500);
        }
        return hisResponse.toString();
    }

    @RequestMapping(value = "/sendSms", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String sendSms(@RequestBody SmsRequest req) {
        HisResponse hisResponse = new HisResponse();
        try {
            Integer success = smsService.sendSms(req);
            hisResponse.setBody(success);
        } catch (Exception e) {
            hisResponse.setErrorCode(500);
            hisResponse.setErrorMessage("发送短信失败");
        }
        return hisResponse.toString();
    }
}
