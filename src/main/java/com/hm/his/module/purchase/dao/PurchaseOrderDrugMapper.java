package com.hm.his.module.purchase.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.purchase.model.PurchaseOrderDrug;

public interface PurchaseOrderDrugMapper {
	/**
	 * <p>Description:根据ID查询采购单药品详细<p>
	 * @author ZhouPengyu
	 * @date 2016-5-31 11:42:40
	 */
	List<PurchaseOrderDrug> getPurchaseOrderDrugList(Integer purchaseOrderId);
	
	/**
	 * <p>Description:根据purchaseOrderId和drugId查询<p>
	 * @author ZhouPengyu
	 * @date 2016年6月14日 下午3:43:39
	 */
	PurchaseOrderDrug getPurchaseOrderDrug(@Param("purchaseOrderId")Integer purchaseOrderId, @Param("drugId")Integer drugId);
	
	void insertPurchaseOrderDrug(@Param("purchaseOrderId")Integer purchaseOrderId, @Param("list")List<PurchaseOrderDrug> drugList);
	
	Integer deleteDrugByOrderId(Integer purchaseOrderId);
}