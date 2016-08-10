package com.hm.his.module.statistics.pojo;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * @param
 * @author SuShaohua
 * @date 2016/4/29
 * @description
 */
public class HospitalStatisticsDetailPojo {
    Double turnover = 0D;
    Integer grossImpression= 0;
    Double totalProfitAmt= 0D;
    Double drugSaleAmt= 0D;
    Double drugSaleProfit= 0D;
    Double examSaleAmt= 0D;
    Double additionSaleAmt= 0D;
    Double examSaleProfit = 0D;

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
    }

    public Integer getGrossImpression() {
        return grossImpression;
    }

    public void setGrossImpression(Integer grossImpression) {
        this.grossImpression = grossImpression;
    }

    public Double getTotalProfitAmt() {
        return totalProfitAmt;
    }

    public void setTotalProfitAmt(Double totalProfitAmt) {
        this.totalProfitAmt = totalProfitAmt;
    }

    public Double getDrugSaleAmt() {
        return drugSaleAmt;
    }

    public void setDrugSaleAmt(Double drugSaleAmt) {
        this.drugSaleAmt = drugSaleAmt;
    }

    public Double getDrugSaleProfit() {
        return drugSaleProfit;
    }

    public void setDrugSaleProfit(Double drugSaleProfit) {
        this.drugSaleProfit = drugSaleProfit;
    }

    public Double getExamSaleAmt() {
        return examSaleAmt;
    }

    public void setExamSaleAmt(Double examSaleAmt) {
        this.examSaleAmt = examSaleAmt;
    }

    public Double getAdditionSaleAmt() {
        return additionSaleAmt;
    }

    public void setAdditionSaleAmt(Double additionSaleAmt) {
        this.additionSaleAmt = additionSaleAmt;
    }

    public List<Map<String, Object>> getDoctorStatisticList() {
        return doctorStatisticList;
    }

    public void setDoctorStatisticList(List<Map<String, Object>> doctorStatisticList) {
        this.doctorStatisticList = doctorStatisticList;
    }

    List<Map<String, Object>> doctorStatisticList = Lists.newArrayList();

    public Double getExamSaleProfit() {
        return examSaleProfit;
    }

    public void setExamSaleProfit(Double examSaleProfit) {
        this.examSaleProfit = examSaleProfit;
    }
}
