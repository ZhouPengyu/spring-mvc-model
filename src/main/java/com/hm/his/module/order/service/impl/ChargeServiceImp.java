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
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.order.model.OrderItemCharge;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemType;
import com.hm.his.module.order.model.OrderOperateRecord.OperateItem;
import com.hm.his.module.order.pojo.ChargeRequest;
import com.hm.his.module.order.pojo.OrderErrorMessage;
import com.hm.his.module.order.service.ChargeService;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.order.service.OrderItemChargeService;
import com.hm.his.module.order.service.OrderItemListService;
import com.hm.his.module.order.service.OrderOperateRecordService;
import com.hm.his.module.order.service.OrderService;

/**
 * 
 * @description 收费服务
 * @author lipeng
 * @date 2016年3月11日
 */
@Service
public class ChargeServiceImp implements ChargeService {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderOperateRecordService orderOperateRecordService;
	@Autowired
	OrderItemListService orderItemListService;
	@Autowired
	OrderItemChargeService orderItemChargeService;
	@Autowired
	OrderDrugService orderDrugService;
	@Autowired
	DrugInventoryService drugInventoryService;

	/**
	 * 收费
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.CHARGE)
	public HisResponse charge(ChargeRequest req) throws RuntimeException {
		// 收费时，先根据病历版本号判断病历是否有更改，如果有更改则提示前端刷新待收费页面
		Long recordVersion = req.getRecordVersion();
		if (recordVersion == null) {
			HisResponse res = new HisResponse();
			res.setErrorCode(400);
			res.setErrorMessage("请求参数错误：recordVersion为空");
			return res;
		}
		Long currentRecordVersion = orderService.getRecordVersion(req.getOrderNo());
		if (currentRecordVersion != null && recordVersion != currentRecordVersion.longValue()) {
			HisResponse res = new HisResponse();
			res.setErrorCode(OrderErrorMessage.RecordChangedOrCharged.getCode());
			res.setErrorMessage(OrderErrorMessage.RecordChangedOrCharged.getMsg());
			return res;
		}
		// 收费之前，更新版本号，避免重复提交
		orderService.updateRecordVersion(req.getOrderNo(), System.currentTimeMillis());
		DoubleWrap thisTimeReceivableAmt = new DoubleWrap();
		thisTimeReceivableAmt.setValue(0d);
		List<OperateItem> operateItems = Lists.newArrayList();
		// 更新订单中的单核项
		this.updateOrderItemListWhenCharge(req, thisTimeReceivableAmt, operateItems);
		// 更新hospitalOrder
		this.updateHospitalOrderWhenCharge(req, thisTimeReceivableAmt);
		// 操作库存
		// 添加日志
		orderOperateRecordService.addChargeRecord(req.getOrderNo(), req.getActualAmt(), thisTimeReceivableAmt.getValue(), operateItems, req.getPayAmount());
		return HisResponse.getInstance();
	}

	/**
	 * 
	 * @description 更新HospitalOrder
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param req
	 */
	private void updateHospitalOrderWhenCharge(ChargeRequest req, DoubleWrap thisTimeReceivableAmt) {
		String orderNo = req.getOrderNo();
		Double actualAmt = req.getActualAmt();
		Double discount = req.getDiscount();
		Integer payMode = req.getPayMode();
		HospitalOrder hospitalOrderParam = new HospitalOrder();
		hospitalOrderParam.setOrderNo(orderNo);

		hospitalOrderParam.setModifier(SessionUtils.getDoctorId());
		hospitalOrderParam.setLastChargeDate(new Date());
		hospitalOrderParam.setLastChargeId(SessionUtils.getDoctorId());

		// 取出状态或操作算出订单的chargeStatus
		HospitalOrder hospitalOrder = orderService.getHospitalOrder(orderNo);
		Integer oldStatus = hospitalOrder.getChargeStatus();
		// 状态
		List<OrderItemList> orderItemLists = orderItemListService.getOrderItemListsByOrderNo(orderNo);
		Integer chargeStatus = ChargeStatus.addStatus(oldStatus, ChargeStatus.CHARGED);
		if (CollectionUtils.isNotEmpty(orderItemLists)) {
			List<Integer> status = orderItemLists.stream().map(itemList -> itemList.getChargeStatus()).collect(Collectors.toList());
			chargeStatus = ChargeStatus.removeToChargedStatusWhenAllCharged(chargeStatus, status);
		}
		hospitalOrderParam.setChargeStatus(chargeStatus);

		// 剩余应收金额 等于 总应收金额减去这次的金额
		Double receivableAmt = thisTimeReceivableAmt.getValue();
		Double oldReceivableAmt = hospitalOrder.getReceivableAmt();
		if (oldReceivableAmt != null) {
			// receivableAmt = totalReceivableAmt - receivableAmt;
			receivableAmt = AmtUtils.subtract(oldReceivableAmt, receivableAmt);
		}
		if (actualAmt != null) {
			Double oldActualAmt = hospitalOrder.getActualAmt();
			if (oldActualAmt == null) {
				oldActualAmt = 0D;
			}
			actualAmt = AmtUtils.add(oldActualAmt, actualAmt);
			hospitalOrderParam.setActualAmt(actualAmt);
		}
		hospitalOrderParam.setPayMode(payMode);
		discount = AmtUtils.add(discount, null == hospitalOrder.getDiscount() ? 0D : hospitalOrder.getDiscount());
		hospitalOrderParam.setDiscount(discount);
		hospitalOrderParam.setReceivableAmt(receivableAmt);
		// 更新版本
		hospitalOrderParam.setRecordVersion(System.currentTimeMillis());
		orderService.update(hospitalOrderParam);
	}

