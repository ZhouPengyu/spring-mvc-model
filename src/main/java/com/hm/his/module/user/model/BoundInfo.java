package com.hm.his.module.user.model;

import com.hm.his.framework.log.annotation.HmLogHelper;

public class BoundInfo {
	
    private Integer boundId;
    @HmLogHelper
    private String openId;
    private String doctorName;
    private String password;
    @HmLogHelper
    private Long doctorId;
    private Long hospitalId;
    private String boundDate;
    private String highPasswd;//高强度的密码

    public String getHighPasswd() {
        return highPasswd;
    }

    public void setHighPasswd(String highPasswd) {
        this.highPasswd = highPasswd;
    }

    public Integer getBoundId() {
        return boundId;
    }

    public void setBoundId(Integer boundId) {
        this.boundId = boundId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getBoundDate() {
		return boundDate;
	}

	public void setBoundDate(String boundDate) {
		this.boundDate = boundDate;
	}
}