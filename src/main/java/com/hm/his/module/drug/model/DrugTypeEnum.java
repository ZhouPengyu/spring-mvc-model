package com.hm.his.module.drug.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Description:药品分类 枚举值
 * Copyright (C) 2015 HMTimes All Right Reserved.
 * createDate：2015年11月13日
 * author：Tangwenwu
 * @version 1.0
 */
public enum DrugTypeEnum {

    herbal(1,"饮片"),
    cnPatentMedicine(2,"中成药"),
    westernMedicine(3,"化学制剂"),
    consumable(4,"耗材"),
    biologicalProduct(5,"生物制品"),
    other(6,"其他");

    private Integer drugType;

    private String drugTypeName;

    private static Map<Integer, String> map = new HashMap<Integer, String>();
    private static Map<String, Integer> typeNameMap = new HashMap<String, Integer>();
    static {
        for (DrugTypeEnum e : DrugTypeEnum.values()) {
            map.put(e.drugType, e.drugTypeName);
            typeNameMap.put(e.drugTypeName,e.drugType);
        }
    }

    private DrugTypeEnum(Integer drugType, String drugTypeName) {
        this.drugType = drugType;
        this.drugTypeName = drugTypeName;
    }

    public static String getDrugTypeNameByType(Integer drugType) {
        return map.get(drugType);
    }

    public static Integer getDrugTypeByName(String drugTypeName) {
        return typeNameMap.get(drugTypeName);
    }

	public Integer getDrugType() {
		return drugType;
	}

	public void setDrugType(Integer drugType) {
		this.drugType = drugType;
	}

	public String getDrugTypeName() {
		return drugTypeName;
	}

	public void setDrugTypeName(String drugTypeName) {
		this.drugTypeName = drugTypeName;
	}

}