package com.hm.his.module.manage.dao;

import java.util.List;

import com.hm.his.module.manage.model.ExaminationList;



/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 惠每检查结果 数据库实现类
 * @version 3.0
 */
public interface ExaminationListMapper {
	
	List<ExaminationList> searchByName(String examName);
	
}
