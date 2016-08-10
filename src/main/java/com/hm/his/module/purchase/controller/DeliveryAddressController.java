package com.hm.his.module.purchase.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.purchase.model.DeliveryAddress;
import com.hm.his.module.purchase.service.DeliveryAddressService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-31 18:26:56
 * @description 收货地址管理
 * @version 1.0
 */
@RestController
@RequestMapping("/purchase")
public class DeliveryAddressController {

	@Autowired(required = false)
	DeliveryAddressService deliveryAddressService;
	
    /**
     * <p>Description:保存收货地址<p>
     * @author ZhouPengyu
     * @date 2016-5-31 18:27:14
     */
    @RequestMapping(value = {"/saveDeliveryAddress"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchPurchaseList(@RequestBody DeliveryAddress requestParams){
    	HisResponse hisResponse = new HisResponse();
    	
    	try {
    		Map<String, Integer> body = new HashMap<String, Integer>();
    		requestParams.setHospitalId(LangUtils.getInteger(SessionUtils.getHospitalId()));
    		Integer deliveryAddressId = deliveryAddressService.saveDeliveryAddress(requestParams);
    		body.put("deliveryAddressId", deliveryAddressId);
    		hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("保存地址失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
    /**
     * <p>Description:获取收货地址<p>
     * @author ZhouPengyu
     * @date 2016-5-31 18:27:14
     */
    @RequestMapping(value = {"/getDeliveryAddress"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getDeliveryAddress(@RequestBody DeliveryAddress requestParams){
    	HisResponse hisResponse = new HisResponse();
    	
    	try {
    		DeliveryAddress deliveryAddress = deliveryAddressService.getDefaultAddress(LangUtils.getInteger(SessionUtils.getHospitalId()));
    		hisResponse.setBody(deliveryAddress);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("获取地址失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
}