	/**
	 * 
	 * @description 更新OrderItemList
	 * @date 2016年3月11日
	 * @author lipeng
	 * @param req
	 */
	private void updateOrderItemListWhenCharge(ChargeRequest req, DoubleWrap thisTimeReceivableAmt, List<OperateItem> operateItems) {
		Long orderAdditionAmtListId = req.getOrderAdditionAmtListId();
		updateOrderAdditionAmtsWhenCharge(orderAdditionAmtListId, thisTimeReceivableAmt, operateItems);
		Long orderExamListId = req.getOrderExamListId();
		updateOrderExamsWhenCharge(orderExamListId, thisTimeReceivableAmt, operateItems);
		List<Long> orderPrescriptionIds = req.getOrderPrescriptionIds();
		updateOrderDrugsWhenCharge(orderPrescriptionIds, thisTimeReceivableAmt, operateItems);
	}

	private void updateOrderDrugsWhenCharge(List<Long> orderPrescriptionIds, DoubleWrap thisTimeReceivableAmt, List<OperateItem> operateItems) {
		if (CollectionUtils.isNotEmpty(orderPrescriptionIds)) {
			for (Long orderPrescriptionId : orderPrescriptionIds) {
				// 更新项，先查出来待收费的项
				List<OrderDrug> orderDrugs = orderDrugService.getOrderDrugsByOrderItemListIdAndChargeStatus(orderPrescriptionId,
						ChargeStatus.TOCHARGE);
				List<OrderItemCharge> orderItemCharges = orderItemChargeService.getOrderItemChargesByItemListIdAndChargeStatus(orderPrescriptionId,
						ChargeStatus.TOCHARGE);
				if (CollectionUtils.isNotEmpty(orderItemCharges)) {
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
					orderItemChargeService.updateOrderItemChargeStatusByItemListId(orderPrescriptionId, ChargeStatus.CHARGED);
					// 更新项的收费员和收费时间
					orderItemChargeService.updateChargeOperatorAndDateByItemListId(orderPrescriptionId);
					// 更新单
					OrderItemList<OrderDrug> orderPrescription = orderItemListService.getOrderItemListById(orderPrescriptionId);
					Integer oldChargeStatus = orderPrescription.getChargeStatus();
					// 给单增加收费状态
					Integer chargeStatus = ChargeStatus.addStatus(oldChargeStatus, ChargeStatus.CHARGED);
					// 移除单的待收费状态，因为每次收费单中的全部都会收费，所以单就不存在待收费状态了
					chargeStatus = ChargeStatus.removeStatus(chargeStatus, ChargeStatus.TOCHARGE);
					orderItemListService.updateOrderItemListChargeSatus(orderPrescriptionId, chargeStatus);
				}
				// 减库存操作
				orderDrugService.cutInventory(orderDrugs);
			}
		}
	}

