package com.hm.his.module.user.model;

import java.io.Serializable;
import java.util.Map;

import com.hm.his.framework.log.annotation.HmLogHelper;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-23 12:03:22
 * @description 医生用户实体类
 * @version 3.0
 */
public class Doctor implements Serializable{
	private static final long serialVersionUID = 3151836054325779057L;
	@HmLogHelper
	private Long doctorId;
	@HmLogHelper
	private String doctorName;
	private String realName;	//真实姓名
	private String phone;
	private Integer gender;
	private String mail;	//邮箱
	private String laboratoryName;	//科室名称
	private Long laboratoryId;	//科室ID
	private Long hospitalId;	//医院ID
	private String password;
	private String hospitalName;	//医院名称
	private Long isAdmin; // 是否是管理员
	private Map<String, Object> function;	//权限控制
	private Long status;	//用户状态	0 未审核， 1审核通过，2  账号锁定，3 手机未激活   4:删除    -1：用户被禁用
	private Long flag;	//默认为启用 1:启用	  0:禁用

	private String highPasswd;//高强度的密码

	public String getHighPasswd() {
		return highPasswd;
	}

	public void setHighPasswd(String highPasswd) {
		this.highPasswd = highPasswd;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Long getLaboratoryId() {
		return laboratoryId;
	}

	public void setLaboratoryId(Long laboratoryId) {
		this.laboratoryId = laboratoryId;
	}

	public Long getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Long isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getLaboratoryName() {
		return laboratoryName;
	}

	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}

	public Map<String, Object> getFunction() {
		return function;
	}

	public void setFunction(Map<String, Object> function) {
		this.function = function;
	}

	public Long getFlag() {
		return flag;
	}

	public void setFlag(Long flag) {
		this.flag = flag;
	}

}
