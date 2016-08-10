package com.hm.his.module.statistics.service.impl;

import com.google.common.collect.Lists;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.dao.OrderDrugMapper;
import com.hm.his.module.order.dao.SellDrugRecordMapper;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.PayModeEnum;
import com.hm.his.module.order.model.SellDrugRecord;
import com.hm.his.module.order.pojo.OrderTotalAmtPojo;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.outpatient.dao.PatientDiagnosisMapper;
import com.hm.his.module.outpatient.dao.PatientInquiryMapper;
import com.hm.his.module.outpatient.dao.PatientMapper;
import com.hm.his.module.outpatient.model.Patient;
import com.hm.his.module.outpatient.model.PatientDiagnosis;
import com.hm.his.module.outpatient.model.PatientInquiry;
import com.hm.his.module.outpatient.service.PatientDiagnosisService;
import com.hm.his.module.statistics.pojo.*;
import com.hm.his.module.statistics.service.AdditionAmtStatisticsService;
import com.hm.his.module.statistics.service.DrugSaleStatisticsService;
import com.hm.his.module.statistics.service.ExamStatisticsService;
import com.hm.his.module.statistics.service.OrderStatisticsService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.DoctorService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderStatisticsServiceImpl implements OrderStatisticsService {
    @Autowired(required = false)
    HospitalOrderMapper hospitalOrderMapper;
    @Autowired(required = false)
    PatientInquiryMapper patientInquiryMapper;
    @Autowired(required = false)
    SellDrugRecordMapper sellDrugRecordMapper;
    @Autowired(required = false)
    PatientMapper patientMapper;
    @Autowired(required = false)
    OrderDrugMapper orderDrugMapper;
    @Autowired
    PatientDiagnosisService patientDiagnosisService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    OrderService orderService;
    @Autowired
    ExamStatisticsService examStatisticsService;
    @Autowired
    DrugSaleStatisticsService drugSaleStatisticsService;
    @Autowired
    AdditionAmtStatisticsService additionAmtStatisticsService;
    @Autowired(required = false)
    PatientDiagnosisMapper patientDiagnosisMapper;

    public OrderStatisticsPojo orderStatistics1(StatisticsRequest req) {
        StatisticsRequest.handleStatisticsRequest(req);
        OrderStatisticsPojo re = new OrderStatisticsPojo();
        /**
         * 总人次
         */
        Integer grossImpression = hospitalOrderMapper.selectCountOfOrderStatisticsItemPojos(req);
        if (grossImpression == null) {
            return null;
        }
        List<HospitalOrder> hospitalOrders = hospitalOrderMapper.selectOrderStatisticsItemPojos(req);
        if (CollectionUtils.isEmpty(hospitalOrders)) {
            return null;
        }
        re.setGrossImpression(grossImpression);
        req.setTotalCount(grossImpression);
        re.setTotalPage(req.getTotalPage());
        List<OrderStatisticsItemPojo> orders = hospitalOrders.stream().map(order -> {
            return this.getOrderStatisticsItemPojoByHospitalOrder(order);
        }).collect(Collectors.toList());
        /**
         * 实收金额合计
         */
        Double totalActualAmt = orders.stream().mapToDouble(order -> {
            return order.getActualAmt();
        }).sum();
        // 保留两位小数
        totalActualAmt = AmtUtils.decimalFormat(totalActualAmt);
        re.setTotalActualAmt(totalActualAmt);
        /**
         * 应收金额合计
         */
        Double totalReceivableAmt = orders.stream().mapToDouble(OrderStatisticsItemPojo::getReceivableAmt).sum();
        totalReceivableAmt = AmtUtils.decimalFormat(totalReceivableAmt);
        re.setTotalReceivableAmt(totalReceivableAmt);
        // 排序
        orders = orders.stream().sorted((order1, order2) -> {
            Date order1ChargeDate = order1.getChargeDate();
            Date order2ChargeDate = order2.getChargeDate();
            if (order1ChargeDate == null || order2ChargeDate == null) {
                return 0;
            }
            return order2ChargeDate.compareTo(order1ChargeDate);
        }).collect(Collectors.toList());

        // 分页
        if (req.getStartRecord() != null && req.getPageSize() != null) {
            orders = orders.stream().skip(req.getStartRecord()).limit(req.getPageSize()).collect(Collectors.toList());
        }
        re.setOrders(orders);
        return re;
    }

    /**
     * 门诊日志查询优化
     *
     * @param req
     * @return
     * @date 2016年7月15日
     * @author lipeng
     */
    @Override
    public OrderStatisticsPojo orderStatistics(StatisticsRequest req) {
        StatisticsRequest.handleStatisticsRequest(req);
        OrderStatisticsPojo re = new OrderStatisticsPojo();
        /**
         * 总人次
         */
        Integer grossImpression = hospitalOrderMapper.selectCountOfOrderStatisticsItemPojos(req);
        if (grossImpression == null) {
            return null;
        }
        //总人次
        re.setGrossImpression(grossImpression);
        //总数
        req.setTotalCount(grossImpression);
        //总页数
        re.setTotalPage(req.getTotalPage());
        // 查询数据库
        List<OrderStatisticsItemPojo> orders = hospitalOrderMapper
                .selectOrderStatisticsItemPojosByStatisticsRequest(req);
        if (CollectionUtils.isEmpty(orders)) {
            return null;
        }
        // 订单中的病历id集合
        List<Long> recordIdList = orders.stream().filter(order -> {
            return order.getOrderType() == HospitalOrder.TYPE_PATIENT_RECORD;
        }).map(OrderStatisticsItemPojo::getRecordId).collect(Collectors.toList());
        // 订单号集合
        List<String> orderNos = orders.stream().map(OrderStatisticsItemPojo::getOrderNo).collect(Collectors.toList());
        // 患者诊断map
        Map<Long, List<PatientDiagnosis>> patientDiagnosisMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(recordIdList)) {
            List<PatientDiagnosis> patientDiagnosis = patientDiagnosisMapper.getDiagnosisByRecordList(recordIdList);
            patientDiagnosisMap = patientDiagnosis.stream()
                    .collect(Collectors.groupingBy(PatientDiagnosis::getRecordId));
        }
        // 订单中已退费项目的应收金额map
        Map<String, Double> orderRefundReceivableAmtAmtMap = orderService.getReceivableRefundAmts(orderNos);
        for (OrderStatisticsItemPojo order : orders) {
            // 患者信息
            String patientInfo = order.getPatientInfo();
            if (patientInfo != null) {
                String[] patientInfoArr = patientInfo.split(",");
                if (StringUtils.equals(patientInfoArr[0], "1")) {
                    order.setGender("男");
                } else if (StringUtils.equals(patientInfoArr[0], "0")) {
                    order.setGender("女");
                }
                order.setPhoneNo(patientInfoArr[1]);
                order.setAge(StringUtils.isEmpty(patientInfoArr[2]) ? null : Double.valueOf(patientInfoArr[2]));
                order.setAgeType(patientInfoArr[3]);
                if (order.getOrderType() == HospitalOrder.TYPE_PATIENT_RECORD) {
                    //如果是病例订单的话，设置诊断
                    List<PatientDiagnosis> patientDiagnosis = patientDiagnosisMap.get(order.getRecordId());
                    if (CollectionUtils.isNotEmpty(patientDiagnosis)) {
                        List<String> diagnosisNameList = patientDiagnosis.stream().map(PatientDiagnosis::getDiseaseName)
                                .distinct().collect(Collectors.toList());
                        String diagnosis = StringUtils.join(diagnosisNameList, "、");
                        order.setDiagnosis(diagnosis);
                    }
                }
            }
            // 付款方式
            order.setPayModeName(PayModeEnum.getPayModeNameByMode(order.getPayMode()));
            // 订单中未收费项目的应收金额
            Double orderReceivableAmt = order.getOrderReceivableAmtAmt() == null ? 0D
                    : order.getOrderReceivableAmtAmt();
            // 订单总应收金额
            Double orderTotalReceivableAmt = order.getOrderTotalReceivableAmtAmt() == null ? 0D
                    : order.getOrderTotalReceivableAmtAmt();
            // 订单实退金额
            Double orderRefundAmt = order.getOrderRefundAmt() == null ? 0D : order.getOrderRefundAmt();
            // 订单中的实收金额
            Double orderActualAmt = order.getOrderActualAmt() == null ? 0D : order.getOrderActualAmt();
            // 应收金额=已经收费项目的应收金额-已经退费的项目的应收金额 =总应收金额-待应收金额-退费项目的应收金额
            Double receivableAmt = AmtUtils.subtract(orderTotalReceivableAmt, orderReceivableAmt);
            Double receivableRefundAmt = orderRefundReceivableAmtAmtMap.get(order.getOrderNo()) == null ? 0D
                    : orderRefundReceivableAmtAmtMap.get(order.getOrderNo());
            order.setReceivableAmt(AmtUtils.subtract(receivableAmt, receivableRefundAmt));
            // 显示实收金额=真实实收金额-实退金额
            Double actualAmt = AmtUtils.subtract(orderActualAmt, orderRefundAmt);
            order.setActualAmt(actualAmt);
        }
        /**
         * 实收金额合计
         */
        OrderTotalAmtPojo orderTotalAmtPojo = hospitalOrderMapper.selectOrderTotalAmtPojo(req);
        if (orderTotalAmtPojo != null) {
            // 最终实收金额（实收-实退）合计
            Double totalFinalActualAmt = orderTotalAmtPojo.getTotalFinalActualAmt() == null ? 0D
                    : orderTotalAmtPojo.getTotalFinalActualAmt();
            re.setTotalActualAmt(AmtUtils.decimalFormat(totalFinalActualAmt));
            // 已经收费项目的应收金额合计
            Double totalAlreadyReceivableAmt = orderTotalAmtPojo.getTotalAlreadyReceivableAmt() == null ? 0D
                    : orderTotalAmtPojo.getTotalAlreadyReceivableAmt();
            Double totalReceivableRefundAmt = hospitalOrderMapper
                    .selectReceivableAmtSumFromOrderItemChargeByStatisticsRequest(req);
            // 已退费项目的应收金额合计
            totalReceivableRefundAmt = totalReceivableRefundAmt == null ? 0D : totalReceivableRefundAmt;
            re.setTotalReceivableAmt(AmtUtils.subtract(totalAlreadyReceivableAmt, totalReceivableRefundAmt));

        }
        //		Double totalActualAmt = orders.stream().mapToDouble(order -> {
        //			return order.getActualAmt();
        //		}).sum();
        //		// 保留两位小数
        //		totalActualAmt = AmtUtils.decimalFormat(totalActualAmt);
        //		re.setTotalActualAmt(totalActualAmt);
        /**
         * 应收金额合计
         */
        //		Double totalReceivableAmt = orders.stream().mapToDouble(OrderStatisticsItemPojo::getReceivableAmt).sum();
        //		totalReceivableAmt = AmtUtils.decimalFormat(totalReceivableAmt);
        //		re.setTotalReceivableAmt(totalReceivableAmt);

        re.setOrders(orders);
        return re;
    }

    private OrderStatisticsItemPojo getOrderStatisticsItemPojoByHospitalOrder(HospitalOrder order) {
        if (order == null) {
            return null;
        }
        Double age = null;
        String ageType = null;
        String gender = null;
        String diagnosis = null;
        String phoneNo = null;
        OrderStatisticsItemPojo pojo = new OrderStatisticsItemPojo();
        if (HospitalOrder.TYPE_PATIENT_RECORD == order.getOrderType()) {
            // 病历
            Long recordId = order.getPatientRecordId();
            PatientInquiry patientInquiry = patientInquiryMapper.getInquiryByRecordId(recordId);
            Patient patient = patientMapper.getPatientById(order.getPatientId());
            if (patientInquiry != null) {
                age = patientInquiry.getAge();
                ageType = patientInquiry.getAgeType();
                if (patient != null) {
                    if (patient.getGender() != null) {
                        gender = patient.getGender() == 1 ? "男" : "女";
                    } else {
                        gender = "";
                    }
                    phoneNo = patient.getPhoneNo();
                }

                try {
                    List<PatientDiagnosis> patientDiagnosisList = patientDiagnosisService.getDiagnosisByRecordId(recordId);
                    if (CollectionUtils.isNotEmpty(patientDiagnosisList)) {
                        List<String> diagnosisNames = patientDiagnosisList.stream().map(patientDiagnosis -> {
                            return patientDiagnosis.getDiseaseName();
                        }).collect(Collectors.toList());
                        diagnosis = StringUtils.join(diagnosisNames, "、");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else if (HospitalOrder.TYPE_SELL_DRUG == order.getOrderType()) {
            // 直接售药
            Long sellDrugRecordId = order.getSellDrugRecordId();
            SellDrugRecord sellDrugRecord = sellDrugRecordMapper.selectById(sellDrugRecordId);
            if (sellDrugRecord != null) {
                age = sellDrugRecord.getAge();
                ageType = sellDrugRecord.getAgeType();
                if (sellDrugRecord.getGender() != null) {
                    gender = sellDrugRecord.getGender() == 1 ? "男" : "女";
                } else {
                    gender = "";
                }

                phoneNo = sellDrugRecord.getPhoneNo();
            }
        }
        Double orderReceivableAmt = order.getReceivableAmt() == null ? 0D : order.getReceivableAmt();
        Double orderTotalReceivableAmt = order.getTotalReceivableAmt() == null ? 0D : order.getTotalReceivableAmt();
        Double orderRefundAmt = order.getRefundAmt() == null ? 0D : order.getRefundAmt();
        Double orderActualAmt = order.getActualAmt() == null ? 0D : order.getActualAmt();
        // 应收金额=已经收费项目的应收金额-已经退费的项目的应收金额 =总应收金额-待应收金额-退费项目的应收金额
        Double receivableAmt = AmtUtils.subtract(orderTotalReceivableAmt, orderReceivableAmt);
        Double receivableRefundAmt = orderService.getReceivableRefundAmt(order.getOrderNo());
        pojo.setReceivableAmt(AmtUtils.subtract(receivableAmt, receivableRefundAmt));
        // 显示实收金额=真实实收金额-实退金额
        Double actualAmt = AmtUtils.subtract(orderActualAmt, orderRefundAmt);
        pojo.setActualAmt(actualAmt);
        pojo.setAge(age);
        pojo.setAgeType(ageType);
        pojo.setChargeDate(order.getLastChargeDate());
        Doctor doctor = doctorService.getDoctorById(order.getCreater());
        pojo.setDoctorName(doctor == null ? null : doctor.getRealName());
        pojo.setDiagnosis(diagnosis);
        pojo.setPhoneNo(phoneNo);
        pojo.setGender(gender);
        pojo.setOrderNo(order.getOrderNo());
        pojo.setOrderType(order.getOrderType());
        pojo.setPatientId(order.getPatientId());
        pojo.setPatientName(order.getPatientName());
        pojo.setRecordId(order.getPatientRecordId());
        pojo.setSellDrugRecordId(order.getSellDrugRecordId());
        pojo.setPayModeName(PayModeEnum.getPayModeNameByMode(order.getPayMode()));
        return pojo;
    }

    @Override
    public Integer selectCountOfWorkloadItemPojos(StatisticsRequest req) {
        return hospitalOrderMapper.selectCountOfWorkloadItemPojos(req);
    }

    @Override
    public WorkloadStatisticsPojo workloadStatistics(StatisticsRequest req) {
        StatisticsRequest.handleStatisticsRequest(req);
        WorkloadStatisticsPojo po = new WorkloadStatisticsPojo();
        List<WorkloadStatisticsItemPojo> allWorkload = new ArrayList<>();
        List<WorkloadStatisticsItemPojo> loadExam = examStatisticsService.loadExamStatistics(req);
        Integer totalCount = 0;
        List<String> orderNoList = Lists.newArrayList();
        if (null != loadExam) {
            allWorkload.addAll(loadExam);
            totalCount += loadExam.size();
            orderNoList.addAll(loadExam.get(0).getOrderNoList());
        }
        List<WorkloadStatisticsItemPojo> loadDrug = drugSaleStatisticsService.loadDrugStatistics(req);
        if (null != loadDrug) {
            allWorkload.addAll(loadDrug);
            totalCount += loadDrug.size();
            orderNoList.addAll(loadDrug.get(0).getOrderNoList());
        }
        List<WorkloadStatisticsItemPojo> loadAmt = additionAmtStatisticsService.loadAdditionalStatistics(req);
        if (null != loadAmt) {
            allWorkload.addAll(loadAmt);
            totalCount += loadAmt.size();
            orderNoList.addAll(loadAmt.get(0).getOrderNoList());
        }
        //金额合计
        Double totalReceivableAmt = allWorkload.stream().filter(workload -> workload.getReceivableAmt() != null)
                .mapToDouble(workload -> {
                    return workload.getReceivableAmt();
                }).sum();
        po.setTotalReceivableAmt(totalReceivableAmt);
        //总页数
        req.setTotalCount(totalCount);
        Integer totalPage = req.getTotalPage();
        po.setTotalPage(totalPage);
        //总人次
        Integer grossImpression = new ArrayList<>(new HashSet<>(orderNoList)).size();
        po.setGrossImpression(grossImpression);
        //排序
        List<WorkloadStatisticsItemPojo> workload = allWorkload.stream().sorted((load1, load2) -> {
            Double receiableAmt1 = load1.getReceivableAmt() == null ? 0D : load1.getReceivableAmt();
            Double receiableAmt2 = load2.getReceivableAmt() == null ? 0D : load2.getReceivableAmt();
            return receiableAmt2.compareTo(receiableAmt1);
        }).collect(Collectors.toList());
        // 分页
        if (req.getStartRecord() != null & req.getPageSize() != null) {
            workload = workload.stream().skip(req.getStartRecord()).limit(req.getPageSize())
                    .collect(Collectors.toList());
        }
        po.setLoadList(workload);
        return po;
    }
}