	/**
	 * 
	 * @description 减库存
	 * @date 2016年3月15日
	 * @author lipeng
	 * @param orderDrugs
	 */
	@Deprecated
	private void cutInventory(List<OrderDrug> orderDrugs) {
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
							drugInventoryService.cutInventory(inventoryOperatePojo);
						}
					});
	}

	private void updateOrderExamsWhenCharge(Long orderExamListId, DoubleWrap thisTimeReceivableAmt, List<OperateItem> operateItems) {
		if (orderExamListId != null) {
			List<OrderItemCharge> orderItemCharges = orderItemChargeService.getOrderItemChargesByItemListIdAndChargeStatus(orderExamListId,
					ChargeStatus.TOCHARGE);
			// 更新项，先查出来待收费的项
			if (CollectionUtils.isNotEmpty(orderItemCharges)) {
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
				orderItemChargeService.updateOrderItemChargeStatus(orderItemCharges, ChargeStatus.CHARGED);
				// 更新项的收费员和收费时间
				orderItemChargeService.updateChargeOperatorAndDateByItemListId(orderExamListId);
				// 更新单
				OrderItemList<OrderExam> orderExamList = orderItemListService.getOrderItemListById(orderExamListId);
				Integer oldChargeStatus = orderExamList.getChargeStatus();
				// 给单增加收费状态
				Integer chargeStatus = ChargeStatus.addStatus(oldChargeStatus, ChargeStatus.CHARGED);
				// 移除单的待收费状态，因为每次收费单中的全部都会收费，所以单就不存在待收费状态了
				chargeStatus = ChargeStatus.removeStatus(chargeStatus, ChargeStatus.TOCHARGE);
				orderItemListService.updateOrderItemListChargeSatus(orderExamListId, chargeStatus);
			}
		}
	}

	private void updateOrderAdditionAmtsWhenCharge(Long orderAdditionAmtListId, DoubleWrap thisTimeReceivableAmt, List<OperateItem> operateItems) {
		// 附加费用收费
		if (orderAdditionAmtListId != null) {
			// 更新项，先查出来待收费的项
			List<OrderItemCharge> orderItemCharges = orderItemChargeService.getOrderItemChargesByItemListIdAndChargeStatus(orderAdditionAmtListId,
					ChargeStatus.TOCHARGE);
			if (CollectionUtils.isNotEmpty(orderItemCharges)) {
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
				orderItemChargeService.updateOrderItemChargeStatus(orderItemCharges, ChargeStatus.CHARGED);
				// 更新项的收费员和收费时间
				orderItemChargeService.updateChargeOperatorAndDateByItemListId(orderAdditionAmtListId);
				// 更新单
				OrderItemList<OrderAdditionAmt> orderAdditionAmtList = orderItemListService.getOrderItemListById(orderAdditionAmtListId);
				// OrderItemList<OrderAdditionAmt>();
				// select itemList where listId=1
				Integer oldChargeStatus = orderAdditionAmtList.getChargeStatus();
				// 给单增加收费状态
				Integer chargeStatus = ChargeStatus.addStatus(oldChargeStatus, ChargeStatus.CHARGED);
				// 移除单的待收费状态，因为每次收费单中的全部都会收费，所以单就不存在待收费状态了
				chargeStatus = ChargeStatus.removeStatus(chargeStatus, ChargeStatus.TOCHARGE);
				orderItemListService.updateOrderItemListChargeSatus(orderAdditionAmtListId, chargeStatus);
			}
		}
	}

	public static void main(String[] args) {
		DoubleWrap dw = new DoubleWrap();
		Double d = 1d;
		dw.setValue(d);
		add(dw);
		System.out.println(dw.getValue());
		Integer ds = null;
		System.out.println(ds == 0);
	}

	public static void add(DoubleWrap dw) {
		Double d = dw.getValue();
		if (d != null) {
			dw.setValue(d + 100);
		}

	}
}
