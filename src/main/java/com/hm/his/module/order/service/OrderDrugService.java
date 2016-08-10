package com.hm.his.module.order.service;

import java.util.List;

import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.statistics.pojo.NameRequst;

/**
 * 
 * @description 订单处方药物服务
 * @author lipeng
 * @date 2016年3月4日
 */
public interface OrderDrugService {

	/**
	 * 
	 * @description 根据病历创建订单处方以及药物
	 * @date 2016年3月4日
	 * @author lipeng
	 * @param orderNo
	 * @param patientRecord
	 */
	List<OrderItemList<OrderDrug>> createOrderDrugsByPatentPrescriptionList(Long recordId, List<List<PatientDrug>> patentPrescriptionList);

	/**
	 * 
	 * @description 根据病历创建订单处方以及药物
	 * @date 2016年3月4日
	 * @author lipeng
	 * @param orderNo
	 * @param patientRecord
	 */
	List<OrderItemList<OrderDrug>> createOrderDrugsByChinesePrescriptionList(String orderNo, List<PatientChineseDrug> chinesePrescriptionList);

	List<OrderItemList<OrderDrug>> firstCreateOrderDrugsByChinesePrescriptionList(String orderNo, List<PatientChineseDrug> chinesePrescriptionList);



	/**
	 * 
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderItemListId
	 * @return
	 */
	List<OrderDrug> getOrderDrugsByOrderItemListIdAndChargeStatus(Long orderItemListId, Integer chargeStatus);

	List<OrderItemList<OrderDrug>> createOrderDrugsByPatentPrescriptionList(String orderNo, List<List<PatientDrug>> patentPrescriptionList);

	List<OrderItemList<OrderDrug>> firstCreateOrderDrugsByPatentPrescriptionList(String orderNo, List<PatientDrug> patentPrescriptionList);

	void deleteOrderDrugsByOrderItemListId(Long orderItemListId);

	List<OrderItemList<OrderDrug>> createOrderDrugsByChinesePrescriptionList(Long recordId, List<PatientChineseDrug> chinesePrescriptionList);

	/**
	 * 
	 * @description 模糊搜索药物名称
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<String> searchOrderDrugNames(NameRequst req);

	/**
	 * 
	 * @description 指定药物是否有订单
	 * @date 2016年4月12日
	 * @author lipeng
	 * @param drugId
	 * @param dataSource
	 * @return
	 */
	boolean hasOrderByDrugIdAndDataSource(Long drugId, Integer dataSource);

	/**
	 * 减库存
	 * @date 2016年6月16日
	 * @author lipeng
	 * @param orderDrugs
	 */
	void cutInventory(List<OrderDrug> orderDrugs);

	/**
	 * 返回库存
	 * @date 2016年6月16日
	 * @author lipeng
	 * @param orderDrugs
	 */
	void returnInventory(List<OrderDrug> orderDrugs);
}
