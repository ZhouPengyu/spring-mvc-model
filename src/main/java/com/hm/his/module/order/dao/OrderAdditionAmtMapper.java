package com.hm.his.module.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.statistics.pojo.AdditionAmtStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.AdditionAmtStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;

public interface OrderAdditionAmtMapper {
	void delete(Long id);

	void insert(OrderAdditionAmt orderAdditionAmt);

	List<OrderAdditionAmt> selectAll();

	void insertList(List<OrderAdditionAmt> orderAdditionAmts);

	OrderAdditionAmt selectById(Long id);

	void update(OrderAdditionAmt orderAdditionAmt);

	/**
	 * 
	 * @description
	 * @date 2016年3月8日
	 * @author lipeng
	 * @param orderItemListId
	 * @return
	 */
	List<OrderAdditionAmt> selectByOrderItemListIdAndChargeStatus(@Param("orderItemListId") Long orderItemListId,
			@Param("chargeStatus") Integer chargeStatus);

	void updateOrderAdditionAmtByListIdAndChargeStatus(@Param("orderAdditionAmtListId") Long orderAdditionAmtListId,
			@Param("chargeStatus") Integer chargeStatus);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param recordId
	 * @param additionalId
	 * @return
	 */
	OrderAdditionAmt selectByRecordIdAndAdditionalId(@Param("recordId") Long recordId, @Param("additionalId") Long additionalId);

	/**
	 * 
	 * @description
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param id
	 */
	void deleteByOrderItemListId(@Param("orderItemListId") Long orderItemListId);

	/*************************以下区间为统计相关****************************************/
	/**
	 * 
	 * @description 根据附加费用名称统计数量和销售金额，并按条件过滤的数据
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	List<AdditionAmtStatisticsItemPojo> selectAdditionAmtStatisticsItemPojos(StatisticsRequest req);

	/**
	 * 
	 * @description  根据附加费用名称统计数量和销售金额，并按条件过滤后的记录条数
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	Integer selectCountOfAdditionAmtStatisticsItemPojos(StatisticsRequest req);

	/**
	 * 
	 * @description 根据附加费用名称统计数量和销售金额，并按条件过滤后的总销售金额和总数
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	AdditionAmtStatisticsPojo selectTotalSaleAmtAndCount(StatisticsRequest req);

	/**
	 * @description  统计医生开出的附加费用详情
	 * @author SuShaohua
	 * @date 2016-07-06 11:28
	 * @param 
	*/
	List<OrderAdditionAmt> selectLoadsAdditionalStatistics(StatisticsRequest req);
	/*****************************************************************************/

}