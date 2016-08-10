package com.hm.his.module.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.pojo.GetOrderListParam;
import com.hm.his.module.order.pojo.OrderTotalAmtPojo;
import com.hm.his.module.statistics.pojo.OrderStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;

public interface HospitalOrderMapper {
	void delete(String orderNo);

	void insert(HospitalOrder hospitalOrder);

	HospitalOrder selectByOrderNo(String orderNo);

	void update(HospitalOrder hospitalOrderParam);

	/**
	 * 
	 * @description 根据病历id修改订单基本信息
	 * @date 2016年3月23日
	 * @author lipeng
	 * @param hospitalOrderParam
	 */
	void updateByRecordId(HospitalOrder hospitalOrderParam);

	/**
	 * 
	 * @description
	 * @date 2016年3月7日
	 * @author lipeng
	 * @param param
	 * @return
	 */
	List<HospitalOrder> selectHospitalOrders(GetOrderListParam param);

	/**
	 * 
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param param
	 * @return
	 */
	int selectHospitalOrderCount(GetOrderListParam param);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param recordId
	 * @return
	 */
	HospitalOrder selectByRecordId(@Param("recordId") Long recordId);

	/**
	 * 
	 * @description 更新病历版本，在病历发生变化时调用
	 * @date 2016年3月23日
	 * @author lipeng
	 */
	void updateRecordVersion(HospitalOrder hospitalOrderParam);

	/***********************************统计相关************************************************/
	/**
	 * 
	 * @description 根据条件查询订单
	 * @date 2016年4月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<HospitalOrder> selectOrderStatisticsItemPojos(StatisticsRequest req);

	/**
	 * 
	 * @description 根据条件查询订单 优化
	 * @date 2016年4月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<OrderStatisticsItemPojo> selectOrderStatisticsItemPojosByStatisticsRequest(StatisticsRequest req);

	/**
	 * 
	 * @description 根据条件查询订单数
	 * @date 2016年4月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	Integer selectCountOfOrderStatisticsItemPojos(StatisticsRequest req);

	/**
	 * 
	 * @description 查询指定医生的订单数
	 * @date 2016年4月12日
	 * @author lipeng
	 * @param doctorId
	 * @return
	 */
	Integer selectCountByCreater(@Param("creater") Long creater);

	/**
	 * @description 查询所用医生的就诊人次
	 * @author SuShaohua
	 * @date 2016-07-08 11:02
	 * @param
	*/
	Integer selectCountOfWorkloadItemPojos(StatisticsRequest req);
	/**
	 * 
	 * @date 2016年7月16日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	OrderTotalAmtPojo selectOrderTotalAmtPojo(StatisticsRequest req);
	/**
	 * 
	 * @date 2016年7月16日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	Double selectReceivableAmtSumFromOrderItemChargeByStatisticsRequest(StatisticsRequest req);
}