package com.hm.his.module.statistics.controller;

import com.hm.his.framework.exception.ExcelException;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.ExcelUtil;
import com.hm.his.module.statistics.pojo.*;
import com.hm.his.module.statistics.service.AdditionAmtStatisticsService;
import com.hm.his.module.statistics.service.DrugSaleStatisticsService;
import com.hm.his.module.statistics.service.ExamStatisticsService;
import com.hm.his.module.statistics.service.OrderStatisticsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * @author lipeng
 * @description
 * @date 2016年3月1日
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    AdditionAmtStatisticsService additionAmtStatisticsService;
    @Autowired
    DrugSaleStatisticsService drugSaleStatisticsService;
    @Autowired
    ExamStatisticsService examStatisticsService;
    @Autowired
    OrderStatisticsService orderStatisticsService;

    /**
     * @param req
     * @return
     * @description 药品销售统计
     * @date 2016年3月24日
     * @author lipeng
     */
    @RequestMapping(value = "/drugSaleStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String drugSaleStatistics(@RequestBody StatisticsRequest req) {
        DrugSaleStatisticsPojo body = drugSaleStatisticsService.drugSaleStatistics(req);
        return HisResponse.getInstance(body).toString();
    }

    @RequestMapping(value = "/exportDrugSaleStatistics", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String exportDrugSaleStatistics(StatisticsRequest statisticsRequest, HttpServletRequest request, HttpServletResponse response) {
        // 统计不分页
        statisticsRequest.setPageSize(null);
        DrugSaleStatisticsPojo body = drugSaleStatisticsService.drugSaleStatistics(statisticsRequest);
        if (body == null || CollectionUtils.isEmpty(body.getDrugs())) {
            // 没有查询到数据，不会进行下载
            HisResponse res = new HisResponse();
            res.setErrorCode(StatisticsErrorMessage.Null.getCode());
            res.setErrorMessage(StatisticsErrorMessage.Null.getMsg());
            return res.toString();
        }
        LinkedHashMap<String, String> headMap = this.getOrderDrugsExcelHeadMap();
        try {
            ExcelUtil.listToExcel(body.getDrugs(), headMap, "药品销售详情", response);
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return HisResponse.getInstance().toString();
    }

    @RequestMapping(value = "/examStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String examStatistics(@RequestBody StatisticsRequest req) {
        ExamStatisticsPojo body = examStatisticsService.examStatistics(req);
        return HisResponse.getInstance(body).toString();
    }

    @RequestMapping(value = "/exportExamStatistics", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String exportExamStatistics(StatisticsRequest statisticsRequest, HttpServletRequest request, HttpServletResponse response) {
        // 统计不分页
        statisticsRequest.setPageSize(null);
        ExamStatisticsPojo body = examStatisticsService.examStatistics(statisticsRequest);
        if (body == null || CollectionUtils.isEmpty(body.getExams())) {
            // 没有查询到数据，不会进行下载
            HisResponse res = new HisResponse();
            res.setErrorCode(StatisticsErrorMessage.Null.getCode());
            res.setErrorMessage(StatisticsErrorMessage.Null.getMsg());
            return res.toString();
        }
        LinkedHashMap<String, String> headMap = this.getOrderExamsExcelHeadMap();
        try {
            ExcelUtil.listToExcel(body.getExams(), headMap, "检查项目详情", response);
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return HisResponse.getInstance().toString();
    }

    @RequestMapping(value = "/additionAmtStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String additionAmtStatistics(@RequestBody StatisticsRequest req) {
        AdditionAmtStatisticsPojo body = additionAmtStatisticsService.additionAmtStatistics(req);
        return HisResponse.getInstance(body).toString();
    }

    @RequestMapping(value = "/exportAdditionAmtStatistics", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String exportAdditionAmtStatistics(StatisticsRequest statisticsRequest, HttpServletRequest request, HttpServletResponse response) {
        // 统计不分页
        statisticsRequest.setPageSize(null);
        AdditionAmtStatisticsPojo body = additionAmtStatisticsService.additionAmtStatistics(statisticsRequest);
        if (body == null || CollectionUtils.isEmpty(body.getAdditionAmts())) {
            // 没有查询到数据，不会进行下载
            HisResponse res = new HisResponse();
            res.setErrorCode(StatisticsErrorMessage.Null.getCode());
            res.setErrorMessage(StatisticsErrorMessage.Null.getMsg());
            return res.toString();
        }
        LinkedHashMap<String, String> headMap = this.getOrderAdditionAmtsExcelHeadMap();
        try {
            ExcelUtil.listToExcel(body.getAdditionAmts(), headMap, "附加费用详情", response);
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return HisResponse.getInstance().toString();
    }

    /**
     * <p>门诊日志统计</p>
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/orderStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String orderStatistics(@RequestBody StatisticsRequest req) {
        OrderStatisticsPojo body = orderStatisticsService.orderStatistics(req);
        return HisResponse.getInstance(body).toString();
    }

    @RequestMapping(value = "/exportOrderStatistics", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String exportOrderStatistics(StatisticsRequest statisticsRequest, HttpServletRequest request, HttpServletResponse response) {
        // 统计不分页
        statisticsRequest.setPageSize(null);
        OrderStatisticsPojo body = orderStatisticsService.orderStatistics(statisticsRequest);
        if (body == null || CollectionUtils.isEmpty(body.getOrders())) {
            // 没有查询到数据，不会进行下载
            HisResponse res = new HisResponse();
            res.setErrorCode(StatisticsErrorMessage.Null.getCode());
            res.setErrorMessage(StatisticsErrorMessage.Null.getMsg());
            return res.toString();
        }
        LinkedHashMap<String, String> headMap = this.getOrdersExcelHeadMap();
        try {
            ExcelUtil.listToExcel(body.getOrders(), headMap, "门诊日志详情", response);
        } catch (ExcelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return HisResponse.getInstance().toString();
    }

    private LinkedHashMap<String, String> getOrdersExcelHeadMap() {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("patientName", "患者");
        fieldMap.put("gender", "性别");
        fieldMap.put("ageDesc", "年龄");
        fieldMap.put("phoneNo", "	电话");
        fieldMap.put("diagnosis", "诊断");
        fieldMap.put("receivableAmt", "应收金额");
        fieldMap.put("actualAmt", "实收金额");
        fieldMap.put("doctorName", "医生");
        fieldMap.put("chargeDateDesc", "日期");
        return fieldMap;
    }

    private LinkedHashMap<String, String> getOrderDrugsExcelHeadMap() {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("drugTypeName", "药品类型");
        fieldMap.put("saleChannelName", "销售渠道");
        fieldMap.put("drugName", "药品名称");
        fieldMap.put("specification", "规格");
        fieldMap.put("manufacturer", "厂家");
        fieldMap.put("countDesc", "数量");
        fieldMap.put("purchaseAmt", "进货价");
        fieldMap.put("saleAmt", "销售价");
        fieldMap.put("profit", "利润");
        return fieldMap;
    }

    private LinkedHashMap<String, String> getOrderExamsExcelHeadMap() {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("examName", "检查项目");
        fieldMap.put("count", "检查项目数量");
        fieldMap.put("costAmt", "成本价");
        fieldMap.put("profitAmt", "利润");
        fieldMap.put("saleAmt", "销售价");
        return fieldMap;
    }

    private LinkedHashMap<String, String> getOrderAdditionAmtsExcelHeadMap() {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("additionAmtName", "附加费用名称");
        fieldMap.put("count", "数量");
        fieldMap.put("saleAmt", "金额");
        return fieldMap;
    }

    /**
     * @param
     * @description 医生工作统计
     * @author SuShaohua
     * @date 2016-07-05 21:15
     */
    @RequestMapping(value = "/workloadStatistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String workloadStatistics(@RequestBody StatisticsRequest req) {
        WorkloadStatisticsPojo body = orderStatisticsService.workloadStatistics(req);
        return HisResponse.getInstance(body).toString();
    }

    @RequestMapping(value = "/exportWorkloadStatistics", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String exportWorkloadStatistics(StatisticsRequest req, HttpServletRequest request, HttpServletResponse response) {
        req.setPageSize(null);
        WorkloadStatisticsPojo body = orderStatisticsService.workloadStatistics(req);
        if (body == null || CollectionUtils.isEmpty(body.getLoadList())) {
            HisResponse res = new HisResponse();
            res.setErrorCode(StatisticsErrorMessage.Null.getCode());
            res.setErrorMessage(StatisticsErrorMessage.Null.getMsg());
            return res.toString();
        }
        LinkedHashMap<String, String> headMap = this.getWorkLoadsExcelHeadMap();
        try {
            ExcelUtil.listToExcel(body.getLoadList(), headMap, "医生工作统计", response);
        } catch (ExcelException e) {
            e.printStackTrace();
        }
        return HisResponse.getInstance().toString();
    }

    public LinkedHashMap<String, String> getWorkLoadsExcelHeadMap() {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("doctorName", "医生");
        fieldMap.put("itemTypeName", "类型");
        fieldMap.put("itemName", "名称");
        fieldMap.put("specification", "规格");
        fieldMap.put("manufacturer", "厂家");
        fieldMap.put("countDesc", "数量");
        fieldMap.put("receivableAmt", "应收金额");
        return fieldMap;
    }
}
