package com.hm.his.module.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.user.dao.HospitalMapper;
import com.hm.his.module.user.model.Hospital;
import com.hm.his.module.user.service.HospitalService;

@Service("HospitalService")
public class HospitalServiceImpl implements HospitalService {

	@Autowired(required = false)
	HospitalMapper hospitalMapper;
	
	@Override
	public boolean saveHospital(Hospital hospital) {
		if (hospital == null) return false;
        if (hospital.getHospitalId() == null) {
        	hospitalMapper.insertHospital(hospital);
        } else {
        	hospitalMapper.updateHospital(hospital);
        }
        return true;
	}

	@Override
	public String getHospitalName(Long doctorId) {
		return hospitalMapper.getHospitalName(doctorId);
	}

	@Override
	public Hospital getHospitalByDoctorId(Integer doctorId) {
		Hospital hospital = new Hospital();
		try {
			hospital = hospitalMapper.getHospitalByDoctorId(doctorId);
			return hospital;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Hospital getHospitalById(Long hospitalId) {
		return hospitalMapper.getHospitalById(hospitalId);
	}

}
