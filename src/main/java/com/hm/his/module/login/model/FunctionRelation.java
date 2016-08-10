package com.hm.his.module.login.model;

public class FunctionRelation {
	
	private Long functionRelationId;
	private Long functionId;
	private Long doctorId;
	public Long getFunctionRelationId() {
		return functionRelationId;
	}
	public void setFunctionRelationId(Long functionRelationId) {
		this.functionRelationId = functionRelationId;
	}
	public Long getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}
	public Long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
	
}
