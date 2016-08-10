package com.hm.his.module.drug.pojo;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;
import com.hm.his.module.drug.model.DrugTypeEnum;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 惠每药物库导入pojo
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/28
 * Time: 10:50
 * CopyRight:HuiMei Engine
 */
public class ExcelDrugTrade {

    private Long id;
    private String tradeName;  //商品名（中文）
    private String commonName;//	String	是	通用名
    private Integer drugType;//	int	是	药品分类   枚举值  饮片：1   中成药：2   化学制剂：3   耗材:4     生物制品:5
    private Double specMinimumDosage;//	String	否	最小剂量
    private String specMinimumUnit;//	String	否	最小单位
    private String specUnit;//	是	规格单位
    private Integer specUnitaryRatio;//	String	是	换算比
    private String specPackageUnit;//	String	是	包装单位
    private String manufacturer;//	Int	是	生产厂家
    private String approvalNumber;//	String	否	批准文号
    private String barCode;//	String	否	条形码

    private Integer cdssDrugCommonId;

    private String commonPinyin;
    private String commonPinyinFirst;
    private String tradePinyin;
    private String tradePinyinFirst;

    private String errorMsg;//

    public static void main(String[] args) throws IOException, BiffException {
//        List<ExcelDrugTrade> drugTrades = ExcelDrugTrade.parseExcel();
//        System.out.println(drugTrades);

        String drugName = "阿莫西林克拉维酸钾分散片(4:1),阿莫西林克拉维酸钾干混悬剂(4:1),阿莫西林克拉维酸钾颗粒(4:1),鼻炎通喷雾剂,古汉养生精口服液,华佗一粒救心丸,黄芪精口服液,混合重组人胰岛素,姜黄消痤搽剂,精蛋白重组人胰岛素混合注射液,口腔溃疡灵胶囊,利血生片,美扑伪麻片,肛泰贴膏,维C金银花露";
        List<String> drugList = Arrays.asList(drugName.split(","));
        drugList.stream().forEach(drug->{
//            System.out.printf("%s   %s   %s \n\r",drug,PinyinHelper.convertToPinyinString(drug, "", PinyinFormat.WITHOUT_TONE),PinyinHelper.getShortPinyin(drug));
            System.out.println(PinyinHelper.getShortPinyin(drug));
        });

    }

    /*public static List<ExcelDrugTrade> parseDrugExcel() throws IOException, BiffException {
//        String checkItem = "尿液分析(尿常规)";
//        String[] checkItems = splitCheckItem(checkItem);
//        System.out.println(Arrays.asList(checkItems));
        // his_drug_test.xls 第一次导入完成后，最大ID: 5605  共 5071 条
        // his_drug_part2.xls 第二次导入完成后，最大ID： 5605
        String examinationItemPath = "D:\\hm\\data_process\\huimei_drug\\his_drug_part3.xls";

        Workbook rwb = Workbook.getWorkbook(new File(examinationItemPath));

        Sheet sheet = rwb.getSheet(0);
        System.out.println("xls==========行数：" + sheet.getRows() + "  列数" + sheet.getColumns());// 得到一共多少行多少列

        List<ExcelDrugTrade> drugTrades = Lists.newArrayList();
        //跳过 第一行
        for (int i = 1; i < sheet.getRows(); i++) {
            ExcelDrugTrade pojo = new ExcelDrugTrade();
            pojo.setTradeName(sheet.getCell(0, i).getContents());
            pojo.setCommonName(sheet.getCell(1, i).getContents());

            drugTrades.add(pojo);
        }
        return drugTrades;
    }*/


