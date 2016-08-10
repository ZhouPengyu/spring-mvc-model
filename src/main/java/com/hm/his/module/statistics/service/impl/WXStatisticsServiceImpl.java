package com.hm.his.module.statistics.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.LangUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.statistics.pojo.*;
import com.hm.his.module.statistics.service.*;
import com.hm.his.module.user.service.BoundInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @param
 * @author SuShaohua
 * @date 2016/4/29
 * @description
 */
@Service("WXStatisticsService")
public class WXStatisticsServiceImpl implements WXStatisticsService {
    @Autowired
    OrderStatisticsService orderStatisticsService;
    @Autowired
    DrugSaleStatisticsService drugSaleStatisticsService;
    @Autowired
    ExamStatisticsService examStatisticsService;
    @Autowired
    AdditionAmtStatisticsService additionAmtStatisticsService;
    @Autowired
    BoundInfoService boundInfoService;

    /**
     * <p>统计明细</p>
     * @param req
     * @return
     */
    @Override
    public HospitalStatisticsDetailPojo statisticsDetail(WXStatisticsRequest req) {
        WXStatisticsRequest.handleStatisticsDate(req);
        HospitalStatisticsDetailPojo re = new HospitalStatisticsDetailPojo();
        //营业额，总人次，及医生工作统计
        OrderStatisticsPojo order = orderStatisticsService.orderStatistics(req);
        Integer grossImpression = 0;
        Double turnover = 0D;
        List<Map<String, Object>> doctorStatisticList = Lists.newArrayList();

        if (order != null) {
            grossImpression = order.getGrossImpression();
            turnover = order.getTotalActualAmt();
            List<OrderStatisticsItemPojo> orderStatisticsItemList = Lists.newArrayList();
            orderStatisticsItemList = order.getOrders();
            Set<String> doctorSet = Sets.newHashSet();
            for (OrderStatisticsItemPojo o : orderStatisticsItemList) {
                if (o.getDoctorName() != null)
                    doctorSet.add(o.getDoctorName());
            }
            for (String doctor : doctorSet) {
                Map<String, Object> map = Maps.newHashMap();
                Double doctorAmt = 0D;
                Integer doctorImpression = 0;
                for (OrderStatisticsItemPojo o : orderStatisticsItemList) {
                    if (doctor.equals(o.getDoctorName()) && o.getDoctorName() != null) {
                        doctorAmt += o.getActualAmt();
                        doctorImpression++;
                    }
                }

                map.put("doctorName", doctor);
                map.put("doctorImpression", doctorImpression);
                map.put("doctorAmt", AmtUtils.decimalFormat(doctorAmt));
                doctorStatisticList.add(map);
            }
        }
        Collections.sort(doctorStatisticList, new Comparator<Map<String, Object>>() {
            public int compare(Map arg0, Map arg1) {
                Double value0 = (Double) arg0.get("doctorAmt");
                Double value1 = (Double) arg1.get("doctorAmt");
                return value1.compareTo(value0);
            }
        });

        re.setGrossImpression(grossImpression);
        re.setTurnover(AmtUtils.decimalFormat(turnover));
        re.setDoctorStatisticList(doctorStatisticList);
        //药品销售统计
        DrugSaleStatisticsPojo drug = drugSaleStatisticsService.drugSaleStatistics(req);
        Double drugSaleAmt = 0D;
        Double drugSaleProfit = 0D;
        if (drug != null) {
            drugSaleAmt = drug.getTotalSaleAmt();
            drugSaleProfit = drug.getTotalProfit();
        }
        re.setDrugSaleAmt(AmtUtils.decimalFormat(drugSaleAmt));
        re.setDrugSaleProfit(AmtUtils.decimalFormat(drugSaleProfit));
        //检查统计
        ExamStatisticsPojo exam = examStatisticsService.examStatistics(req);
        Double examSaleAmt = 0D;
        Double examSaleProfit = 0D;
        if (exam != null) {
            examSaleAmt = exam.getTotalSaleAmt();
            examSaleProfit = exam.getTotalProfit();
        }
        re.setExamSaleAmt(AmtUtils.decimalFormat(examSaleAmt = examSaleAmt == null ? 0 : examSaleAmt));
        re.setExamSaleProfit(examSaleProfit);
        //附加费用统计
        AdditionAmtStatisticsPojo addition = additionAmtStatisticsService.additionAmtStatistics(req);
        Double additionSaleAmt = 0D;
        if (addition != null) {
            additionSaleAmt = addition.getTotalSaleAmt();
        }
        re.setAdditionSaleAmt(AmtUtils.decimalFormat(additionSaleAmt = additionSaleAmt == null ? 0 : additionSaleAmt));
        Double totalProfitAmt = AmtUtils.decimalFormat(drugSaleProfit + examSaleProfit + additionSaleAmt);
        re.setTotalProfitAmt(totalProfitAmt);

        return re;
    }

