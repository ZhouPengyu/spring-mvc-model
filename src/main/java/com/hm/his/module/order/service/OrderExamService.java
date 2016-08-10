package com.hm.his.module.order.service;

import java.util.List;

import com.hm.his.module.order.model.HospitalOrder;
import org.apache.ibatis.annotations.Param;

import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.statistics.pojo.NameRequst;

public interface OrderExamService {
	/**
	 * 
	 * @description 根据订单号和病历创建订单检查
	 * @date 2016年3月4日
	 * @author lipeng
	 * @param orderNo
	 * @param patientExams
	 */
	OrderItemList<OrderExam> createOrderExams(String orderNo, List<PatientExam> patientExams);

	/**
	 * 
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderItemListId
	 * @return
	 */
	List<OrderExam> getOrderExamsByOrderItemListIdAndChargeStatus(Long orderItemListId, Integer chargeStatus);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param id
	 * @return
	 */
	OrderExam getOrderExamByRecordIdAndPatientExamId(Long recordId, Long patientExamId);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param id
	 */
	void deleteOrderExam(Long id);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param recordId
	 * @param patientExamId
	 */
	void deleteOrderExamByRecordIdAndPatientExamId(Long recordId, Long patientExamId);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param patientExam
	 */
	OrderExam saveOrderExam(Long recordId, PatientExam patientExam);

	/**
	 * 
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param recordId
	 * @param patientExams
	 */
	OrderItemList<OrderExam> saveOrderExams(Long recordId, List<PatientExam> patientExams);

	/**
	 *
	 * @description
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param recordId
	 * @param patientExams
	 */
	OrderItemList<OrderExam> firstSaveOrderExams(Long recordId, List<PatientExam> patientExams, HospitalOrder hospitalOrder);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param orderExamListId
	 */
	void deleteOrderExamsByOrderItemListId(Long orderExamListId);

	OrderItemList selectOrderExamListByRecordId(@Param("recordId") Long recordId);

	/**
	 * 
	 * @description 模糊搜索检查名
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<String> searchOrderExamNames(NameRequst req);

}
