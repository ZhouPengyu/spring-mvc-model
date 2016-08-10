package com.hm.his.module.drug.controller;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.exception.ExcelException;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.*;
import com.hm.his.module.drug.model.DrugManufacturer;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.model.DrugTypeEnum;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.*;
import com.hm.his.module.drug.service.DrugManufacturerService;
import com.hm.his.module.drug.service.DrugService;
import com.hm.his.module.drug.service.DrugTradeService;
import com.hm.his.module.manage.service.HospitalConfigService;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  诊所药品Controller
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/26
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
@RestController
@RequestMapping("/inventory")
public class DrugController {

	@Autowired(required = false)
	DrugService drugService;
	@Autowired(required = false)
	DrugManufacturerService drugManufacturerService;
	@Autowired(required = false)
	HospitalConfigService hospitalConfigService;
	@Autowired(required = false)
	DrugTradeService drugTradeService;

	private static final String ERROR_DRUG_LIST = "errorDrugList";

	private static final String EXCEL_FILE = ".xls";
	private static final String EXCEL_UPLOAD_FILE_PATH = "/hm/uploadFile/";



	/**
	 *  功能描述：  5.1.	药品列表查询  --显示药品/耗材的列表，可根据相关属性搜索
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/searchDrugList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchDrugList(@RequestBody DrugRequest drugRequest, HttpServletRequest request) {
		HttpSession session = request.getSession();
		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		// 检查 诊所 是否已配置过药品信息： isConfig 1：已配置 0：未配置
		Map<String, Object> body = Maps.newHashMap();
		// Integer countAllDrug =
		// drugService.countHospitalDrugAllByHospitalId(drugRequest.getHospitalId());
		// if(countAllDrug<1){
		// body.put("isConfig",0);
		// body.put("totalPage",0);
		// body.put("drugList",null);
		// }else{
		body.put("isConfig", 1);
		List<HospitalDrug> hospList = drugService.searchDrugList(drugRequest);
		Integer countResult = drugService.countHospitalDrug(drugRequest);
//		System.out.println("searchDrugList===============countResult=="+countResult);
		drugRequest.setTotalCount(countResult);
		body.put("totalPage", drugRequest.getTotalPage());
		body.put("drugList", hospList);
		// }
		HisResponse hisResponse = HisResponse.getInstance(body);
		return hisResponse.toString();
	}

	/**
	 *  5.12.	药品名称搜索sug（药品列表）
	 *
	 *用户在药品列表的搜索框中输入“通用名/商品名/药品条码/批准文号”信息时，仅返回诊所匹配的药品名称
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/searchDrugNameList", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchDrugNameList(@RequestBody DrugRequest drugRequest) {
		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		List<HospitalDrug> hospList = drugService.searchDrugNameList(drugRequest);
		Map<String, Object> body = Maps.newHashMap();
		body.put("drugList", hospList);
		HisResponse hisResponse = HisResponse.getInstance(body);
		return hisResponse.toString();
	}

	/**
	 *  功能描述： 5.2.	药品搜索sug根据药物名称  --用户在搜索框中输入“通用名/商品名/药品条码/批准文号”信息
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/searchDrugByName", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchDrugByName(@RequestBody DrugRequest drugRequest) {
		List<HospitalDrugSug> hospList = Lists.newArrayList();
		DrugResponse drugResp = new DrugResponse();
		drugResp.setVersion(drugRequest.getVersion());
		drugResp.setMatchRule(2);

		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		drugRequest.setLimit(10);
//		logger.info(drugRequest);
		String drugName = drugRequest.getDrugName();
		if (StringUtils.isNotBlank(drugName)) {
			drugName = drugRequest.getDrugName().trim();
			drugRequest.setDrugName(drugName);
			// 当 dataSource 为0 时，表示：用户在 查询药品列表处搜索
			if (null != drugRequest.getDataSource() && drugRequest.getDataSource() == 0) {
				hospList = drugService.searchDrugByName(drugRequest);
				drugResp.setDrugList(hospList);
			} else {
				// 当 dataSource 为1 时，表示：用户在 新增药品 处搜索
				// 当 药品名称 为条形码时，先查 诊所的药品库，是否有重复药品
				 if (drugName.length() > 9 && RegexUtil.isNumbersOrLetters(drugName)) {
					// 当 drugName >=10个字符，且全部为字母或数字 时，按条形码搜索
					List<DrugTrade> drugTrades = drugTradeService.searchHMDrugByBarCode(drugRequest);
					if (CollectionUtils.isNotEmpty(drugTrades)) {
						if (drugTrades.size() == 1) {// 根据条形码仅找到一个药品时，封装drugInfo，前端自动填充
							DrugTrade dTrade = drugTrades.get(0);
							drugResp.setMatchRule(1);
							drugResp.setDrugInfo(drugTradeService.hmDrugToHospitalDrug(dTrade));
						} else {// 根据条形码找到多个药品时，封装drugList，前端sug提示
							hospList = drugTrades.stream().map(drugTrade -> {
								return drugService.processHospitalDrugSugByHMDrug(drugTrade, true);
							}).collect(Collectors.toList());
							drugResp.setDrugList(hospList);
						}
					} else {// 为空时，需要继续调用根据药品名称搜索药物的sug
						hospList = drugService.searchDrugByName(drugRequest);
						drugResp.setDrugList(hospList);
					}
				} else {
					hospList = drugService.searchDrugByName(drugRequest);
					drugResp.setDrugList(hospList);
				}
			}

		} else {
			drugResp.setDrugList(hospList);
		}
		HisResponse hisResponse = HisResponse.getInstance(drugResp);
		return hisResponse.toString();
	}


	/**
	 *  功能描述： 5.3.	药品搜索sug （处方，售药模块）
	 *  医生或售药人员在药品搜索框中输入“通用名/商品名/药品条码/批准文号”信息时，调用此接口，返回信息中包括药品的价格信息
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/searchDrugByNameForSaleDrug", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchDrugByNameForSaleDrug(@RequestBody DrugRequest drugRequest) {
		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		drugRequest.setLimit(5);
		if (drugRequest.getDrugType() != null && drugRequest.getDrugType().equals(1)) {
			drugRequest.setDrugType(DrugTypeEnum.herbal.getDrugType());
		} else if (drugRequest.getDrugType() != null && drugRequest.getDrugType().equals(100)) {
			drugRequest.setDrugType(100);
		} else {
			drugRequest.setDrugType(null);
		}
		drugRequest.setDataSource(2);
		HisResponse hisResponse = new HisResponse();
		List<HospitalDrugSug> hospList = Lists.newArrayList();
		if (StringUtils.isBlank(drugRequest.getDrugName())) {
			hisResponse.setErrorCode(50003L);
			hisResponse.setErrorMessage("药品名称不能为空");
			return hisResponse.toString();
		}

		DrugResponse drugResp = new DrugResponse();
		drugResp.setVersion(drugRequest.getVersion());
		drugResp.setMatchRule(2);

		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		String drugName = drugRequest.getDrugName().trim();
		drugRequest.setDrugName(drugName);
//		logger.info(drugRequest);
		// 当 drugName >=10个字符，且全部为字母或数字 时，按条形码搜索
		if (drugName.length() > 9 && RegexUtil.isNumbersOrLetters(drugName)) {
			List<HospitalDrugSug> hospitalDrugSugs = drugService.searchDrugByBarCodeForSale(drugRequest);
			if (CollectionUtils.isNotEmpty(hospitalDrugSugs)) {
				if (hospitalDrugSugs.size() == 1) {// 根据条形码仅找到一个药品时，封装drugInfo，前端自动填充
					HospitalDrugSug drugSug = hospitalDrugSugs.get(0);
					drugResp.setMatchRule(1);
					drugResp.setDrugInfo(drugSug);
				} else {// 根据条形码找到多个药品时，封装drugList，前端sug提示
					drugResp.setDrugList(hospitalDrugSugs);
				}
			} else {// 为空时，需要继续调用根据药品名称搜索药物的sug
				hospList = drugService.searchDrugByNameForSale(drugRequest);
				drugResp.setDrugList(hospList);
			}
		} else {
			hospList = drugService.searchDrugByNameForSale(drugRequest);
			drugResp.setDrugList(hospList);
		}
		hisResponse = HisResponse.getInstance(drugResp);
		return hisResponse.toString();
	}

	/**
	 *  功能描述：2.3.	药品搜索sug （批次入库模块）
	 --医生或售药人员在批次入库模块，在药品搜索框中输入“通用名/商品名/药品条码/批准文号”信息时，调用此接口，返回相应的药品信息.
	 * @author:  tangwenwu
	 * @createDate   2016/6/1
	 *
	 */
	@RequestMapping(value = "/searchDrugByNameForInsock", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchDrugByNameForInsock(@RequestBody DrugRequest drugRequest) {
		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		drugRequest.setLimit(5);
		drugRequest.setDataSource(0);
		HisResponse hisResponse = new HisResponse();
		List<HospitalDrugSug> hospList = Lists.newArrayList();
		if (StringUtils.isBlank(drugRequest.getDrugName())) {
			hisResponse.setErrorCode(50003L);
			hisResponse.setErrorMessage("药品名称不能为空");
			return hisResponse.toString();
		}

		DrugResponse drugResp = new DrugResponse();
		drugResp.setVersion(drugRequest.getVersion());
		drugResp.setMatchRule(2);

		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		String drugName = drugRequest.getDrugName().trim();
		drugRequest.setDrugName(drugName);

		// 当 drugName >=10个字符，且全部为字母或数字 时，按条形码搜索
		if (drugName.length() > 9 && RegexUtil.isNumbersOrLetters(drugName)) {
			List<HospitalDrugSug> hospitalDrugSugs = drugService.searchDrugByBarCodeForSale(drugRequest);
			if (CollectionUtils.isNotEmpty(hospitalDrugSugs)) {
				if (hospitalDrugSugs.size() == 1) {// 根据条形码仅找到一个药品时，封装drugInfo，前端自动填充
					HospitalDrugSug drugSug = hospitalDrugSugs.get(0);
					drugResp.setMatchRule(1);
					drugResp.setDrugInfo(drugSug);
				} else {// 根据条形码找到多个药品时，封装drugList，前端sug提示
					drugResp.setDrugList(hospitalDrugSugs);
				}
			} else {// 为空时，需要继续调用根据药品名称搜索药物的sug
				hospList = drugService.searchDrugByNameForSale(drugRequest);
				drugResp.setDrugList(hospList);
			}
		} else {
			hospList = drugService.searchDrugByNameForSale(drugRequest);
			drugResp.setDrugList(hospList);
		}
		hisResponse = HisResponse.getInstance(drugResp);
		return hisResponse.toString();
	}

	/**
	 *  功能描述： 5.4.	新增药品
	 *
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/saveDrug", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String saveDrug(@RequestBody HospitalDrug hospitalDrug) {
		hospitalDrug.setHospitalId(SessionUtils.getHospitalId());
		HisResponse hisResponse = new HisResponse();
		if (DrugCtlHelper.checkPostDrugData(hospitalDrug, hisResponse))
			return hisResponse.toString();

		hospitalDrug.setCreater(SessionUtils.getDoctorId());
		hisResponse = drugService.saveDrug(hospitalDrug);
		return hisResponse.toString();
	}

	/**
	 *  功能描述：5.5.	生产厂家sug
	 *  用户在添加药物的生产厂家时，sug层提示，最多5条
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/searchManufacturer", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String searchManufacturer(@RequestBody DrugRequest drugRequest) {
		Map<String, Object> body = Maps.newHashMap();
		if (StringUtils.isBlank(drugRequest.getManufacturer())) {
			body.put("manufacturerList", null);
			HisResponse hisResponse = HisResponse.getInstance(body);
			return hisResponse.toString();
		} else {
			drugRequest.setManufacturer(drugRequest.getManufacturer().trim());
		}
		List<DrugManufacturer> manufactList = drugManufacturerService.searchManufacturerByName(drugRequest.getManufacturer());
		body.put("manufacturerList", manufactList);
		HisResponse hisResponse = HisResponse.getInstance(body);
		return hisResponse.toString();
	}

	/**
	 *  功能描述：5.6.	查询药品详细信息
	 *
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/drugDetail", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String drugDetail(@RequestBody DrugRequest drugRequest) {
		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		HospitalDrug hospDrug = new HospitalDrug();
		if (null != drugRequest.getDataSource() && drugRequest.getDataSource().equals(1)) {
			hospDrug = drugTradeService.getDrugByHMDrugId(drugRequest.getDrugId());
		} else {
			hospDrug = drugService.getDrugDetailById(drugRequest.getDrugId());
		}
		HisResponse hisResponse = HisResponse.getInstance(hospDrug);
		return hisResponse.toString();
	}

	/**
	 *  功能描述：5.13.	根据条形码验证药品是否重复
	 *
	 * @author:  tangwenwu
	 * @createDate   2016/5/11
	 *
	 */
	@RequestMapping(value = "/checkDrugRepeatedByBarCode", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String checkDrugRepeatedByBarCode(@RequestBody HospitalDrug hospDrug) {
		hospDrug.setHospitalId(SessionUtils.getHospitalId());
		HisResponse hisResponse = new HisResponse();
		if (StringUtils.isBlank(hospDrug.getBarCode())) {
			return HisResponse.getInstance().toString();
		}
		hospDrug = drugService.checkDrugRepeatedByBarCode(hospDrug);
		if (hospDrug != null) {
			hisResponse.setErrorCode(50001L);
			hisResponse.setErrorMessage("当前新增药品与诊所已配置的药品信息重复");
		}
		hisResponse.setBody(hospDrug);
		return hisResponse.toString();
	}

	/**
	 *  功能描述：5.7.	导入药品excel 使用批次
	 *
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/importDrugsWithBranch", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
	//@RequestMapping(value = "/importDrugsWithBranch", produces ="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String importDrugsWithBranch(@RequestParam("excelFile") CommonsMultipartFile excelFile, HttpServletRequest request,
			HttpServletResponse response) throws ExcelException {
		HttpSession session = request.getSession();
		session.setAttribute(ERROR_DRUG_LIST, null);
		InputStream in = null;
		Workbook rwb = null;
		try {
			List<HospitalDrugExcel> errorList = Lists.newArrayList();
			List<ImportResultPojo.DrugResult> drugResults = Lists.newArrayList();
			// 获取前台exce的输入流
			in = excelFile.getInputStream();
			rwb = Workbook.getWorkbook(in);
			Sheet sheet = rwb.getSheet(0);
			String fileName  = SessionUtils.getHospitalId()+"--"+DateTools.getDate(DateTools.FILE_DATE_FORMAT)+EXCEL_FILE;


			String fileEnd = IPAndPathUtils.getResourcePath();
			if(fileEnd.indexOf(":") >-1){
				fileEnd = fileEnd.substring(0,fileEnd.indexOf(":")+1);
				fileEnd = fileEnd+ EXCEL_UPLOAD_FILE_PATH;
			}else{
				fileEnd = EXCEL_UPLOAD_FILE_PATH;
			}
			String filePath = fileEnd+fileName;
			System.out.println("importDrugsWithBranch--filePath======"+filePath);
			FileOutputStream  uploadFile = new FileOutputStream(filePath);
			uploadFile.write(excelFile.getBytes());
			uploadFile.close();

			int rows = sheet.getRows();// 获得excel行数
			if (!DrugCtlHelper.checkExcelWithBranch(rwb)) {
				// response.setContentType("text/html;charset=UTF-8");
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5100);
				hisResponse.setErrorMessage("表格格式错误，请重新下载导入表格模板");
				return hisResponse.toString();
			}

			Long hospitalId = SessionUtils.getHospitalId();
			Long doctorId = SessionUtils.getDoctorId();

			if (hospitalId == null) {
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5101);
				hisResponse.setErrorMessage("诊所ID为空，请重新登录！");
				return hisResponse.toString();
			}
			if (doctorId == null) {
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5102);
				hisResponse.setErrorMessage("医生ID为空，请重新登录！");
				return hisResponse.toString();
			}

			// excel每一行为表头，跳过
			for (int i = 1; i < rows; i++) {
				String errorMsg = "";
				boolean canAdd = true;// 能否保存到数据库的标志
				HospitalDrug hospitalDrug = new HospitalDrug();
				HospitalDrugExcel drugExcel = new HospitalDrugExcel();

				hospitalDrug.setHospitalId(hospitalId);
				hospitalDrug.setStatus(1);
				hospitalDrug.setFlag(1);
				hospitalDrug.setCreater(doctorId);
				// "药品名称",
				String drugName = sheet.getCell(0, i).getContents();
				if (StringUtils.isNotBlank(drugName)) {
					hospitalDrug.setDrugName(drugName.trim());
					hospitalDrug.setPinyin(PinyinHelper.convertToPinyinString(hospitalDrug.getDrugName(), "", PinyinFormat.WITHOUT_TONE));
					hospitalDrug.setPinyinFirst(PinyinHelper.getShortPinyin(hospitalDrug.getDrugName()));
				} else {
					errorMsg += "药品名称不能为空,";
					canAdd = false;
				}
				// "分类",
				String drugType = sheet.getCell(1, i).getContents();
				if (StringUtils.isNotBlank(drugType)) {
					drugExcel.setDrugTypeStr(drugType.trim());
					Integer drugTypeId = DrugTypeEnum.getDrugTypeByName(drugType.trim());
					if (drugTypeId != null) {
						hospitalDrug.setDrugType(drugTypeId);
					} else {
						errorMsg += "分类无法匹配,";
						canAdd = false;
					}
				} else {
					errorMsg += "分类不能为空,";
					canAdd = false;
				}
				// "是否OTC",
				String isOTC = sheet.getCell(2, i).getContents();
				if (StringUtils.isNotBlank(isOTC)) {
					drugExcel.setIsOtcStr(isOTC.trim());
					hospitalDrug.setIsOtc(isOTC.trim().equals("是") ? 1 : 0);
				} else {
					hospitalDrug.setIsOtc(0);
				}
				// "批准文号",
				String approvalNumber = sheet.getCell(3, i).getContents();
				if (StringUtils.isNotBlank(approvalNumber)) {
					hospitalDrug.setApprovalNumber(approvalNumber.trim());
				}
				// "条形码",
				String barCode = sheet.getCell(4, i).getContents();
				if (StringUtils.isNotBlank(barCode)) {
					hospitalDrug.setBarCode(barCode.trim());
					// 2.条形码重复时，认为重复 2016-05-03 条形码不再作为药品重复检查因素
					// if(drugService.checkDrugRepeatedByBarCode(hospitalDrug)
					// >0){
					// errorMsg +=
					// "药品条形码："+hospitalDrug.getBarCode()+"已存在，不能重复添加,";
					// canAdd = false;
					// }
				}

				// 产品需求倒退，暂时注释
				// //"最小剂量",
				// String spec_minimum_dosage = sheet.getCell(5,
				// i).getContents();
				// if (StringUtils.isNotBlank(spec_minimum_dosage)) {
				// Double specMinimumDosage =
				// LangUtils.getDouble(spec_minimum_dosage);
				// if(specMinimumDosage !=null && specMinimumDosage >0){
				// hospitalDrug.setSpecMinimumDosage(specMinimumDosage);
				// }else{
				// errorMsg += "最小剂量必须是数字,且不能为负数,";
				// canAdd = false;
				// }
				// }
				// //"最小单位",
				// String spec_minimum_unit = sheet.getCell(6, i).getContents();
				// if (StringUtils.isNotBlank(spec_minimum_unit)) {
				// hospitalDrug.setSpecMinimumUnit(spec_minimum_unit.trim());
				// }else{
				// if (hospitalDrug.getSpecMinimumDosage() != null){
				// errorMsg += "最小剂量不为空时，最小单位为必填,";
				// canAdd = false;
				// }
				// }
				// "规格包装描述",
				String spec_desc = sheet.getCell(5, i).getContents();
				if (StringUtils.isNotBlank(spec_desc)) {
					hospitalDrug.setSpecification(spec_desc.trim());
				}

				// "包装单位",
				String spec_package_unit = sheet.getCell(6, i).getContents();
				if (StringUtils.isNotBlank(spec_package_unit)) {
					hospitalDrug.setSpecPackageUnit(spec_package_unit.trim());
				} else {
					errorMsg += "包装单位不能为空,";
					canAdd = false;
				}

				// excel 文件 在随机点选 后，会出现空行，当 药品名称，分类，规格包装、厂家名称 全部为空时，认为是
				// 最后一行垃圾数据，抛弃
				if (StringUtils.isEmpty(drugName) && StringUtils.isEmpty(drugType) && StringUtils.isEmpty(spec_package_unit)) {
					continue;
				}
				// "生产厂家",
				String manufacturer = sheet.getCell(7, i).getContents();
				if (StringUtils.isNotBlank(manufacturer)) {
					hospitalDrug.setManufacturer(manufacturer.trim());
				} else {
					// 若药品类型为饮片或耗材时，必填项为：名称、包装单位 --生产厂家 不必填
					if(hospitalDrug.getDrugType() !=null){
						if (hospitalDrug.getDrugType() == 2 || hospitalDrug.getDrugType() == 3 || hospitalDrug.getDrugType() == 5) {
							errorMsg += "生产厂家不能为空,";
							canAdd = false;
						}
					}
				}

				// 1.名称、规格包装、厂家名称 相同时认为重复
				if (drugService.checkDrugRepeatedByNameAndSpec(hospitalDrug) > 0) {
					errorMsg += "药品名称、规格包装描述、包装单位、厂家名称已存在，不能重复添加,";
					canAdd = false;
				}

				// "支持拆零"
				String open_stock = sheet.getCell(8, i).getContents();
				if (StringUtils.isNotBlank(open_stock)) {
					drugExcel.setOpenStockStr(open_stock);
					hospitalDrug.setOpenStock(open_stock.trim().equals("是") ? 1 : 0);
				} else {
					hospitalDrug.setOpenStock(0);
				}
				// 支持拆零选择为是时，拆零单位为必填项
				if (hospitalDrug.getOpenStock() == 1) {
					// "小包装单位",
					String spec_unit = sheet.getCell(9, i).getContents();
					if (StringUtils.isNotBlank(spec_unit)) {
						hospitalDrug.setSpecUnit(spec_unit.trim());
					} else {
						errorMsg += "支持拆零时，小包装单位不能为空,";
						canAdd = false;
					}
					// "换算比",
					String spec_unitary_ratio = sheet.getCell(10, i).getContents();
					if (StringUtils.isNotBlank(spec_unitary_ratio)) {
						Integer specUnitaryRatio = LangUtils.getInteger(spec_unitary_ratio);
						if (specUnitaryRatio != null && specUnitaryRatio > 0) {
							hospitalDrug.setSpecUnitaryRatio(specUnitaryRatio);
						} else {
							errorMsg += "换算比不是正整数,";
							canAdd = false;
						}
					} else {
						errorMsg += "支持拆零时，换算比不能为空,";
						canAdd = false;
					}
				}

				// "库存阈值",
				String inventory_threshold = sheet.getCell(11, i).getContents();
				if (StringUtils.isNotBlank(inventory_threshold)) {
					Integer inventoryThreshold = LangUtils.getInteger(inventory_threshold);
					if (inventoryThreshold != null) {
						if (inventoryThreshold < 0) {
							errorMsg += "库存阈值不能小于0,";
							canAdd = false;
						} else if (inventoryThreshold > 999999) {
							errorMsg += "库存阈值最大值为 999999,";
							canAdd = false;
						} else {
							hospitalDrug.setInventoryThreshold(inventoryThreshold);
						}
					} else {
						errorMsg += "库存阈值不是正整数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setInventoryThreshold(0);
				}
				// "助记码", 货架码
				String goodsShelfCode = sheet.getCell(12, i).getContents();
				if (StringUtils.isNotBlank(goodsShelfCode)) {
					hospitalDrug.setGoodsShelfCode(goodsShelfCode.trim());
				}
				// "处方价",
				String prescription_price = sheet.getCell(13, i).getContents();
				if (StringUtils.isNotBlank(prescription_price)) {
					Double prescriptionPrice = LangUtils.getDouble(prescription_price);
					if (prescriptionPrice != null) {
						hospitalDrug.setPrescriptionPrice(prescriptionPrice);
					} else {
						errorMsg += "处方价必须是数字,且不能为负数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setPrescriptionPrice(0.00);
				}
				// "供应商"
				String supplier = sheet.getCell(14, i).getContents();
				if (StringUtils.isNotBlank(supplier)) {
					hospitalDrug.setSupplier(supplier.trim());
				}

				// "用法",
				String instruction = sheet.getCell(15, i).getContents();
				if (StringUtils.isNotBlank(instruction)) {
					hospitalDrug.setInstruction(instruction.trim());
				}

				// "使用频次"
				String frequency = sheet.getCell(16, i).getContents();
				if (StringUtils.isNotBlank(frequency)) {
					hospitalDrug.setFrequency(frequency.trim());
				}
				// "单次剂量"
				String singleDosage = sheet.getCell(17, i).getContents();
				if (StringUtils.isNotBlank(singleDosage)) {
					hospitalDrug.setSingleDosage(singleDosage.trim());
				}
				// "单次剂量单位" 必须为最小单位或规格单位中的一项-----没有这些字段了，
				String singleDosageUnit = sheet.getCell(18, i).getContents();
				if (StringUtils.isNotBlank(singleDosageUnit)) {
					hospitalDrug.setSingleDosageUnit(singleDosageUnit.trim());
				}
				// ,"开药量",
				String prescribeAmount = sheet.getCell(19, i).getContents();
				if (StringUtils.isNotBlank(prescribeAmount)) {
					Double prescribeAmountDou = LangUtils.getDouble(prescribeAmount);
					if (prescribeAmountDou != null) {
						hospitalDrug.setPrescribeAmount(prescribeAmountDou);
					} else {
						errorMsg += "开药量不是数字,";
						canAdd = false;
					}
				}
				// "开药量单位"
				String prescribeAmountUnit = sheet.getCell(20, i).getContents();
				if (StringUtils.isNotBlank(prescribeAmountUnit)) {
					hospitalDrug.setPrescribeAmountUnit(prescribeAmountUnit.trim());
				}
				// ""医嘱",
				String doctor_advice = sheet.getCell(21, i).getContents();
				if (StringUtils.isNotBlank(doctor_advice)) {
					hospitalDrug.setDoctorAdvice(doctor_advice.trim());
				}

				// 设置每个药品的导入结果
				ImportResultPojo.DrugResult drugResult = new ImportResultPojo.DrugResult();
				drugResult.setDrugName(hospitalDrug.getDrugName());
				drugResult.setRowId(i);
				// 保存药品
				if (canAdd) {
					HisResponse hisResponse = drugService.saveDrug(hospitalDrug);
					if (!hisResponse.getHasError()) {
						drugResult.setResult(true);
					} else {
						drugResult.setErrorMsg("药品保存失败");
						drugResult.setResult(false);
					}
				} else {
					drugResult.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
					drugResult.setResult(false);
				}
				if (!drugResult.getResult()) {
					hospitalDrug.setErrorMsg(drugResult.getErrorMsg());
					BeanUtils.copyProperties(hospitalDrug, drugExcel, false);
					errorList.add(drugExcel);
				}
				drugResults.add(drugResult);

			}
			ImportResultPojo importResultPojo = new ImportResultPojo();
			importResultPojo.setFailList(drugResults.stream().filter(drugResult -> !drugResult.getResult()).collect(Collectors.toList()));
			importResultPojo.setFailCount(drugResults.stream().filter(drugResult -> !drugResult.getResult()).count());
			importResultPojo.setSuccessList(drugResults.stream().filter(ImportResultPojo.DrugResult::getResult).collect(Collectors.toList()));
			importResultPojo.setSuccessCount(drugResults.stream().filter(ImportResultPojo.DrugResult::getResult).count());
			if (importResultPojo.getFailCount() > 0) {
				session.setAttribute(ERROR_DRUG_LIST, errorList);
			}
			HisResponse hisResponse = HisResponse.getInstance(importResultPojo);
			return hisResponse.toString();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
			if (rwb != null) {
				rwb.close();
			}

		}

		HisResponse hisResp = HisResponse.getInstance();
		return hisResp.toString();

	}

	//		@RequestMapping(value = "/importDrugsObject", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
		@RequestMapping(value = "/importDrugsObject", produces ="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public HisResponse importDrugsObject(@RequestParam("excelFile") CommonsMultipartFile excelFile, HttpServletRequest request, HttpServletResponse response)
			throws ExcelException {
		HttpSession session = request.getSession();
		session.setAttribute(ERROR_DRUG_LIST, null);
		InputStream in = null;
		Workbook rwb = null;
		try {
			List<HospitalDrugExcel> errorList = Lists.newArrayList();
			List<ImportResultPojo.DrugResult> drugResults = Lists.newArrayList();
			// 获取前台exce的输入流
			in = excelFile.getInputStream();
			String fileName  = SessionUtils.getHospitalId()+"--"+DateTools.getDate(DateTools.FILE_DATE_FORMAT)+EXCEL_FILE;
			rwb = Workbook.getWorkbook(in);
			Sheet sheet = rwb.getSheet(0);
			String fileEnd = IPAndPathUtils.getResourcePath();
			if(fileEnd.indexOf(":") >-1){
				fileEnd = fileEnd.substring(0,fileEnd.indexOf(":")+1);
				fileEnd = fileEnd+ EXCEL_UPLOAD_FILE_PATH;
			}else{
				fileEnd = EXCEL_UPLOAD_FILE_PATH;
			}
			final String filePath = fileEnd+fileName;
			System.out.println("importDrugs--filePath======"+filePath);
			FileOutputStream  uploadFile = new FileOutputStream(filePath);
			uploadFile.write(excelFile.getBytes());
			uploadFile.close();

			int rows = sheet.getRows();// 获得excel行数
			if (!DrugCtlHelper.checkExcel(rwb)) {
				// response.setContentType("text/html;charset=UTF-8");
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5100);
				hisResponse.setErrorMessage("表格格式错误，请重新下载导入表格模板");
				return hisResponse;
			}

			Long hospitalId = SessionUtils.getHospitalId();
			Long doctorId = SessionUtils.getDoctorId();

			if (hospitalId == null) {
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5101);
				hisResponse.setErrorMessage("诊所ID为空，请重新登录！");
				return hisResponse;
			}
			if (doctorId == null) {
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5102);
				hisResponse.setErrorMessage("医生ID为空，请重新登录！");
				return hisResponse;
			}

			// excel每一行为表头，跳过
			for (int i = 1; i < rows; i++) {
				String errorMsg = "";
				boolean canAdd = true;// 能否保存到数据库的标志
				HospitalDrug hospitalDrug = new HospitalDrug();
				HospitalDrugExcel drugExcel = new HospitalDrugExcel();

				hospitalDrug.setHospitalId(hospitalId);
				hospitalDrug.setStatus(1);
				hospitalDrug.setFlag(1);
				hospitalDrug.setCreater(doctorId);
				// "药品名称",
				String drugName = sheet.getCell(0, i).getContents();
				if (StringUtils.isNotBlank(drugName)) {
					hospitalDrug.setDrugName(drugName.trim());
					hospitalDrug.setPinyin(PinyinHelper.convertToPinyinString(hospitalDrug.getDrugName(), "", PinyinFormat.WITHOUT_TONE));
					hospitalDrug.setPinyinFirst(PinyinHelper.getShortPinyin(hospitalDrug.getDrugName()));
				} else {
					errorMsg += "药品名称不能为空,";
					canAdd = false;
				}
				// "分类",
				String drugType = sheet.getCell(1, i).getContents();
				if (StringUtils.isNotBlank(drugType)) {
					drugExcel.setDrugTypeStr(drugType.trim());
					Integer drugTypeId = DrugTypeEnum.getDrugTypeByName(drugType.trim());
					if (drugTypeId != null) {
						hospitalDrug.setDrugType(drugTypeId);
					} else {
						errorMsg += "分类无法匹配,";
						canAdd = false;
					}
				} else {
					errorMsg += "分类不能为空,";
					canAdd = false;
				}
				// "是否OTC",
				String isOTC = sheet.getCell(2, i).getContents();
				if (StringUtils.isNotBlank(isOTC)) {
					drugExcel.setIsOtcStr(isOTC.trim());
					hospitalDrug.setIsOtc(isOTC.trim().equals("是") ? 1 : 0);
				} else {
					hospitalDrug.setIsOtc(0);
				}
				// "批准文号",
				String approvalNumber = sheet.getCell(3, i).getContents();
				if (StringUtils.isNotBlank(approvalNumber)) {
					hospitalDrug.setApprovalNumber(approvalNumber.trim());
				}
				// "条形码",
				String barCode = sheet.getCell(4, i).getContents();
				if (StringUtils.isNotBlank(barCode)) {
					hospitalDrug.setBarCode(barCode.trim());
					// 2.条形码重复时，认为重复 2016-05-03 条形码不再作为药品重复检查因素
					// if(drugService.checkDrugRepeatedByBarCode(hospitalDrug)
					// >0){
					// errorMsg +=
					// "药品条形码："+hospitalDrug.getBarCode()+"已存在，不能重复添加,";
					// canAdd = false;
					// }
				}

				// 产品需求倒退，暂时注释
				// //"最小剂量",
				// String spec_minimum_dosage = sheet.getCell(5,
				// i).getContents();
				// if (StringUtils.isNotBlank(spec_minimum_dosage)) {
				// Double specMinimumDosage =
				// LangUtils.getDouble(spec_minimum_dosage);
				// if(specMinimumDosage !=null && specMinimumDosage >0){
				// hospitalDrug.setSpecMinimumDosage(specMinimumDosage);
				// }else{
				// errorMsg += "最小剂量必须是数字,且不能为负数,";
				// canAdd = false;
				// }
				// }
				// //"最小单位",
				// String spec_minimum_unit = sheet.getCell(6, i).getContents();
				// if (StringUtils.isNotBlank(spec_minimum_unit)) {
				// hospitalDrug.setSpecMinimumUnit(spec_minimum_unit.trim());
				// }else{
				// if (hospitalDrug.getSpecMinimumDosage() != null){
				// errorMsg += "最小剂量不为空时，最小单位为必填,";
				// canAdd = false;
				// }
				// }
				// "规格包装描述",
				String spec_desc = sheet.getCell(5, i).getContents();
				if (StringUtils.isNotBlank(spec_desc)) {
					hospitalDrug.setSpecification(spec_desc.trim());
				}

				// "包装单位",
				String spec_package_unit = sheet.getCell(6, i).getContents();
				if (StringUtils.isNotBlank(spec_package_unit)) {
					hospitalDrug.setSpecPackageUnit(spec_package_unit.trim());
				} else {
					errorMsg += "包装单位不能为空,";
					canAdd = false;
				}

				// excel 文件 在随机点选 后，会出现空行，当 药品名称，分类，规格包装、厂家名称 全部为空时，认为是
				// 最后一行垃圾数据，抛弃
				if (StringUtils.isEmpty(drugName) && StringUtils.isEmpty(drugType) && StringUtils.isEmpty(spec_package_unit)) {
					continue;
				}
				// "生产厂家",
				String manufacturer = sheet.getCell(7, i).getContents();
				if (StringUtils.isNotBlank(manufacturer)) {
					hospitalDrug.setManufacturer(manufacturer.trim());
				} else {
					// 若药品类型为饮片或耗材时，必填项为：名称、包装单位 --生产厂家 不必填
					if(hospitalDrug.getDrugType() !=null) {
						if (hospitalDrug.getDrugType() == 2 || hospitalDrug.getDrugType() == 3 || hospitalDrug.getDrugType() == 5) {
							errorMsg += "生产厂家不能为空,";
							canAdd = false;
						}
					}
				}

				// 1.名称、规格包装、厂家名称 相同时认为重复
				if (drugService.checkDrugRepeatedByNameAndSpec(hospitalDrug) > 0) {
					errorMsg += "药品名称、规格包装描述、包装单位、厂家名称已存在，不能重复添加,";
					canAdd = false;
				}

				// "支持拆零"
				String open_stock = sheet.getCell(8, i).getContents();
				if (StringUtils.isNotBlank(open_stock)) {
					drugExcel.setOpenStockStr(open_stock);
					hospitalDrug.setOpenStock(open_stock.trim().equals("是") ? 1 : 0);
				} else {
					hospitalDrug.setOpenStock(0);
				}
				// 支持拆零选择为是时，拆零单位为必填项
				if (hospitalDrug.getOpenStock() == 1) {
					// "小包装单位",
					String spec_unit = sheet.getCell(9, i).getContents();
					if (StringUtils.isNotBlank(spec_unit)) {
						hospitalDrug.setSpecUnit(spec_unit.trim());
					} else {
						errorMsg += "支持拆零时，小包装单位不能为空,";
						canAdd = false;
					}
					// "换算比",
					String spec_unitary_ratio = sheet.getCell(10, i).getContents();
					if (StringUtils.isNotBlank(spec_unitary_ratio)) {
						Integer specUnitaryRatio = LangUtils.getInteger(spec_unitary_ratio);
						if (specUnitaryRatio != null && specUnitaryRatio > 0) {
							hospitalDrug.setSpecUnitaryRatio(specUnitaryRatio);
						} else {
							errorMsg += "换算比不是正整数,";
							canAdd = false;
						}
					} else {
						errorMsg += "支持拆零时，换算比不能为空,";
						canAdd = false;
					}
				}

				//"库存",
				String inventory_count = sheet.getCell(11, i).getContents();
				if (StringUtils.isNotBlank(inventory_count)) {
					Double inventoryCount = LangUtils.getDouble(inventory_count);
					if(inventoryCount !=null){
						if(inventoryCount < 0){
							errorMsg += "库存不能小于0,";
							canAdd = false;
						}else if(inventoryCount > 999999){
							errorMsg += "库存最大值为 999999,";
							canAdd = false;
						}else{
							hospitalDrug.setInventoryCount(inventoryCount);
						}
					}else{
						errorMsg += "库存不是正整数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setInventoryCount(0D);
				}

				// "库存阈值",
				String inventory_threshold = sheet.getCell(12, i).getContents();
				if (StringUtils.isNotBlank(inventory_threshold)) {
					Integer inventoryThreshold = LangUtils.getInteger(inventory_threshold);
					if (inventoryThreshold != null) {
						if (inventoryThreshold < 0) {
							errorMsg += "库存阈值不能小于0,";
							canAdd = false;
						} else if (inventoryThreshold > 999999) {
							errorMsg += "库存阈值最大值为 999999,";
							canAdd = false;
						} else {
							hospitalDrug.setInventoryThreshold(inventoryThreshold);
						}
					} else {
						errorMsg += "库存阈值不是正整数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setInventoryThreshold(0);
				}
				// "助记码", 货架码
				String goodsShelfCode = sheet.getCell(13, i).getContents();
				if (StringUtils.isNotBlank(goodsShelfCode)) {
					hospitalDrug.setGoodsShelfCode(goodsShelfCode.trim());
				}
				// "进货价",
				String purchase_price = sheet.getCell(14, i).getContents();
				if (StringUtils.isNotBlank(purchase_price)) {
					Double purchasePrice = LangUtils.getDouble(purchase_price);
					if (purchasePrice != null) {
						hospitalDrug.setPurchasePrice(purchasePrice);
					} else {
						errorMsg += "进货价必须是数字,且不能为负数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setPurchasePrice(0.00);
				}
				// "处方价",
				String prescription_price = sheet.getCell(15, i).getContents();
				if (StringUtils.isNotBlank(prescription_price)) {
					Double prescriptionPrice = LangUtils.getDouble(prescription_price);
					if (prescriptionPrice != null) {
						hospitalDrug.setPrescriptionPrice(prescriptionPrice);
					} else {
						errorMsg += "处方价必须是数字,且不能为负数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setPrescriptionPrice(0.00);
				}
				// "供应商"
				String supplier = sheet.getCell(16, i).getContents();
				if (StringUtils.isNotBlank(supplier)) {
					hospitalDrug.setSupplier(supplier.trim());
				}

				// "用法",
				String instruction = sheet.getCell(17, i).getContents();
				if (StringUtils.isNotBlank(instruction)) {
					hospitalDrug.setInstruction(instruction.trim());
				}

				// "使用频次"
				String frequency = sheet.getCell(18, i).getContents();
				if (StringUtils.isNotBlank(frequency)) {
					hospitalDrug.setFrequency(frequency.trim());
				}
				// "单次剂量"
				String singleDosage = sheet.getCell(19, i).getContents();
				if (StringUtils.isNotBlank(singleDosage)) {
					hospitalDrug.setSingleDosage(singleDosage.trim());
				}
				// "单次剂量单位" 必须为最小单位或规格单位中的一项-----没有这些字段了，
				String singleDosageUnit = sheet.getCell(20, i).getContents();
				if (StringUtils.isNotBlank(singleDosageUnit)) {
					hospitalDrug.setSingleDosageUnit(singleDosageUnit.trim());
				}
				// ,"开药量",
				String prescribeAmount = sheet.getCell(21, i).getContents();
				if (StringUtils.isNotBlank(prescribeAmount)) {
					Double prescribeAmountDou = LangUtils.getDouble(prescribeAmount);
					if (prescribeAmountDou != null) {
						hospitalDrug.setPrescribeAmount(prescribeAmountDou);
					} else {
						errorMsg += "开药量不是数字,";
						canAdd = false;
					}
				}
				// "开药量单位"
				String prescribeAmountUnit = sheet.getCell(22, i).getContents();
				if (StringUtils.isNotBlank(prescribeAmountUnit)) {
					hospitalDrug.setPrescribeAmountUnit(prescribeAmountUnit.trim());
				}
				// ""医嘱",
				String doctor_advice = sheet.getCell(23, i).getContents();
				if (StringUtils.isNotBlank(doctor_advice)) {
					hospitalDrug.setDoctorAdvice(doctor_advice.trim());
				}
				// "效期",
				String validityDateStr = sheet.getCell(24, i).getContents();

				if (StringUtils.isBlank(validityDateStr)) {
					validityDateStr = "2999-01-01";
				}
				try {
					if(validityDateStr.indexOf("-") >-1){
						hospitalDrug.setValidityDate(DateTools.formatDateString(validityDateStr,DateTools.ONLY_DATE_FORMAT));
					}else if(validityDateStr.indexOf("/") >-1){
						hospitalDrug.setValidityDate(DateTools.formatDateString(validityDateStr,"yyyy/MM/dd"));
					}else{
						hospitalDrug.setValidityDate(DateTools.formatDateString(validityDateStr,"yyyyMMdd"));
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorMsg += "效期格式不对，应为：yyyyMMdd,";
					canAdd = false;
				}

				// 设置每个药品的导入结果
				ImportResultPojo.DrugResult drugResult = new ImportResultPojo.DrugResult();
				drugResult.setDrugName(hospitalDrug.getDrugName());
				drugResult.setRowId(i);
				// 保存药品
				if (canAdd) {
					HisResponse hisResponse = drugService.saveDrug(hospitalDrug);
					if (!hisResponse.getHasError()) {
						drugResult.setResult(true);
					} else {
						drugResult.setErrorMsg("药品保存失败");
						drugResult.setResult(false);
					}
				} else {
					drugResult.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
					drugResult.setResult(false);
				}
				if (!drugResult.getResult()) {
					hospitalDrug.setErrorMsg(drugResult.getErrorMsg());
					BeanUtils.copyProperties(hospitalDrug, drugExcel, false);
					errorList.add(drugExcel);
				}
				drugResults.add(drugResult);
			}
			ImportResultPojo importResultPojo = new ImportResultPojo();
			importResultPojo.setFailList(drugResults.stream().filter(drugResult -> !drugResult.getResult()).collect(Collectors.toList()));
			importResultPojo.setFailCount(drugResults.stream().filter(drugResult -> !drugResult.getResult()).count());
			importResultPojo.setSuccessList(drugResults.stream().filter(ImportResultPojo.DrugResult::getResult).collect(Collectors.toList()));
			importResultPojo.setSuccessCount(drugResults.stream().filter(ImportResultPojo.DrugResult::getResult).count());
			if (importResultPojo.getFailCount() > 0) {
				session.setAttribute(ERROR_DRUG_LIST, errorList);
			}
			HisResponse hisResponse = HisResponse.getInstance(importResultPojo);
			return hisResponse;
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
			if (rwb != null) {
				rwb.close();
			}

		}

		HisResponse hisResp = HisResponse.getInstance();
		return hisResp;

	}


	@RequestMapping(value = "/importDrugs", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
//		@RequestMapping(value = "/importDrugs", produces ="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String importDrugs(@RequestParam("excelFile") CommonsMultipartFile excelFile, HttpServletRequest request, HttpServletResponse response)
			throws ExcelException {
		HttpSession session = request.getSession();
		session.setAttribute(ERROR_DRUG_LIST, null);
		InputStream in = null;
		Workbook rwb = null;
		try {
			List<HospitalDrugExcel> errorList = Lists.newArrayList();
			List<ImportResultPojo.DrugResult> drugResults = Lists.newArrayList();
			// 获取前台exce的输入流
			in = excelFile.getInputStream();
			String fileName  = SessionUtils.getHospitalId()+"--"+DateTools.getDate(DateTools.FILE_DATE_FORMAT)+EXCEL_FILE;
			rwb = Workbook.getWorkbook(in);
			Sheet sheet = rwb.getSheet(0);
			String fileEnd = IPAndPathUtils.getResourcePath();
			if(fileEnd.indexOf(":") >-1){
				fileEnd = fileEnd.substring(0,fileEnd.indexOf(":")+1);
				fileEnd = fileEnd+ EXCEL_UPLOAD_FILE_PATH;
			}else{
				fileEnd = EXCEL_UPLOAD_FILE_PATH;
			}
			final String filePath = fileEnd+fileName;
			System.out.println("importDrugs--filePath======"+filePath);
			FileOutputStream  uploadFile = new FileOutputStream(filePath);
			uploadFile.write(excelFile.getBytes());
			uploadFile.close();

			int rows = sheet.getRows();// 获得excel行数
			if (!DrugCtlHelper.checkExcel(rwb)) {
				// response.setContentType("text/html;charset=UTF-8");
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5100);
				hisResponse.setErrorMessage("表格格式错误，请重新下载导入表格模板");
				return hisResponse.toString();
			}

			Long hospitalId = SessionUtils.getHospitalId();
			Long doctorId = SessionUtils.getDoctorId();

			if (hospitalId == null) {
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5101);
				hisResponse.setErrorMessage("诊所ID为空，请重新登录！");
				return hisResponse.toString();
			}
			if (doctorId == null) {
				HisResponse hisResponse = new HisResponse();
				hisResponse.setErrorCode(5102);
				hisResponse.setErrorMessage("医生ID为空，请重新登录！");
				return hisResponse.toString();
			}

			// excel每一行为表头，跳过
			for (int i = 1; i < rows; i++) {
				String errorMsg = "";
				boolean canAdd = true;// 能否保存到数据库的标志
				HospitalDrug hospitalDrug = new HospitalDrug();
				HospitalDrugExcel drugExcel = new HospitalDrugExcel();

				hospitalDrug.setHospitalId(hospitalId);
				hospitalDrug.setStatus(1);
				hospitalDrug.setFlag(1);
				hospitalDrug.setCreater(doctorId);
				// "药品名称",
				String drugName = sheet.getCell(0, i).getContents();
				if (StringUtils.isNotBlank(drugName)) {
					hospitalDrug.setDrugName(drugName.trim());
					hospitalDrug.setPinyin(PinyinHelper.convertToPinyinString(hospitalDrug.getDrugName(), "", PinyinFormat.WITHOUT_TONE));
					hospitalDrug.setPinyinFirst(PinyinHelper.getShortPinyin(hospitalDrug.getDrugName()));
				} else {
					errorMsg += "药品名称不能为空,";
					canAdd = false;
				}
				// "分类",
				String drugType = sheet.getCell(1, i).getContents();
				if (StringUtils.isNotBlank(drugType)) {
					drugExcel.setDrugTypeStr(drugType.trim());
					Integer drugTypeId = DrugTypeEnum.getDrugTypeByName(drugType.trim());
					if (drugTypeId != null) {
						hospitalDrug.setDrugType(drugTypeId);
					} else {
						errorMsg += "分类无法匹配,";
						canAdd = false;
					}
				} else {
					errorMsg += "分类不能为空,";
					canAdd = false;
				}
				// "是否OTC",
				String isOTC = sheet.getCell(2, i).getContents();
				if (StringUtils.isNotBlank(isOTC)) {
					drugExcel.setIsOtcStr(isOTC.trim());
					hospitalDrug.setIsOtc(isOTC.trim().equals("是") ? 1 : 0);
				} else {
					hospitalDrug.setIsOtc(0);
				}
				// "批准文号",
				String approvalNumber = sheet.getCell(3, i).getContents();
				if (StringUtils.isNotBlank(approvalNumber)) {
					hospitalDrug.setApprovalNumber(approvalNumber.trim());
				}
				// "条形码",
				String barCode = sheet.getCell(4, i).getContents();
				if (StringUtils.isNotBlank(barCode)) {
					hospitalDrug.setBarCode(barCode.trim());
					// 2.条形码重复时，认为重复 2016-05-03 条形码不再作为药品重复检查因素
					// if(drugService.checkDrugRepeatedByBarCode(hospitalDrug)
					// >0){
					// errorMsg +=
					// "药品条形码："+hospitalDrug.getBarCode()+"已存在，不能重复添加,";
					// canAdd = false;
					// }
				}

				// 产品需求倒退，暂时注释
				// //"最小剂量",
				// String spec_minimum_dosage = sheet.getCell(5,
				// i).getContents();
				// if (StringUtils.isNotBlank(spec_minimum_dosage)) {
				// Double specMinimumDosage =
				// LangUtils.getDouble(spec_minimum_dosage);
				// if(specMinimumDosage !=null && specMinimumDosage >0){
				// hospitalDrug.setSpecMinimumDosage(specMinimumDosage);
				// }else{
				// errorMsg += "最小剂量必须是数字,且不能为负数,";
				// canAdd = false;
				// }
				// }
				// //"最小单位",
				// String spec_minimum_unit = sheet.getCell(6, i).getContents();
				// if (StringUtils.isNotBlank(spec_minimum_unit)) {
				// hospitalDrug.setSpecMinimumUnit(spec_minimum_unit.trim());
				// }else{
				// if (hospitalDrug.getSpecMinimumDosage() != null){
				// errorMsg += "最小剂量不为空时，最小单位为必填,";
				// canAdd = false;
				// }
				// }
				// "规格包装描述",
				String spec_desc = sheet.getCell(5, i).getContents();
				if (StringUtils.isNotBlank(spec_desc)) {
					hospitalDrug.setSpecification(spec_desc.trim());
				}

				// "包装单位",
				String spec_package_unit = sheet.getCell(6, i).getContents();
				if (StringUtils.isNotBlank(spec_package_unit)) {
					hospitalDrug.setSpecPackageUnit(spec_package_unit.trim());
				} else {
					errorMsg += "包装单位不能为空,";
					canAdd = false;
				}

				// excel 文件 在随机点选 后，会出现空行，当 药品名称，分类，规格包装、厂家名称 全部为空时，认为是
				// 最后一行垃圾数据，抛弃
				if (StringUtils.isEmpty(drugName) && StringUtils.isEmpty(drugType) && StringUtils.isEmpty(spec_package_unit)) {
					continue;
				}
				// "生产厂家",
				String manufacturer = sheet.getCell(7, i).getContents();
				if (StringUtils.isNotBlank(manufacturer)) {
					hospitalDrug.setManufacturer(manufacturer.trim());
				} else {
					// 若药品类型为饮片或耗材时，必填项为：名称、包装单位 --生产厂家 不必填
					if(hospitalDrug.getDrugType() !=null) {
						if (hospitalDrug.getDrugType() == 2 || hospitalDrug.getDrugType() == 3 || hospitalDrug.getDrugType() == 5) {
							errorMsg += "生产厂家不能为空,";
							canAdd = false;
						}
					}
				}

				// 1.名称、规格包装、厂家名称 相同时认为重复
				if (drugService.checkDrugRepeatedByNameAndSpec(hospitalDrug) > 0) {
					errorMsg += "药品名称、规格包装描述、包装单位、厂家名称已存在，不能重复添加,";
					canAdd = false;
				}

				// "支持拆零"
				String open_stock = sheet.getCell(8, i).getContents();
				if (StringUtils.isNotBlank(open_stock)) {
					drugExcel.setOpenStockStr(open_stock);
					hospitalDrug.setOpenStock(open_stock.trim().equals("是") ? 1 : 0);
				} else {
					hospitalDrug.setOpenStock(0);
				}
				// 支持拆零选择为是时，拆零单位为必填项
				if (hospitalDrug.getOpenStock() == 1) {
					// "小包装单位",
					String spec_unit = sheet.getCell(9, i).getContents();
					if (StringUtils.isNotBlank(spec_unit)) {
						hospitalDrug.setSpecUnit(spec_unit.trim());
					} else {
						errorMsg += "支持拆零时，小包装单位不能为空,";
						canAdd = false;
					}
					// "换算比",
					String spec_unitary_ratio = sheet.getCell(10, i).getContents();
					if (StringUtils.isNotBlank(spec_unitary_ratio)) {
						Integer specUnitaryRatio = LangUtils.getInteger(spec_unitary_ratio);
						if (specUnitaryRatio != null && specUnitaryRatio > 0) {
							hospitalDrug.setSpecUnitaryRatio(specUnitaryRatio);
						} else {
							errorMsg += "换算比不是正整数,";
							canAdd = false;
						}
					} else {
						errorMsg += "支持拆零时，换算比不能为空,";
						canAdd = false;
					}
				}

				//"库存",
				String inventory_count = sheet.getCell(11, i).getContents();
				if (StringUtils.isNotBlank(inventory_count)) {
					Double inventoryCount = LangUtils.getDouble(inventory_count);
					if(inventoryCount !=null){
						if(inventoryCount < 0){
							errorMsg += "库存不能小于0,";
							canAdd = false;
						}else if(inventoryCount > 999999){
							errorMsg += "库存最大值为 999999,";
							canAdd = false;
						}else{
							hospitalDrug.setInventoryCount(inventoryCount);
						}
					}else{
						errorMsg += "库存不是正整数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setInventoryCount(0D);
				}

				// "库存阈值",
				String inventory_threshold = sheet.getCell(12, i).getContents();
				if (StringUtils.isNotBlank(inventory_threshold)) {
					Integer inventoryThreshold = LangUtils.getInteger(inventory_threshold);
					if (inventoryThreshold != null) {
						if (inventoryThreshold < 0) {
							errorMsg += "库存阈值不能小于0,";
							canAdd = false;
						} else if (inventoryThreshold > 999999) {
							errorMsg += "库存阈值最大值为 999999,";
							canAdd = false;
						} else {
							hospitalDrug.setInventoryThreshold(inventoryThreshold);
						}
					} else {
						errorMsg += "库存阈值不是正整数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setInventoryThreshold(0);
				}
				// "助记码", 货架码
				String goodsShelfCode = sheet.getCell(13, i).getContents();
				if (StringUtils.isNotBlank(goodsShelfCode)) {
					hospitalDrug.setGoodsShelfCode(goodsShelfCode.trim());
				}
				// "进货价",
				String purchase_price = sheet.getCell(14, i).getContents();
				if (StringUtils.isNotBlank(purchase_price)) {
					Double purchasePrice = LangUtils.getDouble(purchase_price);
					if (purchasePrice != null) {
						hospitalDrug.setPurchasePrice(purchasePrice);
					} else {
						errorMsg += "进货价必须是数字,且不能为负数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setPurchasePrice(0.00);
				}
				// "处方价",
				String prescription_price = sheet.getCell(15, i).getContents();
				if (StringUtils.isNotBlank(prescription_price)) {
					Double prescriptionPrice = LangUtils.getDouble(prescription_price);
					if (prescriptionPrice != null) {
						hospitalDrug.setPrescriptionPrice(prescriptionPrice);
					} else {
						errorMsg += "处方价必须是数字,且不能为负数,";
						canAdd = false;
					}
				} else {
					hospitalDrug.setPrescriptionPrice(0.00);
				}
				// "供应商"
				String supplier = sheet.getCell(16, i).getContents();
				if (StringUtils.isNotBlank(supplier)) {
					hospitalDrug.setSupplier(supplier.trim());
				}

				// "用法",
				String instruction = sheet.getCell(17, i).getContents();
				if (StringUtils.isNotBlank(instruction)) {
					hospitalDrug.setInstruction(instruction.trim());
				}

				// "使用频次"
				String frequency = sheet.getCell(18, i).getContents();
				if (StringUtils.isNotBlank(frequency)) {
					hospitalDrug.setFrequency(frequency.trim());
				}
				// "单次剂量"
				String singleDosage = sheet.getCell(19, i).getContents();
				if (StringUtils.isNotBlank(singleDosage)) {
					hospitalDrug.setSingleDosage(singleDosage.trim());
				}
				// "单次剂量单位" 必须为最小单位或规格单位中的一项-----没有这些字段了，
				String singleDosageUnit = sheet.getCell(20, i).getContents();
				if (StringUtils.isNotBlank(singleDosageUnit)) {
					hospitalDrug.setSingleDosageUnit(singleDosageUnit.trim());
				}
				// ,"开药量",
				String prescribeAmount = sheet.getCell(21, i).getContents();
				if (StringUtils.isNotBlank(prescribeAmount)) {
					Double prescribeAmountDou = LangUtils.getDouble(prescribeAmount);
					if (prescribeAmountDou != null) {
						hospitalDrug.setPrescribeAmount(prescribeAmountDou);
					} else {
						errorMsg += "开药量不是数字,";
						canAdd = false;
					}
				}
				// "开药量单位"
				String prescribeAmountUnit = sheet.getCell(22, i).getContents();
				if (StringUtils.isNotBlank(prescribeAmountUnit)) {
					hospitalDrug.setPrescribeAmountUnit(prescribeAmountUnit.trim());
				}
				// ""医嘱",
				String doctor_advice = sheet.getCell(23, i).getContents();
				if (StringUtils.isNotBlank(doctor_advice)) {
					hospitalDrug.setDoctorAdvice(doctor_advice.trim());
				}
				// "效期",
				String validityDateStr = sheet.getCell(24, i).getContents();

				if (StringUtils.isBlank(validityDateStr)) {
					validityDateStr = "2999-01-01";
				}
				try {
					if(validityDateStr.indexOf("-") >-1){
						hospitalDrug.setValidityDate(DateTools.formatDateString(validityDateStr,DateTools.ONLY_DATE_FORMAT));
					}else if(validityDateStr.indexOf("/") >-1){
						hospitalDrug.setValidityDate(DateTools.formatDateString(validityDateStr,"yyyy/MM/dd"));
					}else{
						hospitalDrug.setValidityDate(DateTools.formatDateString(validityDateStr,"yyyyMMdd"));
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorMsg += "效期格式不对，应为：yyyyMMdd,";
					canAdd = false;
				}

				// 设置每个药品的导入结果
				ImportResultPojo.DrugResult drugResult = new ImportResultPojo.DrugResult();
				drugResult.setDrugName(hospitalDrug.getDrugName());
				drugResult.setRowId(i);
				// 保存药品
				if (canAdd) {
					HisResponse hisResponse = drugService.saveDrug(hospitalDrug);
					if (!hisResponse.getHasError()) {
						drugResult.setResult(true);
					} else {
						drugResult.setErrorMsg("药品保存失败");
						drugResult.setResult(false);
					}
				} else {
					drugResult.setErrorMsg(errorMsg.substring(0, errorMsg.length() - 1));
					drugResult.setResult(false);
				}
				if (!drugResult.getResult()) {
					hospitalDrug.setErrorMsg(drugResult.getErrorMsg());
					BeanUtils.copyProperties(hospitalDrug, drugExcel, false);
					errorList.add(drugExcel);
				}
				drugResults.add(drugResult);
			}
			ImportResultPojo importResultPojo = new ImportResultPojo();
			importResultPojo.setFailList(drugResults.stream().filter(drugResult -> !drugResult.getResult()).collect(Collectors.toList()));
			importResultPojo.setFailCount(drugResults.stream().filter(drugResult -> !drugResult.getResult()).count());
			importResultPojo.setSuccessList(drugResults.stream().filter(ImportResultPojo.DrugResult::getResult).collect(Collectors.toList()));
			importResultPojo.setSuccessCount(drugResults.stream().filter(ImportResultPojo.DrugResult::getResult).count());
			if (importResultPojo.getFailCount() > 0) {
				session.setAttribute(ERROR_DRUG_LIST, errorList);
			}
			HisResponse hisResponse = HisResponse.getInstance(importResultPojo);
			return hisResponse.toString();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
			if (rwb != null) {
				rwb.close();
			}

		}

		HisResponse hisResp = HisResponse.getInstance();
		return hisResp.toString();

	}


	/**
	 *  功能描述：5.8.	下载导入失败药品excel
	 *  当导入药品接口中 failCount 大于 0 时，需要调用此接口，下载失败详情的excel
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/downloadFailDrugs", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String downloadFailDrugs(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		List<HospitalDrugExcel> errorDrugs = (List<HospitalDrugExcel>) session.getAttribute(ERROR_DRUG_LIST);
		try {
			if (CollectionUtils.isNotEmpty(errorDrugs)) {
				System.out.println(session.getId() + "===get session ERROR_DRUG_LIST ====" + errorDrugs.size());
				Integer isUseBatchManage = hospitalConfigService.getHospitalIsUseBatchManage();
				LinkedHashMap<String, String> fieldMap = null;
				if (isUseBatchManage == 1) {
					// 使用批次
					fieldMap = DrugCtlHelper.buildMapHeadToHeadWithBranch();
				} else {
					// 不使用批次
					fieldMap = DrugCtlHelper.buildMapHeadToHead();
				}
				// 将list集合转化为excle
				ExcelUtil.listToExcel(errorDrugs, fieldMap, "诊所药品导入", response);

			} else {
				System.out.println("下载导入失败药品为空get session id ====" + session.getId());
			}
		} catch (ExcelException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HisResponse hisResponse = new HisResponse();
		hisResponse.setBody(null);
		return hisResponse.toString();
	}

	/**
	 *  功能描述：5.9.	修改保存药品
	 *
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/modifyDrug", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String modifyDrug(@RequestBody HospitalDrug hospitalDrug) {
		HisResponse hisResponse = new HisResponse();
		hospitalDrug.setHospitalId(SessionUtils.getHospitalId());
		if (DrugCtlHelper.checkPostDrugData(hospitalDrug, hisResponse))
			return hisResponse.toString();

		hisResponse = drugService.modifyDrug(hospitalDrug);
		return hisResponse.toString();
	}

	/**
	 *  功能描述：5.10.	删除药品
	 *  用户删除药品，逻辑删除
	 * @author:  tangwenwu
	 * @createDate   2016/3/1
	 *
	 */
	@RequestMapping(value = "/deleteDrug", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String deleteDrug(@RequestBody DrugRequest drugRequest) {
		drugRequest.setHospitalId(SessionUtils.getHospitalId());
		Integer count = drugService.deleteDrug(drugRequest.getDrugIds());
		Map<String, Object> body = Maps.newHashMap();
		body.put("drugId", count);
		HisResponse hisResponse = HisResponse.getInstance(body);
		return hisResponse.toString();
	}

	/**
	 *  功能描述：5.8.	批量修改药品库存阈值
	 *
	 * @author:  tangwenwu
	 * @createDate   2016/6/1
	 *
	 */
	@RequestMapping(value = "/batchModifyDrugInventoryThreshold", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String batchModifyDrugInventoryThreshold(@RequestBody DrugRequest drugRequest) {
		Integer result = drugService.batchModifyDrugInventoryThreshold(drugRequest);
		HisResponse hisResponse = HisResponse.getInstance(result);
		return hisResponse.toString();
	}

	public static void main(String[] args) {
		String string = "2016/8/8";
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = df.parse(string);
			System.out.println(df2.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
