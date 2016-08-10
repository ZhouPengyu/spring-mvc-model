package com.hm.his.module.statistics.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.statistics.pojo.HospitalStatisticsDetailPojo;
import com.hm.his.module.statistics.pojo.HospitalStatisticsPojo;
import com.hm.his.module.statistics.pojo.WXStatisticsRequest;
import com.hm.his.module.statistics.service.*;
import com.hm.his.module.user.service.BoundInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param
 * @author SuShaohua
 * @date 2016/4/27
 * @description
 */
@RestController
@RequestMapping("/weixin/statistics")
public class WXStatisticsController {
    @Autowired
    AdditionAmtStatisticsService additionAmtStatisticsService;
    @Autowired
    DrugSaleStatisticsService drugSaleStatisticsService;
    @Autowired
    ExamStatisticsService examStatisticsService;
    @Autowired
    OrderStatisticsService orderStatisticsService;
    @Autowired
    WXStatisticsService wxStatisticsService;
    @Autowired
    BoundInfoService boundInfoService;


    @RequestMapping(value = "/managementStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String managementStatistics(@RequestBody WXStatisticsRequest req) {
        List<HospitalStatisticsPojo> body = wxStatisticsService.hospitalStatistics(req);
        return HisResponse.getInstance(body).toString();
    }

    @RequestMapping(value = "/statisticsDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String statisticsDetail(@RequestBody WXStatisticsRequest req) {
        HospitalStatisticsDetailPojo body = new HospitalStatisticsDetailPojo();
        req.setPageSize(Integer.MAX_VALUE);  //TODO 分页大小控制

        String openId = SessionUtils.getOpenId();
        //String openId = "oArTyvyCfrTszBtZgiJBd4Fy6IOs";
        Map<String, String> param = Maps.newHashMap();
        param.put("openId", openId);
        param.put("function", "statistics");
        List<Map> hospitalList = boundInfoService.boundHospitalFunction(param);
        if (null == hospitalList) hospitalList = new ArrayList<Map>();
        Set<Long> hospitalIdList = Sets.newHashSet();
        for (Map m : hospitalList) {
            if (1 == LangUtils.getInteger(m.get("status"))) {
                hospitalIdList.add(LangUtils.getLong(m.get("hospitalId")));
            }
        }

        if (req.getHospitalId() == null || req.getHospitalId() == 0l) {
            HospitalStatisticsDetailPojo soloHospital = new HospitalStatisticsDetailPojo();
            Double turnover = 0D;
            Integer grossImpression = 0;
            Double totalProfitAmt = 0D;
            Double drugSaleAmt = 0D;
            Double drugSaleProfit = 0D;
            Double examSaleAmt = 0D;
            Double additionSaleAmt = 0D;
            Double examSaleProfit = 0D;
            List<Map<String, Object>> doctorStatisticList = Lists.newArrayList();

            for (Long id : hospitalIdList) {
                req.setHospitalId(id);
                soloHospital = wxStatisticsService.statisticsDetail(req);
                turnover += soloHospital.getTurnover();
                grossImpression += soloHospital.getGrossImpression();
                totalProfitAmt += soloHospital.getTotalProfitAmt();
                drugSaleAmt += soloHospital.getDrugSaleAmt();
                drugSaleProfit += soloHospital.getDrugSaleProfit();
                examSaleAmt += soloHospital.getExamSaleAmt();
                examSaleProfit += soloHospital.getExamSaleProfit();
                additionSaleAmt += soloHospital.getAdditionSaleAmt();
                doctorStatisticList.addAll(soloHospital.getDoctorStatisticList());
            }

            body.setTurnover(turnover);
            body.setGrossImpression(grossImpression);
            body.setTotalProfitAmt(totalProfitAmt);
            body.setDrugSaleProfit(drugSaleProfit);
            body.setDrugSaleAmt(drugSaleAmt);
            body.setExamSaleAmt(examSaleAmt);
            body.setAdditionSaleAmt(additionSaleAmt);
            body.setExamSaleProfit(examSaleProfit);
            body.setDoctorStatisticList(doctorStatisticList);

        } else if (req.getHospitalId() != null && req.getHospitalId() != 0l && hospitalIdList.contains(req.getHospitalId()))
            body = wxStatisticsService.statisticsDetail(req);
        return HisResponse.getInstance(body).toString();
    }
}
