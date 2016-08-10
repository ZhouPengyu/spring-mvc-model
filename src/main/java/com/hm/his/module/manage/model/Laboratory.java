package com.hm.his.module.manage.model;

import com.hm.his.framework.log.annotation.HmLogHelper;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-19 16:04:17
 * @description 医院科室实体类
 * @version 3.0
 */
public class Laboratory {
	@HmLogHelper
	private Long laboratoryId;
	@HmLogHelper
    private String laboratoryName;	//科室名称
    private Long hospitalId;
    private String createDate;
    private Long creater;
    private String modifyDate;
    private Long modifier;
    private Long flag;
    
	public Long getLaboratoryId() {
		return laboratoryId;
	}
	public void setLaboratoryId(Long laboratoryId) {
		this.laboratoryId = laboratoryId;
	}
	public String getLaboratoryName() {
		return laboratoryName;
	}
	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}
	public Long getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Long getCreater() {
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public Long getFlag() {
		return flag;
	}
	public void setFlag(Long flag) {
		this.flag = flag;
	}
}
