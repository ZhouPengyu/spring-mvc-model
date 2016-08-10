package com.hm.his.module.order.service;

import java.util.List;

import com.hm.his.module.order.model.OrderOperateRecord;
import com.hm.his.module.order.model.OrderOperateRecord.OperateItem;

/**
 * 
 * @description 订单操作日志service
 * @author lipeng
 * @date 2016年3月4日
 */
public interface OrderOperateRecordService {

	/**
	 * 
	 * @description 添加收费记录
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param orderNo
	 * @param actualAmt
	 * @param receivableAmt
	 * @param itemList
	 */
	void addChargeRecord(String orderNo, Double actualAmt, Double receivableAmt, List<OperateItem> itemList, Double payAmount);

	/**
	 * 
	 * @description 添加退费记录
	 * @date 2016年3月13日
	 * @author lipeng
	 * @param orderNo
	 * @param actualAmt
	 * @param receivableAmt
	 * @param itemList
	 */
	void addRefundRecord(String orderNo, Double actualAmt, Double receivableAmt, List<OperateItem> itemList);
}
