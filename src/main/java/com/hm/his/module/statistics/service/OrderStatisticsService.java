package com.hm.his.module.statistics.service;

import com.hm.his.module.statistics.pojo.OrderStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.pojo.WorkloadStatisticsPojo;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月29日
 */
public interface OrderStatisticsService {
	/**
	 * 
	 * @description
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	OrderStatisticsPojo orderStatistics(StatisticsRequest req);

	/**
	 * 医生就诊人次统计
	 * @param req
	 * @return
     */
	Integer selectCountOfWorkloadItemPojos(StatisticsRequest req);
	/**
	 * @description 医生工作统计
	 * @author SuShaohua
	 * @date 2016-07-08 11:50
	 * @param
	*/
	WorkloadStatisticsPojo workloadStatistics(StatisticsRequest req);
}
