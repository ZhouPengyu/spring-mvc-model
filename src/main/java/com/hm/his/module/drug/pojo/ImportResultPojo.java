package com.hm.his.module.drug.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 导入药品结果
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/1
 * Time: 14:30
 * CopyRight:HuiMei Engine
 */
public class ImportResultPojo implements Serializable{
    private static final long serialVersionUID = 3151836054335779057L;
    private Long successCount;//成功导入数量

    private Long failCount;//失败导入数量
    private String downloadKey;//下载文件的Key
    private Long hospitalId;
    private List<DrugResult> failList;//失败列表
    private List<DrugResult> successList;//成功列表

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getFailCount() {
        return failCount;
    }

    public void setFailCount(Long failCount) {
        this.failCount = failCount;
    }

    public String getDownloadKey() {
        return downloadKey;
    }

    public void setDownloadKey(String downloadKey) {
        this.downloadKey = downloadKey;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public List<DrugResult> getFailList() {
        return failList;
    }

    public void setFailList(List<DrugResult> failList) {
        this.failList = failList;
    }

    public List<DrugResult> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<DrugResult> successList) {
        this.successList = successList;
    }

    public static class DrugResult implements Serializable{
        private static final long serialVersionUID = 3151936054335779057L;


        private Boolean result;//成功或失败
        private Integer rowId;//行序号
        private String drugName;//下载文件的Key
        private String errorMsg;//失败原因

        public Integer getRowId() {
            return rowId;
        }

        public void setRowId(Integer rowId) {
            this.rowId = rowId;
        }

        public String getDrugName() {
            return drugName;
        }

        public void setDrugName(String drugName) {
            this.drugName = drugName;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }
    }
}
