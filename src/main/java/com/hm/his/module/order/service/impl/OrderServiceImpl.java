package com.hm.his.module.order.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.hm.his.framework.utils.LangUtils;
import com.hm.his.module.order.model.*;
import com.hm.his.module.outpatient.model.*;
import com.hm.his.module.outpatient.service.PatientDrugService;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.utils.AmtUtils;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.order.dao.HospitalOrderMapper;
import com.hm.his.module.order.dao.OrderAdditionAmtMapper;
import com.hm.his.module.order.dao.OrderDrugMapper;
import com.hm.his.module.order.dao.OrderExamMapper;
import com.hm.his.module.order.dao.OrderItemChargeMapper;
import com.hm.his.module.order.dao.OrderItemListMapper;
import com.hm.his.module.order.pojo.GetOrderListParam;
import com.hm.his.module.order.pojo.GetOrdersRequest;
import com.hm.his.module.order.pojo.HospitalOrderDetailPojo;
import com.hm.his.module.order.pojo.HospitalOrderPojo;
import com.hm.his.module.order.pojo.HospitalOrderPojoList;
import com.hm.his.module.order.pojo.OrderAdditionAmtListPojo;
import com.hm.his.module.order.pojo.OrderExamListPojo;
import com.hm.his.module.order.pojo.OrderItemChargeReceivableAmtPojo;
import com.hm.his.module.order.pojo.OrderPrescriptionPojo;
import com.hm.his.module.order.pojo.SellDrugRecordPojo;
import com.hm.his.module.order.service.OrderAdditionAmtService;
import com.hm.his.module.order.service.OrderDrugService;
import com.hm.his.module.order.service.OrderExamService;
import com.hm.his.module.order.service.OrderItemListService;
import com.hm.his.module.order.service.OrderService;
import com.hm.his.module.order.service.SellDrugRecordService;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.outpatient.service.PatientDiagnosisService;
import com.hm.his.module.outpatient.service.PatientInquiryService;
import com.hm.his.module.outpatient.service.PatientService;
import com.hm.his.module.user.model.Doctor;
import com.hm.his.module.user.service.DoctorService;

