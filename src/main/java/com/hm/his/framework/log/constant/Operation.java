package com.hm.his.framework.log.constant;

import java.util.HashMap;
import java.util.Map;

/**
 *  功能描述：具体操作静态常量
 * @author:  tangww
 * @createDate   2016/4/5
 *
 */
public class Operation {

	/**
	 * 操作ID和操作名映射
	 */
	public static Map map = new HashMap();

	/**
	 * 未知操作类型
	 */
	public static final int UNKNOWN_VALUE = 0;
	/**
	 * 用户登录云his系统
	 */
	public static final int LOGON_VALUE = 1;

	/**
	 * 用户退出云his系统
	 */
	public static final int LOGOFF_VALUE = 2;

	/**
	 * 登录失败 -记录客户端信息
	 */
	public static final int LOGIN_FAILE = 5;

	/**********************************收费/发药*************************************************/
	/**
	 * 收费
	 */
	public static final int CHARGE = 20001001;
	/**
	 * 退费
	 */
	public static final int REFUND = 20001002;
	/**
	 * 直接售药
	 */
	public static final int SELL_DRUG = 20001003;
	/**
	 * 创建订单
	 */
	public static final int CREATE_ORDER = 20001004;
	/**
	 * 新增或修改订单检查项
	 */
	public static final int SAVE_ORDER_EXAM = 20001005;
	/**
	 * 新增订单检查单
	 */
	public static final int ADD_ORDER_EXAM_LIST = 20001006;
	/**
	 * 新增或修改西药处方
	 */
	public static final int SAVE_ORDER_PATENT_PRESCRIPTION = 20001007;
	/**
	 * 新增或修改中药处方
	 */
	public static final int SAVE_ORDER_CHINESE_PRESCRIPTION = 20001008;
	/**
	 * 新增或修改附加费用项
	 */
	public static final int SAVE_ORDER_ADDITION_AMT = 20001009;
	/**
	 *  新增附加费用单
	 */
	public static final int ADD_ORDER_ADDITION_AMT_LIST = 20001010;
	/**
	 * 删除订单中的具体项
	 */
	public static final int DELETE_RECORD_ITEM = 20001011;
	/**
	 * 删除订单中的单（检查单，处方单，附加费用单）以及单中的项
	 */
	public static final int DELETE_ALL_RECORD_ITEMS = 20001012;

	/******************************药品管理*****************************************************/

	/**
	 * 增加药品
	 */
	public static final int DRUG_ADD = 30001001;

	/**
	 * 修改药品
	 */
	public static final int DRUG_MODIFY = 30001002;

	/**
	 * 修改药品库存和价格
	 */
	public static final int DRUG_MODIFY_PRICE = 30001004;

	/**
	 * 删除药品
	 */
	public static final int DRUG_DELETE = 30001003;

	/**
	 * 增加模版
	 */
	public static final int TEMPLATE_ADD = 40001001;
	/**
	 * 修改模版
	 */
	public static final int TEMPLATE_MODIFY = 40001002;
	/**
	 * 删除模版
	 */
	public static final int TEMPLATE_DELETE = 40001003;
	
	/******************微信绑定**********************/
	/**
	 * 绑定诊所
	 */
	public static final int WEIXIN_BOUND = 50001001;
	
	/**
	 * 解除绑定
	 */
	public static final int WEIXIN_RESCISSION = 50001002;
	
	/**
	 * 更新绑定
	 */
	public static final int WEIXIN_UPDATE_BOUND = 50001003;


	/******************************批次入库*****************************************************/

	/**
	 * 增加批次入库
	 */
	public static final int BATCH_INSTOCK_ADD = 80001001;

	/**
	 * 修改批次入库
	 */
	public static final int BATCH_INSTOCK_MODIFY = 80001002;
	
	/**
	 * 新增门诊记录
	 */
	public static final int OUTPATIENT_ADD = 10001001;
	
	/**
	 * 修改门诊记录
	 */
	public static final int OUTPATIENT_MODIFY = 10001002;
	
	/**
	 * 新增患者
	 */
	public static final int OUTPATIENT_PATIENT_ADD = 10001003;
	
	/**
	 * 修改患者
	 */
	public static final int OUTPATIENT_PATIENT_MODIFY = 10001004;
	
	/**
	 * 新增诊所人员
	 */
	public static final int CLINIC_DOCTOR_ADD = 60001001;
	
