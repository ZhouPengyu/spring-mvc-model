package com.hm.his.module.statistics.pojo;

import java.util.List;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月24日
 */
public class DrugSaleStatisticsPojo {
	Double totalSaleAmt;
	Double totalProfit;
	List<DrugSaleStatisticsItemPojo> drugs;
	Integer totalPage;
	
	public Double getTotalSaleAmt() {
		return totalSaleAmt;
	}
	public void setTotalSaleAmt(Double totalSaleAmt) {
		this.totalSaleAmt = totalSaleAmt;
	}
	public Double getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(Double totalProfit) {
		this.totalProfit = totalProfit;
	}
	public List<DrugSaleStatisticsItemPojo> getDrugs() {
		return drugs;
	}
	public void setDrugs(List<DrugSaleStatisticsItemPojo> drugs) {
		this.drugs = drugs;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
	
}
