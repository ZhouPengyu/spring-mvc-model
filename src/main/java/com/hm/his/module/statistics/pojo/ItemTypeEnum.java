package com.hm.his.module.statistics.pojo;

import com.hm.his.module.order.model.PayModeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SuShaohua on 2016/7/6.
 */
public enum ItemTypeEnum {

    exam(17, "检查治疗项"),
    addtional(18, "附加费用");

    private Integer itemType;

    private String itemTypeName;

    private static Map<Integer, String> map = new HashMap<Integer, String>();
    private static Map<String, Integer> typeNameMap = new HashMap<String, Integer>();
    static {
        for (ItemTypeEnum e : ItemTypeEnum.values()) {
            map.put(e.itemType, e.itemTypeName);
            typeNameMap.put(e.itemTypeName, e.itemType);
        }
    }

    private ItemTypeEnum(Integer itemType, String itemTypeName) {
        this.itemType = itemType;
        this.itemTypeName = itemTypeName;
    }

    public static String getItemTypeNameByMode(Integer itemType) {
        return map.get(itemType);
    }

    public static Integer getItemTypeByName(String itemTypeName) {
        return typeNameMap.get(itemTypeName);
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }
}
