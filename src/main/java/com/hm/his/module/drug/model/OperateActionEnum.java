package com.hm.his.module.drug.model;

import java.util.HashMap;
import java.util.Map;


/**
 * Description:操作动作
 * Copyright (C) 2015 HMTimes All Right Reserved.
 * createDate：2015年11月13日
 * author：Tangwenwu
 * @version 1.0
 */
public enum OperateActionEnum {

    initInventory(1,"初始库存"),
    modifyInventory(2,"修改库存"),
    cutInventory(3,"删减库存"),
    returnInventory( 4,"退还库存");

    private Integer actionId;

    private String actionName;

    private static Map<Integer, String> map = new HashMap<Integer, String>();
    static {
        for (OperateActionEnum e : OperateActionEnum.values()) {
            map.put(e.actionId, e.actionName);
        }
    }

    private OperateActionEnum(Integer actionId, String actionName) {
        this.actionId = actionId;
        this.actionName = actionName;
    }

    public static String getActionNameById(Integer actionId) {
        return map.get(actionId);
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}