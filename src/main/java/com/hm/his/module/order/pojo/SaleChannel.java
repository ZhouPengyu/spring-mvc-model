package com.hm.his.module.order.pojo;

import java.util.HashMap;
import java.util.Map;

import com.hm.his.module.drug.model.DrugTypeEnum;

/**
 * 
 * @description 销售渠道
 * @author lipeng
 * @date 2016年3月24日
 */
public enum SaleChannel {
	Prescription(1, "处方开药"), Sell(2, "直接售药");
	private SaleChannel(Integer type, String name) {
		this.type = type;
		this.name = name;
	}

	private static Map<Integer, String> map = new HashMap<>();
	static {
		for (SaleChannel e : SaleChannel.values()) {
			map.put(e.type, e.name);
		}
	}
	private Integer type;
	private String name;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static String getNameByType(Integer type) {
		return map.get(type);
	}
}
