package com.hm.his.module.drug.controller;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.DateTools;
import com.hm.his.module.drug.model.HospitalDrug;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-05-06
 * Time: 15:01
 * CopyRight:HuiMei Engine
 */
public class DrugCtlHelper {

	public static void main(String[] args) {
		//System.out.println(Math.abs(1.3 - 5.6));
		System.out.println("yyyy-MM-dd==="+DateTools.formatDateString("2018-11-9",DateTools.ONLY_DATE_FORMAT));
		System.out.println("yyyy-MM-d==="+DateTools.formatDateString("2018-6-09",DateTools.ONLY_DATE_FORMAT));
		System.out.println("yyyy-M-d==="+DateTools.formatDateString("2018-02-9",DateTools.ONLY_DATE_FORMAT));

		System.out.println("yyyy/M/d==="+DateTools.formatDateString("2018/2/9","yyyy/MM/dd"));
		System.out.println("yyyy/M/d==="+DateTools.formatDateString("2018111","yyyyMMdd"));
	}

	protected static boolean checkPostDrugData(@RequestBody HospitalDrug hospitalDrug, HisResponse hisResponse) {
		if (StringUtils.isBlank(hospitalDrug.getDrugName())) {
			hisResponse.setErrorCode(50003L);
			hisResponse.setErrorMessage("药品名称不能为空");
			return true;
		}
		// 产品需求倒退，暂时注释
		// if (null != hospitalDrug.getSpecMinimumDosage()) {
		// if(hospitalDrug.getSpecMinimumDosage() < 0){
		// hisResponse.setErrorCode(50004L);
		// hisResponse.setErrorMessage("最小剂量必须是数字,且不能为负数");
		// return hisResponse.toString();
		// }
		// if (StringUtils.isBlank(hospitalDrug.getSpecMinimumUnit())) {
		// hisResponse.setErrorCode(50005L);
		// hisResponse.setErrorMessage("最小剂量不为空时，最小单位为必填");
		// return hisResponse.toString();
		// }
		// }
		// if(StringUtils.isBlank(hospitalDrug.getSpecUnit())){
		// hisResponse.setErrorCode(50006L);
		// hisResponse.setErrorMessage("规格单位不能为空");
		// return hisResponse.toString();
		// }

		if (null != hospitalDrug.getSpecUnitaryRatio()) {
			if (hospitalDrug.getSpecUnitaryRatio() < 1) {
				hisResponse.setErrorCode(50007L);
				hisResponse.setErrorMessage("换算比不是正整数");
				return true;
			}
		}

		if (StringUtils.isBlank(hospitalDrug.getSpecPackageUnit())) {
			hisResponse.setErrorCode(50009L);
			hisResponse.setErrorMessage("包装单位不能为空");
			return true;
		}

		// 若药品类型为饮片或耗材时，必填项为：名称、包装单位 --生产厂家 不必填
		if (hospitalDrug.getDrugType() == null) {
			hisResponse.setErrorCode(50009L);
			hisResponse.setErrorMessage("药品类型不能为空");
			return true;
		} else if (hospitalDrug.getDrugType() == 2 || hospitalDrug.getDrugType() == 3 || hospitalDrug.getDrugType() == 5) {
			if (StringUtils.isBlank(hospitalDrug.getManufacturer())) {
				hisResponse.setErrorCode(50010L);
				hisResponse.setErrorMessage("生产厂家不能为空");
				return true;
			}
		}

		// "库存阈值",
		if (null != hospitalDrug.getInventoryThreshold()) {

			if (hospitalDrug.getInventoryThreshold() < 0) {
				hisResponse.setErrorCode(50011L);
				hisResponse.setErrorMessage("库存阈值不是正整数");
				return true;
			}
		} else {
			hospitalDrug.setInventoryThreshold(0);
		}
		// "进货价",
		if (null != hospitalDrug.getPurchasePrice()) {

			if (hospitalDrug.getPurchasePrice() < 0) {
				hisResponse.setErrorCode(50012L);
				hisResponse.setErrorMessage("进货价必须是数字,且不能为负数");
				return true;
			}
		} else {
			hospitalDrug.setPurchasePrice(0.00);
		}

		// "处方价",
		if (null != hospitalDrug.getPrescriptionPrice()) {

			if (hospitalDrug.getPrescriptionPrice() < 0) {
				hisResponse.setErrorCode(50013L);
				hisResponse.setErrorMessage("处方价必须是数字,且不能为负数");
				return true;
			}
		} else {
			hospitalDrug.setPrescriptionPrice(0.00);
		}

		// "是否拆零 设置 为 不拆零",
		if (null == hospitalDrug.getOpenStock()) {
			hospitalDrug.setOpenStock(0);
		}

		// 支持拆零选择为是时，拆零单位为必填项
		if (hospitalDrug.getOpenStock() == 1) {
			// "小包装单位",
			if (StringUtils.isBlank(hospitalDrug.getSpecUnit())) {
				hisResponse.setErrorCode(50013L);
				hisResponse.setErrorMessage("支持拆零时，小包装单位不能为空");
				return true;
			}
			// "换算比",
			if (null == hospitalDrug.getSpecUnitaryRatio()) {
				hisResponse.setErrorCode(50013L);
				hisResponse.setErrorMessage("支持拆零时，换算比不能为空");
				return true;
			}
		}

		return false;
	}