    public static List<ExcelDrugTrade> parseExcel() throws IOException, BiffException {
//        String checkItem = "尿液分析(尿常规)";
//        String[] checkItems = splitCheckItem(checkItem);
//        System.out.println(Arrays.asList(checkItems));
    // his_drug_test.xls 第一次导入完成后，最大ID: 5605  共 5071 条
        // his_drug_part2.xls 第二次导入完成后，最大ID： 5605
        String examinationItemPath = "D:\\hm\\data_process\\huimei_drug\\his_drug_part4.xls";

        Workbook rwb = Workbook.getWorkbook(new File(examinationItemPath));

        Sheet sheet = rwb.getSheet(0);
        System.out.println("xls==========行数：" + sheet.getRows() + "  列数" + sheet.getColumns());// 得到一共多少行多少列

        List<ExcelDrugTrade> drugTrades = Lists.newArrayList();
        //跳过 前4行
        for (int i = 4; i < sheet.getRows(); i++) {
            ExcelDrugTrade pojo = new ExcelDrugTrade();
            pojo.setTradeName(sheet.getCell(0, i).getContents());
            pojo.setCommonName(sheet.getCell(1, i).getContents());
            String drugTypeName = sheet.getCell(2, i).getContents();
            if (StringUtils.isNotBlank(drugTypeName)) {
                Integer drugTypeId = DrugTypeEnum.getDrugTypeByName(drugTypeName);
                if (drugTypeId != null) {
                    pojo.setDrugType(drugTypeId);
                } else {
                    System.out.println(drugTypeName + " 错误，无法找到ID，行号=" + i);
                }
            } else {
                System.err.println(drugTypeName + " 药物类型为空，行号=" + i);
            }
            String specMinimumDosage = sheet.getCell(3, i).getContents();
            if (StringUtils.isNotBlank(specMinimumDosage)) {
                pojo.setSpecMinimumDosage(Double.parseDouble(specMinimumDosage));
                pojo.setSpecMinimumUnit(sheet.getCell(4, i).getContents());
            }

            pojo.setSpecUnit(sheet.getCell(5, i).getContents());
            String specUnitaryRatio = sheet.getCell(6, i).getContents();
            if (StringUtils.isNotBlank(specUnitaryRatio)) {
                try {
                    Integer ratio = Integer.parseInt(specUnitaryRatio);
                    pojo.setSpecUnitaryRatio(ratio);
                }catch (Exception e){
                    e.printStackTrace();
                    System.err.println(specUnitaryRatio + " 换算比转换异常，行号=" + i);
                }

            } else {
                System.out.println(specUnitaryRatio + " 换算比为空，行号=" + i);
            }
            pojo.setSpecPackageUnit(sheet.getCell(7, i).getContents());
            pojo.setManufacturer(sheet.getCell(8, i).getContents());
            pojo.setApprovalNumber(sheet.getCell(9, i).getContents());
            pojo.setBarCode(sheet.getCell(10, i).getContents());
            pojo.setCommonPinyin(PinyinHelper.convertToPinyinString(pojo.getCommonName(), "", PinyinFormat.WITHOUT_TONE));
            pojo.setCommonPinyinFirst(PinyinHelper.getShortPinyin(pojo.getCommonName()));
            String cdssDrugCommonId = sheet.getCell(11, i).getContents();
            if(StringUtils.isNotBlank(cdssDrugCommonId)){
                pojo.setCdssDrugCommonId(Integer.parseInt(cdssDrugCommonId));
            }

            if (StringUtils.isNotBlank(pojo.getTradeName())) {
                pojo.setTradePinyin(PinyinHelper.convertToPinyinString(pojo.getTradeName(), "", PinyinFormat.WITHOUT_TONE));
                pojo.setTradePinyinFirst(PinyinHelper.getShortPinyin(pojo.getTradeName()));
            }
            drugTrades.add(pojo);
        }
        return drugTrades;
    }


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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getCdssDrugCommonId() {
        return cdssDrugCommonId;
    }

    public void setCdssDrugCommonId(Integer cdssDrugCommonId) {
        this.cdssDrugCommonId = cdssDrugCommonId;
    }

    @Override
    public String toString() {
        return "ExcelDrugTrade{" +
                "tradePinyinFirst='" + tradePinyinFirst + '\'' +
                ", id=" + id +
                ", tradeName='" + tradeName + '\'' +
                ", commonName='" + commonName + '\'' +
                ", drugType=" + drugType +
                ", specMinimumDosage=" + specMinimumDosage +
                ", specMinimumUnit='" + specMinimumUnit + '\'' +
                ", specUnit='" + specUnit + '\'' +
                ", specUnitaryRatio=" + specUnitaryRatio +
                ", specPackageUnit='" + specPackageUnit + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", approvalNumber='" + approvalNumber + '\'' +
                ", barCode='" + barCode + '\'' +
                ", commonPinyin='" + commonPinyin + '\'' +
                ", commonPinyinFirst='" + commonPinyinFirst + '\'' +
                ", tradePinyin='" + tradePinyin + '\'' +
                '}';
    }
}
