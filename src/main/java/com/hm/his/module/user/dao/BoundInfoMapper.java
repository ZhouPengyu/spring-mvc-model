package com.hm.his.module.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.user.model.BoundInfo;

public interface BoundInfoMapper {
	
	/**
	 * <p>Description:获取我的诊所<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:08
	 */
	List<BoundInfo> getBoundByOpenId(String openId);

	List<BoundInfo> searchBoundForUpdatePwd();
	
	/**
	 * <p>Description:绑定诊所<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:19
	 */
	Integer insertBoundInfo(BoundInfo boundInfo);
    
	/**
	 * <p>Description:取消绑定<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:45
	 */
	Integer rescissionHospital(@Param("openId")String openId, @Param("hospitalId")Long hospitalId);

	/**
	 * <p>Description:更新绑定信息<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:54
	 */
	Integer updateBoundInfo(BoundInfo boundInfo);

	int updateByPrimaryKeySelective(BoundInfo record);
	
	/**
	 * <p>Description:判断是否绑定<p>
	 * @author ZhouPengyu
	 * @date 2016年5月11日 下午4:17:40
	 */
	Integer isBoundingByDoctor(@Param("openId")String openId, @Param("doctorName")String doctorName);
	
	/**
	 * <p>Description:获取医生ID<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:45
	 */
	Long getDoctorId(@Param("openId")String openId, @Param("hospitalId")Long hospitalId);
	
	/**
	 * <p>Description:判断是否绑定<p>
	 * @author ZhouPengyu
	 * @date 2016年5月4日 上午10:52:45
	 */
	Integer isBoundingByHospital(@Param("openId")String openId, @Param("hospitalId")Long hospitalId);
}