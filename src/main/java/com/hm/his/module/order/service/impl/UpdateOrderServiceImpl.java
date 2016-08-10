package com.hm.his.module.order.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.dao.OrderItemListMapper;
import com.hm.his.module.order.model.AmtOperateType;
import com.hm.his.module.order.model.ChargeStatus;
import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderAdditionAmt;
import com.hm.his.module.order.model.OrderDrug;
import com.hm.his.module.order.model.OrderExam;
import com.hm.his.module.order.model.OrderItemList;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.order.service.ChargeStatusService;
import com.hm.his.module.order.service.OrderAdditionAmtService;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.order.service.OrderExamService;
import com.hm.his.module.order.service.OrderItemChargeService;
import com.hm.his.module.order.service.OrderItemListService;
import com.hm.his.module.order.service.UpdateOrderService;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.PatientExam;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月11日
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UpdateOrderServiceImpl implements UpdateOrderService {

	@Autowired
	OrderExamService orderExamService;
	@Autowired
	OrderAdditionAmtService orderAdditionAmtService;
	@Autowired
	OrderDrugService orderDrugService;
	@Autowired
	OrderItemChargeService orderItemChargeService;
	@Autowired
	ChargeStatusService chargeStatusService;
	@Autowired(required = false)
	HospitalOrderMapper hospitalOrderMapper;
	@Autowired(required = false)
	OrderItemListMapper orderItemListMapper;
	@Autowired
	OrderItemListService orderItemListService;

	// Logger logger = Logger.getLogger(this.getClass());
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.SAVE_ORDER_EXAM)
	@Override
	public void saveOrderExam(Long recordId, PatientExam patientExam) {
		// logger.info("保存检查项：病历id：" + recordId);
		if (recordId == null || patientExam == null) {
			return;
		}
		// 如果已经存在对应的检查项，则先删除
		this.deleteRecordItem(recordId, patientExam.getPatientExamId(), OrderItemListType.EXAM);
		OrderItemList orderItemList = orderExamService.selectOrderExamListByRecordId(recordId);
		if (orderItemList == null) {
			// 如果此检查项没有对应的单，那么直接调用创建单的方法
			List<PatientExam> patientExams = Lists.newArrayList();
			patientExams.add(patientExam);
			this.addOrderExamList(recordId, patientExams);
			return;
		} else {
			// 保存检查
			OrderExam orderExam = orderExamService.saveOrderExam(recordId, patientExam);
			Double receivableAmt = orderExam.getReceivableAmt();
			// 更新检查单状态
			chargeStatusService.updateOrderItemListChargeStatus(orderExam.getOrderItemListId());
			// 更新订单状态
			chargeStatusService.updateHospitalOrderChargeStatus(recordId);
			HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
			// 更新检查单应收金额
			this.updateOrderItemListReceivableAmt(receivableAmt, orderItemList, AmtOperateType.ADD);
			// 更新订单应收金额
			this.updateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
			// 更新订单中的病历版本号
			this.updateRecordVersion(recordId);
		}
	}


	/**
	 * 
	 * @description 更新病历版本
	 * @date 2016年3月23日
	 * @author lipeng
	 * @param orderNo
	 */
	private void updateRecordVersion(String orderNo) {
		HospitalOrder hospitalOrder = new HospitalOrder();
		hospitalOrder.setOrderNo(orderNo);
		hospitalOrder.setRecordVersion(System.currentTimeMillis());
		hospitalOrderMapper.updateRecordVersion(hospitalOrder);
	}

	/**
	 * 
	 * @description  更新病历版本
	 * @date 2016年3月23日
	 * @author lipeng
	 * @param recordId
	 */
	private void updateRecordVersion(Long recordId) {
		HospitalOrder hospitalOrder = new HospitalOrder();
		hospitalOrder.setPatientRecordId(recordId);
		hospitalOrder.setRecordVersion(System.currentTimeMillis());
		hospitalOrderMapper.updateRecordVersion(hospitalOrder);
	}

	/**
	 *  功能描述：首次保存病历时，批量新增 检查单里的检查项：新增新额检查项，计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @param
	 * @return
	 * @throws
	 * @author:  tangww
	 * @createDate   2016-07-29
	 *
	 */
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.ADD_ORDER_EXAM_LIST)
	@Override
	public void addOrderExamList(Long recordId, List<PatientExam> patientExams) {
		if (recordId == null || CollectionUtils.isEmpty(patientExams)) {
			return;
		}
		OrderItemList<OrderExam> orderItemList = orderExamService.saveOrderExams(recordId, patientExams);
		if (orderItemList == null) {
			return;
		}
		// 更新订单状态
		chargeStatusService.updateHospitalOrderChargeStatus(recordId);
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		// 更新订单应收金额
		Double receivableAmt = orderItemList.getReceivableAmt();
		this.updateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
		this.updateRecordVersion(recordId);
	}

	@Override
	public void firstAddOrderExamList(Long recordId, List<PatientExam> patientExams, HospitalOrder hospitalOrder) {
		if (recordId == null || CollectionUtils.isEmpty(patientExams)) {
			return;
		}
		OrderItemList<OrderExam> orderItemList = orderExamService.firstSaveOrderExams(recordId, patientExams,hospitalOrder);
		if (orderItemList == null) {
			return;
		}
		// 更新订单收费 状态  ---首次新增保存
		//chargeStatusService.updateHospitalOrderChargeStatus(recordId);
		// 更新订单应收金额
		Double receivableAmt = orderItemList.getReceivableAmt();
		this.firstUpdateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
//		this.updateRecordVersion(recordId);
	}

	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.SAVE_ORDER_PATENT_PRESCRIPTION)
	@Override
	public void saveOrderPatentPrescription(Long recordId, List<PatientDrug> patentPrescriptionList) {
		if (recordId == null || CollectionUtils.isEmpty(patentPrescriptionList)) {
			return;
		}
		// 如果已存在，先删除，再保存
		this.deleteRecordItem(recordId, patentPrescriptionList.get(0).getPrescription(), OrderItemListType.PATIENT_PRESCRIPTION);
		List<List<PatientDrug>> patentPrescriptionLists = Lists.newArrayList();
		patentPrescriptionLists.add(patentPrescriptionList);
		List<OrderItemList<OrderDrug>> orderItemLists = orderDrugService.createOrderDrugsByPatentPrescriptionList(recordId, patentPrescriptionLists);
		if (orderItemLists == null) {
			return;
		}
		// 更新订单状态
		chargeStatusService.updateHospitalOrderChargeStatus(recordId);
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		// 更新订单应收金额
		Double receivableAmt = null;
		for (OrderItemList orderItemList : orderItemLists) {
			receivableAmt = orderItemList.getReceivableAmt();
		}
		this.updateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
		this.updateRecordVersion(recordId);
	}

	@Override
	public void firstSaveOrderPatentPrescription(Long recordId, List<PatientDrug> patentPrescriptionList,HospitalOrder hospitalOrder) {
		if (recordId == null || CollectionUtils.isEmpty(patentPrescriptionList)) {
			return;
		}
		List<OrderItemList<OrderDrug>> orderItemLists = orderDrugService.firstCreateOrderDrugsByPatentPrescriptionList(hospitalOrder.getOrderNo(), patentPrescriptionList);
		if (orderItemLists == null) {
			return;
		}
		// 更新订单状态
		//chargeStatusService.updateHospitalOrderChargeStatus(recordId);
		// 更新订单应收金额
		Double receivableAmt = 0D;
		for (OrderItemList orderItemList : orderItemLists) {
			receivableAmt += orderItemList.getReceivableAmt();
		}
		this.firstUpdateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
		//this.updateRecordVersion(recordId);
	}

	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.SAVE_ORDER_CHINESE_PRESCRIPTION)
	@Override
	public void saveOrderChinesePrescription(Long recordId, PatientChineseDrug patientChineseDrug) {
		if (recordId == null || patientChineseDrug == null) {
			return;
		}
		// 如果已存在，先删除，再保存
		this.deleteRecordItem(recordId, patientChineseDrug.getPrescription(), OrderItemListType.CHINESE_PRESCRIPTION);
		List<PatientChineseDrug> chinesePrescriptionList = Lists.newArrayList();
		chinesePrescriptionList.add(patientChineseDrug);
		List<OrderItemList<OrderDrug>> orderItemLists = orderDrugService.createOrderDrugsByChinesePrescriptionList(recordId, chinesePrescriptionList);
		if (orderItemLists == null) {
			return;
		}
		// 更新订单状态
		chargeStatusService.updateHospitalOrderChargeStatus(recordId);
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		// 更新订单应收金额
		Double receivableAmt = null;
		for (OrderItemList orderItemList : orderItemLists) {
			receivableAmt = orderItemList.getReceivableAmt();
		}
		this.updateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
		this.updateRecordVersion(recordId);
	}

	@Override
	public void firstSaveOrderChinesePrescription(Long recordId, PatientChineseDrug patientChineseDrug, HospitalOrder hospitalOrder) {
		if (recordId == null || patientChineseDrug == null) {
			return;
		}
		List<PatientChineseDrug> chinesePrescriptionList = Lists.newArrayList();
		chinesePrescriptionList.add(patientChineseDrug);
		List<OrderItemList<OrderDrug>> orderItemLists = orderDrugService.firstCreateOrderDrugsByChinesePrescriptionList(hospitalOrder.getOrderNo(), chinesePrescriptionList);
		if (orderItemLists == null) {
			return;
		}
		// 更新订单状态
		//chargeStatusService.updateHospitalOrderChargeStatus(recordId);
		// 更新订单应收金额
		Double receivableAmt = 0D;
		for (OrderItemList orderItemList : orderItemLists) {
			receivableAmt += orderItemList.getReceivableAmt();
		}
		this.firstUpdateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
		//this.updateRecordVersion(recordId);
	}

	@Override
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.SAVE_ORDER_ADDITION_AMT)
	public void saveOrderAdditionAmt(Long recordId, PatientAdditional patientAdditional) {
		if (recordId == null || patientAdditional == null) {
			return;
		}
		// 如果已经存在对应的附加费用项，则先删除
		this.deleteRecordItem(recordId, patientAdditional.getAdditionalId(), OrderItemListType.ADDITION_AMT);
		// 如果不存在对应的附加费用单，先创建
		OrderItemList orderItemList = orderAdditionAmtService.selectOrderAdditionAmtListByRecordId(recordId);
		if (orderItemList == null) {
			List<PatientAdditional> patientAdditionals = Lists.newArrayList();
			patientAdditionals.add(patientAdditional);
			this.addOrderAdditionAmtList(recordId, patientAdditionals);
			return;
		} else {
			OrderAdditionAmt orderAdditionAmt = orderAdditionAmtService.saveOrderAdditionAmt(recordId, patientAdditional);
			Double receivableAmt = orderAdditionAmt.getReceivableAmt();
			// 更新附加费用单状态
			chargeStatusService.updateOrderItemListChargeStatus(orderAdditionAmt.getOrderItemListId());
			// 更新订单状态
			chargeStatusService.updateHospitalOrderChargeStatus(recordId);
			HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
			// 更附加费用查单应收金额
			this.updateOrderItemListReceivableAmt(receivableAmt, orderItemList, AmtOperateType.ADD);
			// 更新订单应收金额
			this.updateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
			this.updateRecordVersion(recordId);
		}
	}

	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.ADD_ORDER_ADDITION_AMT_LIST)
	@Override
	public void addOrderAdditionAmtList(Long recordId, List<PatientAdditional> patientAdditionals) {
		if (recordId == null || CollectionUtils.isEmpty(patientAdditionals)) {
			return;
		}
		// 添加附加费用单
		OrderItemList<OrderAdditionAmt> orderAdditionAmtList = orderAdditionAmtService.saveOrderAdditionAmts(recordId, patientAdditionals);
		if (orderAdditionAmtList == null) {
			return;
		}
		// 更新订单状态，添加待收费状态，应收金额修改，总应收金额修改 增加
		// 更新订单状态
		chargeStatusService.updateHospitalOrderChargeStatus(recordId);
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		// 更新订单应收金额
		Double receivableAmt = orderAdditionAmtList.getReceivableAmt();
		this.updateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
		this.updateRecordVersion(recordId);
	}

	@Override
	public void firstAddOrderAdditionAmtList(Long recordId, List<PatientAdditional> patientAdditionals, HospitalOrder hospitalOrder) {
		if (recordId == null || CollectionUtils.isEmpty(patientAdditionals)) {
			return;
		}
		// 添加附加费用单
		OrderItemList<OrderAdditionAmt> orderAdditionAmtList = orderAdditionAmtService.firstSaveOrderAdditionAmts(recordId, patientAdditionals,hospitalOrder);
		if (orderAdditionAmtList == null) {
			return;
		}
		// 更新订单状态，添加待收费状态，应收金额修改，总应收金额修改 增加
		// 更新订单状态
		//chargeStatusService.updateHospitalOrderChargeStatus(recordId);
		// 更新订单应收金额
		Double receivableAmt = orderAdditionAmtList.getReceivableAmt();
		this.firstUpdateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.ADD);
		//this.updateRecordVersion(recordId);
	}

	/**
	 *  删除检查或附加费用项或处方
	 */
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.DELETE_RECORD_ITEM)
	@Override
	public void deleteRecordItem(Long recordId, Long id, OrderItemListType type) {
		if (recordId == null || id == null || type == null) {
			return;
		}
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		if (hospitalOrder == null) {
			return;
		}
		String orderNo = hospitalOrder.getOrderNo();
		Double receivableAmt = null;
		if (type == OrderItemListType.ADDITION_AMT) {
			// 先查出来，拿到应收金额
			OrderAdditionAmt orderAdditionAmt = orderAdditionAmtService.getOrderAdditionAmtByRecordIdAndAdditionalId(recordId, id);
			if (orderAdditionAmt == null) {
				return;
			}
			Integer chargeSatus = orderAdditionAmt.getChargeStatus();
			if (ChargeStatus.hasStatus(chargeSatus, ChargeStatus.CHARGED) || ChargeStatus.hasStatus(chargeSatus, ChargeStatus.REFUND)) {
				// 如果该项目已经收费或退费了，抛异常
				throw new RuntimeException(orderAdditionAmt.getAmtName() + " 已经收费过了，不能修改或删除！");
			}
			Long orderItemListId = orderAdditionAmt.getOrderItemListId();
			// 删除
			orderAdditionAmtService.deleteOrderAdditionAmt(orderAdditionAmt.getId());
			// 更新当状态
			chargeStatusService.updateOrderItemListChargeStatus(orderItemListId);
			receivableAmt = orderAdditionAmt.getReceivableAmt();
			// 更新单额应收金额
			OrderItemList orderItemList = orderItemListMapper.selectById(orderItemListId);
			updateOrderItemListReceivableAmt(receivableAmt, orderItemList, AmtOperateType.MINUS);
			// 删除了项，如果对应单也没有了，要删除么？如果对应订单也没有了，要删除么？
		} else if (type == OrderItemListType.EXAM) {
			OrderExam orderExam = orderExamService.getOrderExamByRecordIdAndPatientExamId(recordId, id);
			if (orderExam == null) {
				return;
			}
			Integer chargeSatus = orderExam.getChargeStatus();
			if (ChargeStatus.hasStatus(chargeSatus, ChargeStatus.CHARGED) || ChargeStatus.hasStatus(chargeSatus, ChargeStatus.REFUND)) {
				// 如果该项目已经收费或退费了，抛异常
				throw new RuntimeException(orderExam.getExamName() + " 已经收费过了，不能删除！");
			}
			Long orderItemListId = orderExam.getOrderItemListId();
			orderExamService.deleteOrderExam(orderExam.getId());
			// 更新当状态
			chargeStatusService.updateOrderItemListChargeStatus(orderItemListId);
			receivableAmt = orderExam.getReceivableAmt();
			// 更新单应收金额
			OrderItemList orderItemList = orderItemListMapper.selectById(orderItemListId);
			updateOrderItemListReceivableAmt(receivableAmt, orderItemList, AmtOperateType.MINUS);
		} else if (type == OrderItemListType.PATIENT_PRESCRIPTION || type == OrderItemListType.CHINESE_PRESCRIPTION) {
			OrderItemList orderPrescription = orderItemListMapper.selectByOrderNoAndForeignId(orderNo, id);
			if (orderPrescription == null) {
				return;
			}
			Integer chargeSatus = orderPrescription.getChargeStatus();
			if (ChargeStatus.hasStatus(chargeSatus, ChargeStatus.CHARGED) || ChargeStatus.hasStatus(chargeSatus, ChargeStatus.REFUND)) {
				// 如果该项目已经收费或退费了，抛异常
				throw new RuntimeException("该处方已经收费过了，不能删除！");
			}
			// 删除
			orderItemListService.deleteOrderItemList(orderPrescription);
			receivableAmt = orderPrescription.getReceivableAmt();
		}

		// 更新订单总应收金额和剩余应收金额
		updateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.MINUS);
		// 更新订单状态
		chargeStatusService.updateHospitalOrderChargeStatus(orderNo);
		this.updateRecordVersion(recordId);
	}

	private void updateOrderItemListReceivableAmt(Double amt, OrderItemList orderItemList, AmtOperateType amtOperateType) {
		if (orderItemList == null || amt == null || amt == 0 || amtOperateType == null) {
			return;
		}
		Double oldItemListReceivableAmt = orderItemList.getReceivableAmt();
		if (oldItemListReceivableAmt != null) {
			OrderItemList orderItemListParam = new OrderItemList();
			orderItemListParam.setId(orderItemList.getId());
			Double receivableAmt = null;
			if (amtOperateType == AmtOperateType.ADD) {
				// receivableAmt = oldItemListReceivableAmt + amt;
				receivableAmt = AmtUtils.add(oldItemListReceivableAmt, amt);
			} else if (amtOperateType == AmtOperateType.MINUS) {
				// receivableAmt = oldItemListReceivableAmt - amt;
				receivableAmt = AmtUtils.subtract(oldItemListReceivableAmt, amt);
			}
			orderItemListParam.setReceivableAmt(receivableAmt);
			orderItemListParam.setModifier(SessionUtils.getDoctorId());
			orderItemListMapper.update(orderItemListParam);
		}
	}

	private void firstUpdateHospitalOrderReceivableAmts(HospitalOrder hospitalOrder, Double amt, AmtOperateType amtOperateType) {
		if (hospitalOrder == null || amt == null || amt == 0 || amtOperateType == null) {
			return;
		}
		Double oldOrderTotalReceivableAmt = hospitalOrder.getTotalReceivableAmt();
		Double oldOrderReceivableAmt = hospitalOrder.getReceivableAmt();
		HospitalOrder hospitalOrderParam = new HospitalOrder();
		hospitalOrderParam.setOrderNo(hospitalOrder.getOrderNo());
		Double receivableAmt = null;
		if (oldOrderReceivableAmt != null) {
			if (amtOperateType == AmtOperateType.ADD) {
				// receivableAmt = oldOrderReceivableAmt + amt;
				receivableAmt = AmtUtils.add(oldOrderReceivableAmt, amt);
			} else if (amtOperateType == AmtOperateType.MINUS) {
				// receivableAmt = oldOrderReceivableAmt - amt;
				receivableAmt = AmtUtils.subtract(oldOrderReceivableAmt, amt);
			}
		}
		hospitalOrderParam.setReceivableAmt(receivableAmt);
		hospitalOrder.setReceivableAmt(receivableAmt);
		Double totalReceivableAmt = null;

		if (oldOrderTotalReceivableAmt != null) {
			if (amtOperateType == AmtOperateType.ADD) {
				totalReceivableAmt = AmtUtils.add(oldOrderTotalReceivableAmt, amt);
			} else if (amtOperateType == AmtOperateType.MINUS) {
				totalReceivableAmt = AmtUtils.subtract(oldOrderTotalReceivableAmt, amt);
			}
		}
		hospitalOrderParam.setTotalReceivableAmt(totalReceivableAmt);
		hospitalOrder.setTotalReceivableAmt(totalReceivableAmt);
		hospitalOrderParam.setModifier(SessionUtils.getDoctorId());
		hospitalOrderMapper.update(hospitalOrderParam);
	}


	private void updateHospitalOrderReceivableAmts(HospitalOrder hospitalOrder, Double amt, AmtOperateType amtOperateType) {
		if (hospitalOrder == null || amt == null || amt == 0 || amtOperateType == null) {
			return;
		}
		Double oldOrderTotalReceivableAmt = hospitalOrder.getTotalReceivableAmt();
		Double oldOrderReceivableAmt = hospitalOrder.getReceivableAmt();
		HospitalOrder hospitalOrderParam = new HospitalOrder();
		hospitalOrderParam.setOrderNo(hospitalOrder.getOrderNo());
		Double receivableAmt = null;
		if (oldOrderReceivableAmt != null) {
			if (amtOperateType == AmtOperateType.ADD) {
				// receivableAmt = oldOrderReceivableAmt + amt;
				receivableAmt = AmtUtils.add(oldOrderReceivableAmt, amt);
			} else if (amtOperateType == AmtOperateType.MINUS) {
				// receivableAmt = oldOrderReceivableAmt - amt;
				receivableAmt = AmtUtils.subtract(oldOrderReceivableAmt, amt);
			}
		}
		hospitalOrderParam.setReceivableAmt(receivableAmt);
		Double totalReceivableAmt = null;

		if (oldOrderTotalReceivableAmt != null) {
			if (amtOperateType == AmtOperateType.ADD) {
				totalReceivableAmt = AmtUtils.add(oldOrderTotalReceivableAmt, amt);
			} else if (amtOperateType == AmtOperateType.MINUS) {
				totalReceivableAmt = AmtUtils.subtract(oldOrderTotalReceivableAmt, amt);
			}
		}
		hospitalOrderParam.setTotalReceivableAmt(totalReceivableAmt);
		hospitalOrderParam.setModifier(SessionUtils.getDoctorId());
		hospitalOrderMapper.update(hospitalOrderParam);
	}

	/**
	 * 删除病历下的全部检查或附加费用项或处方
	 */
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.DELETE_ALL_RECORD_ITEMS)
	@Override
	public void deleteAllRecordItems(Long recordId, OrderItemListType type) {
		if (recordId == null || type == null) {
			return;
		}
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByRecordId(recordId);
		if (hospitalOrder == null) {
			return;
		}
		String orderNo = hospitalOrder.getOrderNo();
		Double receivableAmt = 0D;
		List<OrderItemList> orderItemLists = orderItemListMapper.selectByRecordIdAndType(recordId, type.getType());
		if (CollectionUtils.isEmpty(orderItemLists)) {
			return;
		}
		for (OrderItemList orderItemList : orderItemLists) {
			Integer chargeSatus = orderItemList.getChargeStatus();
			if (ChargeStatus.hasStatus(chargeSatus, ChargeStatus.CHARGED) || ChargeStatus.hasStatus(chargeSatus, ChargeStatus.REFUND)) {
				// 如果该项目已经收费或退费了，抛异常
				throw new RuntimeException("有项目已经收费过了，不能删除！");
			}
			Double itemListReceivableAmt = orderItemList.getReceivableAmt();
			if (itemListReceivableAmt != null) {
				receivableAmt = AmtUtils.add(receivableAmt, itemListReceivableAmt);
			}
			orderItemListService.deleteOrderItemList(orderItemList);
		}
		// 更新订单状态
		chargeStatusService.updateHospitalOrderChargeStatus(orderNo);
		// 更新订单总应收金额和剩余应收金额
		updateHospitalOrderReceivableAmts(hospitalOrder, receivableAmt, AmtOperateType.MINUS);
		this.updateRecordVersion(recordId);
	}
}
