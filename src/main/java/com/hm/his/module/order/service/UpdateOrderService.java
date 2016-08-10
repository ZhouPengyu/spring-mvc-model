package com.hm.his.module.order.service;

import java.util.List;

import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.order.model.OrderItemListType;
import com.hm.his.module.outpatient.model.PatientAdditional;
import com.hm.his.module.outpatient.model.PatientChineseDrug;
import com.hm.his.module.outpatient.model.PatientDrug;
import com.hm.his.module.outpatient.model.PatientExam;

/**
 * 
 * @description 更新订单服务
 * @author lipeng
 * @date 2016年3月9日
 */
public interface UpdateOrderService {
	/**
	 * 
	 * @description 新增或修改检查单里的检查项,如果此订单已经有此检查项则删除，并新增新额检查项，计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patientExam
	 */
	void saveOrderExam(Long recordId, PatientExam patientExam);


	/**
	 * 
	 * @description 新增检查单,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patientExams
	 */
	void addOrderExamList(Long recordId, List<PatientExam> patientExams);


	/**
	 *
	 * @description 首次 新增检查单,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年8月2日
	 * @author tangwenwu
	 * @param recordId
	 * @param patientExams
	 */
	void firstAddOrderExamList(Long recordId, List<PatientExam> patientExams, HospitalOrder hospitalOrder);

	/**
	 * 
	 * @description 新增或(收费前)修改西药处方,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patentPrescriptionList
	 */
	void saveOrderPatentPrescription(Long recordId, List<PatientDrug> patentPrescriptionList);

	/**
	 *
	 * @description 新增或(收费前)修改西药处方,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patentPrescriptionList
	 */
	void firstSaveOrderPatentPrescription(Long recordId, List<PatientDrug> patentPrescriptionList,HospitalOrder hospitalOrder);

	/**
	 * 
	 * @description 新增或(收费前)修改中药处方,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patientChineseDrug
	 */
	void saveOrderChinesePrescription(Long recordId, PatientChineseDrug patientChineseDrug);

	/**
	 *
	 * @description 新增或(收费前)修改中药处方,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patientChineseDrug
	 */
	void firstSaveOrderChinesePrescription(Long recordId, PatientChineseDrug patientChineseDrug,HospitalOrder hospitalOrder);

	/**
	 * 
	 * @description 新增或修改附加费用项,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patientAdditional
	 */
	void saveOrderAdditionAmt(Long recordId, PatientAdditional patientAdditional);

	/**
	 * 
	 * @description 新增附加费用单,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patientAdditionals
	 */
	void addOrderAdditionAmtList(Long recordId, List<PatientAdditional> patientAdditionals);

	/**
	 *
	 * @description 新增附加费用单,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月9日
	 * @author lipeng
	 * @param recordId
	 * @param patientAdditionals
	 */
	void firstAddOrderAdditionAmtList(Long recordId, List<PatientAdditional> patientAdditionals,HospitalOrder hospitalOrder);

	/**
	 * 
	 * @description 删除订单中的具体项,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月10日
	 * @author lipeng
	 * @param recordId
	 * @param id 
	 * @param type 检查，附加费用，或处方
	 */
	void deleteRecordItem(Long recordId, Long id, OrderItemListType type);

	/**
	 *  
	 * @description 删除订单中的单（检查单，处方单，附加费用单）以及单中的项,计算订单的应收金额，以及更新订单的收费情况状态orderStatus
	 * @date 2016年3月10日
	 * @author lipeng
	 * @param rocordId
	 * @param type 检查，附加费用，或处方
	 */
	void deleteAllRecordItems(Long recordId, OrderItemListType type);

}