	/**
	 * 修改诊所人员
	 */
	public static final int CLINIC_DOCTOR_MODIFY = 60001002;
	
	/**
	 * 删除诊所人员
	 */
	public static final int CLINIC_DOCTOR_DELETE = 60001003;

	/**
	 * 科室新增
	 */
	public static final int CLINIC_LABORATORY_ADD = 60001004;
	
	/**
	 * 科室修改
	 */
	public static final int CLINIC_LABORATORY_MODIFY = 60001005;
	
	/**
	 * 科室删除
	 */
	public static final int CLINIC_LABORATORY_DELETE = 60001006;
	
	/**
	 * 检查治疗项新增
	 */
	public static final int CLINIC_EXAM_ADD = 60001007;
	
	/**
	 * 检查治疗项修改
	 */
	public static final int CLINIC_EXAM_MODIFY = 60001008;
	
	/**
	 * 检查治疗项删除
	 */
	public static final int CLINIC_EXAM_DELETE = 60001009;
	
	static {
		map.put(LOGON_VALUE, "登录");
		map.put(LOGOFF_VALUE, "退出");
		map.put(LOGIN_FAILE,"登录失败");
		map.put(DRUG_ADD, "增加药品");
		map.put(DRUG_MODIFY, "修改药品");
		map.put(DRUG_MODIFY_PRICE, "修改药品库存价格");

		map.put(DRUG_DELETE, "删除药品");
		map.put(TEMPLATE_ADD, "增加模版");
		map.put(TEMPLATE_MODIFY, "修改模版");
		map.put(TEMPLATE_DELETE, "删除模版");
		
		

		// 收费发药 start
		map.put(CHARGE, "收费");
		map.put(REFUND, "退费");
		map.put(SELL_DRUG, "直接售药");
		map.put(CREATE_ORDER, "创建订单");
		map.put(SAVE_ORDER_EXAM, "保存检查项");
		map.put(ADD_ORDER_EXAM_LIST, "新增检查单");
		map.put(SAVE_ORDER_PATENT_PRESCRIPTION, "保存西药处方");
		map.put(SAVE_ORDER_CHINESE_PRESCRIPTION, "保存中药处方");
		map.put(SAVE_ORDER_ADDITION_AMT, "保存附加费用项");
		map.put(ADD_ORDER_ADDITION_AMT_LIST, "新增附加费用单");
		map.put(DELETE_RECORD_ITEM, "删除订单中的项");
		map.put(DELETE_ALL_RECORD_ITEMS, "删除订单中的单及其项");
		// 收费发药 end
		
		// 微信公众号
		map.put(WEIXIN_BOUND, "绑定诊所");
		map.put(WEIXIN_RESCISSION, "解除绑定");
		map.put(WEIXIN_UPDATE_BOUND, "更新绑定");

		map.put(BATCH_INSTOCK_ADD, "增加批次入库");
		map.put(BATCH_INSTOCK_MODIFY, "修改批次入库");
		
		//门诊
		map.put(OUTPATIENT_ADD, "新增门诊记录");
		map.put(OUTPATIENT_MODIFY, "修改门诊记录");
		map.put(OUTPATIENT_PATIENT_ADD, "新增患者");
		map.put(OUTPATIENT_PATIENT_MODIFY, "修改患者");
		
		//诊所管理
		map.put(CLINIC_DOCTOR_ADD, "诊所人员新增");
		map.put(CLINIC_DOCTOR_MODIFY, "诊所人员修改");
		map.put(CLINIC_DOCTOR_DELETE, "诊所人员删除");
		
		map.put(CLINIC_LABORATORY_ADD, "诊所科室新增");
		map.put(CLINIC_LABORATORY_MODIFY, "诊所科室修改");
		map.put(CLINIC_LABORATORY_DELETE, "诊所科室删除");
		
		map.put(CLINIC_EXAM_ADD, "诊所科室新增");
		map.put(CLINIC_EXAM_MODIFY, "诊所科室修改");
		map.put(CLINIC_EXAM_DELETE, "诊所科室删除");
	}

	/**
	 * 
	 * Definition: 通过操作ID获取操作中文名
	 * author: tangwenwu
	 * Created date:  2015-4-5
	 * @param operationId
	 * @return
	 */
	public static String getOperationName(int operationId) {
		return (String) map.get(operationId);
	}

}
