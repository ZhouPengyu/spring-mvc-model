package com.hm.his.outpatient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.hm.his.BaseControllerTest;
import com.hm.his.framework.cache.redis.JedisHelper;
import com.hm.his.module.purchase.model.PurchaseOrder;
import com.hm.his.module.purchase.model.PurchaseOrderDrug;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-conf.xml", "classpath:servlet.xml" })
public class HospitalExamControllerTest  extends BaseControllerTest {

	@SuppressWarnings("all")
	@Test
    public void verifyExamName() throws Exception {
		Map map = new HashMap();
//    	map.put("doctorName", "惠每");
//		map.put("password", "413f98f448a05365873eae33e54efc58");
//		map.put("loginStatus", 1);
//		JedisHelper.set("zpy", "zyou");
//		JedisHelper.del("zpy");
//    	System.out.println(JedisHelper.get(String.class, "zpy"));
//    	JedisHelper.removeJob("zpy");
//    	System.out.println(JedisHelper.get(String.class, "zpy"));
//    	Thread.sleep(1000);
//    	System.out.println(JedisHelper.get(String.class, "zpy"));
        super.testRole("/manage/searchAllExam", JSON.toJSONString(map));
    }
    
    @Test
    public void saveOrder() throws Exception {
    	PurchaseOrder purchaseOrder = new PurchaseOrder();
    	purchaseOrder.setMessage("HAJ");
    	purchaseOrder.setDeliveryAddressId(1);
    	PurchaseOrderDrug orderDrug = new PurchaseOrderDrug();
    	orderDrug.setDrugId(18);
    	orderDrug.setDrugName("香砂六君丸");
    	orderDrug.setDrugSpecification("9g/袋*10/盒");
    	orderDrug.setPackageUnit("盒");
    	orderDrug.setPurchaseDrugManufacturers("白山健宁药业有限公司");
    	orderDrug.setPurchaseQuantity(40);
    	
    	PurchaseOrderDrug orderDrug1 = new PurchaseOrderDrug();
    	orderDrug1.setDrugId(1);
    	orderDrug1.setDrugName("1");
    	orderDrug1.setDrugSpecification("10g/袋*12/盒");
    	orderDrug1.setPackageUnit("盒");
    	orderDrug1.setPurchaseDrugManufacturers("上海现代制药股份有限公司");
    	orderDrug1.setPurchaseQuantity(40);
    	List<PurchaseOrderDrug> list = new ArrayList<PurchaseOrderDrug>();
    	list.add(orderDrug1);
    	list.add(orderDrug);
    	purchaseOrder.setPurchaseOrderDrugList(list);
//        super.testRole("/purchase/savePurchaseOrder", JSON.toJSONString(purchaseOrder));
    }
}
