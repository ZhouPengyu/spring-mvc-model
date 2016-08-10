package com.hm.his.module.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.statistics.pojo.NameRequst;
import com.hm.his.module.statistics.pojo.StatisticsRequest;

public interface OrderDrugMapper {

	void insert(OrderDrug orderDrug);

	void delete(Long id);

	List<OrderDrug> selectAll();

	void insertList(List<OrderDrug> orderDrugs);

	OrderDrug selectById(Long id);

	void update(OrderDrug orderDrug);

	/**
	 * 
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderItemListId
	 * @return
	 */
	List<OrderDrug> selectByOrderItemListIdAndChargeStatus(@Param("orderItemListId") Long orderItemListId, @Param("chargeStatus") Integer chargeStatus);

	void deleteByOrderItemListId(@Param("orderItemListId") Long orderItemListId);

	List<OrderDrug> selectByIds(@Param("ids") List<Long> ids);

	/**
	 * 
	 * @description 模糊搜索订单药物名称
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<String> searchOrderDrugNames(NameRequst req);

	/**
	 * 
	 * @description
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param orderNo
	 * @return
	 */
	List<OrderDrug> selectListByOrderNo(@Param("orderNo") String orderNo);

	/**
	 * 
	 * @description
	 * @date 2016年4月5日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<OrderDrug> selectByStatisticsRequest(StatisticsRequest req);

	/**
	 * 
	 * @description 根据条件查询list 不分页
	 * @date 2016年4月8日
	 * @author lipeng
	 * @param queryParam
	 * @return
	 */
	List<OrderDrug> selectList(OrderDrug queryParam);
	/**
	 * 
	 * @description  查询指定药物的订单数
	 * @date 2016年4月12日
	 * @author lipeng
	 * @param drugId
	 * @param dataSource
	 * @return
	 */
	Integer selectCountByDrugIdAndDataSource(@Param("drugId") Long drugId, @Param("dataSource") Integer dataSource);
	/**
	 * @description 查询一定时间内医生所开出的药品
	 * @author SuShaohua
	 * @date 2016-07-06 17:57
	 * @param
	*/
	List<OrderDrug> selectLoadsDrugStatistics(StatisticsRequest req);
}