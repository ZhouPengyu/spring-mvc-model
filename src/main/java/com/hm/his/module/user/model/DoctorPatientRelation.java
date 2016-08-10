package com.hm.his.module.user.model;


/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-4-8 11:23:20
 * @description 医生患者关联类
 * @version 3.0
 */
public class DoctorPatientRelation {
	private Long id;
	private Long doctorId;	//医生ID
	private Long patientId;	//患者ID
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
	public Long getPatientId() {
		return patientId;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
}
