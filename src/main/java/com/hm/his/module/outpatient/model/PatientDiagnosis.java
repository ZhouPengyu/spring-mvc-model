package com.hm.his.module.outpatient.model;

/**
 * Created by wangjialin on 15/12/14.
 */
public class PatientDiagnosis {
    private Long recordId;
    private Long diseaseId;
    private String diseaseName;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }
}
