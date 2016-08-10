package com.hm.his.module.statistics.service;

import com.hm.his.module.statistics.pojo.DrugSaleStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.pojo.WorkloadStatisticsItemPojo;

import java.util.List;

/**
 * 
 * 
 * @author lipeng
 * @date 2016年3月29日
 */
public interface DrugSaleStatisticsService {
	/**
	 * 
	 * 
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	DrugSaleStatisticsPojo drugSaleStatistics(StatisticsRequest req);

	/**
	 * @description 医生工作统计药物统计
	 * @author SuShaohua
	 * @date 2016-07-06 10:02
	 * @param
	*/
	List<WorkloadStatisticsItemPojo> loadDrugStatistics(StatisticsRequest req);
}
