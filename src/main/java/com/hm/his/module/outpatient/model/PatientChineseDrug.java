package com.hm.his.module.outpatient.model;

import java.util.List;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-4 11:28:35
 * @description 中药处方实体类
 * @version 3.0
 */
public class PatientChineseDrug {
    private Long paChDrugId;
    private Long recordId;
    private Long totalDosage;
    private Long dailyDosage;
    private String frequency;
    private String  usage;
    private String requirement;
    private Long prescription;		//处方编号
    private List<PatientChineseDrugPieces> decoctionPiecesList;
    
    private Integer isCharged;
    
	public Long getPaChDrugId() {
		return paChDrugId;
	}
	public void setPaChDrugId(Long paChDrugId) {
		this.paChDrugId = paChDrugId;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public Long getTotalDosage() {
		return totalDosage;
	}
	public void setTotalDosage(Long totalDosage) {
		this.totalDosage = totalDosage;
	}
	public Long getDailyDosage() {
		return dailyDosage;
	}
	public void setDailyDosage(Long dailyDosage) {
		this.dailyDosage = dailyDosage;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public Long getPrescription() {
		return prescription;
	}
	public void setPrescription(Long prescription) {
		this.prescription = prescription;
	}
	public List<PatientChineseDrugPieces> getDecoctionPiecesList() {
		return decoctionPiecesList;
	}
	public void setDecoctionPiecesList(
			List<PatientChineseDrugPieces> decoctionPiecesList) {
		this.decoctionPiecesList = decoctionPiecesList;
	}
	public Integer getIsCharged() {
		return isCharged;
	}
	public void setIsCharged(Integer isCharged) {
		this.isCharged = isCharged;
	}
}
