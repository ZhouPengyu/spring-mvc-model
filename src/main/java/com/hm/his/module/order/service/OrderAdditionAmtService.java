package com.hm.his.module.order.service;

import java.util.List;

import com.hm.his.module.order.model.HospitalOrder;
import org.apache.ibatis.annotations.Param;

import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

/**
 * 
 * @description 订单附加费用服务
 * @author lipeng
 * @date 2016年3月4日
 */
public interface OrderAdditionAmtService {
	/**
	 * 
	 * @description 根据病历创建订单附加费用
	 * @date 2016年3月4日
	 * @author lipeng
	 * @param orderNo
	 * @param additionalList
	 */
	OrderItemList<OrderAdditionAmt> createOrderAdditionAmts(String orderNo, List<PatientAdditional> additionalList);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param orderNo
	 * @param additionalList
	 * @return
	 */
	OrderItemList<OrderAdditionAmt> saveOrderAdditionAmts(Long recordId, List<PatientAdditional> additionalList);


	/**
	 *
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param orderNo
	 * @param additionalList
	 * @return
	 */
	OrderItemList<OrderAdditionAmt> firstSaveOrderAdditionAmts(Long recordId, List<PatientAdditional> additionalList, HospitalOrder hospitalOrder);

	/**
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	List<OrderAdditionAmt> getOrderAdditionAmtsByOrderItemListIdAndChargeStatus(Long orderItemListId, Integer chargeStatus);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param id
	 * @return
	 */
	OrderAdditionAmt getOrderAdditionAmtByRecordIdAndAdditionalId(Long recordId, Long additionalId);

	void deleteOrderAdditionAmt(Long id);

	OrderAdditionAmt saveOrderAdditionAmt(Long recordId,PatientAdditional patientAdditional);

	void updateOrderAdditionAmtByListIdAndChargeStatus(Long orderAdditionAmtListId, Integer chargeStatus);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param orderItemListId
	 */
	void deleteOrderAdditionAmtsByOrderItemListId(Long orderItemListId);
	
	OrderItemList selectOrderAdditionAmtListByRecordId(@Param("recordId") Long recordId);
}
