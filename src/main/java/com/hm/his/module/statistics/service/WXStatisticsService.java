package com.hm.his.module.statistics.service;

import com.hm.his.module.statistics.pojo.HospitalStatisticsDetailPojo;
import com.hm.his.module.statistics.pojo.HospitalStatisticsPojo;
import com.hm.his.module.statistics.pojo.WXStatisticsRequest;

import java.util.List;

/**
 * @param
 * @author SuShaohua
 * @date 2016/4/29
 * @description
 */
public interface WXStatisticsService {
    HospitalStatisticsDetailPojo statisticsDetail(WXStatisticsRequest req);
    List<HospitalStatisticsPojo> hospitalStatistics(WXStatisticsRequest req);
}
