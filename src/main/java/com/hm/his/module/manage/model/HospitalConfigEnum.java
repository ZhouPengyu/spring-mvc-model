package com.hm.his.module.manage.model;

import java.util.HashMap;
import java.util.Map;


/**
 * Description:药品分类 枚举值
 * Copyright (C) 2015 HMTimes All Right Reserved.
 * createDate：2015年11月13日
 * author：Tangwenwu
 * @version 1.0
 */
public enum HospitalConfigEnum {

    useBatchManage("useBatchManage","是否使用批次入库"),
    paperType("paperType","纸张类型"),
    invoiceLookedUp("invoiceLookedUp","抬头"),
    inscribe("inscribe","落款");


    private String attrName;

    private String attrNameCN;

    private static Map<String, String> map = new HashMap<String, String>();
    private static Map<String, String> typeNameMap = new HashMap<String, String>();
    static {
        for (HospitalConfigEnum e : HospitalConfigEnum.values()) {
            map.put(e.attrName, e.attrNameCN);
            typeNameMap.put(e.attrNameCN,e.attrName);
        }
    }

    private HospitalConfigEnum(String attrName, String attrNameCN) {
        this.attrName = attrName;
        this.attrNameCN = attrNameCN;
    }

    public static String getAttrNameCNByName(String attrName) {
        return map.get(attrName);
    }

    public static String getAttrNameByCN(String attrNameCN) {
        return typeNameMap.get(attrNameCN);
    }



}