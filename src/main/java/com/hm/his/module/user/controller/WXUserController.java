package com.hm.his.module.user.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hm.his.framework.crypt.MD5Utils;
import com.hm.his.module.user.dao.BoundInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.user.model.BoundInfo;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;
import com.hm.his.module.user.service.BoundInfoService;
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
@RequestMapping("/weixin/user")
public class WXUserController {

	@Autowired(required = false)
	BoundInfoService boundInfoService;
	@Autowired(required = false)
	DoctorService doctorService;
	@Autowired(required = false)
	HospitalService hospitalService;
	
	/**
	 * <p>Description:我的诊所<p>
	 * @author ZhouPengyu
	 * @date 2016-5-3 15:46:11
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getMyHospital", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyHospital(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		try{
			List<Map> body = new ArrayList<Map>();
			String openId = SessionUtils.getOpenId();
			List<BoundInfo> boundInfoList= boundInfoService.getBoundByOpenId(openId);
			for (BoundInfo boundInfo : boundInfoList) {
				Map<String, Object> hospitalMap = new HashMap<String, Object>();
				Hospital hospital = hospitalService.getHospitalById(boundInfo.getHospitalId());
				if(null != hospital){
					hospitalMap.put("hospitalId", hospital.getHospitalId());
					hospitalMap.put("hospitalName", hospital.getHospitalName());
					Integer status = 1;
					Doctor doctor = doctorService.getDoctorById(boundInfo.getDoctorId());
					if(null == doctor){
						status = 3;
					}else{
						boolean success = doctorService.verifyPasswordByHighPasswd(boundInfo.getDoctorName(), boundInfo.getHighPasswd());
						if(!success)
							status = 2;
					}
					hospitalMap.put("status", status);
					body.add(hospitalMap);
				}
			}
			hisResponse.setBody(body);
		}catch(Exception e){
			e.printStackTrace();
		}
		return hisResponse.toString();
	};
	
	/**
	 * <p>Description:绑定诊所<p>
	 * @author ZhouPengyu
	 * @date 2016年3月25日 上午11:06:56
	 */
	@RequestMapping(value = "/boundHospital", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String boundHospital(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			String doctorName = requestParams.get("userName");
			String password = requestParams.get("password");
			Long hospitalId = LangUtils.getLong(requestParams.get("hospitalId"));
			String openId = SessionUtils.getOpenId();
			Long status = null;
			Doctor doctor = doctorService.getDoctorByName(doctorName);
			if (doctor !=null){
				
				//判断诊所是否重复绑定
				Integer isBoundingByHospital = boundInfoService.isBoundingByHospital(openId, doctor.getHospitalId());
				if(isBoundingByHospital>0 && (null == hospitalId || hospitalId==0L)){
					hisResponse.setErrorCode(4011);
					hisResponse.setErrorMessage("已绑定同一诊所的其他账号，请勿重复绑定");
					return hisResponse.toString();
				}
				
				//判断账号是否重复绑定
				Integer isBoundingByDoctor = boundInfoService.isBoundingByDoctor(openId, doctorName);
				if(isBoundingByDoctor>0 && (null == hospitalId || hospitalId==0L)){
					hisResponse.setErrorCode(4011);
					hisResponse.setErrorMessage("绑定账号重复");
					return hisResponse.toString();
				}
				boolean success = doctorService.verifyPassword(doctorName, password);
				if(success){
					if(doctor.getFlag() !=null && doctor.getFlag().equals(0L)){
						doctor.setStatus(-1L);
					}else if(doctor.getStatus() !=null &&doctor.getStatus()==1L){
						if(null == hospitalId || hospitalId==0L){
							BoundInfo boundInfo = new BoundInfo();
							boundInfo.setOpenId(openId);
							boundInfo.setDoctorName(doctorName);
							boundInfo.setPassword(password);
							//密码加密升级
							boundInfo.setHighPasswd(MD5Utils.passwordSaltHash(boundInfo.getDoctorName(),boundInfo.getPassword()));
							boundInfo.setDoctorId(doctor.getDoctorId());
							boundInfo.setHospitalId(doctor.getHospitalId());
							boundInfo.setBoundDate(DateUtil.getGeneralDate(new Date()));
							boundInfoService.insertBoundInfo(boundInfo);
						}else{
							if(hospitalId == doctor.getHospitalId()){
								BoundInfo boundInfo = new BoundInfo();
								boundInfo.setOpenId(openId);
								boundInfo.setDoctorName(doctorName);
								boundInfo.setPassword(password);
								//密码加密升级
								boundInfo.setHighPasswd(MD5Utils.passwordSaltHash(boundInfo.getDoctorName(),boundInfo.getPassword()));
								boundInfo.setDoctorId(doctor.getDoctorId());
								boundInfo.setHospitalId(doctor.getHospitalId());
								boundInfoService.updateBoundInfo(boundInfo);
							}else{
								doctor.setStatus(-2L);
							}
						}
					}
					status = doctor.getStatus();
				}else{
					hisResponse.setErrorCode(4011);
					hisResponse.setErrorMessage("用户名或密码错误");
				}
			}else{
				hisResponse.setErrorCode(4011);
				hisResponse.setErrorMessage("账号不存在");
				return hisResponse.toString();
			}
			body.put("status", status);
			hisResponse.setBody(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hisResponse.toString();
	};
	
	/**
	 * <p>Description:解除绑定<p>
	 * @author ZhouPengyu
	 * @date 2016年3月25日 上午11:06:56
	 */
	@RequestMapping(value = "/rescissionHospital", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String rescissionHospital(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			Long hospitalId = Long.parseLong(requestParams.get("hospitalId"));
			String openId = SessionUtils.getOpenId();
			Integer status = null;
			status = boundInfoService.rescissionHospital(openId, hospitalId);
			body.put("status", status);
			hisResponse.setBody(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hisResponse.toString();
	};
	
	/**
	 * <p>Description:扫码入库诊所列表<p>
	 * @author ZhouPengyu
	 * @date 2016-5-5 10:38:10
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/boundHospitalFunction", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String boundHospitalFunction(@RequestBody Map<String, String> requestParams){
		HisResponse hisResponse = new HisResponse();
		try{
			requestParams.put("openId", SessionUtils.getOpenId());
			List<Map> body = boundInfoService.boundHospitalFunction(requestParams);
			hisResponse.setBody(body);
		}catch(Exception e){
			e.printStackTrace();
		}
		return hisResponse.toString();
	};


	private static String UPDATE_KEY="ea3979d463da81d5a79229d6f10c817a";


	@Autowired(required = false)
	BoundInfoMapper boundInfoMapper;
	/**
	 *  功能描述：批量更新用户的密码 到增强密码字段中
	 * @author:  tangww
	 * @createDate   2016-07-13
	 *
	 */
	@RequestMapping(value = {"/batchUpdateBoundInfo"}, produces = "application/json;charset=UTF-8",  method = RequestMethod.GET)
	@ResponseBody
	public String batchUpdateBoundInfo(HttpServletRequest request){
		HisResponse hisResponse = new HisResponse();

		String key = request.getParameter("key");
		if(StringUtils.isNotBlank(key)&&key.equals(UPDATE_KEY)){

			List<BoundInfo> boundInfos = boundInfoMapper.searchBoundForUpdatePwd();
			boundInfos.stream().forEach(boundInfo -> {
				boundInfo.setHighPasswd(MD5Utils.passwordSaltHash(boundInfo.getDoctorName(),boundInfo.getPassword()));
				boundInfo.setPassword(null);
				boundInfo.setOpenId(null);
				boundInfo.setBoundDate(null);
				boundInfo.setDoctorId(null);
				boundInfo.setHospitalId(null);
				boundInfo.setDoctorName(null);
				boundInfoMapper.updateByPrimaryKeySelective(boundInfo);
			});
			Map<String, Object> body = new HashMap<String, Object>();

			body.put("执行结果", "共更新"+boundInfos.size()+"条记录");
			hisResponse.setBody(body);
		}else{
			hisResponse.setErrorCode(400L);
			hisResponse.setErrorMessage("");
		}
		return hisResponse.toString();
	}

}
