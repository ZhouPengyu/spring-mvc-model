package com.hm.his.module.outpatient.model;

/**
 * Created by wangjialin on 15/12/21.
 */
public class ExamSample {
    private String sampleId;
    private String sampleName;

    public ExamSample(){
    }

    public ExamSample(String sampleId, String sampleName){
        this.sampleId = sampleId;
        this.sampleName = sampleName;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }
}
