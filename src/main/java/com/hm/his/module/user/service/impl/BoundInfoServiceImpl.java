package com.hm.his.module.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.login.model.Function;
import com.hm.his.module.user.dao.BoundInfoMapper;
import com.hm.his.module.user.model.BoundInfo;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.model.Hospital;
import com.hm.his.module.user.service.BoundInfoService;
import com.hm.his.module.user.service.DoctorService;
import com.hm.his.module.user.service.HospitalService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-4 10:56:02
 * @description 绑定服务接口实现类
 * @version 1.0
 */
@Service("BoundInfoService")
public class BoundInfoServiceImpl implements BoundInfoService {

	@Autowired(required = false)
	private BoundInfoMapper boundInfoMapper;
	@Autowired(required = false)
	private DoctorService doctorService;
	@Autowired(required = false)
	HospitalService hospitalService;
	
	@Override
	public List<BoundInfo> getBoundByOpenId(String openId) {
		List<BoundInfo> boundInfoList = new ArrayList<BoundInfo>();
		try {
			boundInfoList = boundInfoMapper.getBoundByOpenId(openId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boundInfoList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@SystemLogAnno(functionId = FunctionConst.WEIXIN_LOG,operationId = Operation.WEIXIN_BOUND)
	public Integer insertBoundInfo(BoundInfo boundInfo) {
		Integer result = 0;
		try {
			result = boundInfoMapper.insertBoundInfo(boundInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@SystemLogAnno(functionId = FunctionConst.WEIXIN_LOG,operationId = Operation.WEIXIN_RESCISSION)
	public Integer rescissionHospital(String openId, Long hospitalId) {
		Integer result = 0;
		try {
			result = boundInfoMapper.rescissionHospital(openId, hospitalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@SystemLogAnno(functionId = FunctionConst.WEIXIN_LOG,operationId = Operation.WEIXIN_UPDATE_BOUND)
	public Integer updateBoundInfo(BoundInfo boundInfo) {
		Integer result = 0;
		try {
			result = boundInfoMapper.updateBoundInfo(boundInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> boundHospitalFunction(Map<String, String> params) {
		List<Map> hospitalList = new ArrayList<Map>();
		try {
			List<BoundInfo> boundInfoList= this.getBoundByOpenId(params.get("openId"));
			for (BoundInfo boundInfo : boundInfoList) {
				Map<String, Object> hospitalMap = new HashMap<String, Object>();
				Hospital hospital = hospitalService.getHospitalById(boundInfo.getHospitalId());
				if(null != hospital){
					hospitalMap.put("hospitalId", hospital.getHospitalId());
					hospitalMap.put("hospitalName", hospital.getHospitalName());
					boolean isAdd = false;
					Doctor doctor = doctorService.getDoctorById(boundInfo.getDoctorId());
					boolean success = doctorService.verifyPasswordByHighPasswd(boundInfo.getDoctorName(), boundInfo.getHighPasswd());
					if(null != doctor && success){
						isAdd = true;
					}
					Integer status = 0;
					if(isAdd){
						if(null != doctor.getIsAdmin() && doctor.getIsAdmin() == 1L){
							status = 1;
						}else{
							List<Function> functionList = doctorService.getDoctorFunction(doctor.getDoctorId());
							if(null == functionList)
								functionList = new ArrayList<Function>();
							//判断药品入库权限
							if("inventory".equals(params.get("function"))){
								Optional<Function> funListOptional = functionList.stream().filter(funtion -> funtion.getFunctionId() == 3000L).findFirst();
								if(funListOptional.isPresent()){
									status = 1;
								}
							}else if("statistics".equals(params.get("function"))){
								Optional<Function> funListOptional = functionList.stream().filter(funtion -> funtion.getFunctionId() == 5000L).findFirst();
								if(funListOptional.isPresent()){
									status = 1;
								}
							}
						}
						hospitalMap.put("status", status);
						hospitalList.add(hospitalMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hospitalList;
	}
	
	@Override
	public Integer isBoundingByDoctor(String openId, String doctorName) {
		Integer result = 0;
		try {
			result = boundInfoMapper.isBoundingByDoctor(openId, doctorName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void setDoctorToSession(Long hospitalId) {
		Long doctorId = 0L;
		try {
			doctorId = boundInfoMapper.getDoctorId(SessionUtils.getOpenId(), hospitalId);
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("doctorId", doctorId);
			session.setAttribute("hospitalId", hospitalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer isBoundingByHospital(String openId, Long hospitalId) {
		Integer result = 0;
		try {
			result = boundInfoMapper.isBoundingByHospital(openId, hospitalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
