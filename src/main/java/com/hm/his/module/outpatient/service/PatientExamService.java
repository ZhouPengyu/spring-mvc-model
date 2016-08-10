package com.hm.his.module.outpatient.service;

import java.util.List;

import com.hm.his.module.order.model.HospitalOrder;
import com.hm.his.module.outpatient.model.PatientExam;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;

/**
 * Created by wangjialin on 15/12/14.
 */
public interface PatientExamService {

	/**
	 * <p>Description:插入病历检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
	void firstAddInquiryExam(PatientInquiryRequest patientInquiryRequest);

	/**
	 * <p>Description:插入病历检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
    Long insertExam(PatientExam patientExam);
    
    /**
	 * <p>Description:修改病历检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
    Long updateExam(PatientExam patientExam);
    
    /**
	 * <p>Description:删除病历检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
    Long deleteExam(Long patientExamId);
	
	/**
	 * <p>Description:根据病历ID查询检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
    List<PatientExam> getExamByRecordId(Long recordId);
    
    /**
	 * <p>Description:批量插入病历检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
    Integer insertExamList( List<PatientExam> patientExamList);
    
    /**
     * <p>Description:根据病历删除检查<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午2:38:04
     */
    public void delExamByRecordId(Long recordId);
}
