package com.hm.his.module.manage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.manage.model.HospitalExam;


/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 医院检查结果 数据库实现类
 * @version 3.0
 */
public interface HospitalExamMapper {
	
	Integer insertHospitalExam(HospitalExam hospitalExam);
	
	Integer deleteHospitalExam(@Param("examId")Long examId, @Param("hospitalId")Long hospitalId);
	
	Integer updateHospitalExam(HospitalExam hospitalExam);
	
	/**
	 * <p>Description:查询医院下检查项<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:12:33
	 */
	List<HospitalExam> searchHospitalExam(Map<String, Object> requestParams);

	List<HospitalExam> searchHospitalExamByIds(List<Long> examIds);
	
	/**
	 * <p>Description:查询医院下检查项总数<p>
	 * @author ZhouPengyu
	 * @date 2016年3月18日 上午11:29:33
	 */
	Integer searchHospitalExamTotal(Map<String, Object> requestParams);
	
	/**
	 * <p>Description:验证检查项重名<p>
	 * @author ZhouPengyu
	 * @date 2016年3月12日 下午7:29:11
	 */
	Integer verifyExamName(@Param("examName")String examName, @Param("hospitalId")Long hospitalId);
}
