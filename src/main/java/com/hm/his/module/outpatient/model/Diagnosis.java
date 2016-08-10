package com.hm.his.module.outpatient.model;

/**
 * Created by wangjialin on 15/12/16.
 */
public class Diagnosis {
    private Long diseaseId;
    private String diseaseName;

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
