package com.hm.his.module.order.service;

import java.util.List;
import java.util.Map;

import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.pojo.GetOrderListParam;
import com.hm.his.module.order.pojo.GetOrdersRequest;
import com.hm.his.module.order.pojo.HospitalOrderDetailPojo;
import com.hm.his.module.order.pojo.HospitalOrderPojoList;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月1日
 */
public interface OrderService {
	/**
	 * 
	 * 查询诊所的待收费订单
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	HospitalOrderPojoList getToChargeOrders(GetOrdersRequest req);

	/**
	 * 
	 * 查询诊所的已收费订单
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	HospitalOrderPojoList getChargedOrders(GetOrdersRequest req);

	/**
	 * 
	 * 查询诊所的已退费订单
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	HospitalOrderPojoList getRefundedOrders(GetOrdersRequest req);

	/**
	 * 
	 * 获取待收费订单详情
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	HospitalOrderDetailPojo getToChargeOrderInfo(GetOrdersRequest req);

	/**
	 * 
	 * 获取已收费订单详情
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	HospitalOrderDetailPojo getChargedOrderInfo(GetOrdersRequest req);

	/**
	 * 
	 * 获取已退费订单详情
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	HospitalOrderDetailPojo getRefundedOrderInfo(HospitalOrder req);

	/**
	 * 
	 * 创建订单编号
	 * @date 2016年3月2日
	 * @author lipeng
	 * @return
	 */
	String createOrderNo();

	/**
	 * 
	 * 根据病历创建订单
	 * @date 2016年3月4日
	 * @author lipeng
	 * @param patientRecord 病历信息
	 * @throws Exception
	 */
	void createOrder(PatientInquiryRequest patientRecord) throws Exception;

	/**
	 * 
	 * @description
	 * @date 2016年3月7日
	 * @author lipeng
	 * @param hospitalOrder
	 */
	void insertHospitalOrder(HospitalOrder hospitalOrder);

	/**
	 * 
	 * @description
	 * @date 2016年3月7日
	 * @author lipeng
	 * @param hospitalOrder
	 */
	void update(HospitalOrder hospitalOrder);

	/**
	 * 
	 * @description
	 * @date 2016年3月7日
	 * @author lipeng
	 * @param param
	 * @return
	 */
	List<HospitalOrder> getHospitalOrders(GetOrderListParam param);

	/**
	 * 
	 * 
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param param
	 * @return
	 */
	int getHospitalOrderCount(GetOrderListParam param);

	/**
	 * 
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	HospitalOrder getHospitalOrder(String orderNo);

	/**
	 * 
	 * @description
	 * @date 2016年3月10日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	Long getRecordVersion(String orderNo);

	/**
	 * 
	 * 更具病历id 创建订单，如果该病历已经有订单则不新增，并返回订单号
	 * @date 2016年3月17日
	 * @author lipeng
	 * @param recordId
	 * @return 订单号
	 */
	HospitalOrder createHospitalOrder(PatientInquiryRequest patientInquiryRequest);

	/**
	 * 
	 *  更新病历版本号
	 * @date 2016年4月1日
	 * @author lipeng
	 * @param orderNo
	 * @param version
	 */
	void updateRecordVersion(String orderNo, Long version);

	/**
	 * 
	 * 获取指定订单的退费项目的金额合计
	 * @date 2016年4月5日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	Double getReceivableRefundAmt(String orderNo);

	/**
	 * 获取指定订单的退费项目的金额合计,如果没有退费项目的，默认为0
	 * @date 2016年7月16日
	 * @author lipeng
	 * @param orderNos
	 * @return
	 */
	Map<String, Double> getReceivableRefundAmts(List<String> orderNos);

	/**
	 * 
	 * 医生是否有订单
	 * @date 2016年4月12日
	 * @author lipeng
	 * @param doctorId
	 * @return
	 */
	boolean hasOrderByDoctorId(Long doctorId);
}
