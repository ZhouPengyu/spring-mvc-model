package com.hm.his.framework.log.constant;

import java.util.HashMap;
import java.util.Map;


/**
 *  功能描述：定义操作结果类
 * @author:  tangww
 * @createDate   2016/4/5
 *
 */
public class OperResultConst {
	
	/**
	 * ID对应中文名的映射
	 */
	private static Map map = new HashMap(2);
	
	/**
	 * 操作成功
	 */
	public static final int SUCCESS = 1;

	/**
	 * 操作失败
	 */
	public static final int FAILED = 0;

	/**
	 * 操作发生异常
	 */
	public static final int EXCEPTION = -1;
	
	
	static {
		map.put(SUCCESS, "操作成功");
		map.put(FAILED, "操作失败");
		map.put(EXCEPTION, "操作发生异常");
	}
	
	/**
	 * 
	 * Definition: 通过操作结果类型ID获取操作结果中文名
	 * author: LanChao
	 * Created date: Jan 16, 2012
	 * @param resultId
	 * @return
	 */
	public static String getOperResultName(int resultId) {
		return (String) map.get(resultId);
	}
	
}
