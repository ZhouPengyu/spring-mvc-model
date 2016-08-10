package com.hm.his.module.manage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.manage.model.Laboratory;


/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 医院科室 实现类
 * @version 3.0
 */
public interface LaboratoryMapper {
	
	Integer insertLaboratory(Laboratory laboratory);
	
	Integer deleteLaboratory(@Param("laboratoryId")Long LaboratoryId, @Param("hospitalId")Long hospitalId);
	
	Integer updateLaboratory(Laboratory laboratory);
	
	/**
	 * <p>Description:查询医院科室<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:12:33
	 */
	List<Laboratory> searchLaboratory(Map<String, Object> requestParams);
	
	/**
	 * <p>Description:查询医院科室总数<p>
	 * @author ZhouPengyu
	 * @date 2016年3月18日 上午11:29:33
	 */
	Integer searchLaboratoryTotal(Map<String, Object> requestParams);
	
	/**
	 * <p>Description:验证科室重名<p>
	 * @author ZhouPengyu
	 * @date 2016年3月12日 下午7:29:11
	 */
	Integer verifyLaboratoryName(@Param("laboratoryName")String laboratoryName, @Param("hospitalId")Long hospitalId);
}
