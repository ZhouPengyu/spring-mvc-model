package com.hm.his.module.purchase.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SmsCaptchaUtil;
import com.hm.his.module.purchase.dao.PurchaseOrderDrugMapper;
import com.hm.his.module.purchase.dao.PurchaseOrderMapper;
import com.hm.his.module.purchase.model.PurchaseOrder;
import com.hm.his.module.purchase.model.PurchaseOrderDrug;
import com.hm.his.module.purchase.model.request.PurchaseOrderRequest;
import com.hm.his.module.purchase.service.DeliveryAddressService;
import com.hm.his.module.purchase.service.PurchaseService;
import com.hm.his.module.quote.model.PurchaseQuoteRelation;
import com.hm.his.module.quote.model.QuoteOrder;
import com.hm.his.module.quote.service.QuoteService;
import com.hm.his.module.user.service.HospitalService;

@Service("PurchaseService")
public class PurchaseServiceImpl implements PurchaseService {

	@Autowired(required = false)
	PurchaseOrderMapper purchaseOrderMapper;
	@Autowired(required = false)
	PurchaseOrderDrugMapper purchaseOrderDrugMapper;
	@Autowired(required = false)
	QuoteService quoteService;
	@Autowired(required = false)
	DeliveryAddressService deliveryAddressService;
	@Autowired(required = false)
	HospitalService hospitalService;
	
