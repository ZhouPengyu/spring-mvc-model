package com.hm.his.module.order.model;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>支付方式</p>
 * Created by SuShaohua on 2016/7/5.
 */
public enum PayModeEnum {

    cash(1,"现金"),
    alipay(2,"支付宝"),
    tenpay(3,"微信"),
    bankcard(4,"银行卡"),
    other(5,"其他");

    private Integer payMode;

    private String payModeName;

    private static Map<Integer, String> map = new HashMap<Integer, String>();
    private static Map<String, Integer> typeNameMap = new HashMap<String, Integer>();
    static {
        for (PayModeEnum e : PayModeEnum.values()) {
            map.put(e.payMode, e.payModeName);
            typeNameMap.put(e.payModeName,e.payMode);
        }
    }

    private PayModeEnum(Integer payMode, String payModeName) {
        this.payMode = payMode;
        this.payModeName = payModeName;
    }

    public static String getPayModeNameByMode(Integer payMode) {
        return map.get(payMode);
    }

    public static Integer getPayModeByName(String payModeName) {
        return typeNameMap.get(payModeName);
    }

    public Integer getPayMode() {
        return payMode;
    }

    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }
}
