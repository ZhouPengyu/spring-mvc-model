package com.hm.his.module.manage.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.his.BaseControllerTest;
import com.hm.his.module.manage.model.ConfigAttribute;
import com.hm.his.module.manage.model.HospitalConfig;
import com.hm.his.module.manage.model.HospitalConfigEnum;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-05-31
 * Time: 17:39
 * CopyRight:HuiMei Engine
 */
public class HospitalControllerTest extends BaseControllerTest{

    @Test
    public void testSearchHospitalConfig() throws Exception {
        HospitalConfig hospitalConfig = new HospitalConfig();
        hospitalConfig.setHospitalId(23L);
        hospitalConfig.setConfigType(1);
        super.testRole("/manage/searchHospitalConfig", JSON.toJSONString(hospitalConfig));
    }

    @Test
    public void testSaveHospitalConfig() throws Exception {
//        HospitalConfig hospitalConfig = new HospitalConfig();
//        hospitalConfig.setConfigType(2);
//        List<ConfigAttribute> configAttrs = Lists.newArrayList();
//        ConfigAttribute attr1 = new ConfigAttribute();
//        attr1.setAttrName("paperType");
//        attr1.setAttrValue("58");
//        ConfigAttribute attr2 = new ConfigAttribute();
//        attr2.setAttrName("invoiceLookedUp");
//        attr2.setAttrValue("鹏鹏诊所");
//        ConfigAttribute attr3 = new ConfigAttribute();
//        attr3.setAttrName("inscribe");
//        attr3.setAttrValue("谢谢惠顾");
//        configAttrs.add(attr1);
//        configAttrs.add(attr2);
//        configAttrs.add(attr3);
//        hospitalConfig.setConfigAttrs(configAttrs);
//        super.testRole("/manage/saveHospitalConfig", JSON.toJSONString(hospitalConfig));

        HospitalConfig hospitalConfig = new HospitalConfig();
        hospitalConfig.setConfigType(1);
        List<ConfigAttribute> configAttrs = Lists.newArrayList();
        ConfigAttribute attr1 = new ConfigAttribute();
        attr1.setAttrName(HospitalConfigEnum.useBatchManage.name());
        attr1.setAttrValue("1");

        configAttrs.add(attr1);

        hospitalConfig.setConfigAttrs(configAttrs);
        super.testRole("/manage/saveHospitalConfig", JSON.toJSONString(hospitalConfig));
    }
}