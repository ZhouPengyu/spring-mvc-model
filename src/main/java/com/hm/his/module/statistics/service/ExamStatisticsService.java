package com.hm.his.module.statistics.service;

import com.hm.his.module.statistics.pojo.ExamStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.pojo.WorkloadStatisticsItemPojo;

import java.util.List;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月29日
 */
public interface ExamStatisticsService {
	/**
	 * 
	 * @description
	 * @date 2016年3月29日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	ExamStatisticsPojo examStatistics(StatisticsRequest req);
	/**
	 * @description 医生工作量检查详情统计
	 * @author SuShaohua
	 * @date 2016-07-06 10:01
	 * @param
	*/
	List<WorkloadStatisticsItemPojo> loadExamStatistics(StatisticsRequest req);
}
