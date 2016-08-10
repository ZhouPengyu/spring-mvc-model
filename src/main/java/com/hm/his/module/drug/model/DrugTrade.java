package com.hm.his.module.drug.model;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.Date;

/**
 *
 * @description HM商品药物表
 * @author tangwenwu
 * @date 2015年11月5日
 */

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？

public class DrugTrade{

	private Long id;
	private String   tradeName  ;  //商品名（中文）
	private String commonName;//	String	是	通用名
	private Integer drugType;//	int	是	药品分类   枚举值  饮片：1   中成药：2   化学制剂：3   耗材:4     生物制品:5   其他：6
	private Double specMinimumDosage;//	String	否	最小剂量
	private String specMinimumUnit;//	String	否	最小单位
	private String specUnit;//	是	规格单位
	private Integer specUnitaryRatio;//	String	是	换算比
	private String specPackageUnit;//	String	是	包装单位
	private String manufacturer;//	Int	是	生产厂家
	private String approvalNumber;//	String	否	批准文号
	private String barCode;//	String	否	条形码

	private String crowd;//	String	否	人群
	private String instruction;//	String	否	用法
	private String frequency;//	String	否	使用频次
	private String singleDosage;//	String	否	单次剂量
	private String singleDosageUnit;//	String	否	单次剂量单位
	private String doctorAdvice;//	String	否	医嘱
	private Integer openStock;//	int	是	支持拆零   1:支持  0：不支持
	private Integer isOtc;//	int	否	是否OTC 1：是 0：否
	private Integer status;//	int	是	状态 1：启用 2：禁用  3：已删除
	private String commonPinyin;
	private String commonPinyinFirst;
	private String tradePinyin;
	private String tradePinyinFirst;

	private Integer drugCommonId; // cdss库中药品通用名ID

	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 創建人id
	 */
	private Long creater;
	/**
	 * 最后修改时间
	 */
	private Date modifyDate;
	/**
	 * 最后修改人id
	 */
	private Long modifier;

	private String specification;//包装规格描述

	private Integer dataSource;//数据来源 0: 惠每自行整理   1： CFDA数据

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public Integer getDrugType() {
		return drugType;
	}

	public void setDrugType(Integer drugType) {
		this.drugType = drugType;
	}

	public Double getSpecMinimumDosage() {
		return specMinimumDosage;
	}

	public void setSpecMinimumDosage(Double specMinimumDosage) {
		this.specMinimumDosage = specMinimumDosage;
	}

	public String getSpecMinimumUnit() {
		return specMinimumUnit;
	}

	public void setSpecMinimumUnit(String specMinimumUnit) {
		this.specMinimumUnit = specMinimumUnit;
	}

	public String getSpecUnit() {
		return specUnit;
	}

	public void setSpecUnit(String specUnit) {
		this.specUnit = specUnit;
	}

	public Integer getSpecUnitaryRatio() {
		return specUnitaryRatio;
	}

	public void setSpecUnitaryRatio(Integer specUnitaryRatio) {
		this.specUnitaryRatio = specUnitaryRatio;
	}

	public String getSpecPackageUnit() {
		return specPackageUnit;
	}

	public void setSpecPackageUnit(String specPackageUnit) {
		this.specPackageUnit = specPackageUnit;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getApprovalNumber() {
		return approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getCrowd() {
		return crowd;
	}

	public void setCrowd(String crowd) {
		this.crowd = crowd;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getSingleDosage() {
		return singleDosage;
	}

	public void setSingleDosage(String singleDosage) {
		this.singleDosage = singleDosage;
	}

	public String getSingleDosageUnit() {
		return singleDosageUnit;
	}

	public void setSingleDosageUnit(String singleDosageUnit) {
		this.singleDosageUnit = singleDosageUnit;
	}

	public String getDoctorAdvice() {
		return doctorAdvice;
	}

	public void setDoctorAdvice(String doctorAdvice) {
		this.doctorAdvice = doctorAdvice;
	}

	public Integer getOpenStock() {
		return openStock;
	}

	public void setOpenStock(Integer openStock) {
		this.openStock = openStock;
	}

	public Integer getIsOtc() {
		return isOtc;
	}

	public void setIsOtc(Integer isOtc) {
		this.isOtc = isOtc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCommonPinyin() {
		return commonPinyin;
	}

	public void setCommonPinyin(String commonPinyin) {
		this.commonPinyin = commonPinyin;
	}

	public String getCommonPinyinFirst() {
		return commonPinyinFirst;
	}

	public void setCommonPinyinFirst(String commonPinyinFirst) {
		this.commonPinyinFirst = commonPinyinFirst;
	}

	public String getTradePinyin() {
		return tradePinyin;
	}

	public void setTradePinyin(String tradePinyin) {
		this.tradePinyin = tradePinyin;
	}

	public String getTradePinyinFirst() {
		return tradePinyinFirst;
	}

	public void setTradePinyinFirst(String tradePinyinFirst) {
		this.tradePinyinFirst = tradePinyinFirst;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public Integer getDrugCommonId() {
		return drugCommonId;
	}

	public void setDrugCommonId(Integer drugCommonId) {
		this.drugCommonId = drugCommonId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getDataSource() {
		return dataSource;
	}

	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	//	public static void main(String[] args) {
//		String commonName = "云南白药痔疮膏";
//		String commonPinyin = PinyinHelper.convertToPinyinString(commonName, "", PinyinFormat.WITHOUT_TONE);
//		String commonPinyinFirst = PinyinHelper.getShortPinyin(commonName);
//		System.out.println(commonPinyin);
//		System.out.println(commonPinyinFirst);
//	}
}
