package com.hm.his.module.outpatient.dao;

import java.util.List;

import com.hm.his.module.outpatient.model.Diagnosis;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-7 10:55:16
 * @description 诊断 数据库实现类
 * @version 3.0
 */
public interface DiagnosisMapper {
	/**
	 * <p>Description:根据名称查询诊断<p>
	 * @author ZhouPengyu
	 * @date 2016年3月8日 下午3:45:54
	 */
    List<Diagnosis> fuzzyQueryByName(String diseaseName);
    
    /**
     * <p>Description:默认疾病集合<p>
     * <p>Company: H.M<p>
     * @author ZhouPengyu
     * @date 2015-12-30 上午10:35:11
     */
    List<Diagnosis> fuzzyQueryByDefault();

    /**
     * <p>根据诊断id查询诊断<p>
     * <p>Company: H.M<p>
     * @author wangjialin
     * @date 2015-12-30 下午08:00:11
     */
    List<Diagnosis> getDiagnosisByIdList(List<Long> idList);
}
