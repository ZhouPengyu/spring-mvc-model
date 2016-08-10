package com.hm.his.module.manage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hm.his.framework.model.HisResponse;

@RestController
public class ManageControllerTest {
	

	@RequestMapping(value = "/getHospitalInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getHospitalInfo(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("realName", "张三");
		body.put("phone", "15911111111");
		body.put("hospitalName", "哈哈");
		body.put("address", "哈哈路");
		body.put("createDate", "2016-12-12 12:12:12");
		body.put("hospitalNumber", "2016121214312");
		body.put("organizationLicense", "201231231#HM#2302341314");
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
	
	@RequestMapping(value = "/sendSmsCaptcha", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String sendSmsCaptcha(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("result", "1");
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
	
	@RequestMapping(value = "/getAccountInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getAccountInfo(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("doctorId", "1");
		body.put("doctorName", "qwer");
		body.put("realName", "张三");
		body.put("gender", "1");
		body.put("phoneNo", "15911111111");
		body.put("mail", "2016121214@23.com");
		body.put("hospitalName", "哈哈");
		body.put("address", "哈哈路");
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
	
	@RequestMapping(value = "/retrievePassword", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrievePassword(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("result", "1");
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
	
	@RequestMapping(value = "/resetPassword", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String resetPassword(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("result", "1");
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
	
	@RequestMapping(value = "/activationPhoneNo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String activationPhoneNo(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("result", "1");
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
}
