package com.hm.his.module.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.framework.model.MultipleDataSource;
import com.hm.his.module.manage.dao.ExaminationListMapper;
import com.hm.his.module.manage.model.ExaminationList;
import com.hm.his.module.manage.service.ExaminationListService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-8 15:34:15
 * @description 医院检查接口实现类
 * @version 3.0
 */
@Service("ExaminationListService")
public class ExaminationListServiceImpl implements ExaminationListService{
    @Autowired (required = false)
    ExaminationListMapper examinationListMapper;

	@Override
	public List<ExaminationList> searchByName(String examName) {
		try{
			MultipleDataSource.setDataSourceKey("hmcdss");
			return examinationListMapper.searchByName(examName);
        }finally {
            MultipleDataSource.setDataSourceKey("his");
        }
	}

}