	@Override
	public List<PurchaseOrder> queryPurchaseOrder(PurchaseOrderRequest request) {
		List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();
		try {
			purchaseOrderList = purchaseOrderMapper.queryPurchaseOrder(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrderList;
	}
	@Override
	public Integer queryPurchaseOrderCount(PurchaseOrderRequest request) {
		Integer count = 0;
		try {
			count = purchaseOrderMapper.queryPurchaseOrderCount(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public List<PurchaseOrder> queryPurchaseOrderAll(PurchaseOrderRequest request) throws Exception {
		List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();
		try {
			purchaseOrderList = purchaseOrderMapper.queryPurchaseOrderAll(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrderList;
	}
	@Override
	public Integer getQuoteCount(Integer purchaseOrderId) {
		Integer count = 0;
		try {
			count = purchaseOrderMapper.getQuoteCount(purchaseOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public Integer getQuoteOrderId(Integer purchaseOrderId) {
		Integer quoteOrderId = 0;
		try {
			quoteOrderId = purchaseOrderMapper.getQuoteOrderId(purchaseOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quoteOrderId;
	}
	@Override
	public PurchaseOrder getPurchaseOrder(Integer purchaseOrderId) {
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		try {
			purchaseOrder = purchaseOrderMapper.getPurchaseOrder(purchaseOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrder;
	}
	@Override
	public List<PurchaseOrderDrug> getPurchaseOrderDrugList(Integer purchaseOrderId) throws Exception {
		List<PurchaseOrderDrug> purchaseOrderDrugList = new ArrayList<PurchaseOrderDrug>();
		try {
			purchaseOrderDrugList = purchaseOrderDrugMapper.getPurchaseOrderDrugList(purchaseOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrderDrugList;
	}
	@Override
	public PurchaseOrder getPurchaseOrderDetail(Integer purchaseOrderId) throws Exception {
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		try {
			purchaseOrder = purchaseOrderMapper.getPurchaseOrder(purchaseOrderId);
			purchaseOrder.setPurchaseOrderDrugList(purchaseOrderDrugMapper.getPurchaseOrderDrugList(purchaseOrderId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrder;
	}
	
	@Override
	public Integer savePurchaseOrder(PurchaseOrder purchaseOrder) {
		Integer purchaseOrderId = 0;
		try {
			if(null != purchaseOrder.getPurchaseOrderId() && purchaseOrder.getPurchaseOrderId() > 0){
				if(purchaseOrderMapper.updatePurchaseOrder(purchaseOrder) > 0){
					//清除历史药品
					Integer result = purchaseOrderDrugMapper.deleteDrugByOrderId(purchaseOrder.getPurchaseOrderId());
					if(result > 0){
						purchaseOrderDrugMapper.insertPurchaseOrderDrug(purchaseOrder.getPurchaseOrderId(), purchaseOrder.getPurchaseOrderDrugList());
						purchaseOrderId = purchaseOrder.getPurchaseOrderId();
					}
				}
				
			}else{
				if(purchaseOrderMapper.insertPurchaseOrder(purchaseOrder) > 0){
					purchaseOrderDrugMapper.insertPurchaseOrderDrug(purchaseOrder.getPurchaseOrderId(), purchaseOrder.getPurchaseOrderDrugList());
					purchaseOrderId = purchaseOrder.getPurchaseOrderId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrderId;
	}
	
	@Override
	public Integer cancelPurchaseOrder(Integer purchaseOrderId) {
		Integer result = 0;
		try {
			result = purchaseOrderMapper.cancelPurchaseOrder(purchaseOrderId);
			purchaseOrderMapper.updateQuoteOrderByCancelOrder(purchaseOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public Integer confirmSupplier(Integer purchaseOrderId, Integer quoteOrderId) {
		Integer result = 0;
		try {
			result = purchaseOrderMapper.confirmSupplier(purchaseOrderId, quoteOrderId);
			PurchaseQuoteRelation relation = new PurchaseQuoteRelation();
			relation.setPurchaseOrderId(purchaseOrderId);
			relation.setQuoteOrderId(quoteOrderId);
			purchaseOrderMapper.updatePurchaseOrderStatus(purchaseOrderId, 3);
			purchaseOrderMapper.cancelOtherSupplier(purchaseOrderId);
			quoteService.confirmQuoteOrder(quoteOrderId);
			QuoteOrder quoteOrder = quoteService.getQuoteOrderDetail(quoteOrderId);
			PurchaseOrder purchaseOrder = purchaseOrderMapper.getPurchaseOrder(purchaseOrderId);
			
            quoteOrder.setHospitalPhoneNo(deliveryAddressService.getDeliveryAddressById(quoteOrder.getWayId()).getPhoneNo());
            quoteOrder.setHospitalName(hospitalService.getHospitalName(LangUtils.getLong(purchaseOrder.getCreater())));
			
			String smsDetail = quoteOrder.getSupplierName()+",您对"+quoteOrder.getHospitalName()+"（联系电话："+quoteOrder.getHospitalPhoneNo()+"）"+"的报价单被确定下单，请您尽快准备药品出库。";
			SmsCaptchaUtil.sendSms(quoteOrder.getPhoneNo(), smsDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public PurchaseOrder getPurchaseOrderByNo(String purchaseOrderNumber)
			throws Exception {
		PurchaseOrder purchaseOrder = null;
		try {
			PurchaseOrderRequest request = new PurchaseOrderRequest();
			request.setPurchaseOrderNumber(purchaseOrderNumber);
			List<PurchaseOrder> purchaseOrderList = purchaseOrderMapper.queryPurchaseOrder(request);
			if(null != purchaseOrderList)
				purchaseOrder = purchaseOrderList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrder;
	}
	@Override
	public Integer getOutOfStockSpecies(Integer quoteOrderId) {
		Integer count = 0;
		try {
			count = purchaseOrderMapper.getOutOfStockSpecies(quoteOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public Integer updatePurchaseOrderStatus(Integer purchaseOrderId, Integer status) {
		Integer result = 0;
		try {
			result = purchaseOrderMapper.updatePurchaseOrderStatus(purchaseOrderId, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public PurchaseOrderDrug getPurchaseOrderDrug(Integer purchaseOrderId, Integer drugId) {
		PurchaseOrderDrug drug = null;
		try {
			drug = purchaseOrderDrugMapper.getPurchaseOrderDrug(purchaseOrderId, drugId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drug;
	}

}
