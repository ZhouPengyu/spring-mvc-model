package com.hm.his.module.manage.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.hm.his.module.login.facade.LoginFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.instock.service.InstockService;
import com.hm.his.module.manage.model.ConfigAttribute;
import com.hm.his.module.manage.model.HospitalConfig;
import com.hm.his.module.manage.model.HospitalConfigEnum;
import com.hm.his.module.manage.service.HospitalConfigService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;
import com.hm.his.module.user.service.DoctorService;
import com.hm.his.module.user.service.HospitalService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 11:35:38
 * @description 诊所管理
 * @version 3.0
 */
@RestController
@RequestMapping("/manage")
public class HospitalController {
	
	@Autowired(required = false)
    DoctorService doctorService;
    @Autowired(required = false)
    HospitalService hospitalService;

	@Autowired(required = false)
	InstockService instockService;
	
	/**
	 * <p>Description:诊所信息<p>
	 * @author ZhouPengyu
	 * @date 2016-3-2 上午11:39:16
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getHospitalInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getHospitalInfo(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		Hospital hospital = hospitalService.getHospitalById(SessionUtils.getHospitalId());
		if(hospital.getCreateDate().contains("."))
			hospital.setCreateDate(hospital.getCreateDate().substring(0, hospital.getCreateDate().indexOf(".")));
		body = JSON.parseObject(JSON.toJSONString(hospital), HashMap.class);
		Doctor doctor = new Doctor();
		doctor.setHospitalId(SessionUtils.getHospitalId());
		doctor.setIsAdmin(1l);
		doctor = doctorService.searchDoctor(doctor).get(0);
		body.put("realName", doctor.getRealName());
		body.put("phone", doctor.getPhone());
		hisResponse.setBody(body);
		return hisResponse.toString();
	};




	@Autowired(required = false)
	HospitalConfigService hospitalConfigService;

	/**
	 *  功能描述： 6.2.	获得医院的系统设置信息
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/searchHospitalConfig", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchHospitalConfig(@RequestBody HospitalConfig hospitalConfig){
		hospitalConfig.setHospitalId(SessionUtils.getHospitalId());
		HisResponse hisResponse = new HisResponse();
		try {
			if(null==hospitalConfig.getConfigType()){
				hisResponse.setErrorCode(50001L);
				hisResponse.setErrorMessage("配置类型不能为空");
				return hisResponse.toString();
			}
			HospitalConfig  hospConfig = hospitalConfigService.searchHospitalConfigList(hospitalConfig);
			if(hospitalConfig.getConfigType() == 1){
				if(hospConfig !=null){
					hospConfig = hospitalConfigService.putUseBatchManageToSession(hospConfig);
				}else{
					hospConfig = hospitalConfigService.putUseBatchManageToSession(hospitalConfig);
				}
			}
			hisResponse.setBody(hospConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hisResponse.toString();
	}




	/**
	 *  功能描述： 6.3.	保存医院的系统设置
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/saveHospitalConfig", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String saveHospitalConfig(@RequestBody HospitalConfig hospitalConfig){
		hospitalConfig.setHospitalId(SessionUtils.getHospitalId());
		try {
			boolean saveResult = hospitalConfigService.saveHospitalConfig(hospitalConfig);
			if(saveResult ){
				if(hospitalConfig.getConfigType() == 1){
					updateUseBatchManageConfig(hospitalConfig);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		HisResponse hisResponse = HisResponse.getInstance();
		return hisResponse.toString();
	}

	@Autowired
	LoginFacade loginFacade;
	/**
	 *  功能描述：如果用户更改了 是否使用批次管理，则需要将 批次药品的库存 和 总库存做转换
	 * @author:  tangww
	 * @createDate   2016-06-17
	 *
	 */
	private void updateUseBatchManageConfig(@RequestBody HospitalConfig hospitalConfig) {

		try {
			HttpSession session = SessionUtils.getSession();
			if(CollectionUtils.isNotEmpty(hospitalConfig.getConfigAttrs())){
				ConfigAttribute configAttr = hospitalConfig.getConfigAttrs().get(0);
				if(configAttr.getAttrName().equals(HospitalConfigEnum.useBatchManage.name())){
					Integer isUseBatchManage = hospitalConfigService.getHospitalIsUseBatchManage();
					//如果 用户变更了是否使用批次，则需要切换
					if(isUseBatchManage != null && !isUseBatchManage.toString().equals(configAttr.getAttrValue())){
						//将 本诊所下所有用户的 systemFrame 删除
						loginFacade.deleteSystemFrameCacheByHospitalId(hospitalConfig.getHospitalId());
						session.setAttribute(SessionUtils.HOSPITAL_IS_USE_BATCH_MANAGE,configAttr.getAttrValue());

						if(configAttr.getAttrValue().equals("1")){
							// 从 不使用 到 使用 ：将现有库存 生成 一个批次 （将 药品上的数量，进货价和剩余库存 记录为批次信息） 需要注意拆零药品折成盒的问题
							instockService.systemSubmitDrugInsock();
						}else{
							// 从 使用  到不使用  ：将批次中的剩余库存更新 为 0
							instockService.systemCleanInsockDrugSurplusCount();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * <p>Description:获取当前时间<p>
	 * @author ZhouPengyu
	 * @date 2016年6月20日 下午1:43:30
	 */
	@RequestMapping(value = "/getServerDate", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getServerDate(@RequestBody Map<String, Object> requestParams){
		HisResponse hisResponse = new HisResponse();
		hisResponse.setBody(DateUtil.formatDate(new Date()));
		return hisResponse.toString();
	}

}
