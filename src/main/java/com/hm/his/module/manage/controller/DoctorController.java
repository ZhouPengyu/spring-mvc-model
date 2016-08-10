package com.hm.his.module.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hm.his.framework.crypt.MD5Utils;
import com.hm.his.module.login.facade.LoginFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.login.model.Function;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.DoctorService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 11:35:38
 * @description 病历管理
 * @version 3.0
 */
@RestController
@RequestMapping("/manage")
public class DoctorController {
	
	@Autowired(required = false)
	DoctorService doctorService;
	@Autowired(required = false)
	OrderService orderService;
	
	/**
	 * <p>Description:删除医生信息<p>
	 * @author ZhouPengyu
	 * @date 2016年3月12日 下午6:20:45
	 */
	@RequestMapping(value = "/deleteDoctor", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String deleteDoctor(@RequestBody Map<String, Long> requestParams){
        HisResponse hisResponse = new HisResponse();
        Long doctorId = requestParams.get("doctorId");
        Long hospitalId = SessionUtils.getHospitalId();
        Integer result = doctorService.deleteDoctor(doctorId, hospitalId);
        
        Map<String, Object> body = new HashMap<String, Object>();
        if(result>0){
        	body.put("result", 1);
		}else{
			body.put("result", 0);
		}
        
        return hisResponse.toString();
    }

    @Autowired
    LoginFacade loginFacade;
	
	/**
	 * <p>Description:保存<p>
	 * @author ZhouPengyu
	 * @date 2016年3月12日 下午6:28:52
	 */
	@RequestMapping(value = "/saveDoctor", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String saveDoctor(@RequestBody Doctor doctor){
        HisResponse hisResponse = new HisResponse();
        doctor.setHospitalId(SessionUtils.getHospitalId());
        doctor.setIsAdmin(0l);
        Integer nameCount = 0;
        if(null != doctor.getDoctorId() && doctor.getDoctorId() != 0L){
        	String doctorName = doctorService.getDoctorById(doctor.getDoctorId()).getDoctorName();
        	if(null != doctor.getDoctorName() && !doctor.getDoctorName().equals(doctorName)){
        		nameCount = doctorService.verifyUserName(doctor.getDoctorName());
        	}
        }else{
        	nameCount = doctorService.verifyUserName(doctor.getDoctorName());
        }
        if(nameCount>0){
        	hisResponse.setErrorCode(4011);
        	hisResponse.setErrorMessage("登录名存在重复！");
        	return hisResponse.toString();
        }else{
            //密码加密升级
            if(StringUtils.isNotBlank(doctor.getPassword())){
                doctor.setHighPasswd(MD5Utils.passwordSaltHash(doctor.getDoctorName(),doctor.getPassword()));
            }
        	doctorService.saveDoctor(doctor);
        }
        @SuppressWarnings("unchecked")
		List<Long> functionList = (List<Long>) doctor.getFunction().get("first");
        if(functionList!=null && functionList.size()>0){
        	doctorService.deleteDoctorFunctionByDocId(doctor.getDoctorId());
        	doctorService.saveDoctorFunction(doctor.getDoctorId(), functionList);
        }
        loginFacade.deleteSystemFrameCache(doctor.getDoctorId());
        Map<String, Object> result = new HashMap<>();
        result.put("doctorId", doctor.getDoctorId());
        hisResponse.setBody(result);
        return hisResponse.toString();
    }
	
	/**
	 * <p>Description:查询医生名称列表<p>
	 * @author ZhouPengyu
	 * @date 2016年4月18日 下午4:45:24
	 */
	@RequestMapping(value = "/searchDoctorNameList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDoctorNameList(@RequestBody Map<String, String> requestParams){
        HisResponse hisResponse = new HisResponse();
        Map<String, Object> result = new HashMap<>();
        Doctor doctorMap = new Doctor();
        doctorMap.setHospitalId(SessionUtils.getHospitalId());
        List<Doctor> doctorList = doctorService.searchDoctor(doctorMap);
        result.put("doctorList", doctorList);
        hisResponse.setBody(result);
        return hisResponse.toString();
    }
	
	@RequestMapping(value = "/searchDoctor", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchDoctor(@RequestBody Map<String, String> requestParams){
        HisResponse hisResponse = new HisResponse();
        Map<String, Object> result = new HashMap<>();
        Doctor doctorMap = new Doctor();
        doctorMap.setHospitalId(SessionUtils.getHospitalId());
        doctorMap.setIsAdmin(0l);
        List<Doctor> doctorList = doctorService.searchDoctor(doctorMap);
        for (Doctor doctor : doctorList) {
        	Map<String, Object> functionMap = new HashMap<String, Object>();
        	List<Long> longList = new ArrayList<Long>();
        	List<Function> firstFun = doctorService.getDoctorFunction(doctor.getDoctorId());
        	if(firstFun!=null && firstFun.size()>0){
        		for (Function function : firstFun) {
        			longList.add(function.getFunctionId());
				}
        	}
        	functionMap.put("first", longList);
        	doctor.setFunction(functionMap);
		}
        result.put("doctorList", doctorList);
        hisResponse.setBody(result);
        return hisResponse.toString();
    }
	
	@RequestMapping(value = "/getDoctorById", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getDoctorById(@RequestBody Map<String, Long> requestParams){
        HisResponse hisResponse = new HisResponse();
        Doctor doctor = new Doctor();
        doctor.setDoctorId(requestParams.get("doctorId"));
        doctor.setHospitalId(SessionUtils.getHospitalId());
        List<Doctor> doctorList = doctorService.searchDoctor(doctor);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if(doctorList!=null && doctorList.size()==1){
        	doctor = doctorList.get(0);
        	Map<String, Object> functionMap = new HashMap<String, Object>();
        	List<Long> longList = new ArrayList<Long>();
        	List<Function> firstFun = doctorService.getDoctorFunction(doctor.getDoctorId());
        	if(firstFun!=null && firstFun.size()>0){
        		for (Function function : firstFun) {
        			longList.add(function.getFunctionId());
				}
        	}
        	functionMap.put("first", longList);
        	doctor.setFunction(functionMap);
        	resultMap = JSON.parseObject(JSON.toJSONString(doctor), HashMap.class);
        	resultMap.put("editable", !orderService.hasOrderByDoctorId(doctor.getDoctorId()));
        	hisResponse.setBody(resultMap);
        }
        return hisResponse.toString();
    }
}
