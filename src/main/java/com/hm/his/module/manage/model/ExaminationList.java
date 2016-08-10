package com.hm.his.module.manage.model;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-19 16:04:17
 * @description 惠每检查实体类
 * @version 3.0
 */
public class ExaminationList {
    private Long examId;
    private String examType;
    private String examName;
    private Long effectiveDuration;
    private String examDescribe;
    private String examSite;
    private String queryIndexpyall;
    private String queryIndexpysimple;
    private String createDate;
    private String modifyDate;
    
	public Long getExamId() {
		return examId;
	}
	public void setExamId(Long examId) {
		this.examId = examId;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Long getEffectiveDuration() {
		return effectiveDuration;
	}
	public void setEffectiveDuration(Long effectiveDuration) {
		this.effectiveDuration = effectiveDuration;
	}
	public String getExamDescribe() {
		return examDescribe;
	}
	public void setExamDescribe(String examDescribe) {
		this.examDescribe = examDescribe;
	}
	public String getExamSite() {
		return examSite;
	}
	public void setExamSite(String examSite) {
		this.examSite = examSite;
	}
	public String getQueryIndexpyall() {
		return queryIndexpyall;
	}
	public void setQueryIndexpyall(String queryIndexpyall) {
		this.queryIndexpyall = queryIndexpyall;
	}
	public String getQueryIndexpysimple() {
		return queryIndexpysimple;
	}
	public void setQueryIndexpysimple(String queryIndexpysimple) {
		this.queryIndexpysimple = queryIndexpysimple;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
    
}
