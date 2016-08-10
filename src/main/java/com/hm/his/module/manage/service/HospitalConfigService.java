package com.hm.his.module.manage.service;


import com.hm.his.module.manage.model.HospitalConfig;

/**
 * @author Tangwenwu
 * @company H.M
 * @date 2016-3-2 15:16:05
 * @description 诊所配置服务接口
 * @version 3.0
 */
public interface HospitalConfigService {
	
	/**
	 * <p>Description:根据诊所ID和配置类型查询医院配置信息<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-3-23 10:29:22
	 */
	HospitalConfig searchHospitalConfigList(HospitalConfig hospitalConfig) throws Exception;
	/**
	 * <p>Description:登录时，把诊所配置信息设置 到sessin 中<p>
	 * <p>Company: H.M<p>
	 * @author tangwenwu
	 * @date 2016-6-16 10:29:22
	 */
//	void loginInitConfigToSession();

	Integer getHospitalIsUseBatchManage();

	HospitalConfig putUseBatchManageToSession(HospitalConfig hospConfig) throws Exception;

	/**
	 * <p>Description:诊所保存<p>
	 * <p>Company: H.M<p>
	 * @author ZhouPengyu
	 * @date 2016-1-20 上午11:09:32
	 */
	boolean saveHospitalConfig(HospitalConfig hospitalConfig) throws Exception;
	

	
}
