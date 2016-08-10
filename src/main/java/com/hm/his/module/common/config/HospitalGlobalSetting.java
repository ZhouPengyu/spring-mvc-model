package com.hm.his.module.common.config;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-06-07
 * Time: 11:30
 * CopyRight:HuiMei Engine
 */
public class HospitalGlobalSetting {
    private Integer hospitalUseBatchManage =0; //所在医院是否使用 批次入库管理   1:是   0： 否

    public Integer getHospitalUseBatchManage() {
        return hospitalUseBatchManage;
    }

    public void setHospitalUseBatchManage(Integer hospitalUseBatchManage) {
        this.hospitalUseBatchManage = hospitalUseBatchManage;
    }
}
