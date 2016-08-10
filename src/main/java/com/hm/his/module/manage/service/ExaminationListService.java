package com.hm.his.module.manage.service;

import java.util.List;

import com.hm.his.module.manage.model.ExaminationList;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-8 15:34:15
 * @description 医院检查服务接口
 * @version 3.0
 */
public interface ExaminationListService {
	
	List<ExaminationList> searchByName(String examName) throws Exception;
	
}
