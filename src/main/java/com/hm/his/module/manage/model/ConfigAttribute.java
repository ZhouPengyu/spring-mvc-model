package com.hm.his.module.manage.model;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-05-31
 * Time: 16:03
 * CopyRight:HuiMei Engine
 */
public class ConfigAttribute {
    private String attrName;//属性名称

    private String attrValue;//属性值
    private String remark;//备注

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
