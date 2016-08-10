package com.hm.his.module.drug.pojo;

import java.util.List;

/**
 * 药品信息搜索sug POJO
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/26
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
public class HospitalDrugSug {
    private Long id;//	Int	是	药品ID
    private Long hospitalId;//	int	是	诊所ID
    private Long hmDrugId; //惠每药物库的药品ID
    private String drugName;//	String	是	药品名称
    private Integer drugType;//	int	是	药品分类   枚举值  饮片：1  中成药：2 西药：3 耗材:4


    private String frequency;//	String	否	使用频次
    private String singleDosage;//	String	否	单次剂量
    private String singleDosageUnit;//	String	否	单次剂量单位
    private String usage;//	String	否	用法
    private String doctorAdvice;//	String	否	医嘱
    private String saleUnit;//		销售单位
    private String manufacturer;//	Int	是	生产厂家

    private Integer openStock;//	int	是	支持拆零

    private String specification;//规格
    private Integer inventoryCount;//	int	是	库存统计
    private Integer dataSource;//药品数据源 1：惠每  0：诊所
    private String barCode;//	String	否	条形码
    private List<SaleWayPojo> saleWays;//售药方式列表

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDoctorAdvice() {
        return doctorAdvice;
    }

    public void setDoctorAdvice(String doctorAdvice) {
        this.doctorAdvice = doctorAdvice;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getOpenStock() {
        return openStock;
    }

    public void setOpenStock(Integer openStock) {
        this.openStock = openStock;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Integer getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Integer inventoryCount) {
        this.inventoryCount = inventoryCount;
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


    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Long getHmDrugId() {
        return hmDrugId;
    }

    public void setHmDrugId(Long hmDrugId) {
        this.hmDrugId = hmDrugId;
    }

    @Override
    public String toString() {
        return "HospitalDrugSug{" +
                "id=" + id +
                ", hospitalId=" + hospitalId +
                ", drugName='" + drugName + '\'' +
                ", drugType=" + drugType +
                ", frequency='" + frequency + '\'' +
                ", usage='" + usage + '\'' +
                ", doctorAdvice='" + doctorAdvice + '\'' +
                ", saleUnit='" + saleUnit + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", openStock=" + openStock +
                ", specification='" + specification + '\'' +
                ", inventoryCount=" + inventoryCount +
                ", dataSource=" + dataSource +
                ", saleWays=" + saleWays +
                '}';
    }
}
