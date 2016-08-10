package com.hm.his.module.manage.service;

import java.util.List;
import java.util.Map;

import com.hm.his.module.manage.model.HospitalExam;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-8 15:34:15
 * @description 医院检查服务接口
 * @version 3.0
 */
public interface HospitalExamService {
	
	Integer insertHospitalExam(HospitalExam hospitalExam) throws Exception;
	
	Integer deleteHospitalExam(Long examId, Long hospitalId) throws Exception;
	
	Integer updateHospitalExam(HospitalExam hospitalExam) throws Exception;
	
	/**
	 * <p>Description:查询医院下检查项<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:12:33
	 */
	List<HospitalExam> searchHospitalExam(Map<String, Object> requestParams) throws Exception;

	/**
	 * <p>Description:查询医院下检查项<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午5:12:33
	 */
	List<HospitalExam> searchHospitalExamByIds(List<Long> examIds) throws Exception;
	
	/**
	 * <p>Description:查询医院下检查项总数<p>
	 * @author ZhouPengyu
	 * @date 2016年3月18日 上午11:29:33
	 */
	Integer searchHospitalExamTotal(Map<String, Object> requestParams) throws Exception;
	
	/**
	 * <p>Description:验证检查项重名<p>
	 * @author ZhouPengyu
	 * @date 2016年3月12日 下午7:29:11
	 */
	Integer verifyExamName(String examName, Long hospitalId) throws Exception;
	
	/**
     * <p>Description:插入默认检查<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午2:38:04
     */
    public void insertDefaultExam(Long hospitalId, Long doctorId) throws Exception;
	
}
