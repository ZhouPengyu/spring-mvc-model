package com.hm.his.module.drug.service.impl;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.hm.his.framework.utils.BeanUtils;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.pojo.ExcelDrugTrade;
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

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/28
 * Time: 10:57
 * CopyRight:HuiMei Engine
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-conf.xml", "classpath:mybatis.xml" })
public class DrugTradeServiceImplTest {

    @Autowired
    DrugTradeService drugTradeService;

    @Autowired
    DrugService drugService;



    //@Test
    public void testFindAllDrugTrade() throws Exception {
        List<ExcelDrugTrade> drugTrades = ExcelDrugTrade.parseExcel();
        Date createDate = new Date();
        for(ExcelDrugTrade excelDrugTrade : drugTrades){
            DrugTrade drugTrade = new DrugTrade();
            BeanUtils.copyProperties(excelDrugTrade,drugTrade,false);
            drugTrade.setDrugCommonId(excelDrugTrade.getCdssDrugCommonId());
            drugTrade.setStatus(1);
            drugTrade.setCreater(1L);
            drugTrade.setCreateDate(createDate);
            drugTradeService.saveDrug(drugTrade);
        }



    }

    //@Test
    public void testSearchDrugSaleWayById() throws Exception {
        List<SaleWayPojo>  saleWayPojos = drugService.searchDrugSaleWayById(1,97L);
        System.out.println(saleWayPojos);
    }

    //@Test
    public void testSaveDrug() throws Exception {

    }

   // @Test
    public void testModifyDrug() throws Exception {

    }
}