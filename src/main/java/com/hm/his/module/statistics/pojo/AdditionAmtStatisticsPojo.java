package com.hm.his.module.statistics.pojo;

import java.util.List;

public class AdditionAmtStatisticsPojo {
	Integer additionAmtCount;
	Double totalSaleAmt;
	List<AdditionAmtStatisticsItemPojo> additionAmts;
	Integer totalPage;

	public Integer getAdditionAmtCount() {
		return additionAmtCount;
	}

	public void setAdditionAmtCount(Integer additionAmtCount) {
		this.additionAmtCount = additionAmtCount;
	}

	public Double getTotalSaleAmt() {
		return totalSaleAmt;
	}

	public void setTotalSaleAmt(Double totalSaleAmt) {
		this.totalSaleAmt = totalSaleAmt;
	}

	public List<AdditionAmtStatisticsItemPojo> getAdditionAmts() {
		return additionAmts;
	}

	public void setAdditionAmts(List<AdditionAmtStatisticsItemPojo> additionAmts) {
		this.additionAmts = additionAmts;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

}
