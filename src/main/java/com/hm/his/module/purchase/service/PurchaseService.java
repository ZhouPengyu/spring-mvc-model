package com.hm.his.module.purchase.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.purchase.model.PurchaseOrder;
import com.hm.his.module.purchase.model.PurchaseOrderDrug;
import com.hm.his.module.purchase.model.request.PurchaseOrderRequest;

public interface PurchaseService {
	
	/**
	 * <p>Description:根据条件查询采购单列表<p>
	 * @author ZhouPengyu
	 * @date 2016年5月30日 下午4:36:07
	 */
	List<PurchaseOrder> queryPurchaseOrder(PurchaseOrderRequest request) throws Exception;
	
	/**
	 * <p>Description:查询采购订单<p>
	 * @author ZhouPengyu
	 * @date 2016年6月6日 上午10:38:59
	 */
	List<PurchaseOrder> queryPurchaseOrderAll(PurchaseOrderRequest request) throws Exception;
	
	/**
	 * <p>Description:根据编号查询采购单<p>
	 * @author ZhouPengyu
	 * @date 2016-6-1 18:05:09
	 */
	PurchaseOrder getPurchaseOrderByNo(String purchaseOrderNumber) throws Exception;
	
	/**
	 * <p>Description:根据条件查询采购单总数<p>
	 * @author ZhouPengyu
	 * @date 2016年5月30日 下午4:36:07
	 */
	Integer queryPurchaseOrderCount(PurchaseOrderRequest request) throws Exception;
	
	/**
	 * <p>Description:查询报价商家总数<p>
	 * @author ZhouPengyu
	 * @date 2016年5月30日 下午4:36:07
	 */
	Integer getQuoteCount(Integer purchaseOrderId) throws Exception;
	
	/**
	 * <p>Description:查询报价单<p>
	 * @author ZhouPengyu
	 * @date 2016年5月30日 下午4:36:07
	 */
	Integer getQuoteOrderId(Integer purchaseOrderId) throws Exception;
	
	/**
	 * <p>Description:根据ID查询采购单<p>
	 * @author ZhouPengyu
	 * @date 2016-5-31 11:42:40
	 */
	PurchaseOrder getPurchaseOrder(Integer purchaseOrderId) throws Exception;
	
	/**
	 * <p>Description:根据ID查询采购单药品详细<p>
	 * @author ZhouPengyu
	 * @date 2016-5-31 11:42:40
	 */
	List<PurchaseOrderDrug> getPurchaseOrderDrugList(Integer purchaseOrderId) throws Exception;
	
	/**
	 * <p>Description:根据ID查询采购单详细<p>
	 * @author ZhouPengyu
	 * @date 2016-5-31 11:42:40
	 */
	PurchaseOrder getPurchaseOrderDetail(Integer purchaseOrderId) throws Exception;
	
	/**
	 * <p>Description:保存采购订单<p>
	 * @author ZhouPengyu
	 * @date 2016年6月1日 上午11:21:25
	 */
	Integer savePurchaseOrder(PurchaseOrder purchaseOrder) throws Exception;
	
	/**
	 * <p>Description:取消采购订单<p>
	 * @author ZhouPengyu
	 * @date 2016年6月1日 下午4:38:58
	 */
	Integer cancelPurchaseOrder(Integer purchaseOrderId) throws Exception;
	
	/**
     * <p>Description:确定供货商<p>
     * @author ZhouPengyu
     * @date 2016年6月1日 下午5:17:19
     */
    Integer confirmSupplier(Integer purchaseOrderId, Integer quoteOrderId) throws Exception;
    
    /**
     * <p>Description:供货单缺货总数<p>
     * @author ZhouPengyu
     * @date 2016年6月3日 上午11:26:26
     */
    Integer getOutOfStockSpecies(Integer quoteOrderId) throws Exception;
    
    /**
     * <p>Description:更新采购单状态<p>
     * @author ZhouPengyu
     * @date 2016年6月3日 下午4:40:38
     */
    Integer updatePurchaseOrderStatus(Integer purchaseOrderId, Integer status) throws Exception;
    
	/**
	 * <p>Description:根据purchaseOrderId和drugId查询<p>
	 * @author ZhouPengyu
	 * @date 2016年6月14日 下午3:43:39
	 */
    PurchaseOrderDrug getPurchaseOrderDrug(Integer purchaseOrderId, Integer drugId);
	
}
