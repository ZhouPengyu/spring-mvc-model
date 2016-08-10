package com.hm.his.framework.model;

/**
 * 分页的相关参数
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/1
 * Time: 10:47
 * CopyRight:HuiMei Engine
 */
public class PageRequest extends Request {

	private Integer currentPage = 1; // 当前页数 默认当前页：1
	private Integer pageSize=20; // 分页数量大小 默认分页大小 ：20
	private Integer startRecord = 0; // 分页查询时起始记录 默认：0

	private Integer totalPage; // 总页数
	private Integer totalCount;// 总记录数
	private Integer limit;
	private Integer isDescSort = 0; //是否为倒序  desc 排序规则



	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStartRecord() {
		if (currentPage == null || pageSize == null) {
			return 0;
		}
		return (currentPage - 1) * pageSize;
	}

	public void setStartRecord(Integer startRecord) {
		this.startRecord = startRecord;
	}

	public Integer getTotalPage() {
		if (null != totalCount&&pageSize!=null) {
			if (totalCount % pageSize == 0) {
				return totalCount / pageSize;
			} else {
				return 1 + (totalCount / pageSize);
			}
		}
		return 0;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getIsDescSort() {
		return isDescSort;
	}

	public void setIsDescSort(Integer isDescSort) {
		this.isDescSort = isDescSort;
	}

	public static void main(String[] args) {
		int totalCount = 221;
		int pageSize = 20;
		if (totalCount % pageSize == 0) {
			System.out.println(totalCount / pageSize);
		} else {
			System.out.println(totalCount / pageSize + 1);
		}
		System.out.println(Math.ceil(216 / 20));
		System.out.println((int) Math.ceil(216 / 20));
	}

}
