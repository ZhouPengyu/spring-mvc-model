package com.hm.his.module.purchase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.purchase.model.PurchaseOrder;
import com.hm.his.module.purchase.model.request.PurchaseOrderRequest;

public interface PurchaseOrderMapper {
	
	List<PurchaseOrder> queryPurchaseOrder(PurchaseOrderRequest request);
	
	Integer queryPurchaseOrderCount(PurchaseOrderRequest request);
	
	/**
	 * <p>Description:查询报价商家总数<p>
	 * @author ZhouPengyu
	 * @date 2016年5月30日 下午4:36:07
	 */
	Integer getQuoteCount(Integer purchaseOrderId);
	
	/**
	 * <p>Description:查询报价单<p>
	 * @author ZhouPengyu
	 * @date 2016年5月30日 下午4:36:07
	 */
	Integer getQuoteOrderId(Integer purchaseOrderId);
	
	/**
	 * <p>Description:取消订单<p>
	 * @author ZhouPengyu
	 * @date 2016年6月1日 下午4:38:58
	 */
	Integer cancelPurchaseOrder(Integer purchaseOrderId);

    Integer insertPurchaseOrder(PurchaseOrder purchaseOrder);
    
    Integer updatePurchaseOrder(PurchaseOrder purchaseOrder);

    PurchaseOrder getPurchaseOrder(Integer purchaseOrderId);
    
    /**
     * <p>Description:确定供货商<p>
     * @author ZhouPengyu
     * @date 2016年6月1日 下午5:17:19
     */
    Integer confirmSupplier(@Param("purchaseOrderId")Integer purchaseOrderId, @Param("quoteOrderId")Integer quoteOrderId);
    
    /**
     * <p>Description:供货单缺货总数<p>
     * @author ZhouPengyu
     * @date 2016年6月3日 上午11:26:26
     */
    Integer getOutOfStockSpecies(Integer quoteOrderId);
    
    /**
     * <p>Description:更新采购单状态<p>
     * @author ZhouPengyu
     * @date 2016年6月3日 下午4:40:38
     */
    Integer updatePurchaseOrderStatus(@Param("purchaseOrderId")Integer purchaseOrderId, @Param("status")Integer status);
    
	/**
	 * <p>Description:查询采购订单<p>
	 * @author ZhouPengyu
	 * @date 2016年6月6日 上午10:38:59
	 */
	List<PurchaseOrder> queryPurchaseOrderAll(PurchaseOrderRequest request) throws Exception;
	
	/**
	 * <p>Description:取消其他供货单<p>
	 * @author ZhouPengyu
	 * @date 2016年6月15日 下午6:04:14
	 */
	Integer cancelOtherSupplier(Integer purchaseOrderId);
	
	/**
	 * <p>Description:订单被取消,更新供货单状态<p>
	 * @author ZhouPengyu
	 * @date 2016-6-17 17:11:05
	 */
	Integer updateQuoteOrderByCancelOrder(Integer purchaseOrderId);

}