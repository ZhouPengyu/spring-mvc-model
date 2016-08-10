package com.hm.his.module.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.statistics.pojo.ExamStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.ExamStatisticsPojo;
import com.hm.his.module.statistics.pojo.NameRequst;
import com.hm.his.module.statistics.pojo.StatisticsRequest;

public interface OrderExamMapper {

	void insert(OrderExam orderExam);

	void delete(Long id);

	List<OrderExam> selectAll();

	void insertList(List<OrderExam> orderExams);

	//OrderExam selectById(Long id);

	void update(OrderExam orderExam);

	/**
	 * 
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderItemListId
	 * @return
	 */
	List<OrderExam> selectByOrderItemListIdAndChargeStatus(@Param("orderItemListId") Long orderItemListId, @Param("chargeStatus") Integer chargeStatus);

	void deleteByOrderItemListId(@Param("orderItemListId") Long orderExamListId);

	OrderExam selectByRecordIdAndPatientExamId(@Param("recordId") Long recordId, @Param("patientExamId") Long patientExamId);

	/*************************以下区间为统计相关****************************************/
	/**
	 * 
	 * @description 根据检查项名称统计检查项的数量和销售金额，并按条件过滤的数据
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<ExamStatisticsItemPojo> selectExamStatisticsItemPojos(StatisticsRequest req);

	/**
	 * 
	 * @description 根据检查项名称统计检查项的数量和销售金额，并按条件过滤后的记录条数
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	Integer selectCountOfExamStatisticsItemPojos(StatisticsRequest req);

	/**
	 * 
	 * @description 根据检查项名称统计检查项的数量和销售金额，并按条件过滤后的总销售金额和总数
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	ExamStatisticsPojo selectTotalSaleAmtAndCount(StatisticsRequest req);

	/**
	 * 
	 * @description 搜索订单检查项名称
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<String> searchOrderExamNames(NameRequst req);

	/**
	 * @description
	 * @author SuShaohua
	 * @date 2016-07-06 16:48
	 * @param
	*/
	List<OrderExam> selectLoadExamStatistics(StatisticsRequest req);

	/*************************************************************************************/
}