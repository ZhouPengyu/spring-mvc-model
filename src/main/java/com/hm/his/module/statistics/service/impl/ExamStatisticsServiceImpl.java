package com.hm.his.module.statistics.service.impl;

import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.module.order.dao.OrderExamMapper;
import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.statistics.pojo.ExamStatisticsItemPojo;
import com.hm.his.module.statistics.pojo.ExamStatisticsPojo;
import com.hm.his.module.statistics.pojo.StatisticsRequest;
import com.hm.his.module.statistics.pojo.WorkloadStatisticsItemPojo;
import com.hm.his.module.statistics.service.ExamStatisticsService;
import com.hm.his.module.user.service.DoctorService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamStatisticsServiceImpl implements ExamStatisticsService {

    @Autowired(required = false)
    OrderExamMapper orderExamMapper;
    @Autowired
    DoctorService doctorService;

    @Override
    public ExamStatisticsPojo examStatistics(StatisticsRequest req) {
        StatisticsRequest.handleStatisticsRequest(req);
        ExamStatisticsPojo re = new ExamStatisticsPojo();
        // 只统计已收费且未退费的检查
        List<ExamStatisticsItemPojo> exams = orderExamMapper.selectExamStatisticsItemPojos(req);
        Double totalProfit = exams.stream().filter(exam -> exam.getCostAmt() != null && exam.getSaleAmt() != null).mapToDouble(exam -> {
            Double profit = AmtUtils.subtract(exam.getSaleAmt(), exam.getCostAmt());
            exam.setProfitAmt(profit);
            return profit;
        }).sum();
        re.setExams(exams);
        re.setTotalProfit(totalProfit);
        Integer totalCount = orderExamMapper.selectCountOfExamStatisticsItemPojos(req);
        req.setTotalCount(totalCount);
        re.setTotalPage(req.getTotalPage());
        ExamStatisticsPojo totalSaleAmtAndCount = orderExamMapper.selectTotalSaleAmtAndCount(req);
        if (totalSaleAmtAndCount != null) {
            re.setExamCount(totalSaleAmtAndCount.getExamCount());
            re.setTotalSaleAmt(totalSaleAmtAndCount.getTotalSaleAmt());
        }
        return re;
    }

    @Override
    public List<WorkloadStatisticsItemPojo> loadExamStatistics(StatisticsRequest req) {
        List<WorkloadStatisticsItemPojo> loadsExam = new ArrayList<>();
        List<OrderExam> orderExams = orderExamMapper.selectLoadExamStatistics(req);
        if (CollectionUtils.isEmpty(orderExams)) {
            return null;
        }
        List<String> orderNoList = orderExams.stream().map(orderExam -> {
            return orderExam.getOrderNo();
        }).collect(Collectors.toList());
        Map<String, List<OrderExam>> orderExamMap = orderExams.stream().collect(Collectors.groupingBy(orderExam -> {
            return orderExam.getExamName() + orderExam.getCreater();
        }));
        Collection<List<OrderExam>> orderExamLists = orderExamMap.values();
        for (List<OrderExam> orderExamList : orderExamLists) {
            WorkloadStatisticsItemPojo itemPojo = createExamSaleStatisticsItemPojo(orderExamList);
            if (null != itemPojo) {
                itemPojo.setOrderNoList(orderNoList);
                loadsExam.add(itemPojo);
            }
        }
        return loadsExam;
    }

    private WorkloadStatisticsItemPojo createExamSaleStatisticsItemPojo(List<OrderExam> orderExamList) {
        WorkloadStatisticsItemPojo itemPojo = null;
        OrderExam orderExam = orderExamList.get(0);
        if (null != orderExam) {
            itemPojo = new WorkloadStatisticsItemPojo();
            itemPojo.setItemTypeName("检查治疗项");
            itemPojo.setItemName(orderExam.getExamName());
            itemPojo.setSpecification("");
            itemPojo.setManufacturer("");
            itemPojo.setSaleUnit("");
            //医生工作统计
            itemPojo.setDoctorName(orderExam.getDoctorName());
            Double totalcount = orderExamList.stream().mapToDouble(order -> {
                Double count = LangUtils.getDouble(order.getCount());
                return count == null ? 1 : count;
            }).sum();
            itemPojo.setCount(totalcount);
            itemPojo.setCountDesc(String.valueOf(totalcount.intValue()));
            Double receivableAmt = orderExamList.stream().filter(order -> order.getSalePrice() != null).mapToDouble(order -> {
                Double receivable = order.getReceivableAmt();
                return AmtUtils.decimalFormat(null == receivable ? 0D : receivable);
            }).sum();
            itemPojo.setReceivableAmt(receivableAmt);
        }
        return itemPojo;
    }
}