/**
 * 
 * @description
 * @author lipeng
 * @date 2016年3月2日
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired(required = false)
	HospitalOrderMapper hospitalOrderMapper;
	@Autowired(required = false)
	OrderItemListMapper orderItemListMapper;
	@Autowired(required = false)
	OrderExamMapper orderExamMapper;
	@Autowired(required = false)
	OrderDrugMapper orderDrugMapper;
	@Autowired(required = false)
	OrderAdditionAmtMapper orderAdditionAmtMapper;
	@Autowired
	OrderExamService orderExamService;
	@Autowired
	OrderDrugService orderDrugService;
	@Autowired
	OrderAdditionAmtService orderAdditionAmtService;
	@Autowired
	PatientService patientService;
	@Autowired
	PatientInquiryService patientInquiryService;
	@Autowired
	DoctorService doctorService;
	@Autowired
	SellDrugRecordService sellDrugRecordService;
	@Autowired
	OrderItemListService orderItemListService;
	@Autowired
	PatientDiagnosisService patientDiagnosisService;
	@Autowired(required = false)
	OrderItemChargeMapper orderItemChargeMapper;
	@Autowired
	PatientDrugService patientDrugService;

	@Override
	public HospitalOrderPojoList getToChargeOrders(GetOrdersRequest req) {
		HospitalOrderPojoList re = new HospitalOrderPojoList();
		GetOrderListParam getOrderListParam = GetOrderListParam.get(req, ChargeStatus.TOCHARGE,
				GetOrderListParam.OrderByCreateDate);
		List<HospitalOrder> hospitalOrders = this.getHospitalOrders(getOrderListParam);
		List<HospitalOrderPojo> orders = getHospitalOrderPojosByHospitalOrders(hospitalOrders, ChargeStatus.TOCHARGE);
		re.setOrders(orders);
		int count = this.getHospitalOrderCount(getOrderListParam);
		req.setTotalCount(count);
		re.setTotalPage(req.getTotalPage());
		return re;
	}

	@Override
	public HospitalOrderPojoList getChargedOrders(GetOrdersRequest req) {
		HospitalOrderPojoList re = new HospitalOrderPojoList();
		GetOrderListParam getOrderListParam = GetOrderListParam.get(req, ChargeStatus.CHARGED,
				GetOrderListParam.OrderByChargeDate);
		List<HospitalOrder> hospitalOrders = this.getHospitalOrders(getOrderListParam);
		List<HospitalOrderPojo> orders = getHospitalOrderPojosByHospitalOrders(hospitalOrders, ChargeStatus.CHARGED);
		re.setOrders(orders);
		int count = this.getHospitalOrderCount(getOrderListParam);
		req.setTotalCount(count);
		re.setTotalPage(req.getTotalPage());
		return re;
	}

	@Override
	public HospitalOrderPojoList getRefundedOrders(GetOrdersRequest req) {
		HospitalOrderPojoList re = new HospitalOrderPojoList();
		GetOrderListParam getOrderListParam = GetOrderListParam.get(req, ChargeStatus.REFUND,
				GetOrderListParam.OrderByRefundate);
		List<HospitalOrder> hospitalOrders = this.getHospitalOrders(getOrderListParam);
		List<HospitalOrderPojo> orders = getHospitalOrderPojosByHospitalOrders(hospitalOrders, ChargeStatus.REFUND);
		re.setOrders(orders);
		int count = this.getHospitalOrderCount(getOrderListParam);
		req.setTotalCount(count);
		re.setTotalPage(req.getTotalPage());
		return re;
	}

	private List<HospitalOrderPojo> getHospitalOrderPojosByHospitalOrders(List<HospitalOrder> hospitalOrders,
			Integer includeStatus) {
		if (CollectionUtils.isEmpty(hospitalOrders)) {
			return null;
		}
		List<HospitalOrderPojo> orders = hospitalOrders.stream().map(hospitalOrder -> {
			return this.getHospitalOrderPojoByHospitalOrder(includeStatus, hospitalOrder);
		}).collect(Collectors.toList());
		return orders;
	}

	private HospitalOrderDetailPojo getHospitalOrderPojoByHospitalOrder(Integer chargeStatus,
			HospitalOrder hospitalOrder) {
		HospitalOrderDetailPojo order = new HospitalOrderDetailPojo();
		// 订单基本信息
		order.setOrderNo(hospitalOrder.getOrderNo());
		order.setRecordVersion(hospitalOrder.getRecordVersion());
		order.setPatientName(hospitalOrder.getPatientName());
		Double receivableAmt = hospitalOrder.getReceivableAmt();
		Double totalReceivableAmt = hospitalOrder.getTotalReceivableAmt();
		Long doctorId = null;
		if (hospitalOrder.getOrderType() == HospitalOrder.TYPE_PATIENT_RECORD) {
			// 订单来自病历
			// 病人信息
			Patient p = patientService.getPatientById(hospitalOrder.getPatientId());
			if (p != null) {
				order.setAge(p.getAge());
				order.setAgeType(p.getAgeType());
				if (p.getGender() != null) {
					order.setGender(p.getGender() == 1 ? "男" : "女");
				}
			}
			// 病历信息
			PatientInquiry patientInquiry = patientInquiryService
					.getInquiryByRecordId(hospitalOrder.getPatientRecordId());
			try {
				if (patientInquiry != null) {
					String diagnosis = null;
					List<PatientDiagnosis> patientDiagnosisList = patientDiagnosisService.getDiagnosisByRecordId(hospitalOrder.getPatientRecordId());
					if (CollectionUtils.isNotEmpty(patientDiagnosisList)) {
						List<String> diagnosisNames = patientDiagnosisList.stream().map(patientDiagnosis -> {
							return patientDiagnosis.getDiseaseName();
						}).collect(Collectors.toList());
						diagnosis = StringUtils.join(diagnosisNames, "、");
					}
					order.setDiagnosis(diagnosis);
					doctorId = patientInquiry.getDoctorId();
					
						order.setDiagnosisDate(DateUtils.parseDate(patientInquiry.getCreateDate(), "yyyy-MM-dd"));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (hospitalOrder.getOrderType() == HospitalOrder.TYPE_SELL_DRUG) {
			// 订单来自直接售药
			SellDrugRecord sellDrugRecord = sellDrugRecordService
					.getSellDrugRecordById(hospitalOrder.getSellDrugRecordId());
			if (sellDrugRecord != null) {
				order.setAge(sellDrugRecord.getAge() == null ? null : sellDrugRecord.getAge());
				order.setAgeType(sellDrugRecord.getAgeType());
				order.setGender(sellDrugRecord.getGenderDesc());
				doctorId = sellDrugRecord.getCreater();
			}
		}
		Doctor doctor = doctorService.getDoctorById(doctorId);
		order.setDoctorName(doctor == null ? null : doctor.getRealName());
		Long operatorId = null;
		if (chargeStatus == ChargeStatus.TOCHARGE) {
			// 待收费
			order.setOperateDate(hospitalOrder.getCreateDate());
			order.setReceivableAmt(receivableAmt);
		} else if (chargeStatus == ChargeStatus.CHARGED) {
			// 已收费
			if (totalReceivableAmt != null || receivableAmt != null) {
				Double ReceivableAmt = totalReceivableAmt - receivableAmt;
				order.setReceivableAmt(ReceivableAmt);
			}
			order.setOperateDate(hospitalOrder.getLastChargeDate());
			order.setActualAmt(hospitalOrder.getActualAmt());
			order.setChargeDate(hospitalOrder.getLastChargeDate());
			operatorId = hospitalOrder.getLastChargeId();
		} else if (chargeStatus == ChargeStatus.REFUND) {
			// 已退费
			order.setOperateDate(hospitalOrder.getLastRefundDate());
			order.setRefundAmt(hospitalOrder.getRefundAmt());
			order.setChargeDate(hospitalOrder.getLastChargeDate());
			operatorId = hospitalOrder.getLastRefundId();
		}
		if (null != hospitalOrder.getDiscount()) {
			order.setDiscount(hospitalOrder.getDiscount());
		}
		if (null != hospitalOrder.getPayMode()) {
			order.setPayModeName(PayModeEnum.getPayModeNameByMode(hospitalOrder.getPayMode()));
			order.setPayMode(hospitalOrder.getPayMode());
		}
		// 操作人
		Doctor operator = doctorService.getDoctorById(operatorId);
		order.setOperatorName(operator == null ? null : operator.getRealName());
		// 收费人
		Long chargeId = hospitalOrder.getLastChargeId();
		Doctor chargeDoctor = doctorService.getDoctorById(chargeId);
		order.setChargeName(chargeDoctor == null ? null : chargeDoctor.getRealName());
		return order;
	}

	@Override
	public HospitalOrderDetailPojo getToChargeOrderInfo(GetOrdersRequest req) {
		if (req == null || StringUtils.isEmpty(req.getOrderNo())) {
			return null;
		}
		String orderNo = req.getOrderNo();
		HospitalOrder hospitalOrder = this.getHospitalOrder(orderNo);
		HospitalOrderDetailPojo order = getHospitalOrderDetailPojoByHospitalOrder(hospitalOrder, ChargeStatus.TOCHARGE);
		// 补全药物的使用信息
		this.setDrugUsageInfo(order);
		return order;
	}

	private HospitalOrderDetailPojo getHospitalOrderDetailPojoByHospitalOrder(HospitalOrder hospitalOrder,
			Integer chargeStatus) {
		if (hospitalOrder == null) {
			return null;
		}
		// 订单基本信息
		HospitalOrderDetailPojo order = this.getHospitalOrderPojoByHospitalOrder(chargeStatus, hospitalOrder);
		String orderNo = hospitalOrder.getOrderNo();
		// 查询orderItemList
		List<OrderItemList> orderItemLists = orderItemListService.getOrderItemListsByOrderNoAndChargeStatus(orderNo,
				chargeStatus);
		if (CollectionUtils.isEmpty(orderItemLists)) {
			return order;
		}
		OrderItemList orderAdditionAmtList = null;
		OrderItemList orderExamList = null;
		OrderItemList orderSellDrugRecord = null;
		List<OrderItemList<OrderDrug>> orderPrescriptions = new ArrayList<OrderItemList<OrderDrug>>();
		for (OrderItemList orderItemList : orderItemLists) {
			if (OrderItemListType.ADDITION_AMT.getType() == orderItemList.getType()) {
				orderAdditionAmtList = orderItemList;
			} else if (OrderItemListType.EXAM.getType() == orderItemList.getType()) {
				orderExamList = orderItemList;
			} else if (OrderItemListType.PATIENT_PRESCRIPTION.getType() == orderItemList.getType()
					|| OrderItemListType.CHINESE_PRESCRIPTION.getType() == orderItemList.getType()) {
				orderPrescriptions.add(orderItemList);
			} else if (OrderItemListType.SELL_DRUG.getType() == orderItemList.getType()) {
				orderSellDrugRecord = orderItemList;
			}
		}
		if (HospitalOrder.TYPE_PATIENT_RECORD == hospitalOrder.getOrderType()) {
			// 如果订单来自病历
			/**
			 * 附加费用
			 */
			if (orderAdditionAmtList != null) {
				OrderAdditionAmtListPojo orderAdditionAmtListPojo = new OrderAdditionAmtListPojo();
				orderAdditionAmtListPojo.setOrderAdditionAmtListId(orderAdditionAmtList.getId());
				List<OrderAdditionAmt> orderAdditionAmts = orderAdditionAmtService
						.getOrderAdditionAmtsByOrderItemListIdAndChargeStatus(orderAdditionAmtList.getId(),
								chargeStatus);
				orderAdditionAmtListPojo.setOrderAdditionAmts(orderAdditionAmts);
				order.setOrderAdditionAmtList(orderAdditionAmtListPojo);
			}

			/**
			 *  检查治疗
			 */

			if (orderExamList != null) {
				OrderExamListPojo orderExamListPojo = new OrderExamListPojo();
				orderExamListPojo.setOrderExamListId(orderExamList.getId());
				List<OrderExam> orderExams = orderExamService
						.getOrderExamsByOrderItemListIdAndChargeStatus(orderExamList.getId(), chargeStatus);
				orderExamListPojo.setOrderExams(orderExams);
				order.setOrderExamList(orderExamListPojo);
			}

			/**
			 *  处方列表
			 */
			if (CollectionUtils.isNotEmpty(orderPrescriptions)) {
				List<OrderPrescriptionPojo> orderPrescriptionPojos = orderPrescriptions.stream()
						.map(orderPrescription -> {
							OrderPrescriptionPojo orderPrescriptionPojo = new OrderPrescriptionPojo();
							orderPrescriptionPojo.setOrderPrescriptionId(orderPrescription.getId());
							orderPrescriptionPojo.setPresciptionType(orderPrescription.getType());
							List<OrderDrug> orderDrugs = orderDrugService.getOrderDrugsByOrderItemListIdAndChargeStatus(
									orderPrescription.getId(), chargeStatus);
							orderPrescriptionPojo.setOrderDrugs(orderDrugs);
							return orderPrescriptionPojo;
						}).sorted((p1, p2) -> p1.getPresciptionType().compareTo(p2.getPresciptionType()))
						.collect(Collectors.toList());
				order.setOrderPrescriptions(orderPrescriptionPojos);
			}
		} else if (HospitalOrder.TYPE_SELL_DRUG == hospitalOrder.getOrderType()) {
			if (orderSellDrugRecord != null) {
				SellDrugRecordPojo sellDrugRecord = new SellDrugRecordPojo();
				sellDrugRecord.setSellDrugRecordId(orderSellDrugRecord.getId());
				List<OrderDrug> orderDrugs = orderDrugService
						.getOrderDrugsByOrderItemListIdAndChargeStatus(orderSellDrugRecord.getId(), chargeStatus);
				sellDrugRecord.setOrderDrugs(orderDrugs);
				order.setSellDrugRecord(sellDrugRecord);
			}
		}
		return order;
	}

	@Override
	public HospitalOrderDetailPojo getChargedOrderInfo(GetOrdersRequest req) {
		if (req == null || StringUtils.isEmpty(req.getOrderNo())) {
			return null;
		}
		String orderNo = req.getOrderNo();
		HospitalOrder hospitalOrder = this.getHospitalOrder(orderNo);
		HospitalOrderDetailPojo order = getHospitalOrderDetailPojoByHospitalOrder(hospitalOrder, ChargeStatus.CHARGED);
		// 补全药物的使用信息
		this.setDrugUsageInfo(order);
		return order;
	}

	@Override
	public HospitalOrderDetailPojo getRefundedOrderInfo(HospitalOrder req) {
		if (req == null || StringUtils.isEmpty(req.getOrderNo())) {
			return null;
		}
		String orderNo = req.getOrderNo();
		HospitalOrder hospitalOrder = this.getHospitalOrder(orderNo);
		HospitalOrderDetailPojo order = getHospitalOrderDetailPojoByHospitalOrder(hospitalOrder, ChargeStatus.REFUND);
		return order;
	}

	@Override
	public String createOrderNo() {
		// 20160227 201012 01 + 医生id
		Long doctorId = SessionUtils.getDoctorId();
		String orderNo = doctorId.intValue() + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
		// +医生id
		return orderNo;
	}

	/**
	 * 目前没有用到
	 */
	@Override
	public void createOrder(PatientInquiryRequest patientRecord) throws Exception {
		if (patientRecord == null) {
			return;
		}
		// 1. 创建订单对象HospitalOrder
		HospitalOrder hospitalOrder = this.createHospitalOrder(patientRecord);
		// 订单号
		String orderNo = hospitalOrder.getOrderNo();
		// 2.创建订单中的检查治疗信息
		List<PatientExam> patientExams = patientRecord.getExamList();
		OrderItemList<OrderExam> orderExamList = orderExamService.createOrderExams(orderNo, patientExams);
		// 3.创建订单中的处方及其药物关系
		List<Map<String, Object>> patientDrugMapList = patientRecord.getPatentPrescriptionList();
		List<OrderItemList<OrderDrug>> patentPrescriptions = null;
		if (CollectionUtils.isNotEmpty(patientDrugMapList)) {
			List<List<PatientDrug>> patentPrescriptionList = patientDrugMapList.stream().map(m -> {
				List<PatientDrug> patientDrugList = (List<PatientDrug>) m.get("patentDrugList");
				return patientDrugList;
			}).collect(Collectors.toList());
			patentPrescriptions = orderDrugService.createOrderDrugsByPatentPrescriptionList(orderNo,
					patentPrescriptionList);
		}

		List<PatientChineseDrug> chinesePrescriptionList = patientRecord.getChinesePrescriptionList();
		List<OrderItemList<OrderDrug>> chinesePrescriptions = orderDrugService
				.createOrderDrugsByChinesePrescriptionList(orderNo, chinesePrescriptionList);
		// 4.创建附加费用单
		List<PatientAdditional> additionalList = patientRecord.getAdditionalList();
		OrderItemList<OrderAdditionAmt> orderAdditionAmtList = orderAdditionAmtService.createOrderAdditionAmts(orderNo,
				additionalList);
		// 计算订单应收金额
		List<OrderItemList> itemLists = Lists.newArrayList();
		itemLists.add(orderExamList);
		itemLists.add(orderAdditionAmtList);
		itemLists.addAll(patentPrescriptions);
		itemLists.addAll(chinesePrescriptions);
		Double orderReceivableAmt = this.calculateOrderReceivableAmt(itemLists);
		hospitalOrder.setReceivableAmt(orderReceivableAmt);
		hospitalOrder.setTotalReceivableAmt(orderReceivableAmt);
		this.update(hospitalOrder);
		System.out.println("创建订单成功，订单号：" + orderNo);
	}

	private Double calculateOrderReceivableAmt(List<OrderItemList> itemLists) {
		if (CollectionUtils.isEmpty(itemLists)) {
			return null;
		}
		Double totalAmt = 0d;
		for (OrderItemList itemList : itemLists) {
			Double amt = itemList.getReceivableAmt();
			if (amt != null) {
				// totalAmt += amt;
				totalAmt = AmtUtils.add(totalAmt, amt);
			}
		}
		return totalAmt;
	}

	/**
	 * 
	 * @description 根据病历创建HospitalOrder对象，一个病历只能对应一个订单，这个逻辑由调用者自行控制，另外一旦创建好病历，病历的患者id和名称等基本信息不可以修改,
	 * 所以订单基本信息中的相关内容也不会涉及到修改
	 * @date 2016年3月3日
	 * @author lipeng
	 * @param patientRecord 
	 */
	@SystemLogAnno(functionId = FunctionConst.ORDER_OPER_LOG, operationId = Operation.CREATE_ORDER)
	public HospitalOrder createHospitalOrder(PatientInquiryRequest patientRecord) {
		if (patientRecord == null || patientRecord.getRecordId() == null) {
			return null;
		}

		HospitalOrder hospitalOrder = new HospitalOrder();
		hospitalOrder.setOrderNo(this.createOrderNo());
		hospitalOrder.setCreater(SessionUtils.getDoctorId());
		hospitalOrder.setHospitalId(SessionUtils.getHospitalId());
		hospitalOrder.setOrderType(HospitalOrder.TYPE_PATIENT_RECORD);
		hospitalOrder.setChargeStatus(ChargeStatus.TOCHARGE);
		hospitalOrder.setModifier(SessionUtils.getDoctorId());
		hospitalOrder.setPatientId(patientRecord.getPatientId());
		hospitalOrder.setPatientName(patientRecord.getPatientName());
		hospitalOrder.setPatientRecordId(patientRecord.getRecordId());
		hospitalOrder.setRecordVersion(System.currentTimeMillis());
		hospitalOrder.setReceivableAmt(0D);
		hospitalOrder.setTotalReceivableAmt(0D);
		hospitalOrderMapper.insert(hospitalOrder);
		return hospitalOrder;
	}

	@Override
	public void insertHospitalOrder(HospitalOrder hospitalOrder) {
		hospitalOrderMapper.insert(hospitalOrder);
	}

	@Override
	public void update(HospitalOrder hospitalOrder) {
		hospitalOrderMapper.update(hospitalOrder);
	}

	@Override
	public List<HospitalOrder> getHospitalOrders(GetOrderListParam param) {
		List<HospitalOrder> list = hospitalOrderMapper.selectHospitalOrders(param);
		return list;
	}

	@Override
	public int getHospitalOrderCount(GetOrderListParam param) {
		return hospitalOrderMapper.selectHospitalOrderCount(param);
	}

	@Override
	public HospitalOrder getHospitalOrder(String orderNo) {
		return hospitalOrderMapper.selectByOrderNo(orderNo);
	}

	@Override
	public Long getRecordVersion(String orderNo) {
		HospitalOrder hospitalOrder = hospitalOrderMapper.selectByOrderNo(orderNo);
		return hospitalOrder == null ? null : hospitalOrder.getRecordVersion();
	}

	@Override
	public void updateRecordVersion(String orderNo, Long version) {
		HospitalOrder order = new HospitalOrder();
		order.setOrderNo(orderNo);
		order.setRecordVersion(version);
		this.update(order);
	}

	@Override
	public Double getReceivableRefundAmt(String orderNo) {
		Double re = orderItemChargeMapper.selectReceivableAmtSumByOrderNoAndChargeStatus(orderNo,
				ChargeStatus.REFUND | ChargeStatus.CHARGED);
		return re == null ? 0d : AmtUtils.decimalFormat(re);
	}

	@Override
	public boolean hasOrderByDoctorId(Long doctorId) {
		Integer count = hospitalOrderMapper.selectCountByCreater(doctorId);
		return count != null && count > 0 ? true : false;
	}

	private void setDrugUsageInfo(HospitalOrderDetailPojo orderDetailPojo) {
		if (orderDetailPojo == null || orderDetailPojo.getOrderPrescriptions() == null)
			return;
		List<OrderPrescriptionPojo> orderPrescriptions = orderDetailPojo.getOrderPrescriptions();
		// 获取病历id(recordId)和订单id
		Long recordId = 0L;
		String orderNo = "";
		boolean hasPatient = false;
		boolean hasHerb = false;
		for (OrderPrescriptionPojo pojo : orderPrescriptions) {
			List<OrderDrug> orderDrugs = pojo.getOrderDrugs();
			if (pojo.getPresciptionType() == 1) {
				hasPatient = true;
			} else {
				hasHerb = true;
			}
			if (orderDrugs != null && orderDrugs.size() > 0) {
				if (orderDrugs.get(0).getRecordId() != null) {
					recordId = orderDrugs.get(0).getRecordId();
					orderNo = orderDrugs.get(0).getOrderNo();
				}
			}
		}
		if (recordId == 0)
			return;
		// 获取订单药物对应的处方id
		List<OrderItemList> orderItemLists = orderItemListService.getOrderItemListsByOrderNo(orderNo);
		Map<Long, Long> prescriptionMap = new HashMap<>();
		if (orderItemLists == null)
			return;
		for (OrderItemList orderItemList : orderItemLists) {
			if (orderItemList.getType() == OrderItemListType.CHINESE_PRESCRIPTION.getType()
					|| orderItemList.getType() == OrderItemListType.PATIENT_PRESCRIPTION.getType())
				prescriptionMap.put(orderItemList.getId(), orderItemList.getForeignId());
		}
		// 获取病历中所有的药物
		List<PatientDrug> patientDrugs = new ArrayList<>();
		List<PatientChineseDrug> herbDrugs = new ArrayList<>();
		if (hasPatient) {
			patientDrugs = patientDrugService.getDrugByRecordId(recordId);
		}
		if (hasHerb) {
			herbDrugs = patientDrugService.getChineseDrugByRecordId(recordId);
		}
		Map<Long, Map<Long, PatientDrug>> patientDrugsMap = new HashMap<>();
		for (PatientDrug patientDrug : patientDrugs) {
			Long prescriptionId = patientDrug.getPrescription();
			Long drugId = patientDrug.getDrugId();
			if (!patientDrugsMap.containsKey(prescriptionId)) {
				patientDrugsMap.put(prescriptionId, new HashMap<>());
			}
			patientDrugsMap.get(prescriptionId).put(drugId, patientDrug);
		}
		Map<Long, PatientChineseDrug> herbDrugsMap = new HashMap<>();
		for (PatientChineseDrug herbDrug : herbDrugs) {
			herbDrugsMap.put(herbDrug.getPrescription(), herbDrug);
		}
		// 将药物的使用信息添加到订单的药物中
		for (OrderPrescriptionPojo pojo : orderPrescriptions) {
			List<OrderDrug> orderDrugs = pojo.getOrderDrugs();
			if (orderDrugs == null || orderDrugs.size() == 0)
				continue;
			Long prescriptionId = prescriptionMap.get(orderDrugs.get(0).getOrderItemListId());
			// 成药
			if (pojo.getPresciptionType() == 1) {
				for (OrderDrug orderDrug : orderDrugs) {
					PatientDrug patientDrug = patientDrugsMap.get(prescriptionId).get(orderDrug.getDrugId());
					orderDrug.setUsage(patientDrug.getUsage());
					orderDrug.setFrequency(patientDrug.getFrequency());
					orderDrug.setOnceDosage(patientDrug.getDosage());
					orderDrug.setOnceDosageUnit(patientDrug.getDosageUnit());
				}
			} else {// 饮片
				PatientChineseDrug patientChineseDrug = herbDrugsMap.get(prescriptionId);
				String usage = LangUtils.getString(patientChineseDrug.getUsage(), "");
				if (StringUtils.isNotBlank(usage) && StringUtils.isNotBlank(patientChineseDrug.getRequirement())) {
					usage = usage + "; ";
				}
				usage = usage + LangUtils.getString(patientChineseDrug.getRequirement(), "");
				pojo.setHerbUsage(usage);
				String dailyDosage = LangUtils.getString(patientChineseDrug.getFrequency(), "");
				if (StringUtils.isNotBlank(dailyDosage)) {
					dailyDosage = dailyDosage + "; ";
				}
				dailyDosage = dailyDosage + LangUtils.getString(patientChineseDrug.getDailyDosage(), "0") + "剂/日";
				pojo.setHerbDailyDosage(dailyDosage);
				Long piecesListId = patientChineseDrug.getPaChDrugId();
				List<PatientChineseDrugPieces> pieces = new ArrayList<>();
				pieces = patientDrugService.getChineseDrugPieces(piecesListId);
				Map<Long, PatientChineseDrugPieces> piecesMap = new HashMap<>();
				for (PatientChineseDrugPieces pieces1 : pieces) {
					piecesMap.put(pieces1.getDrugId(), pieces1);
				}
				for (OrderDrug orderDrug : orderDrugs) {
					PatientChineseDrugPieces drugPieces = piecesMap.get(orderDrug.getDrugId());
					orderDrug.setRemark(drugPieces.getComment());
				}
			}
		}
	}

	@Override
	public Map<String, Double> getReceivableRefundAmts(List<String> orderNos) {
		List<OrderItemChargeReceivableAmtPojo> list = orderItemChargeMapper
				.selectReceivableAmtSumByOrderNosAndChargeStatus(orderNos, ChargeStatus.REFUND | ChargeStatus.CHARGED);
		Map<String, Double> map = new HashMap<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (OrderItemChargeReceivableAmtPojo pojo : list) {
				map.put(pojo.getOrderNo(), pojo.getAmt());
			}
		}
		return map;
	}
}
