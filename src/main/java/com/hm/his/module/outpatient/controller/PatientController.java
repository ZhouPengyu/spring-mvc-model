package com.hm.his.module.outpatient.controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.ChineseToEnglish;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.outpatient.model.Patient;
import com.hm.his.module.outpatient.model.PatientDiagnosis;
import com.hm.his.module.outpatient.model.PatientInquiry;
import com.hm.his.module.outpatient.service.PatientDiagnosisService;
import com.hm.his.module.outpatient.service.PatientInquiryService;
import com.hm.his.module.outpatient.service.PatientService;
import com.hm.his.module.user.service.DoctorService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-8 15:12:32
 * @description 病历管理
 * @version 3.0
 */
@RestController
@RequestMapping("/outpatient")
@SuppressWarnings("all")
public class PatientController {
    @Autowired(required = false)
    PatientService patientService;
    @Autowired(required = false)
    PatientInquiryService patientInquiryService;
    @Autowired(required = false)
    PatientDiagnosisService patientDiagnosisService;
    @Autowired(required = false)
    DoctorService doctorService;

    Logger logger = Logger.getLogger(PatientController.class);
    
    /**
     * <p>Description:根据条件查询医生所有患者<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午3:29:49
     */
    @RequestMapping(value = "/getMyPatients", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getMyPatients(@RequestBody Map<String, String> requestParams, HttpServletRequest request){
        int pageSize = 50;
        HisResponse hisResponse = new HisResponse();
        try {
        	String patientConditions = requestParams.get("patientConditions");
            String version = requestParams.get("version");
            
            int page = 1;
            if (requestParams.containsKey("page")){
                page = Integer.valueOf(requestParams.get("page"));
            }
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            if(SessionUtils.isAdmin())
            	parameterMap.put("hospitalId", SessionUtils.getHospitalId());
            else
            	parameterMap.put("doctorId", SessionUtils.getDoctorId());
            parameterMap.put("patientConditions", patientConditions);
            parameterMap.put("startPage", (page-1)*pageSize);
            parameterMap.put("pageSize", pageSize);
            List<Patient> myPatientList = patientService.searchPatientByDoctorId(parameterMap);
            int totalSize = patientService.searchPatientTotalByDoctorId(parameterMap);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> patientList = new ArrayList<>();
            for (Patient patient : myPatientList) {
            	
           	 	Map<String, Object> map = new HashMap<>();
            	PatientInquiry patientInquiry = patientInquiryService.getInquiryNewByPatientId(patient.getPatientId());
            	String diagnosis = "";
            	if(patientInquiry!=null){
            		List<PatientDiagnosis> patientDiagnosisList = patientDiagnosisService.getDiagnosisByRecordId(patientInquiry.getRecordId());
            		for (PatientDiagnosis patientDiagnosis : patientDiagnosisList) {
            			if(diagnosis.equals("")){
            				diagnosis = patientDiagnosis.getDiseaseName();
            			}else{
            				diagnosis+=","+patientDiagnosis.getDiseaseName();
            			}
    				}
    				map.put("recordId", patientInquiry.getRecordId());
    				if(patientInquiry.getModifyDate().contains("."))
    					map.put("date", patientInquiry.getModifyDate().substring(0, patientInquiry.getModifyDate().indexOf(".")));
    				else
    					map.put("date", patientInquiry.getModifyDate());
            	}
            	map.put("diagnosis", diagnosis);
    			map.put("patientId", patient.getPatientId());
                map.put("patientName", patient.getPatientName());
    			map.put("gender", patient.getGender());
    			map.put("age", patient.getAge());
    			map.put("ageType", patient.getAgeType());
    			if(StringUtils.isEmpty(patient.getAgeType())){
                	//得到当前的年份
                    String cYear = DateUtil.formatDate(new Date()).substring(0,4);
                    //得到生日年份
                    String birthYear = patient.getBirthday().substring(0,4);
                    int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
                    patient.setAge(Double.valueOf(age));
                    map.put("age", patient.getAge());
                    map.put("ageType", "岁");
                }
    			map.put("phoneNo", patient.getPhoneNo());
                patientList.add(map);
    		}
            result.put("version", version);
            result.put("patientList", patientList);
            result.put("totalPage", Math.ceil((double)totalSize / pageSize));
            hisResponse.setBody(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return hisResponse.toString();
    }
    
    /**
     * <p>Description:获取患者信息<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午5:42:45
     */
    @RequestMapping(value = "/getPatientInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPatientInfo(@RequestBody Map<String, String> requestParams, HttpServletRequest request){
        HisResponse hisResponse = new HisResponse();
        Long patientId = null;
        if (requestParams.containsKey("patientId")) {
            patientId = Long.valueOf(requestParams.get("patientId"));
        }
        Patient patient;
        if (patientId == null) {
            hisResponse.setErrorCode(1);
            hisResponse.setErrorMessage("patientId is required!");
            return hisResponse.toString();
        }
        Map<String, Object> mapPatient = new HashMap<String, Object>();
		mapPatient.put("doctorId", SessionUtils.getDoctorId());
		mapPatient.put("patientId", patientId);
		patient = patientService.searchDocContainPatient(mapPatient);
		if(SessionUtils.isAdmin())
			patient = patientService.getPatientById(patientId);
		
		if(patient!=null){
			if(StringUtils.isEmpty(patient.getAgeType())){
	        	//得到当前的年份
	            String cYear = DateUtil.formatDate(new Date()).substring(0,4);
	            //得到生日年份
	            String birthYear = patient.getBirthday().substring(0,4);
	            int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
	            patient.setAge(Double.valueOf(age));
	            patient.setAgeType("岁");
	        }
			hisResponse.setBody(patient);
		}else{
			hisResponse.setErrorCode(401);
		}
        return hisResponse.toString();
    }

    /**
     * <p>Description:保存患者信息<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午5:44:53
     */
    @RequestMapping(value = "/savePatientInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String savePatientInfo(@RequestBody Patient patient, HttpServletRequest request){
        HisResponse hisResponse = new HisResponse();
        try {
        	if (patient != null){
            	patient.setDoctorId(SessionUtils.getDoctorId());
            	if(StringUtils.isEmpty(patient.getAgeType())){
                	//得到当前的年份
                    String cYear = DateUtil.formatDate(new Date()).substring(0,4);
                    //得到生日年份
                    String birthYear = patient.getBirthday().substring(0,4);
                    int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
                    patient.setAge(Double.valueOf(age));
                    patient.setAgeType("岁");
                }
            	patient.setPatientPinyin(ChineseToEnglish.getPingYin(patient.getPatientName()));
            	patient.setDiagnosisDate(DateUtil.getGeneralDate(new Date()));
                patientService.savePatient(patient);
                doctorService.insertDoctorPatientRelation(patient.getDoctorId(), patient.getPatientId());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("patientId", patient.getPatientId());
            hisResponse.setBody(map);
            
            Long hospId = 1l;
            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("hospitalGuid", hospId);
            resMap.put("userGuid", patient.getPatientId());
            resMap.put("doctorGuid", patient.getDoctorId());
    		resMap.put("gender", patient.getGender());
            
            if(StringUtils.isEmpty(patient.getAgeType())){
            	//得到当前的年份
                String cYear = DateUtil.formatDate(new Date()).substring(0,4);
                //得到生日年份
                String birthYear = patient.getBirthday().substring(0,4);
                int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
                resMap.put("age", age);
                resMap.put("ageType", "岁");
            }
            
            try{
            	URL url = new URL("http://localhost:8080/cdss/post_user_profile");
            	HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            	urlConnection.setDoOutput(true);
            	urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            	urlConnection.setRequestMethod("POST");
            	OutputStream outputStream = urlConnection.getOutputStream();
            	outputStream.write(JSON.toJSONString(resMap).getBytes());
            	outputStream.flush();
            	outputStream.close();
    			urlConnection.getResponseCode();
            	urlConnection.getResponseMessage();
            }catch (Exception e) {
    			e.printStackTrace();
    		}
            
            try{
            	URL url = new URL("http://localhost:8080/cdss2/post_user_profile");
            	HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            	urlConnection.setDoOutput(true);
            	urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            	urlConnection.setRequestMethod("POST");
            	OutputStream outputStream = urlConnection.getOutputStream();
            	Map<String, Object> resMap2 = new HashMap<String, Object>();
            	resMap2.put("userGuid", patient.getPatientId());
            	outputStream.write(JSON.toJSONString(resMap2).getBytes());
            	outputStream.flush();
            	outputStream.close();
    			urlConnection.getResponseCode();
            	urlConnection.getResponseMessage();
            }catch (Exception e) {
    			e.printStackTrace();
    		}
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("保存患者异常！");
			e.printStackTrace();
		}
        return hisResponse.toString();
    }
    
    /**
     * <p>Description:患者病历记录<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午6:18:46
     */
    @RequestMapping(value = "/getPatientRecords", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPatientRecords(@RequestBody Map<String, String> requestParams){
        int pageSize = 10;
        HisResponse hisResponse = new HisResponse();
        try {
        	Long patientId = null;
            Long hospitalId = SessionUtils.getHospitalId();
            if (requestParams.containsKey("patientId")){
                patientId = Long.valueOf(requestParams.get("patientId"));
            }
            Map<String, Object> mapPatient = new HashMap<String, Object>();
    		mapPatient.put("doctorId", SessionUtils.getDoctorId());
    		mapPatient.put("patientId", patientId);
    		Patient patient = patientService.searchDocContainPatient(mapPatient);
    		
    		if(SessionUtils.isAdmin())
    			patient = patientService.getPatientById(patientId);
    		
    		if(patient!=null){
    			int page = 1;
    	        if (StringUtils.isNotEmpty(requestParams.get("page"))){
    	            page = Integer.valueOf(requestParams.get("page"));
    	        }
    	        if (patientId == null){
    	            hisResponse.setErrorCode(1);
    	            hisResponse.setErrorMessage("patientId is required");
    	            return hisResponse.toString();
    	        }
    	        List<PatientInquiry> patientInquiryList = patientInquiryService.getInquiryByPatient(patientId);
    	        int start = (page - 1) * pageSize;
    	        int totalSize = patientInquiryList.size();
    	        Map<String, Object> result = new HashMap<>();
    	        List<Map<String, Object>> recordList = new ArrayList<>();
    	        if (totalSize - start > 0){
    	        	int end = Math.min(page * pageSize, patientInquiryList.size());
    		        patientInquiryList = patientInquiryList.subList(start, end);
    		        Set<Long> doctorIdSet = new HashSet<>();
    		        List<Long> recordIdList = new ArrayList<>();
    		        for (PatientInquiry patientInquiry : patientInquiryList){
    		            doctorIdSet.add(patientInquiry.getDoctorId());
    		            recordIdList.add(patientInquiry.getRecordId());
    		        }
    		        Map<Long, String> doctorMap = doctorService.getDoctorName(new ArrayList<Long>(doctorIdSet));
    		        List<PatientDiagnosis> diagnosisList = patientDiagnosisService.getDiagnosisByRecordId(recordIdList);
    		        Map<Long, String> diagnosisMap = new HashMap<>();
    		        for (PatientDiagnosis patientDiagnosis : diagnosisList){
    		            Long recordId = patientDiagnosis.getRecordId();
    		            if (diagnosisMap.containsKey(recordId)){
    		                String str = diagnosisMap.get(recordId);
    		                str = str + ", " + patientDiagnosis.getDiseaseName();
    		                diagnosisMap.put(recordId, str);
    		            } else {
    		                diagnosisMap.put(recordId, patientDiagnosis.getDiseaseName());
    		            }
    		        }
    		        for (PatientInquiry patientInquiry : patientInquiryList){
    		            Map<String, Object> map = new HashMap<>();
    		            map.put("recordId", patientInquiry.getRecordId());
    		            map.put("doctorName", doctorMap.get(patientInquiry.getDoctorId()));
    		            String date = patientInquiry.getCreateDate();
    		            if (StringUtils.isNotBlank(date) && date.length() >= 10)
    		            map.put("date", date.substring(0, 10));
    		            
    		            if(hospitalId.equals(1l)){
    		            	map.put("editable", true);
    		            }else{
    		            	Date createDate = DateUtil.parserGeneralDate(patientInquiry.getCreateDate());
    		                
    		            	Calendar calendar = Calendar.getInstance();
    		                calendar.setTime(createDate);
    		                int createDay = calendar.get(Calendar.DAY_OF_MONTH);
    		                Date currentDate = DateUtil.parserGeneralDate(DateUtil.getGeneralDate(new Date()));
    		                calendar.setTime(currentDate);
    		                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    		                if(createDay==currentDay){
    		            		map.put("editable", true);
    		            	}else{
    		            		map.put("editable", false);
    		            	}
    		            }
    					String resSymptom = "该病例暂无主诉";
    					if(patientInquiry != null && StringUtils.isNotEmpty(patientInquiry.getSymptom())){
    						resSymptom = patientInquiry.getSymptom();
    					}
    		            map.put("symptom", resSymptom);
    		            map.put("diagnosis", diagnosisMap.get(patientInquiry.getRecordId()));
    		            recordList.add(map);
    		        }
    	        }
    	        result = JSON.parseObject(JSON.toJSONString(patient), HashMap.class);
    	        if(StringUtils.isEmpty(patient.getAgeType())){
                	//得到当前的年份
                    String cYear = DateUtil.formatDate(new Date()).substring(0,4);
                    //得到生日年份
                    String birthYear = patient.getBirthday().substring(0,4);
                    int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
                    patient.setAge(Double.valueOf(age));
                    result.put("age", patient.getAge());
                    result.put("ageType", "岁");
                }
    	        result.put("recordList", recordList);
    	        result.put("totalPage", Math.ceil((double)totalSize / pageSize));
    	        hisResponse.setBody(result);
    		}else{
    			hisResponse.setErrorCode(401);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hisResponse.toString();
    }

    /**
     * <p>Description:患者sug查询<p>
     * @author ZhouPengyu
     * @date 2016年3月8日 下午7:19:58
     */
    @RequestMapping(value = "/searchPatientBySug", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchPatientBySug(@RequestBody Map<String, String> requestParams, HttpServletRequest request){
        HisResponse hisResponse = new HisResponse();
        Map<String, Object> params = new HashMap<String, Object>();
        String patientName = requestParams.get("patientName");
        String version = requestParams.get("version");
        if(StringUtils.isEmpty(patientName)){
        	Map<String, Object> body = new HashMap<String, Object>();
        	List<Patient> patientList = new ArrayList<Patient>();
    		body.put("patientList", patientList);
    		hisResponse.setBody(body);
            return hisResponse.toString();
        }
        if(LangUtils.isChineseChar(patientName))
        	params.put("patientName", patientName);
        else
        	params.put("patientPinyin", patientName);
        params.put("hospitalId", SessionUtils.getHospitalId());
		List<Patient> searchPatientList = patientService.searchPatientBySug(params);
		
		Map<String, Object> body = new HashMap<String, Object>();
		List<Map<String, Object>> patientList = new ArrayList<>();
        for (Patient patient : searchPatientList) {
        	
       	 	Map<String, Object> map = new HashMap<>();
        	PatientInquiry patientInquiry = patientInquiryService.getInquiryNewByPatientId(patient.getPatientId());
        	if(patientInquiry!=null){
        		map.put("previousHistory", patientInquiry.getPreviousHistory());
        		map.put("personalHistory", patientInquiry.getPersonalHistory());
        		map.put("allergyHistory", patientInquiry.getAllergyHistory());
        		map.put("familyHistory", patientInquiry.getFamilyHistory());
        	}
			map.put("patientId", patient.getPatientId());
            map.put("patientName", patient.getPatientName());
			map.put("gender", patient.getGender());
			map.put("age", patient.getAge());
			map.put("ageType", patient.getAgeType());
			if(StringUtils.isEmpty(patient.getAgeType())){
            	//得到当前的年份
                String cYear = DateUtil.formatDate(new Date()).substring(0,4);
                //得到生日年份
                String birthYear = patient.getBirthday().substring(0,4);
                int age = Integer.parseInt(cYear) - Integer.parseInt(birthYear);
                patient.setAge(Double.valueOf(age));
                map.put("age", patient.getAge());
                map.put("ageType", "岁");
            }
			map.put("phoneNo", patient.getPhoneNo());
            patientList.add(map);
		}
		
        body.put("version", version);
		body.put("patientList", patientList);
		hisResponse.setBody(body);
        return hisResponse.toString();
    }
}
