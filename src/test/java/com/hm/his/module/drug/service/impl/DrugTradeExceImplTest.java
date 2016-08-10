package com.hm.his.module.drug.service.impl;

import com.google.common.collect.Maps;
import com.hm.his.framework.utils.BeanUtils;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.pojo.ExcelDrugTrade;
import com.hm.his.module.drug.pojo.ExcelHospitalDrugRecipe;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.drug.service.DrugTradeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/28
 * Time: 10:57
 * CopyRight:HuiMei Engine
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-conf.xml"})
public class DrugTradeExceImplTest {

    @Autowired
    DrugTradeService drugTradeService;

    @Autowired
    DrugService drugService;



    @Test
    public void testFindDrugTrade() throws Exception {
        List<ExcelHospitalDrugRecipe> drugTrades = ExcelHospitalDrugRecipe.parseExcel();
        Map<String ,ExcelHospitalDrugRecipe> drugRecipeMap = Maps.newHashMap();
        drugTrades.stream().forEach(drugRecipe ->{
            drugRecipeMap.put(drugRecipe.getDrug_id(),drugRecipe);
        });
        drugRecipeMap.entrySet().stream().forEach(entry ->{
            ExcelHospitalDrugRecipe tempDrug = entry.getValue();
            DrugTrade drugTrade = new DrugTrade();
            drugTrade.setCommonName(tempDrug.getDrug_name());
            drugTrade.setApprovalNumber(tempDrug.getApproval_number());
            drugTrade.setBarCode(tempDrug.getBar_code());
            DrugTrade dbTrade = drugTradeService.selectByDrugTrade(drugTrade);
            if(dbTrade != null){
                tempDrug.setCommon_name(dbTrade.getCommonName());
                tempDrug.setDrug_common_id(dbTrade.getId().toString());
            }
        });
        for(ExcelHospitalDrugRecipe excelDrugTrade : drugTrades){
            ExcelHospitalDrugRecipe excelHospitalDrugRecipe = drugRecipeMap.get(excelDrugTrade.getDrug_id());
            if(excelHospitalDrugRecipe !=null){
                excelDrugTrade.setCommon_name(excelHospitalDrugRecipe.getCommon_name());
                excelDrugTrade.setDrug_common_id(excelHospitalDrugRecipe.getDrug_common_id());
            }
        }
        ExcelHospitalDrugRecipe.matchResultToExcel(drugTrades);
    }


}