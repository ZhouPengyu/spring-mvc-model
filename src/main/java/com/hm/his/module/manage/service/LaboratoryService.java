package com.hm.his.module.manage.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.manage.model.Laboratory;


/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 医院科室 接口类
 * @version 3.0
 */
public interface LaboratoryService {
	
	Integer insertLaboratory(Laboratory laboratory) throws Exception;
	
	Integer deleteLaboratory(@Param("LaboratoryId")Long LaboratoryId, @Param("hospitalId")Long hospitalId) throws Exception;
	
	Integer updateLaboratory(Laboratory laboratory) throws Exception;
	
	/**
	 * <p>Description:查询医院科室<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:12:33
	 */
	List<Laboratory> searchLaboratory(Map<String, Object> requestParams) throws Exception;
	
	/**
	 * <p>Description:查询医院科室总数<p>
	 * @author ZhouPengyu
	 * @date 2016年3月18日 上午11:29:33
	 */
	Integer searchLaboratoryTotal(Map<String, Object> requestParams) throws Exception;
	
	/**
	 * <p>Description:验证科室重名<p>
	 * @author ZhouPengyu
	 * @date 2016年3月12日 下午7:29:11
	 */
	Integer verifyLaboratoryName(@Param("laboratoryName")String laboratoryName, @Param("hospitalId")Long hospitalId) throws Exception;
}
