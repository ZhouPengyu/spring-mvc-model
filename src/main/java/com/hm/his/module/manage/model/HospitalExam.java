package com.hm.his.module.manage.model;

import com.hm.his.framework.log.annotation.HmLogHelper;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-3-19 16:04:17
 * @description 医院检查实体类
 * @version 3.0
 */
public class HospitalExam {
	@HmLogHelper
    private Long examId;
	@HmLogHelper
    private String examName;	//检查名称
    private String examPhoneticizeName;		//检查拼音
    private String examAbbreviationName;	//检查拼音简写
    private Double examPrice;	//价格
    private Double examCost;	//检查成本
    private Long hospitalId;
    private String createDate;
    private Long creater;
    private String modifyDate;
    private Long modifier;
    private Long flag;
	public Long getExamId() {
		return examId;
	}
	public void setExamId(Long examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getExamPhoneticizeName() {
		return examPhoneticizeName;
	}
	public void setExamPhoneticizeName(String examPhoneticizeName) {
		this.examPhoneticizeName = examPhoneticizeName;
	}
	public String getExamAbbreviationName() {
		return examAbbreviationName;
	}
	public void setExamAbbreviationName(String examAbbreviationName) {
		this.examAbbreviationName = examAbbreviationName;
	}
	public Double getExamPrice() {
		return examPrice;
	}
	public void setExamPrice(Double examPrice) {
		this.examPrice = examPrice;
	}
	public Double getExamCost() {
		return examCost;
	}
	public void setExamCost(Double examCost) {
		this.examCost = examCost;
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

    @Override
    public String toString() {
        return "HospitalExam{" +
                "examId=" + examId +
                ", examName='" + examName + '\'' +
                ", examPhoneticizeName='" + examPhoneticizeName + '\'' +
                ", examAbbreviationName='" + examAbbreviationName + '\'' +
                ", examPrice=" + examPrice +
                ", hospitalId=" + hospitalId +
                ", createDate='" + createDate + '\'' +
                ", creater=" + creater +
                ", modifyDate='" + modifyDate + '\'' +
                ", modifier=" + modifier +
                ", flag=" + flag +
                '}';
    }
}
