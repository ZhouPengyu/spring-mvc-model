package com.hm.his.module.statistics.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.model.HisResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @author SuShaohua
 * @date 2016/4/27
 * @description  微信经营统计
 */
@RestController
@RequestMapping("/weixin/statisticsTest")
public class WXStatisticsControllerTest {
    @RequestMapping(value = "/managementStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String managementStatistics(@RequestBody Map<String, String> requestParams){
        Map<String, Object> body = Maps.newHashMap();
        List<Map<String, Object>> hospitalList = Lists.newArrayList();
        for (int i = 1; i <= 2;  i++){
            Map<String, Object> map = Maps.newHashMap();
            map.put("hospitalId",i);
            map.put("hospitalName", "云诊所");
            map.put("turnover", 134.67);
            map.put("visitor", 23);
            map.put("access", "1");
            hospitalList.add(map);
        }
        body.put("result", hospitalList);
        System.out.println(HisResponse.getInstance(body).toString());
        return HisResponse.getInstance(body).toString();
    }

    @RequestMapping(value = "/statisticsDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String statisticsDetail(@RequestBody Map<String, String> requestParams){
        Map<String, Object> body = Maps.newHashMap();
        body.put("turnover", 14534.45);
        body.put("grossImpression", 123);
        body.put("totalProfitAmt", 1234.34);
        body.put("drugSaleAmt", 145);
        body.put("drugSaleProfit", 124.23);
        body.put("examSaleAmt", 2132.87);
        body.put("additionSaleAmt", 2434.67);
        List<Map<String, Object>> doctorStatisticList = Lists.newArrayList();
        for (int i = 1; i <= 3; i++){
            Map<String, Object> map = Maps.newHashMap();
            map.put("doctorName", "David");
            map.put("doctorImpression", 123 + i);
            map.put("doctorAmt", "234.6");
            doctorStatisticList.add(map);
        }
        body.put("doctorStatisticList", doctorStatisticList);
        return HisResponse.getInstance(body).toString();
    }
}
