package com.hm.his.module.purchase.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hm.his.module.drug.pojo.DrugRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.purchase.model.PurchaseOrder;
import com.hm.his.module.purchase.model.PurchaseOrderDrug;
import com.hm.his.module.purchase.model.request.PurchaseOrderRequest;
import com.hm.his.module.purchase.model.response.PurchaseOrderDrugResponse;
import com.hm.his.module.purchase.model.response.PurchaseOrderListResponse;
import com.hm.his.module.purchase.model.response.PurchaseOrderResponse;
import com.hm.his.module.purchase.model.response.PurchaseQuoteResponse;
import com.hm.his.module.purchase.service.PurchaseService;
import com.hm.his.module.quote.model.QuoteOrderDrug;
import com.hm.his.module.quote.model.QuoteOrder;
import com.hm.his.module.quote.service.QuoteService;
import com.hm.his.module.user.service.HospitalService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-2 11:35:38
 * @description 采购单管理
 * @version 1.0
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

	@Autowired(required = false)
	PurchaseService purchaseService;
	@Autowired(required = false)
	QuoteService quoteService;
	@Autowired(required = false)
	HospitalService hospitalService;
	@Autowired(required = false)
	DrugService drugService;
	
    /**
     * <p>Description:采购单列表查询<p>
     * @author ZhouPengyu
     * @date 2016-5-30 15:42:53
     */
    @RequestMapping(value = {"/searchPurchaseList"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String searchPurchaseList(@RequestBody PurchaseOrderRequest requestParams){
    	HisResponse hisResponse = new HisResponse();
    	requestParams.setHospitalId(SessionUtils.getHospitalId());
    	requestParams.setPageSize(25);
    	List<PurchaseOrderListResponse> purchaseOrderListResponseList = new ArrayList<PurchaseOrderListResponse>();
    	try {
    		Map<String, Object> body = new HashMap<String, Object>();
    		List<PurchaseOrder> purchaseOrderList = purchaseService.queryPurchaseOrder(requestParams);
    		for (PurchaseOrder purchaseOrder : purchaseOrderList) {
    			PurchaseOrderListResponse purchaseOrderListResponse = new PurchaseOrderListResponse();
    			purchaseOrderListResponse.setPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
    			purchaseOrderListResponse.setPurchaseOrderNumber(purchaseOrder.getPurchaseOrderNumber());
    			purchaseOrderListResponse.setStatus(purchaseOrder.getStatus());
    			purchaseOrderListResponse.setCreateDate(DateUtil.getGeneralDate(purchaseOrder.getCreateDate()));
				if(purchaseOrder.getStatus()==2){
					String supplierName = purchaseService.getQuoteCount(purchaseOrder.getPurchaseOrderId())+"家报价";
					purchaseOrderListResponse.setSupplierName(supplierName);
				}else if(purchaseOrder.getStatus()>2){
					Integer quoteOrderId = purchaseService.getQuoteOrderId(purchaseOrder.getPurchaseOrderId());
					QuoteOrder quoteOrderPojo = quoteService.getQuoteOrderDetail(quoteOrderId);
					purchaseOrderListResponse.setSupplier(quoteOrderPojo.getCreater());
					purchaseOrderListResponse.setSupplierName(quoteOrderPojo.getSupplierName());
					purchaseOrderListResponse.setTotalPrice(quoteOrderPojo.getTotalPrice());
					purchaseOrderListResponse.setSupplierPhoneNo(quoteOrderPojo.getPhoneNo());
					purchaseOrderListResponse.setScheduleDelivery(DateUtil.getGeneralDate(quoteOrderPojo.getScheduleDelivery()));
				}
				purchaseOrderListResponseList.add(purchaseOrderListResponse);
			}
    		int totalSize = purchaseService.queryPurchaseOrderCount(requestParams);
    		body.put("orderList", purchaseOrderListResponseList);
			body.put("totalPage", Math.ceil((double)totalSize / requestParams.getPageSize()));
			hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("查询采购订单失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
    /**
     * <p>Description:采购单详细<p>
     * @author ZhouPengyu
     * @date 2016-5-30 15:42:53
     */
    @RequestMapping(value = {"/getPurchaseOrderDetail"}, produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public String getPurchaseOrderDetail(@RequestBody Map<String, Integer> requestParams){
    	HisResponse hisResponse = new HisResponse();
    	
    	Integer purchaseOrderId = requestParams.get("purchaseOrderId");
    	PurchaseOrderResponse purchaseOrderResponse = null;
    	try {
    		PurchaseOrder purchaseOrder = purchaseService.getPurchaseOrder(purchaseOrderId);
    		
    		QuoteOrder quoteOrderPojo = new QuoteOrder();
    		List<PurchaseOrderDrugResponse> drugResponseList = new ArrayList<PurchaseOrderDrugResponse>();
    		if(purchaseOrder.getStatus()>2){
				Integer quoteOrderId = purchaseService.getQuoteOrderId(purchaseOrder.getPurchaseOrderId());
				quoteOrderPojo = quoteService.getQuoteOrderDetail(quoteOrderId);
				for (QuoteOrderDrug drug : quoteOrderPojo.getDrugList()) {
					
					HospitalDrug hospitalDrug = drugService.getDrugByPurchaseOrderDrugId(LangUtils.getLong(drug.getDrugId()));
					hospitalDrug.setId(null);
					hospitalDrug.setManufacturer(drug.getSupplyDrugManufacturers());
					
					PurchaseOrderDrugResponse drugResponse = new PurchaseOrderDrugResponse(drug);
					if(drugService.checkDrugRepeatedByNameAndSpec(hospitalDrug) == 0){	//确定供货商供货是否是新药
						drugResponse.setIsNew(1);
					}
					if(!StringUtils.equals(drug.getPurchaseDrugManufacturers(), drug.getSupplyDrugManufacturers())){	//判断供货药品的生成厂家是否修改
						drugResponse.setIsChangeManufacturer(1);
						if(null == drugResponse.getIsNew() || drugResponse.getIsNew()!=1){	//如果厂家修改，判断是否是新药，如果已经存在，则替换药品ID
							Integer drugId = drugService.getRepeatedDrugId(hospitalDrug);
							drugResponse.setDrugId(drugId);
						}
	    			}
					drugResponseList.add(drugResponse);
				}
			}else{
				List<PurchaseOrderDrug> drugList = purchaseService.getPurchaseOrderDrugList(purchaseOrderId);
				for (PurchaseOrderDrug drug : drugList) {
					PurchaseOrderDrugResponse drugResponse = new PurchaseOrderDrugResponse(drug);
					drugResponseList.add(drugResponse);
				}
			}

    		purchaseOrderResponse = new PurchaseOrderResponse(purchaseOrder, quoteOrderPojo);
    		if(null!=purchaseOrderResponse.getQuoteWay() && purchaseOrderResponse.getQuoteWay()==2){
    			String address = quoteService.getAddressById(quoteOrderPojo.getWayId()).getAddress();
    			purchaseOrderResponse.setAddress(address);
    		}
    		purchaseOrderResponse.setPurchaseOrderDrugList(drugResponseList);
    		hisResponse.setBody(purchaseOrderResponse);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("查询采购订单失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
    /**
     * <p>Description:保存采购订单<p>
     * @author ZhouPengyu
     * @date 2016年6月1日 下午5:09:53
     */
    @RequestMapping( value={"/savePurchaseOrder"}, produces="application/json;charset=UTF-8", method=RequestMethod.POST)
    @ResponseBody
    public String savePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){
    	HisResponse hisResponse = new HisResponse();
    	purchaseOrder.setPurchaseOrderNumber(String.valueOf(System.currentTimeMillis()));
    	purchaseOrder.setCreater(LangUtils.getInteger(SessionUtils.getDoctorId()));
    	purchaseOrder.setCreateDate(DateUtil.getGeneralDate(new Date()));
    	Map<String, Integer> body = new HashMap<String, Integer>();
    	try {
			Integer purchaseOrderId = purchaseService.savePurchaseOrder(purchaseOrder);
			body.put("purchaseOrderId", purchaseOrderId);
			hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("保存采购订单失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
    /**
     * <p>Description:取消采购订单<p>
     * @author ZhouPengyu
     * @date 2016年6月1日 下午5:09:53
     */
    @RequestMapping( value={"/cancelPurchaseOrder"}, produces="application/json;charset=UTF-8", method=RequestMethod.POST)
    @ResponseBody
    public String cancelPurchaseOrder(@RequestBody Map<String, Integer> requestParams){
    	HisResponse hisResponse = new HisResponse();
    	
    	Integer purchaseOrderId = requestParams.get("purchaseOrderId");
    	Map<String, Integer> body = new HashMap<String, Integer>();
    	try {
			Integer result = purchaseService.cancelPurchaseOrder(purchaseOrderId);
			body.put("result", result);
			hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("取消采购订单失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
    /**
     * <p>Description:确定供货商<p>
     * @author ZhouPengyu
     * @date 2016年6月1日 下午5:09:53
     */
    @RequestMapping( value={"/confirmPurchaseOrder"}, produces="application/json;charset=UTF-8", method=RequestMethod.POST)
    @ResponseBody
    public String confirmPurchaseOrder(@RequestBody Map<String, Integer> requestParams){
    	HisResponse hisResponse = new HisResponse();
    	
    	Integer purchaseOrderId = requestParams.get("purchaseOrderId");
    	Integer quoteOrderId = requestParams.get("quoteOrderId");
    	Map<String, Integer> body = new HashMap<String, Integer>();
    	try {
    		QuoteOrder quoteOrder = quoteService.getQuoteOrderDetail(quoteOrderId);
    		if(quoteOrder.getStatus() == 0){
    			hisResponse.setErrorCode(5000);
    			hisResponse.setErrorMessage("供货商已取消报价，请重新选择");
    		}else{
    			Integer result = purchaseService.confirmSupplier(purchaseOrderId, quoteOrderId);
    			body.put("result", result);
    		}
			hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("确定供货商失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
    /**
     * <p>Description:查询需采购药品列表<p>
     * @author ZhouPengyu
     * @date 2016年6月2日 下午3:22:48
     */
    @RequestMapping( value={"/searchDefaultPurchaseDrugList"}, produces="application/json;charset=UTF-8", method=RequestMethod.POST)
    @ResponseBody
    public String searchDefaultPurchaseDrugList(@RequestBody Map<String, Object> requestParams){
    	HisResponse hisResponse = new HisResponse();
    	
//    	List<PurchaseOrderDrug> orderDrugList = null;
    	try {
			DrugRequest drugRequest = new DrugRequest();
			drugRequest.setHospitalId(SessionUtils.getHospitalId());
    		List<HospitalDrug> drugList = drugService.searchDrugByInventoryAlarm(drugRequest);
    		
//    		orderDrugList = drugList.stream().map(hospitalDrug -> {
//    			PurchaseOrderDrug purchaseOrderDrug = new PurchaseOrderDrug();
//    			purchaseOrderDrug.setDrugId(LangUtils.getInteger(hospitalDrug.getId()));
//    			return purchaseOrderDrug;
//    		}).collect(Collectors.toList());
    		
			hisResponse.setBody(drugList);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("查询阈值告警药品列表失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
    /**
     * <p>Description:查询订单已报价的供应商列表<p>
     * @author ZhouPengyu
     * @date 2016年6月3日 下午4:49:55
     */
    @RequestMapping(value={"/viewTheQuotation"}, produces="application/json;charset=UTF-8", method=RequestMethod.POST)
    @ResponseBody
    public String viewTheQuotation(@RequestBody Map<String, Integer> requestParams){
    	HisResponse hisResponse = new HisResponse();
    	List<PurchaseQuoteResponse> quoteResponseList = new ArrayList<PurchaseQuoteResponse>();
    	try {
    		Map<String, Object> body = new HashMap<String, Object>();
    		
    		Integer purchaseOrderId = requestParams.get("purchaseOrderId");
    		PurchaseOrder purchaseOrder = purchaseService.getPurchaseOrder(purchaseOrderId);
			List<QuoteOrder> quoteOrderList = quoteService.queryQuoteOrderByPurchaseOrderId(purchaseOrderId);
			quoteResponseList = quoteOrderList.stream().map(quoteOrder -> {
				PurchaseQuoteResponse orderResponse = new PurchaseQuoteResponse(quoteOrder);
				if(null!=orderResponse.getQuoteWay() && orderResponse.getQuoteWay()==1){
					orderResponse.setAddress(purchaseOrder.getDeliveryAddress());
				}else{
					String address = quoteService.getAddressById(quoteOrder.getWayId()).getAddress();
					orderResponse.setAddress(address);
				}
				try {
					orderResponse.setOutOfStockSpecies(purchaseService.getOutOfStockSpecies(orderResponse.getQuoteOrderId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return orderResponse;
			}).collect(Collectors.toList());
			
			body.put("purchaseOrderNumber", purchaseOrder.getPurchaseOrderNumber());
			body.put("createDate", DateUtil.getGeneralDate(purchaseOrder.getCreateDate()));
			body.put("supplierOfferCount", purchaseService.getQuoteCount(purchaseOrderId));
			body.put("supplierList", quoteResponseList);
			hisResponse.setBody(body);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("查询供货单失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
    /**
     * <p>Description:查询供应商供应详细<p>
     * @author ZhouPengyu
     * @date 2016年6月3日 下午4:49:55
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value={"/getSupplyQuotationDetail"}, produces="application/json;charset=UTF-8", method=RequestMethod.POST)
    @ResponseBody
    public String getSupplyQuotationDetail(@RequestBody Map<String, Integer> requestParams){
    	HisResponse hisResponse = new HisResponse();
    	List<Map<String, Object>> orderResponseList = new ArrayList<>();
    	try {
    		Integer quoteOrderId = requestParams.get("quoteOrderId");
    		List<QuoteOrderDrug> orderDrugList = quoteService.getQuoteOrderDrugByQuoteOrderId(quoteOrderId);
    		orderResponseList = orderDrugList.stream().map( quoteOrderDrug -> {
    			Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(quoteOrderDrug), HashMap.class);
    			if(!StringUtils.equals(quoteOrderDrug.getPurchaseDrugManufacturers(), quoteOrderDrug.getSupplyDrugManufacturers())){
    				map.put("isChangeManufacturer", 1);
    			}
    			return map;
    		}).collect(Collectors.toList());
			hisResponse.setBody(orderResponseList);
		} catch (Exception e) {
			hisResponse.setErrorCode(5000);
			hisResponse.setErrorMessage("查询供货药品列表失败");
			e.printStackTrace();
		}
    	return hisResponse.toString();
    }
    
//    /**
//     * <p>Description:确定收货入库<p>
//     * @author ZhouPengyu
//     * @date 2016年6月3日 下午4:49:55
//     */
//	@RequestMapping(value={"/confirmDelivery"}, produces="application/json;charset=UTF-8", method=RequestMethod.POST)
//    @ResponseBody
//    public String confirmDelivery(@RequestBody Map<String, Integer> requestParams){
//    	HisResponse hisResponse = new HisResponse();
//    	Map<String, Object> body = new HashMap<>();
//    	try {
//    		body.put("result", 1);
//			hisResponse.setBody(body);
//		} catch (Exception e) {
//			hisResponse.setErrorCode(5000);
//			hisResponse.setErrorMessage("入库失败");
//			e.printStackTrace();
//		}
//    	return hisResponse.toString();
//    }
    
}
