package com.hm.his.module.manage.model;

import java.util.List;

/**
 *  功能描述：诊所配置表
 * @author:  tangww
 * @createDate   2016-05-31
 *
 */
public class HospitalConfig {
    private Long id;

    private Long hospitalId;

    private Integer configType;//配置类型  1:功能配置   2:打印配置

    private String attrName;//属性名称

    private String attrValue;//属性值

    private String remark;//备注

    //页面属性===================
    private List<ConfigAttribute> configAttrs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getConfigType() {
        return configType;
    }

    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName == null ? null : attrName.trim();
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue == null ? null : attrValue.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }


    public List<ConfigAttribute> getConfigAttrs() {
        return configAttrs;
    }

    public void setConfigAttrs(List<ConfigAttribute> configAttrs) {
        this.configAttrs = configAttrs;
    }
}