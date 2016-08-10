package com.hm.his.module.drug.model;

import com.hm.his.framework.log.annotation.HmLogHelper;
import com.hm.his.module.drug.pojo.SaleWayPojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * *    ┏┓　　　┏┓
 * *  ┏┛┻━━━┛┻┓
 * *  ┃　　　　　　　┃
 * *  ┃　　　━　　　┃
 * *  ┃　┳┛　┗┳　┃
 * *  ┃　　　　　　　┃
 * *  ┃　　　┻　　　┃
 * *  ┃　　　　　　　┃
 * *  ┗━┓　　　┏━┛
 * *      ┃　　　┃  神兽保佑
 * *      ┃　　　┃  代码无BUG！
 * *      ┃　　　┗━━━┓
 * *      ┃　　　　　　　┣┓
 * *      ┃　　　　　　　┏┛
 * *      ┗┓┓┏━┳┓┏┛
 * *        ┃┫┫　┃┫┫
 * *        ┗┻┛　┗┻┛
 * * Created by  on 2016/3/23 0023.
 */

/**
 * 诊所药品信息表
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/26
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
public class HospitalDrug implements Serializable{

    @HmLogHelper
    private Long id;//	Int	是	药品ID
    private Long hospitalId;//	int	是	诊所ID
    @HmLogHelper
    private String drugName;//	String	是	药品名称
    private Integer drugType;//	int	是	药品分类   枚举值  饮片：1   中成药：2   化学制剂：3   耗材:4     生物制品:5     其他：6

    private Integer isOtc;//	int	否	是否OTC 1：是 0：否
    private Integer status;//	int	是	状态 1：启用 2：禁用
    private Integer flag;//标识 1：正常 0：删除
    private String approvalNumber;//	String	否	批准文号
    private String barCode;//	String	否	条形码
    private String mnemoniCode;//	String	否	助记码
    private String singleDosage;//	String	否	单次剂量
    private String singleDosageUnit;//	String	否	单次剂量单位
    private String frequency;//	String	否	使用频次
    private Double prescribeAmount;//	Int	否	开药量
    private String prescribeAmountUnit;//	Int	否	开药量单位
    private String instruction;//	String	否	用法
    private String doctorAdvice;//	String	否	医嘱

    private String specification;//规格包装描述
    private Double specMinimumDosage;//	String	否	最小剂量
    private String specMinimumUnit;//	String	否	最小单位
    private String specUnit;//	是	规格单位
    private Integer specUnitaryRatio;//	String	是	换算比
    private String specPackageUnit;//	String	是	包装单位

    private String manufacturer;//	Int	是	生产厂家
    private Double purchasePrice;//	double	是	进货价
    private Double prescriptionPrice;//	double	是	处方价
    private Double salePrice;//	double	否	售药价
    private Integer openStock;//	int	是	支持拆零   1:支持  0：不支持
    private Integer directDrugPurchase;//	int	否	直接购药  1:支持  0：不支持
    private String supplier;//	否	供应商
    private String pinyin;
    private String pinyinFirst;

    private Long hmDrugId; //惠每药物库的药品ID
    private Integer addWay; //增加途径    1：云his     2:公众号his
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


    //--------页面属性--------
    private String drugTypeName;//药品分类名称
    private String useInfo;//使用信息

    private Double inventoryCount;//	int	是	库存统计
    private Double addInventoryCount;//新增加的库存
    private String goodsShelfCode;//	String	否	货架码
    private Integer dataSource;//药品数据源 1：惠每  0：诊所
    private List<SaleWayPojo> saleWays;//售药方式列表
    private String errorMsg;//导入错误信息
    private String saleUnit;//	销售单位
    private String tradeName;// 药物商品名
    private Integer inventoryThreshold;//阈值
    private Integer inventoryCeiling;//库存上限数量
    private Date validityDate;//有效期
    private String validityDateStr;//有效期的字符串格式


    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public Integer getDrugType() {
        return drugType;
    }

    public void setDrugType(Integer drugType) {
        this.drugType = drugType;
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

    public String getMnemoniCode() {
        return mnemoniCode;
    }

    public void setMnemoniCode(String mnemoniCode) {
        this.mnemoniCode = mnemoniCode;
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

    public String getPrescribeAmountUnit() {
        return prescribeAmountUnit;
    }

    public void setPrescribeAmountUnit(String prescribeAmountUnit) {
        this.prescribeAmountUnit = prescribeAmountUnit;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Double getPrescribeAmount() {
        return prescribeAmount;
    }

    public void setPrescribeAmount(Double prescribeAmount) {
        this.prescribeAmount = prescribeAmount;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDoctorAdvice() {
        return doctorAdvice;
    }

    public void setDoctorAdvice(String doctorAdvice) {
        this.doctorAdvice = doctorAdvice;
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

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getPrescriptionPrice() {
        return prescriptionPrice;
    }

    public void setPrescriptionPrice(Double prescriptionPrice) {
        this.prescriptionPrice = prescriptionPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getOpenStock() {
        return openStock;
    }

    public void setOpenStock(Integer openStock) {
        this.openStock = openStock;
    }

    public Integer getDirectDrugPurchase() {
        return directDrugPurchase;
    }

    public void setDirectDrugPurchase(Integer directDrugPurchase) {
        this.directDrugPurchase = directDrugPurchase;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDrugTypeName() {
        return drugTypeName;
    }

    public void setDrugTypeName(String drugTypeName) {
        this.drugTypeName = drugTypeName;
    }

    public String getUseInfo() {
        return useInfo;
    }

    public void setUseInfo(String useInfo) {
        this.useInfo = useInfo;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Double getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Double inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public String getGoodsShelfCode() {
        return goodsShelfCode;
    }

    public void setGoodsShelfCode(String goodsShelfCode) {
        this.goodsShelfCode = goodsShelfCode;
    }

    public Integer getDataSource() {
        return dataSource;
    }

    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }

    public List<SaleWayPojo> getSaleWays() {
        return saleWays;
    }

    public void setSaleWays(List<SaleWayPojo> saleWays) {
        this.saleWays = saleWays;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinFirst() {
        return pinyinFirst;
    }

    public void setPinyinFirst(String pinyinFirst) {
        this.pinyinFirst = pinyinFirst;
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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    public Long getHmDrugId() {
        return hmDrugId;
    }

    public void setHmDrugId(Long hmDrugId) {
        this.hmDrugId = hmDrugId;
    }

    public Integer getAddWay() {
        return addWay;
    }

    public void setAddWay(Integer addWay) {
        this.addWay = addWay;
    }


    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public Double getAddInventoryCount() {
        return addInventoryCount;
    }

    public void setAddInventoryCount(Double addInventoryCount) {
        this.addInventoryCount = addInventoryCount;
    }

    public Integer getInventoryThreshold() {
        return inventoryThreshold;
    }

    public void setInventoryThreshold(Integer inventoryThreshold) {
        this.inventoryThreshold = inventoryThreshold;
    }

    public Integer getInventoryCeiling() {
        return inventoryCeiling;
    }

    public void setInventoryCeiling(Integer inventoryCeiling) {
        this.inventoryCeiling = inventoryCeiling;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public String getValidityDateStr() {
        return validityDateStr;
    }

    public void setValidityDateStr(String validityDateStr) {
        this.validityDateStr = validityDateStr;
    }

    @Override
    public String toString() {
        return "HospitalDrug{" +
                "id=" + id +
                ", hospitalId=" + hospitalId +
                ", drugName='" + drugName + '\'' +
                ", drugType=" + drugType +
                ", isOtc=" + isOtc +
                ", status=" + status +
                ", flag=" + flag +
                ", approvalNumber='" + approvalNumber + '\'' +
                ", barCode='" + barCode + '\'' +
                ", mnemoniCode='" + mnemoniCode + '\'' +
                ", singleDosage='" + singleDosage + '\'' +
                ", frequency='" + frequency + '\'' +
                ", prescribeAmount=" + prescribeAmount +

                ", doctorAdvice='" + doctorAdvice + '\'' +
                ", specMinimumDosage=" + specMinimumDosage +
                ", specMinimumUnit='" + specMinimumUnit + '\'' +
                ", specUnit='" + specUnit + '\'' +
                ", specUnitaryRatio=" + specUnitaryRatio +
                ", specPackageUnit='" + specPackageUnit + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", prescriptionPrice=" + prescriptionPrice +
                ", salePrice=" + salePrice +
                ", openStock=" + openStock +
                ", directDrugPurchase=" + directDrugPurchase +
                ", supplier='" + supplier + '\'' +
                ", drugTypeName='" + drugTypeName + '\'' +
                ", useInfo='" + useInfo + '\'' +
                ", specification='" + specification + '\'' +
                ", inventoryCount=" + inventoryCount +
                ", goodsShelfCode='" + goodsShelfCode + '\'' +
                ", dataSource=" + dataSource +
                ", saleWays=" + saleWays +
                '}';
    }
}
