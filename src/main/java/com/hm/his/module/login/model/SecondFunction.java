package com.hm.his.module.login.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-08-01
 * Time: 11:50
 * CopyRight:HuiMei Engine
 */
public class SecondFunction {

    public Map<String, List<String>> getAllSecondFunMap(){
        Map<String, List<String>> secondFunMap = Maps.newHashMap();
        List<String> clinicFuns = Lists.newArrayList();
        clinicFuns.add("newRecord");
        clinicFuns.add("newPrescription");
        clinicFuns.add("fixSearch");
        secondFunMap.put(FunctionEnum.clinic.toString(),clinicFuns);

        List<String> chargeDrugFuns = Lists.newArrayList();
        chargeDrugFuns.add("noChargeList");
        chargeDrugFuns.add("hasChargeList");
        chargeDrugFuns.add("hasRefundList");
        chargeDrugFuns.add("purchase");
        secondFunMap.put(FunctionEnum.chargeDrug.toString(),chargeDrugFuns);

        List<String> drugManageFuns = Lists.newArrayList();
        drugManageFuns.add("drugList");
        drugManageFuns.add("drugHousing");
        drugManageFuns.add("stockManage");
        drugManageFuns.add("orderEarning");
        drugManageFuns.add("dateEarning");
        drugManageFuns.add("storageRecord");
        drugManageFuns.add("orderList");
        secondFunMap.put(FunctionEnum.drugManage.toString(),drugManageFuns);

        List<String> templateFuns = Lists.newArrayList();
        templateFuns.add("recipelList");
        templateFuns.add("adviceList");
        secondFunMap.put(FunctionEnum.template.toString(),templateFuns);

        List<String> statisticsFuns = Lists.newArrayList();
        statisticsFuns.add("allLogQuery");
        statisticsFuns.add("allSaleDrug");
        statisticsFuns.add("allCureItem");
        statisticsFuns.add("allAddCosts");
        statisticsFuns.add("allWorkCosts");
        secondFunMap.put(FunctionEnum.statistics.toString(),statisticsFuns);

        List<String> clinicManaFuns = Lists.newArrayList();
        clinicManaFuns.add("clinicRegisterInfo");
        clinicManaFuns.add("personManage");
        clinicManaFuns.add("departmentManage");
        clinicManaFuns.add("treatmentSet");
        clinicManaFuns.add("messageSet");
        clinicManaFuns.add("systemSet");
        secondFunMap.put(FunctionEnum.clinicMana.toString(),clinicManaFuns);

        secondFunMap.put(FunctionEnum.userHelp.toString(),null);

        secondFunMap.put(FunctionEnum.userCenter.toString(),null);

        List<String> systemMessageFuns = Lists.newArrayList();
        systemMessageFuns.add("newMessage");
        systemMessageFuns.add("allMessage");
        systemMessageFuns.add("starMessage");
        secondFunMap.put(FunctionEnum.systemMessage.toString(),systemMessageFuns);
        return secondFunMap;
    }
}
