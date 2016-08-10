package com.hm.his.module.outpatient.model.request;

import java.util.List;
import java.util.Map;

import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientDiagnosis;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.outpatient.model.PatientInquiry;
import com.hm.his.module.outpatient.model.response.PatientChineseDrugResponse;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-4 11:28:35
 * @description 病历提交类
 * @version 3.0
 */
public class PatientInquiryRequest extends PatientInquiry {
    private List<PatientDiagnosis> diagnosis;	//诊断集合
    private List<PatientExam> examList;		//检查集合
    private List<Map<String, Object>> patentPrescriptionList;		//西药处方
    private List<PatientChineseDrug> chinesePrescriptionList;	//中药处方
    private List<PatientAdditional> additionalList;		//附件费用集合
    
    private String patientName;		//患者名称
    private Long gender;	//患者性别
    private String phoneNo;		//联系电话
    
    private List<Long> deleteExamIdList;				//删除检查ID集合
    private List<Long> deletePatentPrescriptionList;	//删除西药处方ID集合
    private List<Long> deleteChinesePrescriptionList;	//删除中药处方ID集合
    private List<Long> deleteAdditionalIdList;			//删除附件费用ID集合

	//==========================
	/**
	 * 模板类型     1:中药处方模板     2：西药处方 模板   5 ：病历模板    6：医嘱模板
	 */
	private Long id;
	private Integer tempType;
	/**
	 * 模板名称
	 */
	private String tempName;
	/**
	 * 模板描述
	 */
	private String tempDesc;

	/**
	 * 病历ID 或者处方ID
	 */
	private Long linkId;
	PatientChineseDrugResponse patientChineseDrug;//中药模板返回对象
	private HospitalOrder hospitalOrder;//病历中对应的 订单信息
	//==========================
    
	public List<PatientDiagnosis> getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(List<PatientDiagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}
	public List<PatientExam> getExamList() {
		return examList;
	}
	public void setExamList(List<PatientExam> examList) {
		this.examList = examList;
	}
	public List<Map<String, Object>> getPatentPrescriptionList() {
		return patentPrescriptionList;
	}
	public void setPatentPrescriptionList(
			List<Map<String, Object>> patentPrescriptionList) {
		this.patentPrescriptionList = patentPrescriptionList;
	}
	public List<PatientChineseDrug> getChinesePrescriptionList() {
		return chinesePrescriptionList;
	}
	public void setChinesePrescriptionList(
			List<PatientChineseDrug> chinesePrescriptionList) {
		this.chinesePrescriptionList = chinesePrescriptionList;
	}
	public List<PatientAdditional> getAdditionalList() {
		return additionalList;
	}
	public void setAdditionalList(List<PatientAdditional> additionalList) {
		this.additionalList = additionalList;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Long getGender() {
		return gender;
	}
	public void setGender(Long gender) {
		this.gender = gender;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public List<Long> getDeleteExamIdList() {
		return deleteExamIdList;
	}
	public void setDeleteExamIdList(List<Long> deleteExamIdList) {
		this.deleteExamIdList = deleteExamIdList;
	}
	public List<Long> getDeletePatentPrescriptionList() {
		return deletePatentPrescriptionList;
	}
	public void setDeletePatentPrescriptionList(
			List<Long> deletePatentPrescriptionList) {
		this.deletePatentPrescriptionList = deletePatentPrescriptionList;
	}
	public List<Long> getDeleteChinesePrescriptionList() {
		return deleteChinesePrescriptionList;
	}
	public void setDeleteChinesePrescriptionList(
			List<Long> deleteChinesePrescriptionList) {
		this.deleteChinesePrescriptionList = deleteChinesePrescriptionList;
	}
	public List<Long> getDeleteAdditionalIdList() {
		return deleteAdditionalIdList;
	}
	public void setDeleteAdditionalIdList(List<Long> deleteAdditionalIdList) {
		this.deleteAdditionalIdList = deleteAdditionalIdList;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTempType() {
		return tempType;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getTempDesc() {
		return tempDesc;
	}

	public void setTempDesc(String tempDesc) {
		this.tempDesc = tempDesc;
	}

	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public PatientChineseDrugResponse getPatientChineseDrug() {
		return patientChineseDrug;
	}

	public void setPatientChineseDrug(PatientChineseDrugResponse patientChineseDrug) {
		this.patientChineseDrug = patientChineseDrug;
	}

	public HospitalOrder getHospitalOrder() {
		return hospitalOrder;
	}

	public void setHospitalOrder(HospitalOrder hospitalOrder) {
		this.hospitalOrder = hospitalOrder;
	}
}