	protected static boolean checkPostDrugData4ModifyInventoryAndPrice(@RequestBody HospitalDrug hospitalDrug, HisResponse hisResponse) {
		if (hospitalDrug.getId() == null) {
			hisResponse.setErrorCode(50001L);
			hisResponse.setErrorMessage("药品ID不能为空");
			return true;
		}
		if (hospitalDrug.getHospitalId() == null) {
			hisResponse.setErrorCode(50002L);
			hisResponse.setErrorMessage("医院ID不能为空");
			return true;
		}

		// "库存",
		if (null != hospitalDrug.getAddInventoryCount()) {

			if (hospitalDrug.getAddInventoryCount() < 0) {
				hisResponse.setErrorCode(50011L);
				hisResponse.setErrorMessage("增加的库存不是正整数");
				return true;
			}
		} else {
			hospitalDrug.setAddInventoryCount(0D);
		}
		// "进货价",
		if (null != hospitalDrug.getPurchasePrice()) {

			if (hospitalDrug.getPurchasePrice() < 0) {
				hisResponse.setErrorCode(50012L);
				hisResponse.setErrorMessage("进货价必须是数字,且不能为负数");
				return true;
			}
		} else {
			hospitalDrug.setPurchasePrice(0.00);
		}

		// "处方价",
		if (null != hospitalDrug.getPrescriptionPrice()) {

			if (hospitalDrug.getPrescriptionPrice() < 0) {
				hisResponse.setErrorCode(50013L);
				hisResponse.setErrorMessage("处方价必须是数字,且不能为负数");
				return true;
			}
		} else {
			hospitalDrug.setPrescriptionPrice(0.00);
		}

		return false;
	}

	/**
	 * 使用批次错误信息head
	 * @date 2016年6月17日
	 * @author lipeng
	 * @return
	 */
	protected static LinkedHashMap<String, String> buildMapHeadToHeadWithBranch() {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("drugName", "药品名称");
		fieldMap.put("drugTypeStr", "分类");
		fieldMap.put("isOtcStr", "是否OTC");
		fieldMap.put("approvalNumber", "批准文号");
		fieldMap.put("barCode", "条形码");
		fieldMap.put("specification", "规格包装描述");
		fieldMap.put("specPackageUnit", "包装单位");
		fieldMap.put("manufacturer", "生产厂家");
		fieldMap.put("openStockStr", "是否支持拆零销售");
		fieldMap.put("specUnit", "小包装单位");
		fieldMap.put("specUnitaryRatio", "换算比");
		fieldMap.put("inventoryThreshold", "库存阈值");
		fieldMap.put("goodsShelfCode", "货架码");
		fieldMap.put("prescriptionPrice", "处方价");
		fieldMap.put("supplier", "供应商");
		fieldMap.put("instruction", "用法");
		fieldMap.put("frequency", "使用频次");
		fieldMap.put("singleDosage", "单次剂量");
		fieldMap.put("singleDosageUnit", "单次剂量单位");
		fieldMap.put("prescribeAmount", "开药量");
		fieldMap.put("prescribeAmountUnit", "开药量单位");
		fieldMap.put("doctorAdvice", "医嘱");
		fieldMap.put("errorMsg", "错误消息");
		return fieldMap;
	}

