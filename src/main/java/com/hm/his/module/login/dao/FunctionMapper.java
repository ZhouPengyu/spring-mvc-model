package com.hm.his.module.login.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.login.model.Function;

public interface FunctionMapper {
	
	/**
	 * <p>Description:根据医生ID查询所有权限<p>
	 * @author ZhouPengyu
	 * @date 2016年3月23日 下午3:04:50
	 */
	List<Function> getDoctorFunction(Long doctorId);
	
	/**
	 * <p>Description:根据医生ID删除用户权限<p>
	 * @author ZhouPengyu
	 * @date 2016年3月23日 下午3:06:24
	 */
	Integer deleteDoctorFunctionByDocId(Long doctorId);
	
	/**
	 * <p>Description:保存用户权限<p>
	 * @author ZhouPengyu
	 * @date 2016年3月23日 下午3:06:24
	 */
	void saveDoctorFunction(@Param("doctorId")Long doctorId, @Param("functionIdList")List<Long> functionIdList);
}
