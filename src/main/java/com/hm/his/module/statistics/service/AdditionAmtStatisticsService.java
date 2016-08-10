package com.hm.his.module.statistics.service;

import com.hm.his.module.statistics.pojo.AdditionAmtStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.pojo.WorkloadStatisticsItemPojo;

import java.util.List;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月29日
 */
public interface AdditionAmtStatisticsService {
	/**
	 * 
	 * @description
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	AdditionAmtStatisticsPojo additionAmtStatistics(StatisticsRequest req);
	/**
	 * @description 医生工作量附加费用统计
	 * @author SuShaohua
	 * @date 2016-07-06 10:02
	 * @param
	*/
	List<WorkloadStatisticsItemPojo> loadAdditionalStatistics(StatisticsRequest req);
}
