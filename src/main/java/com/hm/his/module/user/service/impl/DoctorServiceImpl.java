package com.hm.his.module.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hm.his.framework.crypt.MD5Utils;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.login.dao.FunctionMapper;
import com.hm.his.module.login.model.Function;
import com.hm.his.module.user.dao.DoctorMapper;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.DoctorService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description doctor服务实现类
 * @version 3.0
 */
@Service("doctorService")
public class DoctorServiceImpl implements DoctorService{
	
    @Autowired(required = false)
    private DoctorMapper doctorMapper;
    @Autowired(required = false)
    private FunctionMapper functionMapper;

    @Override
    public Doctor getDoctorById(Long doctorId){
        return doctorId != null ? doctorMapper.getDoctorById(doctorId) : null;
    }

    @Override
    public Doctor getDoctorByName(String doctorName) {
        return doctorMapper.getDoctorByName(doctorName);
    }

    @Override
    public boolean saveDoctor(Doctor doctor){
        if (doctor.getDoctorId() == null) {
            insertDoctor(doctor);
        } else {
            updateDoctor(doctor);
        }
        return true;
    }

    /**
     * <p>Description:验证用户名密码-----仅针对 用户录入的情况下使用<p>
     * @author ZhouPengyu
     * @date 2016年3月7日 下午6:09:11
     */
    @Override
    public boolean verifyPassword(String doctorName, String password){
        if (StringUtils.isBlank(doctorName) || StringUtils.isBlank(password)){
            return false;
        }
        // 对密码进行 加盐加密
        String hightPassword = MD5Utils.passwordSaltHash(doctorName, password);
        Doctor doctorPwd = doctorMapper.getPassword(doctorName);
        if(doctorPwd !=null ){
            System.out.printf("verifyPassword------userName:%s,pagePwd:%s,highPwd:%s,dbPwd:%s \n\r",doctorName,password,hightPassword,doctorPwd.getHighPasswd());
            if (StringUtils.isNotBlank(doctorPwd.getHighPasswd())&& hightPassword.equals(doctorPwd.getHighPasswd())) return true;

        }else{
            return false;
        }
        return false;
    }

    /**
     * <p>Description:验证用户名密码  --用于从数据库中查询出来的密码 比较<p>
     * @author ZhouPengyu
     * @date 2016年3月7日 下午6:09:11
     */
    @Override
    public boolean verifyPasswordByHighPasswd(String doctorName, String highPasswd){
        if (StringUtils.isBlank(doctorName) || StringUtils.isBlank(highPasswd)){
            return false;
        }
        Doctor doctorPwd = doctorMapper.getPassword(doctorName);
        if(doctorPwd !=null ){
            if (StringUtils.isNotBlank(doctorPwd.getHighPasswd())&& highPasswd.equals(doctorPwd.getHighPasswd())) return true;
        }else{
            return false;
        }
        return false;
    }

    @SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_DOCTOR_ADD)
    private void insertDoctor(Doctor doctor){
        if (doctor != null && doctor.getDoctorId() == null){
            doctorMapper.insertDoctor(doctor);
        }
    }

    @SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_DOCTOR_MODIFY)
    private void updateDoctor(Doctor doctor){
        doctorMapper.updateDoctor(doctor);
    }

	@Override
	public Integer verifyUserName(String userName) {
		if(StringUtils.isNotEmpty(userName)){
			return doctorMapper.verifyUserName(userName);
		}
		return null;
	}

	@Override
	public Integer verifyHospitalName(String userName) {
		return null;
	}

	@Override
	public List<Function> getDoctorFunction(Long doctorId) {
		return functionMapper.getDoctorFunction(doctorId);
	}

	@Override
    public Map<Long, String> getDoctorName(List<Long> doctorIdList) {
        List<Doctor> doctorList = doctorMapper.getDoctorByIdList(doctorIdList);
        Map<Long, String> result = new HashMap<>();
        if (doctorList == null) return result;
        for (Doctor doctor : doctorList){
            result.put(doctor.getDoctorId(), doctor.getDoctorName());
        }
        return result;
    }
	@Override
	public Map<Long, String> getDoctorRealNames(List<Long> doctorIdList) {
		List<Doctor> doctorList = doctorMapper.getDoctorByIdList(doctorIdList);
		Map<Long, String> result = new HashMap<>();
		if (doctorList == null) return result;
		for (Doctor doctor : doctorList){
			result.put(doctor.getDoctorId(), doctor.getRealName()==null?doctor.getDoctorName():doctor.getRealName());
		}
		return result;
	}

	@Override
	@SystemLogAnno(functionId = FunctionConst.CLINIC_OPER_LOG,operationId = Operation.CLINIC_DOCTOR_DELETE)
	public Integer deleteDoctor(Long doctorId, Long hospitalId) {
		return doctorMapper.deleteDoctor(doctorId, hospitalId);
	}

	@Override
	public List<Doctor> searchDoctor(Doctor doctor) {
		return doctorMapper.searchDoctor(doctor);
	}

	@Override
	public void saveDoctorFunction(Long doctorId, List<Long> functionIdList) {
		functionMapper.saveDoctorFunction(doctorId, functionIdList);
	}

	@Override
	public Integer insertDoctorPatientRelation(Long doctorId, Long patientId) {
		return doctorMapper.insertDoctorPatientRelation(doctorId, patientId);
	}

	@Override
	public Integer isDoctorPatientRelation(Long doctorId, Long patientId) {
		return doctorMapper.isDoctorPatientRelation(doctorId, patientId);
	}

    @Override
    public void saveDoctorPatientRelationForOutpatient(Long doctorId, Long patientId) {
        if(isDoctorPatientRelation(doctorId, patientId)<1){
            insertDoctorPatientRelation(doctorId, patientId);
        }
    }

    @Override
	public Integer deleteDoctorFunctionByDocId(Long doctorId) {
		return functionMapper.deleteDoctorFunctionByDocId(doctorId);
	}

}
