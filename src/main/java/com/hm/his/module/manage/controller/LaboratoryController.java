package com.hm.his.module.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hm.his.module.manage.model.Laboratory;
import com.hm.his.module.manage.service.LaboratoryService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.DoctorService;

@RestController
@RequestMapping("/manage")
public class LaboratoryController {
	
	@Autowired(required = false)
	LaboratoryService laboratoryService;
	@Autowired(required = false)
	DoctorService doctorService;
	
	/**
	 * <p>Description:查询医院下科室<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/searchLaboratory", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchLaboratory(@RequestBody Map<String, Object> requestParams){
		HisResponse hisResponse = new HisResponse();
		try{
			int pageSize = 50;
			int page = 0;
			if(requestParams.containsKey("page")){
				page = Integer.valueOf(requestParams.get("page").toString());
				requestParams.put("startPage", (page-1)*pageSize);
				requestParams.put("pageSize", pageSize);
			}
			//查询科室
	        requestParams.put("hospitalId", SessionUtils.getHospitalId());
			List<Laboratory> laboratoryList = laboratoryService.searchLaboratory(requestParams);
			List<Map> mapList = new ArrayList<Map>();
			for (Laboratory laboratory : laboratoryList) {
				Doctor doctor = new Doctor();
				doctor.setLaboratoryId(laboratory.getLaboratoryId());
				Map laboratoryMap = JSON.parseObject(JSON.toJSONString(laboratory), HashMap.class);
				String laboratoryMember = "";
				List<Doctor> doctorList = doctorService.searchDoctor(doctor);
				for (Doctor member : doctorList) {
					if(StringUtils.isEmpty(laboratoryMember))
						laboratoryMember += member.getRealName();
					else
						laboratoryMember += "、"+member.getRealName();
				}
				laboratoryMap.put("laboratoryMember", laboratoryMember);
				mapList.add(laboratoryMap);
			}
			int totalSize = laboratoryService.searchLaboratoryTotal(requestParams);
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("laboratoryList", mapList);
			body.put("totalPage", Math.ceil((double)totalSize / pageSize));
			hisResponse.setBody(body);
		}catch(Exception e){
			hisResponse.setErrorCode(4011);
    		hisResponse.setErrorMessage("查询医院下科室出错!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
	
	/**
	 * <p>Description:保存医院下科室<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@RequestMapping(value = "/saveLaboratory", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String saveLaboratory(@RequestBody Laboratory laboratory){
		HisResponse hisResponse = new HisResponse();
		try {
			laboratory.setHospitalId(SessionUtils.getHospitalId());
			laboratory.setFlag(1l);
			Map<String, Object> body = new HashMap<String, Object>();
			Integer result = 0;
			laboratory.setCreater(SessionUtils.getDoctorId());
			result = laboratoryService.verifyLaboratoryName(laboratory.getLaboratoryName(), SessionUtils.getHospitalId());
			if(result>0)
				result = 2;
			else{
				if(laboratory.getLaboratoryId()==null){
					result = laboratoryService.insertLaboratory(laboratory);
				}else{
					laboratory.setModifier(SessionUtils.getDoctorId());
					result = laboratoryService.updateLaboratory(laboratory);
				}
			}
			body.put("result", result);
			hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("保存科室失败!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
	
	/**
	 * <p>Description:删除医院下科室<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@RequestMapping(value = "/deleteLaboratory", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String deleteLaboratory(@RequestBody  Map<String, Long> requestParams){
		Long laboratoryId = requestParams.get("laboratoryId");
		HisResponse hisResponse = new HisResponse();
		try {
			Map<String, Object> body = new HashMap<String, Object>();
//			Doctor doctor = new Doctor();
//			doctor.setLaboratoryId(laboratoryId);
//			List<Doctor> doctorList = doctorService.searchDoctor(doctor);
//			if(doctorList!=null && doctorList.size()>0)
//				body.put("result", 2);
			Integer result = laboratoryService.deleteLaboratory(laboratoryId, SessionUtils.getHospitalId());
			if(result>0){
				body.put("result", 1);
			}else{
				body.put("result", 0);
			}
			hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("删除科室失败!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
	
	/**
	 * <p>Description:验证重名<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:36:29
	 */
	@RequestMapping(value = "/verifyLaboratoryName", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String verifyLaboratoryName(@RequestBody  Map<String, String> requestParams){
		String laboratoryName = requestParams.get("laboratoryName");
		HisResponse hisResponse = new HisResponse();
		try {
			Integer result = laboratoryService.verifyLaboratoryName(laboratoryName, SessionUtils.getHospitalId());
			Map<String, Integer> resultMap = new HashMap<String, Integer>();
			if(result>0)
				resultMap.put("result", 0);
			else{
				resultMap.put("result", 1);
			}
			hisResponse.setBody(resultMap);
		} catch (Exception e) {
			hisResponse.setErrorCode(4011);
			hisResponse.setErrorMessage("验证接口异常!");
			e.printStackTrace();
		}
		return hisResponse.toString();
	}
}
