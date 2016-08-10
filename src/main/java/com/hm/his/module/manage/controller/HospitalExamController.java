package com.hm.his.module.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.ChineseToEnglish;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.manage.model.ExaminationList;
import com.hm.his.module.manage.model.HospitalExam;
import com.hm.his.module.manage.service.ExaminationListService;
import com.hm.his.module.manage.service.HospitalExamService;

@RestController
@RequestMapping("/manage")
public class HospitalExamController {
	
	@Autowired(required = false)
	HospitalExamService hospitalExamService;
	
	@Autowired(required = false)
	ExaminationListService examinationListService;
	
	/**
	 * <p>Description:查询医院下检查项<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@RequestMapping(value = "/searchExam", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchExam(@RequestBody Map<String, Object> requestParams){
		HisResponse hisResponse = new HisResponse();
		try {
			int pageSize = 50;
			int page = requestParams.containsKey("page") ? Integer.valueOf(requestParams.get("page").toString()) : 1;
			requestParams.put("startPage", (page-1)*pageSize);
			requestParams.put("pageSize", pageSize);
	        
	        requestParams.put("hospitalId", SessionUtils.getHospitalId());
			List<HospitalExam> hospitalList = hospitalExamService.searchHospitalExam(requestParams);
			int totalSize = hospitalExamService.searchHospitalExamTotal(requestParams);
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("examList", hospitalList);
			body.put("totalPage", Math.ceil((double)totalSize / pageSize));
			hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("查询检查项异常!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
	
	@RequestMapping(value = "/searchAllExam", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchAllExam(@RequestBody Map<String, Object> requestParams){
		HisResponse hisResponse = new HisResponse();
		try{
			requestParams.put("hospitalId", SessionUtils.getHospitalId());
			List<HospitalExam> hospitalExam = hospitalExamService.searchHospitalExam(requestParams);
			Map<String, Object> body = new HashMap<>();
			body.put("examList", hospitalExam);
			hisResponse.setBody(body);
		}catch(Exception e){
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
	
	/**
	 * <p>Description:查询医院下检查项<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@RequestMapping(value = "/searchExamSug", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchExamSug(@RequestBody Map<String, Object> requestParams){
		HisResponse hisResponse = new HisResponse();
        try {
        	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    		Object version = requestParams.get("version");
            requestParams.put("hospitalId", SessionUtils.getHospitalId());
    		List<HospitalExam> hospitalList = hospitalExamService.searchHospitalExam(requestParams);
    		if(hospitalList!=null && hospitalList.size()>0){
    			for (HospitalExam hospitalExam : hospitalList) {
    				@SuppressWarnings("unchecked")
    				Map<String, Object> map = JSON.parseObject(JSON.toJSONString(hospitalExam), HashMap.class);
    				map.put("dataSource", 0);	//诊所检查
    				resultList.add(map);
    			}
    		}else{
    			List<ExaminationList> examinationListList = examinationListService.searchByName(requestParams.get("examName").toString());
    			if(examinationListList != null && examinationListList.size()>0){
    				for (ExaminationList examinationList : examinationListList) {
    					@SuppressWarnings("unchecked")
    					Map<String, Object> map = JSON.parseObject(JSON.toJSONString(examinationList), HashMap.class);
    					map.put("dataSource", 1);	//惠每检查
    					resultList.add(map);
    				}
    			}
    		}
    		
    		Map<String, Object> body = new HashMap<String, Object>();
    		body.put("examList", resultList);
    		body.put("version", version);
    		hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("查询检查项异常!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
	
	/**
	 * <p>Description:保存医院下检查项<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@RequestMapping(value = "/saveExam", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String saveExam(@RequestBody HospitalExam hospitalExam){
		HisResponse hisResponse = new HisResponse();
		try {
			hospitalExam.setHospitalId(SessionUtils.getHospitalId());
			hospitalExam.setExamPhoneticizeName(ChineseToEnglish.getPingYin(hospitalExam.getExamName()));
			hospitalExam.setExamAbbreviationName(ChineseToEnglish.getPinYinHeadChar(hospitalExam.getExamName()));
			hospitalExam.setFlag(1l);
			Integer result = 0;
			if(hospitalExam.getExamId()==null){
				hospitalExam.setCreater(SessionUtils.getDoctorId());
				result = hospitalExamService.insertHospitalExam(hospitalExam);
			}else{
				hospitalExam.setModifier(SessionUtils.getDoctorId());
				result = hospitalExamService.updateHospitalExam(hospitalExam);
			}
			if(result>0)
				return hisResponse.toString();
			else{
				hisResponse.setErrorCode(4011);
				return hisResponse.toString();
			}
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("保存检查项异常!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
	
	/**
	 * <p>Description:删除医院下检查项<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@RequestMapping(value = "/deleteExam", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String deleteExam(@RequestBody  Map<String, Long> requestParams){
		Long examId = requestParams.get("examId");
		HisResponse hisResponse = new HisResponse();
		try {
			Integer result = hospitalExamService.deleteHospitalExam(examId, SessionUtils.getHospitalId());
			if(result>0)
				return hisResponse.toString();
			else{
				hisResponse.setErrorCode(4011);
				return hisResponse.toString();
			}
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("删除检查项异常!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
	
	/**
	 * <p>Description:验证重名<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@RequestMapping(value = "/verifyExamName", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String verifyExamName(@RequestBody  Map<String, String> requestParams){
		String examName = requestParams.get("examName");
		HisResponse hisResponse = new HisResponse();
		try {
			Integer result = hospitalExamService.verifyExamName(examName, SessionUtils.getHospitalId());
			Map<String, Integer> resultMap = new HashMap<String, Integer>();
			if(result>0)
				resultMap.put("result", 0);
			else{
				resultMap.put("result", 1);
			}
			hisResponse.setBody(resultMap);
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("删除名称异常!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
}
