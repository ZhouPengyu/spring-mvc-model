package com.hm.his.module.statistics.pojo;

import com.hm.his.framework.utils.AmtUtils;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月24日
 */
public class ExamStatisticsItemPojo {
	String examName;
	Integer count;
	Double saleAmt;
	Double costAmt;
	Double profitAmt;
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getSaleAmt() {
		return saleAmt;
	}
	public void setSaleAmt(Double saleAmt) {
		if (null != saleAmt){
			saleAmt = AmtUtils.decimalFormat(saleAmt);
		}
		this.saleAmt = saleAmt;
	}

	public Double getCostAmt() {
		return costAmt;
	}

	public void setCostAmt(Double costAmt) {
		this.costAmt = costAmt;
	}

	public Double getProfitAmt() {
		return profitAmt;
	}

	public void setProfitAmt(Double profitAmt) {
		this.profitAmt = profitAmt;
	}
}
