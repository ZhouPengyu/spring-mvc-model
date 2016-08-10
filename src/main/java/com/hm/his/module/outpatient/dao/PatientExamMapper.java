package com.hm.his.module.outpatient.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hm.his.module.outpatient.model.PatientExam;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 患者检查结果 数据库实现类
 * @version 3.0
 */
public interface PatientExamMapper {
	
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
	 * <p>Description:批量修改病历检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
	Long batchUpdateExam(List<PatientExam> patientExams);

    /**
	 * <p>Description:删除病历检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
    Long deleteExam(Long patientExamId);

    Long batchDeleteExam(List<Long> exams);
	/**
	 * <p>Description:根据病历ID查询检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
    public List<PatientExam> getExamByRecordId(Long recordId);
    
    /**
	 * <p>Description:批量插入病历检查结果<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午4:31:37
	 */
    public Integer insertExamList(List<PatientExam> examResultList);
    
    /**
     * <p>Description:根据病历删除检查<p>
     * @author ZhouPengyu
     * @date 2016年3月12日 下午2:38:04
     */
    public void delExamByRecordId(Long recordId);
}