    /**
     * @param req
     * @return
     * @description 统计概览
     */
    @Override
    public List<HospitalStatisticsPojo> hospitalStatistics(WXStatisticsRequest req) {
        WXStatisticsRequest.handleStatisticsDate(req);
        List<HospitalStatisticsPojo> re = Lists.newArrayList();

        String openId = SessionUtils.getOpenId();
        //String openId = "oArTyvyCfrTszBtZgiJBd4Fy6IOs";
        Map<String, String> param = Maps.newHashMap();
        param.put("openId", openId);
        param.put("function", "statistics");
        List<Map> hospitalList = boundInfoService.boundHospitalFunction(param);
        if (null == hospitalList) hospitalList = new ArrayList<Map>();
        /**
         * 对相同hospitalId诊所去重
         */
        Set<Map> hospitalListList = Sets.newHashSet();
        for (Map m : hospitalList) {
            hospitalListList.add(m);
        }
        //TODO Set<Map>只适用于简单map

        for (Map m : hospitalListList) {
            HospitalStatisticsPojo hospitalStatisticsPojo = new HospitalStatisticsPojo();
            hospitalStatisticsPojo.setHospitalId(LangUtils.getLong(m.get("hospitalId")));
            hospitalStatisticsPojo.setHospitalName(LangUtils.getString(m.get("hospitalName")));
            if (0 == LangUtils.getInteger(m.get("status"))) {
                hospitalStatisticsPojo.setTurnover(0D);
                hospitalStatisticsPojo.setVisitor(0);
                hospitalStatisticsPojo.setAccess(0);
            } else {
                req.setHospitalId(LangUtils.getLong(m.get("hospitalId")));
                OrderStatisticsPojo order = orderStatisticsService.orderStatistics(req);
                hospitalStatisticsPojo.setAccess(1);
                if (order != null) {
                    hospitalStatisticsPojo.setTurnover(order.getTotalActualAmt());
                    hospitalStatisticsPojo.setVisitor(order.getGrossImpression());
                } else {
                    hospitalStatisticsPojo.setTurnover(0D);
                    hospitalStatisticsPojo.setVisitor(0);
                }
            }
            re.add(hospitalStatisticsPojo);
        }

        Collections.sort(re, new Comparator<HospitalStatisticsPojo>() {
            public int compare(HospitalStatisticsPojo arg0, HospitalStatisticsPojo arg1) {
                Double value0 = arg0.getTurnover();
                Double value1 = arg1.getTurnover();
                return value1.compareTo(value0);
            }
        });

        //计算所有诊所
        HospitalStatisticsPojo amt = new HospitalStatisticsPojo();
        Double turnover = 0D;
        Integer visitor = 0;
        Integer access = 0;
        for (HospitalStatisticsPojo client : re) {
            turnover += client.getTurnover() == null ? 0D : client.getTurnover();
            visitor += client.getVisitor() == null ? 0 : client.getVisitor();
            access += client.getAccess() == null ? 0 : client.getAccess();
        }
        if (access > 0) {
            amt.setAccess(1);
        } else {
            amt.setAccess(0);
        }
        amt.setTurnover(turnover);
        amt.setVisitor(visitor);
        amt.setHospitalId(0L);
        amt.setHospitalName("所有诊所");
        re.add(0, amt);
        return re;
    }
}
