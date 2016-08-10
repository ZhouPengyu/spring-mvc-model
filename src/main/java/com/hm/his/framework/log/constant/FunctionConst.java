package com.hm.his.framework.log.constant;

import java.util.HashMap;
import java.util.Map;


/**
 *  功能描述：日志类型常量类
 * @author:  tangww
 * @createDate   2016/4/5
 *
 */
public class FunctionConst {
	
	/**
	 * 日志类型ID和日志类型名映射
	 */
	public static Map map = new HashMap();
	
	/**
	 * 未知类型
	 */
	public static final int LOG_UNKNOWN = 0;

	/**
	 * 登录云his日志
	 */
	public static final int LOGON = 1;

	/**
	 * 退出云his日志
	 */
	public static final int LOGOFF = 2;

	/**
	 * 门诊日志
	 */
	public static final int OUTPATIENT_LOG = 1000;

	/**
	 * 收费/发药日志
	 */
	public static final int ORDER_OPER_LOG = 2000;

	/**
	 * 药品管理日志
	 */
	public static final int DRUG_OPER_LOG = 3000;

	/**
	 * 模版日志
	 */
	public static final int TEMPLATE_LOG = 4000;

	/**
	 * 统计日志
	 */
	public static final int STATISTICS_LOG = 5000;

	/**
	 * 诊所管理日志
	 */
	public static final int CLINIC_OPER_LOG = 6000;
	
	/**
	 * 微信相关日志
	 */
	public static final int WEIXIN_LOG = 7000;

	/**
	 * 药品批次入库管理日志
	 */
	public static final int BATCH_INSTOCK_LOG = 8000;

	static {
		map.put(LOG_UNKNOWN, "未知类型");
		map.put(LOGON, "登录云his日志");
		map.put(LOGOFF, "退出云his日志");
		map.put(OUTPATIENT_LOG, "门诊日志");
		map.put(ORDER_OPER_LOG, "收费/发药日志");
		map.put(DRUG_OPER_LOG, "药品管理日志");
		map.put(TEMPLATE_LOG, "模版日志");
		map.put(STATISTICS_LOG, "统计日志");
		map.put(CLINIC_OPER_LOG, "诊所管理日志");
		map.put(WEIXIN_LOG, "微信日志");
		map.put(BATCH_INSTOCK_LOG, "批次入库日志");
	}
	
	/**
	 * 
	 * Definition: 通过日志类型ID获取日志类型中文名
	 * @param logTypeId
	 * @return
	 */
	public static String getLogTypeName(int logTypeId) {
		return (String) map.get(logTypeId);
	}
}
