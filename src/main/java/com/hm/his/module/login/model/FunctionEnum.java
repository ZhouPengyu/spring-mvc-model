package com.hm.his.module.login.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-07-27
 * Time: 11:24
 * CopyRight:HuiMei Engine
 */
public enum FunctionEnum {
    clinic(1000,"门诊"),
    chargeDrug(2000,"收费/发药"),
    drugManage(3000,"药品管理"),
    template(4000,"模版"),
    statistics(5000,"统计"),
    clinicMana(6000,"诊所管理"),
    userHelp(1002,"用户帮助"),
    userCenter(1001,"个人中心"),
    systemMessage(1003,"系统消息");

    private Integer funId;

    private String funName;




    private FunctionEnum(Integer funId, String funName) {
        this.funId = funId;
        this.funName = funName;
    }

    public static void main(String[] args) {
        System.out.println(FunctionEnum.drugManage.toString());
        System.out.println(clinic.toString());
    }

}
