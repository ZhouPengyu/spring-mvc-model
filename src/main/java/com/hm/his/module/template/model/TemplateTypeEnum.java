package com.hm.his.module.template.model;

import java.util.HashMap;
import java.util.Map;


/**
 * Description:模板分类 枚举值
 * Copyright (C) 2015 HMTimes All Right Reserved.
 * createDate：2015年11月13日
 * author：Tangwenwu
 * @version 1.0
 */
public enum TemplateTypeEnum {
    // 1:中药处方模板     2：西药处方 模板   5 ：病历模板    6：医嘱模板
    herbal(1,"中药处方"),
    westernMedicine(2,"西药处方"),
    prescription (3,"处方模板"),
    medicalRecord(5,"病历模板"),
    doctorAdvice(6,"医嘱模板");

    private Integer tempType;

    private String tempTypeName;

    private static Map<Integer, String> map = new HashMap<Integer, String>();
    private static Map<String, Integer> typeNameMap = new HashMap<String, Integer>();
    static {
        for (TemplateTypeEnum e : TemplateTypeEnum.values()) {
            map.put(e.tempType, e.tempTypeName);
            typeNameMap.put(e.tempTypeName,e.tempType);
        }
    }

    private TemplateTypeEnum(Integer tempType, String tempTypeName) {
        this.tempType = tempType;
        this.tempTypeName = tempTypeName;
    }

    public static String getTempTypeNameByType(Integer tempType) {
        return map.get(tempType);
    }

    public static Integer getTempTypeByName(String tempTypeName) {
        return typeNameMap.get(tempTypeName);
    }

	public Integer getTempType() {
		return tempType;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}

	public String getTempTypeName() {
		return tempTypeName;
	}

	public void setTempTypeName(String tempTypeName) {
		this.tempTypeName = tempTypeName;
	}
    




}