package com.hm.his.module.statistics.pojo;

import java.util.List;

/**
 * Created by SuShaohua on 2016/7/5.
 */
public class WorkloadStatisticsPojo {
    /**
     * 总人次
     */
    Integer grossImpression;
    /**
     * 应收金额合计
     */
    Double totalReceivableAmt;

    List<WorkloadStatisticsItemPojo> loadList;

    Integer totalPage;

    public Integer getGrossImpression() {
        return grossImpression;
    }

    public void setGrossImpression(Integer grossImpression) {
        this.grossImpression = grossImpression;
    }

    public Double getTotalReceivableAmt() {
        return totalReceivableAmt;
    }

    public void setTotalReceivableAmt(Double totalReceivableAmt) {
        this.totalReceivableAmt = totalReceivableAmt;
    }

    public List<WorkloadStatisticsItemPojo> getLoadList() {
        return loadList;
    }

    public void setLoadList(List<WorkloadStatisticsItemPojo> loadList) {
        this.loadList = loadList;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
