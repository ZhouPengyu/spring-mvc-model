package com.hm.his.module.statistics.pojo;

import java.util.List;

public class ExamStatisticsPojo {
	Integer examCount;
	Double totalSaleAmt;
	List<ExamStatisticsItemPojo> exams;
	Integer totalPage;
	Double totalProfit;
	public Integer getExamCount() {
		return examCount;
	}
	public void setExamCount(Integer examCount) {
		this.examCount = examCount;
	}
	public Double getTotalSaleAmt() {
		return totalSaleAmt;
	}
	public void setTotalSaleAmt(Double totalSaleAmt) {
		this.totalSaleAmt = totalSaleAmt;
	}
	public List<ExamStatisticsItemPojo> getExams() {
		return exams;
	}
	public void setExams(List<ExamStatisticsItemPojo> exams) {
		this.exams = exams;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Double getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(Double totalProfit) {
		this.totalProfit = totalProfit;
	}
}