	/**
	 * 不使用批次错误信息head
	 * @date 2016年6月17日
	 * @author lipeng
	 * @return
	 */
	protected static LinkedHashMap<String, String> buildMapHeadToHead() {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("drugName", "药品名称");
		fieldMap.put("drugTypeStr", "分类");
		fieldMap.put("isOtcStr", "是否OTC");
		fieldMap.put("approvalNumber", "批准文号");
		fieldMap.put("barCode", "条形码");
		fieldMap.put("specification", "规格包装描述");
		fieldMap.put("specPackageUnit", "包装单位");
		fieldMap.put("manufacturer", "生产厂家");
		fieldMap.put("openStockStr", "是否支持拆零销售");
		fieldMap.put("specUnit", "小包装单位");
		fieldMap.put("specUnitaryRatio", "换算比");
		fieldMap.put("inventoryCount", "库存");
		fieldMap.put("inventoryThreshold", "库存阈值");
		fieldMap.put("goodsShelfCode", "货架码");
		fieldMap.put("purchasePrice", "进货价");
		fieldMap.put("prescriptionPrice", "处方价");
		fieldMap.put("supplier", "供应商");
		fieldMap.put("instruction", "用法");
		fieldMap.put("frequency", "使用频次");
		fieldMap.put("singleDosage", "单次剂量");
		fieldMap.put("singleDosageUnit", "单次剂量单位");
		fieldMap.put("prescribeAmount", "开药量");
		fieldMap.put("prescribeAmountUnit", "开药量单位");
		fieldMap.put("doctorAdvice", "医嘱");
		fieldMap.put("validityDate", "效期");
		fieldMap.put("errorMsg", "错误消息");
		return fieldMap;
	}

	/**
	 * 检查 excel 文件的表头 是否正确
	 *
	 * @param rwb
	 */
	protected static boolean checkExcelWithBranch(Workbook rwb) {

		Sheet sheet = rwb.getSheet(0);
		int rows = sheet.getRows();// ????
		int columns = sheet.getColumns();// ????
		if (columns > 22) {
			columns = columns - 1;// 使用错误文件 导出时，需要删除错误消息列
		}
		/*
		 * String[] heads = new String[] {
		 * "药品名称","药品类型","是否OTC","批准文号","条形码","助记码","单次剂量","使用频次",
		 * "开药量","用法","医嘱","最小剂量","最小单位","规格单位","换算比","包装单位","生产厂家","进货价",
		 * "处方价","售药价","库存数量","货架码","支持拆零","直接购药","供应商" };
		 */

		String[] heads = new String[] { "药品名称", "分类", "是否OTC", "批准文号", "条形码", "规格包装描述", "包装单位", "生产厂家", "是否支持拆零销售", "小包装单位", "换算比", "库存阈值", "货架码",
				"处方价", "供应商", "用法", "使用频次", "单次剂量", "单次剂量单位", "开药量", "开药量单位", "医嘱" };

		if (rows > 1 && columns == heads.length) {
			for (int i = 0; i < columns; i++) {
				String contents = sheet.getCell(i, 0).getContents();
				if (contents == null || !contents.equals(heads[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}

	}

	protected static boolean checkExcel(Workbook rwb) {

		Sheet sheet = rwb.getSheet(0);
		int rows = sheet.getRows();// ????
		int columns = sheet.getColumns();// ????
		if (columns > 25) {
			columns = columns - 1;// 使用错误文件 导出时，需要删除错误消息列
		}
		/*
		 * String[] heads = new String[] {
		 * "药品名称","药品类型","是否OTC","批准文号","条形码","助记码","单次剂量","使用频次",
		 * "开药量","用法","医嘱","最小剂量","最小单位","规格单位","换算比","包装单位","生产厂家","进货价",
		 * "处方价","售药价","库存数量","货架码","支持拆零","直接购药","供应商" };
		 */

		String[] heads = new String[] { "药品名称", "分类", "是否OTC", "批准文号", "条形码", "规格包装描述", "包装单位", "生产厂家", "是否支持拆零销售", "小包装单位", "换算比", "库存", "库存阈值", "货架码",
				"进货价", "处方价", "供应商", "用法", "使用频次", "单次剂量", "单次剂量单位", "开药量", "开药量单位", "医嘱", "效期" };

		if (rows > 1 && columns == heads.length) {
			for (int i = 0; i < columns; i++) {
				String contents = sheet.getCell(i, 0).getContents();
				if (contents == null || !contents.equals(heads[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}

	}

}
