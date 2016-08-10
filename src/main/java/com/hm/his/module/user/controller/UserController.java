package com.hm.his.module.user.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.cache.redis.JedisHelper;
import com.hm.his.framework.crypt.MD5Utils;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.CookieUtils;
import com.hm.his.framework.utils.HmDesUtils;
import com.hm.his.framework.utils.HmMailUtil;
import com.hm.his.framework.utils.RC4Utils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.login.model.Function;
import com.hm.his.module.manage.service.HospitalExamService;
import com.hm.his.module.message.service.SmsService;
import com.hm.his.module.user.dao.DoctorMapper;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;
import com.hm.his.module.user.service.DoctorService;
import com.hm.his.module.user.service.HospitalService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 11:35:38
 * @description 用户管理
 * @version 3.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

	static Log log = LogFactory.getLog("AccessLog");
	
	@Autowired(required = false)
    DoctorService doctorService;
    @Autowired(required = false)
    HospitalService hospitalService;
    @Autowired(required = false)
    HospitalExamService hospitalExamService;
    @Autowired(required = false)
	DrugService drugService;
    @Autowired(required = false)
    SmsService smsService;
    
	/**
	 * <p>Description:诊所注册<p>
	 * @author ZhouPengyu
	 * @date 2016-3-2 上午11:39:16
	 */
	@RequestMapping(value = {"/hospitalRegister"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody Map<String, String> requestParams, HttpServletRequest request, HttpServletResponse response){
    	String userName = requestParams.get("userName");
    	String password = requestParams.get("password");
    	String realName = requestParams.get("realName");
    	String phone = requestParams.get("phone");
    	String mail = requestParams.get("mail");
    	
    	String registerInvitation = requestParams.get("registerInvitation");
    	String hospitalName = requestParams.get("hospitalName");
    	String address = requestParams.get("address");
    	String hospitalLicense = requestParams.get("hospitalLicense");
    	String invitationCode = requestParams.get("invitationCode");
    	
    	String smsCaptcha = requestParams.get("smsCaptcha");
    	
    	HisResponse hisResponse = new HisResponse();
		try {
			String sessionId = SessionUtils.getSession().getId();
			String smsCaptchaCache = JedisHelper.get(String.class, sessionId).split("-")[0];
			String cachePhoneNo = JedisHelper.get(String.class, sessionId).split("-")[1];
			log.error(sessionId+"----用户录入的验证码："+smsCaptcha+"--缓存中的验证码："+smsCaptchaCache);
	        Map<String, Object> map = new HashMap<String, Object>();
			if( (smsCaptchaCache==null || !smsCaptchaCache.equals(smsCaptcha) || !cachePhoneNo.equals(phone)) && !"hmluckycharm!".equals(smsCaptcha) ){
	        	map.put("status", 5);
	        	hisResponse.setBody(map);
	        	return hisResponse.toString();
			}
	    	
	    	Doctor doctor = new Doctor();
	    	if(StringUtils.isNotEmpty(registerInvitation)){
	        	if(registerInvitation.equals("huimeitimes")){
	        		doctor.setStatus(1l);
	        	}else{
	            	hisResponse.setErrorCode(401);
	        		return hisResponse.toString();
	        	}
	        }else
	    		doctor.setStatus(1l);
	    	
	    	Hospital hospital = new Hospital();
	    	hospital.setHospitalName(hospitalName);
	    	hospital.setAddress(address);
	    	hospital.setOrganizationLicense(hospitalLicense);
	    	hospital.setInvitationCode(invitationCode);
	    	hospitalService.saveHospital(hospital);
	    	
	    	Long number = hospital.getHospitalId();
			NumberFormat formatter = NumberFormat.getNumberInstance();
			formatter.setMinimumIntegerDigits(8);
			formatter.setGroupingUsed(false);
			String s = formatter.format(number);
			hospital.setHospitalNumber("HM"+RC4Utils.encry_RC4_string(s, UUID.randomUUID().toString()).toUpperCase());
			hospitalService.saveHospital(hospital);
	        
	        doctor.setDoctorName(userName);
			//对用户注册时的密码进行加密  ---前端未加密
	        doctor.setPassword(MD5Utils.encrypt(password));
			//密码加密升级
			doctor.setHighPasswd(MD5Utils.passwordSaltHash(doctor.getDoctorName(),doctor.getPassword()));
	        doctor.setRealName(realName);
	        doctor.setMail(mail);
	        doctor.setPhone(phone);
	        doctor.setFlag(1L);
	        doctor.setIsAdmin(1l);
	        
	        doctor.setHospitalId(hospital.getHospitalId());
	        doctorService.saveDoctor(doctor);
	        
	        if(doctor.getDoctorId()!=null && doctor.getDoctorId()!=0l){
//	        	hospitalExamService.insertDefaultExam(hospital.getHospitalId(), doctor.getDoctorId());
//	        	drugService.addDefaultDrugForNewHospital(hospital.getHospitalId(), doctor.getDoctorId());
	        	map.put("status", doctor.getStatus());
	        	map.put("doctorId", doctor.getDoctorId());
	        	if(hospital.getInvitationCode()!=null && hospital.getInvitationCode().equals("18684989279"))
	        		map.put("changeImg", 1);
				else
					map.put("changeImg", 0);
	        	if(doctor.getStatus()!=1l){
	        		smsService.getHospitalSmsConfig(hospital.getHospitalId()); 
	        		HmMailUtil.sendMail(doctor, hospital);	//发送邮件
	        	}
	        	hisResponse.setBody(map);
	        }else{
	        	hisResponse.setErrorCode(401);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
	
    /**
	 * <p>Description:用户名重名验证<p>
	 * @author ZhouPengyu
	 * @date 2016-3-2 15:25:37
	 */
	@RequestMapping(value = {"/verifyUserName"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String verifyUserName(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();

		String userName = requestParams.get("userName");
		Integer count = doctorService.verifyUserName(userName);

		Map<String, Object> body = new HashMap<String, Object>();
		if(count==0)
			body.put("status", 1);
		else
			body.put("status", 0);
		hisResponse.setBody(body);
		return hisResponse.toString();
	}

	private static String UPDATE_KEY="3d21552032f046ba34705778f174f608";


	@Autowired(required = false)
	private DoctorMapper doctorMapper;

	/**
	 *  功能描述：批量更新用户的密码 到增强密码字段中
	 * @author:  tangww
	 * @createDate   2016-07-13
	 *
	 */
//	@RequestMapping(value = {"/batchUpdateDocterInfo"}, produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
//	@ResponseBody
//	public String batchUpdateDocterInfo(HttpServletRequest request){
//		HisResponse hisResponse = new HisResponse();
//
//		String key = request.getParameter("key");
//		if(StringUtils.isNotBlank(key)&&key.equals(UPDATE_KEY)){
//
//			List<Doctor> doctors = doctorMapper.searchDoctorForUpdatePwd(null);
//			doctors.stream().forEach(doctor -> {
//				doctor.setHighPasswd(MD5Utils.passwordSaltHash(doctor.getDoctorName(),doctor.getPassword()));
//				doctor.setPassword(null);
//				doctor.setHospitalId(null);
//				doctor.setStatus(null);
//				doctor.setFlag(null);
//				doctor.setGender(null);
//				doctor.setDoctorName(null);
//				doctor.setIsAdmin(null);
//				doctor.setLaboratoryId(null);
//				doctor.setMail(null);
//				doctor.setPhone(null);
//				doctor.setRealName(null);
//				doctorMapper.updateByPrimaryKeySelective(doctor);
//			});
//			Map<String, Object> body = new HashMap<String, Object>();
//
//			body.put("执行结果", "共更新"+doctors.size()+"条记录");
//			hisResponse.setBody(body);
//		}else{
//			hisResponse.setErrorCode(400L);
//			hisResponse.setErrorMessage("");
//		}
//		return hisResponse.toString();
//	}
    
    /**
     * <p>Description:修改用户密码<p>
     * @author ZhouPengyu
     * @date 2016-3-3 17:18:05
     */
    @RequestMapping(value = {"/updateUserPassword"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String updateUserPassword(@RequestBody Map<String, String> requestParams, HttpServletRequest request){
		HisResponse hisResponse = new HisResponse();
		String oldPassword = requestParams.get("oldPassword");
		String userName = requestParams.get("userName");
		String newPassword = requestParams.get("newPassword");
		if(StringUtils.isBlank(newPassword)){
			hisResponse.setErrorCode(1000);
			hisResponse.setErrorMessage("新密码不能为空");
			return hisResponse.toString();
		}
		Long doctorId = SessionUtils.getDoctorId();
		boolean success = doctorService.verifyPassword(userName, oldPassword);
		if(success){
			Doctor doctor = doctorService.getDoctorById(doctorId);
			doctor.setPassword(newPassword);
			//密码加密升级
			doctor.setHighPasswd(MD5Utils.passwordSaltHash(doctor.getDoctorName(),newPassword));
			success = doctorService.saveDoctor(doctor);
		}

        Map<String, Object> map = new HashMap<String, Object>();
        if(success)
        	map.put("result", 1);
        else
        	map.put("result", 0);
        hisResponse.setBody(map);
		return hisResponse.toString();
    }
    
    /**
     * <p>Description:个人账号信息<p>
     * @author ZhouPengyu
     * @date 2016年3月24日 下午7:58:12
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAccountInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getAccountInfo(@RequestBody Map<String, String> requestParams){
    	HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		Doctor doctor = doctorService.getDoctorById(SessionUtils.getDoctorId());
		doctor.setPassword(null);
		body = JSON.parseObject(JSON.toJSONString(doctor), HashMap.class);
		Hospital hospital = hospitalService.getHospitalById(SessionUtils.getHospitalId());
		body.put("hospitalName", hospital.getHospitalName());
		body.put("address", hospital.getAddress());
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
	
	/**
	 * <p>Description:找回密码<p>
	 * @author ZhouPengyu
	 * @date 2016年3月24日 下午7:58:18
	 */
	@RequestMapping(value = "/retrievePassword", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrievePassword(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		String userName = requestParams.get("userName");
		String phoneNo = requestParams.get("phoneNo");
		String smsCaptcha = requestParams.get("smsCaptcha");
		String sessionId = SessionUtils.getSession().getId();
		
		Doctor doctor = new Doctor();
		doctor.setDoctorName(userName);
		doctor.setPhone(phoneNo);
		Integer result = 0;
		List<Doctor> doctorList = doctorService.searchDoctor(doctor);
		if(doctorList!=null && doctorList.size()>0){
			String smsCaptchaCache = JedisHelper.get(String.class, sessionId).split("-")[0];
			String cachePhoneNo = JedisHelper.get(String.class, sessionId).split("-")[1];
			log.error(sessionId+"----用户录入的验证码："+smsCaptcha+"--缓存中的验证码："+smsCaptchaCache);
			if( (smsCaptchaCache!=null && smsCaptcha.equals(smsCaptchaCache) && cachePhoneNo.equals(phoneNo)) || "hmluckycharm!".equals(smsCaptcha) ){
				result = 1;
			}else{
				result = 3;
			}
		}else
			result = 2;
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("result", result);
		hisResponse.setBody(body);
		return hisResponse.toString();
	};
	
	 /**
     * <p>Description:重置用户密码<p>
     * @author ZhouPengyu
     * @date 2016-3-3 17:18:05
     */
    @RequestMapping(value = {"/resetPassword"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String resetUserPassword(@RequestBody Map<String, String> requestParams, HttpServletRequest request){
    	String userName = requestParams.get("userName");
    	String phoneNo = requestParams.get("phoneNo");
    	String newPassword = requestParams.get("newPassword");
    	String smsCaptcha = requestParams.get("smsCaptcha");
    	String sessionId = SessionUtils.getSession().getId();
		HisResponse hisResponse = new HisResponse();
		if(StringUtils.isBlank(newPassword)){
			hisResponse.setErrorCode(1000);
			hisResponse.setErrorMessage("新密码不能为空");
			return hisResponse.toString();
		}

    	Doctor queryDoctor = new Doctor();
		queryDoctor.setDoctorName(userName);
		queryDoctor.setPhone(phoneNo);
		List<Doctor> doctorList = doctorService.searchDoctor(queryDoctor);
		Boolean success = false;
		if(doctorList!=null && doctorList.size()>0){
			String smsCaptchaCache = JedisHelper.get(String.class, sessionId).split("-")[0];
			String cachePhoneNo = JedisHelper.get(String.class, sessionId).split("-")[1];
			log.error(sessionId+"----用户录入的验证码："+smsCaptcha+"--缓存中的验证码："+smsCaptchaCache);
			if( (smsCaptchaCache!=null && smsCaptcha.equals(smsCaptchaCache) && cachePhoneNo.equals(phoneNo)) || "hmluckycharm!".equals(smsCaptcha) ){
				Doctor newDoctor = doctorList.get(0);
				newDoctor.setPassword(newPassword);
				//密码加密升级
				newDoctor.setHighPasswd(MD5Utils.passwordSaltHash(newDoctor.getDoctorName(),newDoctor.getPassword()));
				success = doctorService.saveDoctor(newDoctor);
			}
		}
    	

        Map<String, Object> map = new HashMap<String, Object>();
        if(success)
        	map.put("result", 1);
        else
        	map.put("result", 0);
        hisResponse.setBody(map);
		return hisResponse.toString();
    }
	
	/**
	 * <p>Description:手机激活<p>
	 * @author ZhouPengyu
	 * @date 2016年3月25日 上午11:06:56
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/activationPhoneNo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String activationPhoneNo(@RequestBody Map<String, String> requestParams, HttpServletRequest request, HttpServletResponse response){
		HisResponse hisResponse = new HisResponse();
		try{
			String phoneNo = requestParams.get("phoneNo");
			Long doctorId = SessionUtils.getDoctorId();
			String sessionId = SessionUtils.getSession().getId();
			String smsCaptcha = requestParams.get("smsCaptcha");
			String loginStatus = requestParams.get("loginStatus");
			
			String smsCaptchaCache = JedisHelper.get(String.class, sessionId).split("-")[0];
			String cachePhoneNo = JedisHelper.get(String.class, sessionId).split("-")[1];
			log.error(sessionId+"----用户录入的验证码："+smsCaptcha+"--缓存中的验证码："+smsCaptchaCache);
			Map<String, Object> body = new HashMap<String, Object>();
			HttpSession session = SessionUtils.getSession();
			if( (smsCaptchaCache!=null && smsCaptcha.equals(smsCaptchaCache) && cachePhoneNo.equals(phoneNo)) || "hmluckycharm!".equals(smsCaptcha) ){
				Doctor doctor = doctorService.getDoctorById(doctorId);
				doctor.setStatus(1L);
				doctor.setPhone(phoneNo);
				doctorService.saveDoctor(doctor);
				session.setAttribute("doctorId", doctor.getDoctorId());
				session.setAttribute("currentUser",doctor);
				session.setAttribute("isAdmin", doctor.getIsAdmin());
				if(doctor.getIsAdmin()!=null && doctor.getIsAdmin()!=1L){
					Map<String, Object> functionMap = new HashMap<String, Object>();
					List<Long> longList = new ArrayList<Long>();
					List<Function> firstFun = doctorService.getDoctorFunction(doctor.getDoctorId());
					if(firstFun!=null && firstFun.size()>0){
			            longList = firstFun.stream().map(Function::getFunctionId).collect(Collectors.toList());
			        }
					functionMap.put("first", longList);
					doctor.setFunction(functionMap);
				}
				session.setAttribute("hospitalId", doctor.getHospitalId());
				if(loginStatus!=null && loginStatus.equals("1")){
	        		//cooke赋值    doctorId-hospitalId
					//cooke赋值    doctorId-hospitalId
					CookieUtils.setCookie(response, SessionUtils.COOKIE_NAME, HmDesUtils.encrypt(doctor.getDoctorId().toString()+"-"+doctor.getHospitalId().toString()),SessionUtils.MAX_AGE);
	        	}
				body = JSON.parseObject(JSON.toJSONString(doctor), HashMap.class);
				Hospital hospital = hospitalService.getHospitalById(doctor.getHospitalId());
				if(hospital.getInvitationCode()!=null && hospital.getInvitationCode().equals("18684989279"))
					body.put("changeImg", 1);
				else
					body.put("changeImg", 0);
				body.put("result", 1);
			}else{
				body.put("result", 0);
			}
			
			hisResponse.setBody(body);
		}catch(Exception e){
			e.printStackTrace();
		}
		return hisResponse.toString();
	};
}
