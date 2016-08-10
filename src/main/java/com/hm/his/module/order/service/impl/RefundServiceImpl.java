package com.hm.his.module.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.DoubleWrap;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.drug.model.DrugTypeEnum;
import com.hm.his.module.drug.pojo.InventoryOperatePojo;
import com.hm.his.module.drug.service.DrugInventoryService;
import com.hm.his.module.order.dao.OrderDrugMapper;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.order.model.OrderItemCharge;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemType;
import com.hm.his.module.order.model.OrderOperateRecord.OperateItem;
import com.hm.his.module.order.pojo.OrderAdditionAmtListAndItemIds;
import com.hm.his.module.order.pojo.OrderErrorMessage;
import com.hm.his.module.order.pojo.OrderExamListAndItemIds;
import com.hm.his.module.order.pojo.PrescriptionAndItemIds;
import com.hm.his.module.order.pojo.RefundRequest;
import com.hm.his.module.order.pojo.SellDrugRecordAndItemIds;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.order.service.OrderItemChargeService;
import com.hm.his.module.order.service.OrderItemListService;
import com.hm.his.module.order.service.OrderOperateRecordService;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.order.service.RefundService;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月10日
 */
@Service
public class RefundServiceImpl implements RefundService {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderOperateRecordService orderOperateRecordService;
	@Autowired
	OrderItemListService orderItemListService;
	@Autowired
	OrderItemChargeService orderItemChargeService;
	@Autowired(required = false)
	OrderDrugMapper orderDrugMapper;
	@Autowired
	DrugInventoryService drugInventoryService;
	@Autowired
	OrderDrugService orderDrugService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.REFUND)
	public HisResponse refund(RefundRequest refundRequest) throws RuntimeException {
		Long recordVersion = refundRequest.getRecordVersion();
		if (recordVersion == null) {
			HisResponse res = new HisResponse();
			res.setErrorCode(400);
			res.setErrorMessage("请求参数错误：recordVersion为空");
			return res;
		}
		Long currentRecordVersion = orderService.getRecordVersion(refundRequest.getOrderNo());
		if (currentRecordVersion != null && recordVersion != currentRecordVersion.longValue()) {
			HisResponse res = new HisResponse();
			res.setErrorCode(OrderErrorMessage.RecordChangedOrRefunded.getCode());
			res.setErrorMessage(OrderErrorMessage.RecordChangedOrRefunded.getMsg());
			return res;
		}
		// 退费之前，更新版本号，避免重复提交
		orderService.updateRecordVersion(refundRequest.getOrderNo(), System.currentTimeMillis());
		// 将具体项的状态设置为已退费，更新订单的实收金额，以及订单的状态chargeStatus，记录此次退费日志
		// 操作库存
		DoubleWrap thisTimeReceivableAmt = new DoubleWrap();
		thisTimeReceivableAmt.setValue(0d);
		List<OperateItem> operateItems = Lists.newArrayList();
		// 更新订单中的单核项
		this.updateOrderItemListWhenRefund(refundRequest, thisTimeReceivableAmt, operateItems);
		// 更新hospitalOrder
		this.updateHospitalOrderWhenRefund(refundRequest);
		// 操作库存

		// 记录日志
		orderOperateRecordService.addRefundRecord(refundRequest.getOrderNo(), refundRequest.getRefundAmt(), thisTimeReceivableAmt.getValue(),
				operateItems);
		return HisResponse.getInstance();

	}

	private void updateHospitalOrderWhenRefund(RefundRequest req) {
		String orderNo = req.getOrderNo();
		Double refundAmt = req.getRefundAmt();
		HospitalOrder hospitalOrderParam = new HospitalOrder();
		hospitalOrderParam.setOrderNo(orderNo);

		hospitalOrderParam.setModifier(SessionUtils.getDoctorId());
		hospitalOrderParam.setLastRefundDate(new Date());
		hospitalOrderParam.setLastRefundId(SessionUtils.getDoctorId());

		// 取出状态或操作算出订单的chargeStatus
		HospitalOrder hospitalOrder = orderService.getHospitalOrder(orderNo);
		Integer oldStatus = hospitalOrder.getChargeStatus();
		// 状态
		Integer chargeStatus = ChargeStatus.addStatus(oldStatus, ChargeStatus.REFUND);
		hospitalOrderParam.setChargeStatus(chargeStatus);

		if (refundAmt != null) {
			Double oldRefundAmt = hospitalOrder.getRefundAmt();
			if (oldRefundAmt == null) {
				oldRefundAmt = 0D;
			}
			refundAmt = AmtUtils.add(oldRefundAmt, refundAmt);
			hospitalOrderParam.setRefundAmt(refundAmt);
		}
		// 更新版本
		hospitalOrderParam.setRecordVersion(System.currentTimeMillis());
		orderService.update(hospitalOrderParam);
	}

	/**
	 * 
	 * @description 附加费用退费
	 * @date 2016年3月12日
	 * @author lipeng
	 * @param orderAdditionAmtListAndItemIds
	 * @param operateItems
	 */
	private void updateOrderAdditionAmtsWhenRefund(OrderAdditionAmtListAndItemIds orderAdditionAmtListAndItemIds, DoubleWrap thisTimeReceivableAmt,
			List<OperateItem> operateItems) {
		if (orderAdditionAmtListAndItemIds != null) {
			Long orderAdditionAmtListId = orderAdditionAmtListAndItemIds.getOrderAdditionAmtListId();
			List<Long> orderAdditionAmtIds = orderAdditionAmtListAndItemIds.getOrderAdditionAmtIds();
			if (orderAdditionAmtListId != null && CollectionUtils.isNotEmpty(orderAdditionAmtIds)) {
				// 更新项，先查出退费的项
				List<OrderItemCharge> orderItemCharges = orderItemChargeService.getOrderItemChargesByItemIds(orderAdditionAmtIds);
				if (CollectionUtils.isNotEmpty(orderItemCharges)) {
					orderItemCharges = orderItemCharges.stream().filter(amt -> {
						// 如果项没有退费，才进行退费操作
							Integer oldChargeSatatus = amt.getChargeStatus();
							return !ChargeStatus.hasStatus(oldChargeSatatus, ChargeStatus.REFUND);
						}).map(amt -> amt).collect(Collectors.toList());
					Double thisTimeReceivableAmtValue = thisTimeReceivableAmt == null ? null : thisTimeReceivableAmt.getValue();
					for (OrderItemCharge orderItemCharge : orderItemCharges) {
						Double orderAdditionAmtReceivableAmt = orderItemCharge.getReceivableAmt();
						if (orderAdditionAmtReceivableAmt != null) {
							thisTimeReceivableAmtValue += orderAdditionAmtReceivableAmt;
						}
						// 添加至operateItems
						OperateItem operateItem = new OperateItem(OrderItemType.ADDITION_AMT.getType(), orderItemCharge.getItemId());
						operateItems.add(operateItem);
					}
					thisTimeReceivableAmt.setValue(thisTimeReceivableAmtValue);
					// 更新项的状态
					orderItemChargeService.addChargeStatusToOrderItemCharges(orderAdditionAmtIds, ChargeStatus.REFUND);
					// 更新项的退费员和退费时间
					orderItemChargeService.updateRefundOperatorAndDateByItemIds(orderAdditionAmtIds);
					// 更新单
					OrderItemList<OrderAdditionAmt> orderAdditionAmtList = orderItemListService.getOrderItemListById(orderAdditionAmtListId);
					Integer oldChargeStatus = orderAdditionAmtList.getChargeStatus();
					// 给单增加退费状态
					Integer chargeStatus = ChargeStatus.addStatus(oldChargeStatus, ChargeStatus.REFUND);
					orderItemListService.updateOrderItemListChargeSatus(orderAdditionAmtListId, chargeStatus);
				}
			}
		}
	}

	/**
	 * 
	 * @description 检查项退费
	 * @date 2016年3月12日
	 * @author lipeng
	 * @param orderAdditionAmtListAndItemIds
	 * @param operateItems
	 */
	private void updateOrderExamsWhenRefund(OrderExamListAndItemIds orderExamListAndItemIds, DoubleWrap thisTimeReceivableAmt,
			List<OperateItem> operateItems) {
		if (orderExamListAndItemIds != null) {
			Long orderExamListId = orderExamListAndItemIds.getOrderExamListId();
			List<Long> orderExamIds = orderExamListAndItemIds.getOrderExamIds();
			if (orderExamListId != null && CollectionUtils.isNotEmpty(orderExamIds)) {
				// 更新项，先查出来退费的项
				List<OrderItemCharge> orderItemCharges = orderItemChargeService.getOrderItemChargesByItemIds(orderExamIds);
				if (CollectionUtils.isNotEmpty(orderItemCharges)) {
					orderItemCharges = orderItemCharges.stream().filter(exam -> {
						// 如果项没有退费，才进行退费操作
							Integer oldChargeSatatus = exam.getChargeStatus();
							return !ChargeStatus.hasStatus(oldChargeSatatus, ChargeStatus.REFUND);
						}).map(amt -> amt).collect(Collectors.toList());
					Double thisTimeReceivableAmtValue = thisTimeReceivableAmt == null ? null : thisTimeReceivableAmt.getValue();
					for (OrderItemCharge orderItemCharge : orderItemCharges) {
						Double orderExamReceivableAmt = orderItemCharge.getReceivableAmt();
						if (orderExamReceivableAmt != null) {
							thisTimeReceivableAmtValue += orderExamReceivableAmt;
						}
						// 添加至operateItems
						OperateItem operateItem = new OperateItem(OrderItemType.EXAM.getType(), orderItemCharge.getItemId());
						operateItems.add(operateItem);
					}
					thisTimeReceivableAmt.setValue(thisTimeReceivableAmtValue);
					// 更新项的状态
					orderItemChargeService.addChargeStatusToOrderItemCharges(orderExamIds, ChargeStatus.REFUND);
					// 更新项的退费员和退费时间
					orderItemChargeService.updateRefundOperatorAndDateByItemIds(orderExamIds);
					// 更新单
					OrderItemList<OrderExam> orderExamList = orderItemListService.getOrderItemListById(orderExamListId);
					Integer oldChargeStatus = orderExamList.getChargeStatus();
					// 给单增加收费状态
					Integer chargeStatus = ChargeStatus.addStatus(oldChargeStatus, ChargeStatus.REFUND);
					orderItemListService.updateOrderItemListChargeSatus(orderExamListId, chargeStatus);
				}
			}
		}
	}

	/**
	 * 
	 * @description 药品退费
	 * @date 2016年3月12日
	 * @author lipeng
	 * @param prescriptionAndItemIdsList
	 * @param thisTimeReceivableAmt
	 * @param operateItems
	 */
	private void updateOrderDrugsWhenRefund(List<PrescriptionAndItemIds> prescriptionAndItemIdsList, DoubleWrap thisTimeReceivableAmt,
			List<OperateItem> operateItems) {
		if (CollectionUtils.isNotEmpty(prescriptionAndItemIdsList)) {
			for (PrescriptionAndItemIds prescriptionAndItemIds : prescriptionAndItemIdsList) {
				Long orderPrescriptionId = prescriptionAndItemIds.getOrderPrescriptionId();
				List<Long> orderDrugIds = prescriptionAndItemIds.getOrderDrugIds();
				if (orderPrescriptionId != null && CollectionUtils.isNotEmpty(orderDrugIds)) {
					// 更新项，先查出来退费的项
					List<OrderItemCharge> orderItemCharges = orderItemChargeService.getOrderItemChargesByItemIds(orderDrugIds);
					List<OrderDrug> orderDrugs = orderDrugMapper.selectByIds(orderDrugIds);
					if (CollectionUtils.isNotEmpty(orderItemCharges)) {
						orderItemCharges = orderItemCharges.stream().filter(orderDrug -> {
							// 如果项没有退费，才进行退费操作
								Integer oldChargeSatatus = orderDrug.getChargeStatus();
								return !ChargeStatus.hasStatus(oldChargeSatatus, ChargeStatus.REFUND);
							}).map(amt -> amt).collect(Collectors.toList());
						Double thisTimeReceivableAmtValue = thisTimeReceivableAmt == null ? null : thisTimeReceivableAmt.getValue();
						for (OrderItemCharge orderItemCharge : orderItemCharges) {
							Double orderDrugReceivableAmt = orderItemCharge.getReceivableAmt();
							if (orderDrugReceivableAmt != null) {
								thisTimeReceivableAmtValue += orderDrugReceivableAmt;
							}

							// 添加至operateItems
							OperateItem operateItem = new OperateItem(OrderItemType.DRUG.getType(), orderItemCharge.getItemId());
							operateItems.add(operateItem);
						}
						thisTimeReceivableAmt.setValue(thisTimeReceivableAmtValue);
						// 更新项的状态
						orderItemChargeService.addChargeStatusToOrderItemCharges(orderDrugIds, ChargeStatus.REFUND);
						// 更新项的退费员和退费时间
						orderItemChargeService.updateRefundOperatorAndDateByItemIds(orderDrugIds);
						// 更新单
						OrderItemList<OrderDrug> orderPrescription = orderItemListService.getOrderItemListById(orderPrescriptionId);
						Integer oldChargeStatus = orderPrescription.getChargeStatus();
						// 给单增加退费状态
						Integer chargeStatus = ChargeStatus.addStatus(oldChargeStatus, ChargeStatus.REFUND);
						orderItemListService.updateOrderItemListChargeSatus(orderPrescriptionId, chargeStatus);
						// 药品退费时返回库存
						orderDrugService.returnInventory(orderDrugs);
					}
				}

			}
		}
	}

	/**
	 * 减库存操作，调用文武接口
	 * @description
	 * @date 2016年6月16日
	 * @author lipeng
	 * @param orderDrugs
	 */
	@Deprecated
	private void returnInventory(List<OrderDrug> orderDrugs) {
		if (CollectionUtils.isEmpty(orderDrugs)) {
			return;
		}
		orderDrugs
				.stream()
				.filter(orderDrug -> orderDrug.getDrugId() != null && orderDrug.getDataSource() != null && orderDrug.getDataSource() == 0)
				.forEach(orderDrug -> {
					// 如果药物来自诊所的话，进行库存操作
						Double amount = null;
						HospitalOrder hospitalOrder = orderService.getHospitalOrder(orderDrug.getOrderNo());
						if (HospitalOrder.TYPE_PATIENT_RECORD == hospitalOrder.getOrderType()
								&& DrugTypeEnum.herbal.getDrugType() == orderDrug.getDrugType()) {
							// 如果来自病历并且药物时饮片类型的话，操作总克数
							amount = orderDrug.getTotalGram();
						} else {
							amount = orderDrug.getDosage() == null ? null : orderDrug.getDosage().doubleValue();
						}
						if (amount != null) {
							InventoryOperatePojo inventoryOperatePojo = new InventoryOperatePojo();
							inventoryOperatePojo.setDrugId(orderDrug.getDrugId());
							inventoryOperatePojo.setAmount(amount);
							inventoryOperatePojo.setHospitalId(SessionUtils.getHospitalId());
							inventoryOperatePojo.setUnit(orderDrug.getSaleUnit());
							drugInventoryService.returnInventory(inventoryOperatePojo);
						}
					});
	}

	/**
	 * 
	 * @description 更新OrderItemList
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param req
	 */
	private void updateOrderItemListWhenRefund(RefundRequest req, DoubleWrap thisTimeReceivableAmt, List<OperateItem> operateItems) {
		OrderAdditionAmtListAndItemIds orderAdditionAmtListAndItemIds = req.getOrderAdditionAmtList();
		updateOrderAdditionAmtsWhenRefund(orderAdditionAmtListAndItemIds, thisTimeReceivableAmt, operateItems);
		OrderExamListAndItemIds orderExamListAndItemIds = req.getOrderExamList();
		updateOrderExamsWhenRefund(orderExamListAndItemIds, thisTimeReceivableAmt, operateItems);
		List<PrescriptionAndItemIds> prescriptionAndItemIdsList = req.getOrderPrescriptions();
		updateOrderDrugsWhenRefund(prescriptionAndItemIdsList, thisTimeReceivableAmt, operateItems);
		SellDrugRecordAndItemIds sellDrugRecord = req.getSellDrugRecord();
		this.updateSellDrugRecordOrderDrugsWhenRefund(sellDrugRecord, thisTimeReceivableAmt, operateItems);

	}

	private void updateSellDrugRecordOrderDrugsWhenRefund(SellDrugRecordAndItemIds sellDrugRecord, DoubleWrap thisTimeReceivableAmt,
			List<OperateItem> operateItems) {
		if (sellDrugRecord == null) {
			return;
		}
		Long sellDrugRecordId = sellDrugRecord.getSellDrugRecordId();
		List<Long> orderDrugIds = sellDrugRecord.getOrderDrugIds();
		if (sellDrugRecordId != null && CollectionUtils.isNotEmpty(orderDrugIds)) {
			// 更新项，先查出来退费的项
			List<OrderItemCharge> orderItemCharges = orderItemChargeService.getOrderItemChargesByItemIds(orderDrugIds);
			List<OrderDrug> orderDrugs = orderDrugMapper.selectByIds(orderDrugIds);
			if (CollectionUtils.isNotEmpty(orderItemCharges)) {
				orderItemCharges = orderItemCharges.stream().filter(orderDrug -> {
					// 如果项没有退费，才进行退费操作
						Integer oldChargeSatatus = orderDrug.getChargeStatus();
						return !ChargeStatus.hasStatus(oldChargeSatatus, ChargeStatus.REFUND);
					}).map(amt -> amt).collect(Collectors.toList());
				Double thisTimeReceivableAmtValue = thisTimeReceivableAmt == null ? null : thisTimeReceivableAmt.getValue();
				for (OrderItemCharge orderItemCharge : orderItemCharges) {
					Double orderDrugReceivableAmt = orderItemCharge.getReceivableAmt();
					if (orderDrugReceivableAmt != null) {
						thisTimeReceivableAmtValue += orderDrugReceivableAmt;
					}

					// 添加至operateItems
					OperateItem operateItem = new OperateItem(OrderItemType.DRUG.getType(), orderItemCharge.getItemId());
					operateItems.add(operateItem);
				}
				thisTimeReceivableAmt.setValue(thisTimeReceivableAmtValue);
				// 更新项的状态
				orderItemChargeService.addChargeStatusToOrderItemCharges(orderDrugIds, ChargeStatus.REFUND);
				// 更新项的退费员和退费时间
				orderItemChargeService.updateRefundOperatorAndDateByItemIds(orderDrugIds);
				// 更新单
				OrderItemList<OrderDrug> orderSellDrugList = orderItemListService.getOrderItemListById(sellDrugRecordId);
				if (orderSellDrugList != null) {
					Integer oldChargeStatus = orderSellDrugList.getChargeStatus();
					// 给单增加退费状态
					Integer chargeStatus = ChargeStatus.addStatus(oldChargeStatus, ChargeStatus.REFUND);
					orderItemListService.updateOrderItemListChargeSatus(sellDrugRecordId, chargeStatus);
				}
				orderDrugService.returnInventory(orderDrugs);
			}
		}

	}

}
