package com.hm.his.module.message.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuShaohua
 * @date 2016/7/28 19:37
 * @description
 */
public enum MessageBoxEnum {
    /**
     * 普通邮件 10
     * 星标邮件 13
     * 已删除普通邮件  20
     * 已删除星标邮件  23
     */
    inbox(1,"收件箱"),
    delete(2,"已删除"),
    asterisk(13,"星标通知");

    private Integer boxType;

    private String boxTypeName;

    private static Map<Integer, String> map = new HashMap<Integer, String>();
    private static Map<String, Integer> typeNameMap = new HashMap<String, Integer>();
    static {
        for (MessageBoxEnum e : MessageBoxEnum.values()) {
            map.put(e.boxType, e.boxTypeName);
            typeNameMap.put(e.boxTypeName,e.boxType);
        }
    }

    private MessageBoxEnum(Integer boxType, String boxTypeName) {
        this.boxType = boxType;
        this.boxTypeName = boxTypeName;
    }

    public static String getBoxTypeNameByType(Integer boxType) {
        return map.get(boxType);
    }

    public static Integer getBoxTypeByName(String boxTypeName) {
        return typeNameMap.get(boxTypeName);
    }

    public String getBoxTypeName() {
        return boxTypeName;
    }

    public void setBoxTypeName(String boxTypeName) {
        this.boxTypeName = boxTypeName;
    }

    public Integer getBoxType() {
        return boxType;
    }

    public void setBoxType(Integer boxType) {
        this.boxType = boxType;
    }
}
