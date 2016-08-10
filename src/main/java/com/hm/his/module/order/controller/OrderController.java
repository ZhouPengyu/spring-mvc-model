package com.hm.his.module.order.controller;

import java.util.List;

import com.hm.his.module.order.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.SellDrugRecord;
import com.hm.his.module.order.service.ChargeService;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.order.service.OrderExamService;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.order.service.RefundService;
import com.hm.his.module.order.service.SellDrugService;
import com.hm.his.module.statistics.pojo.NameRequst;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月1日
 */
@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	OrderService orderService;
	@Autowired
	SellDrugService sellDrugService;
	@Autowired
	ChargeService chargeService;
	@Autowired
	RefundService refundService;
	@Autowired
	OrderExamService orderExamService;
	@Autowired
	OrderDrugService orderDrugService;

	/**
	 * 
	 * @description 查询诊所的待收费订单
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getToChargeOrders", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getToChargeOrders(@RequestBody GetOrdersRequest req) {
		HospitalOrderPojoList body = orderService.getToChargeOrders(req);
		HisResponse response = HisResponse.getInstance(body);
		return response.toString();
	}

	/**
	 * 
	 * @description 查询诊所的已收费订单
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getChargedOrders", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getChargedOrders(@RequestBody GetOrdersRequest req) {
		HospitalOrderPojoList body = orderService.getChargedOrders(req);
		HisResponse response = HisResponse.getInstance(body);
		return response.toString();
	}

	/**
	 * 
	 * @description 查询诊所的已退费订单
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getRefundedOrders", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getRefundedOrders(@RequestBody GetOrdersRequest req) {
		HospitalOrderPojoList body = orderService.getRefundedOrders(req);
		HisResponse response = HisResponse.getInstance(body);
		return response.toString();
	}

	/**
	 * 
	 * @description 获取待收费订单详情
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getToChargeOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getToChargeOrderInfo(@RequestBody GetOrdersRequest req) {
		HospitalOrderDetailPojo body = orderService.getToChargeOrderInfo(req);
		HisResponse response = HisResponse.getInstance(body);
		return response.toString();
	}

	/**
	 * 
	 * @description 获取已收费订单详情
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getChargedOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getChargedOrderInfo(@RequestBody GetOrdersRequest req) {
		HospitalOrderDetailPojo body = orderService.getChargedOrderInfo(req);
		HisResponse response = HisResponse.getInstance(body);
		return response.toString();
	}

	/**
	 * 
	 * @description 获取已退费订单详情
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getRefundedOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getRefundedOrderInfo(@RequestBody HospitalOrder req) {
		HospitalOrderDetailPojo body = orderService.getRefundedOrderInfo(req);
		HisResponse response = HisResponse.getInstance(body);
		return response.toString();
	}

	/**
	 * 
	 * @description 收费
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/charge", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String charge(@RequestBody(required = true) ChargeRequest req) {
		if (req.getActualAmt() == null && req.getActualAmt() < 0) {
			HisResponse res = new HisResponse();
			res.setErrorCode(OrderErrorMessage.NonsupportNegative.getCode());
			res.setErrorMessage(OrderErrorMessage.NonsupportNegative.getMsg());
			return res.toString();
		}
		return chargeService.charge(req).toString();
	}

	/**
	 * 
	 * @description 退费
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/refund", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String refund(@RequestBody RefundRequest req) {
		if (req.getRefundAmt() == null && req.getRefundAmt() < 0) {
			HisResponse res = new HisResponse();
			res.setErrorCode(OrderErrorMessage.NonsupportNegative.getCode());
			res.setErrorMessage(OrderErrorMessage.NonsupportNegative.getMsg());
			return res.toString();
		}
		HisResponse hisResponse = refundService.refund(req);
		return hisResponse.toString();
	}

	/**
	 * 
	 * @description 直接售药收费
	 * @date 2016年3月1日
	 * @author lipeng
	 * @param sellDrugRecord
	 * @return
	 */
	@RequestMapping(value = "/sellDrugs", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String sellDrugs(@RequestBody SellDrugRecord sellDrugRecord) {
		sellDrugService.sellDrugs(sellDrugRecord);
		return new HisResponse().toString();
	}
	/**
	 * 
	 * @description  模糊搜索药物名
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	/**
	 * 
	 * @description 订单检查项名称sug
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/searchOrderExamNames", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String searchOrderExamNames(@RequestBody NameRequst req) {
		List<String> list = orderExamService.searchOrderExamNames(req);
		return HisResponse.getInstance(list).toString();
	}
	/**
	 * 
	 * @description 模糊搜索药物名称
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	/**
	 * 
	 * @description 订单药物名称sug
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/searchOrderDrugNames", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String searchOrderDrugNames(@RequestBody NameRequst req) {
		List<String> list = orderDrugService.searchOrderDrugNames(req);
		return HisResponse.getInstance(list).toString();
	}
	/**
	 * 
	 * @description  获取直接售药记录详情
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	/**
	 * 
	 * @description 直接售药详情
	 * @date 2016年3月30日
	 * @author lipeng
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getSellDrugInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getSellDrugInfo(@RequestBody HospitalOrder req) {
		SellDrugInfoPojo body = sellDrugService.getSellDrugInfo(req);
		return HisResponse.getInstance(body).toString();
	}

}
