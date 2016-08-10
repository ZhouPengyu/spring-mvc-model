package com.hm.his.module.drug.model;

public class Dictionary {
    private Integer id;

    private String cnName;

    private String enValue;

    private Integer type;  //字典类型 1:成药用法   2:用药频率  3.用药单位  4.包装单位   5.最小单位   6.饮片用法   7.服用要求   8.规格单位 9.饮片备注

    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName == null ? null : cnName.trim();
    }

    public String getEnValue() {
        return enValue;
    }

    public void setEnValue(String enValue) {
        this.enValue = enValue == null ? null : enValue.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}