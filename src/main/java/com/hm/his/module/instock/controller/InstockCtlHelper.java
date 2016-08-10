package com.hm.his.module.instock.controller;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.drug.model.HospitalDrug;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-05-06
 * Time: 15:01
 * CopyRight:HuiMei Engine
 */
public class InstockCtlHelper {


    protected static boolean checkPostDrugData(@RequestBody HospitalDrug hospitalDrug, HisResponse hisResponse) {
        if(StringUtils.isBlank(hospitalDrug.getDrugName())){
            hisResponse.setErrorCode(50003L);
            hisResponse.setErrorMessage("药品名称不能为空");
            return true;
        }
        // 产品需求倒退，暂时注释
//        if (null != hospitalDrug.getSpecMinimumDosage()) {
//            if(hospitalDrug.getSpecMinimumDosage() < 0){
//                hisResponse.setErrorCode(50004L);
//                hisResponse.setErrorMessage("最小剂量必须是数字,且不能为负数");
//                return hisResponse.toString();
//            }
//            if (StringUtils.isBlank(hospitalDrug.getSpecMinimumUnit())) {
//                hisResponse.setErrorCode(50005L);
//                hisResponse.setErrorMessage("最小剂量不为空时，最小单位为必填");
//                return hisResponse.toString();
//            }
//        }
//        if(StringUtils.isBlank(hospitalDrug.getSpecUnit())){
//            hisResponse.setErrorCode(50006L);
//            hisResponse.setErrorMessage("规格单位不能为空");
//            return hisResponse.toString();
//        }

        if (null != hospitalDrug.getSpecUnitaryRatio()) {
            if (hospitalDrug.getSpecUnitaryRatio() < 1) {
                hisResponse.setErrorCode(50007L);
                hisResponse.setErrorMessage("换算比不是正整数");
                return true;
            }
        }

        if (StringUtils.isBlank(hospitalDrug.getSpecPackageUnit())) {
            hisResponse.setErrorCode(50009L);
            hisResponse.setErrorMessage("包装单位不能为空");
            return true;
        }

        // 若药品类型为饮片或耗材时，必填项为：名称、包装单位  --生产厂家 不必填
        if(hospitalDrug.getDrugType() == null){
            hisResponse.setErrorCode(50009L);
            hisResponse.setErrorMessage("药品类型不能为空");
            return true;
        }else if(hospitalDrug.getDrugType()==2 || hospitalDrug.getDrugType()==3 || hospitalDrug.getDrugType()==5){
            if (StringUtils.isBlank(hospitalDrug.getManufacturer())) {
                hisResponse.setErrorCode(50010L);
                hisResponse.setErrorMessage("生产厂家不能为空");
                return true;
            }
        }

        //"库存",
        if (null != hospitalDrug.getInventoryCount()) {

            if(hospitalDrug.getInventoryCount() <0){
                hisResponse.setErrorCode(50011L);
                hisResponse.setErrorMessage("库存不是正整数");
                return true;
            }
        } else {
            hospitalDrug.setInventoryCount(0D);
        }
        //"进货价",
        if (null != hospitalDrug.getPurchasePrice()) {

            if(hospitalDrug.getPurchasePrice() <0){
                hisResponse.setErrorCode(50012L);
                hisResponse.setErrorMessage("进货价必须是数字,且不能为负数");
                return true;
            }
        } else {
            hospitalDrug.setPurchasePrice(0.00);
        }

        //"处方价",
        if (null != hospitalDrug.getPrescriptionPrice()) {

            if(hospitalDrug.getPrescriptionPrice() <0){
                hisResponse.setErrorCode(50013L);
                hisResponse.setErrorMessage("处方价必须是数字,且不能为负数");
                return true;
            }
        } else {
            hospitalDrug.setPrescriptionPrice(0.00);
        }


        return false;
    }


    protected static boolean checkPostDrugData4ModifyInventoryAndPrice(@RequestBody HospitalDrug hospitalDrug, HisResponse hisResponse) {
        if(hospitalDrug.getId() == null){
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("药品ID不能为空");
            return true;
        }
        if(hospitalDrug.getHospitalId() == null){
            hisResponse.setErrorCode(50002L);
            hisResponse.setErrorMessage("医院ID不能为空");
            return true;
        }


        //"库存",
        if (null != hospitalDrug.getAddInventoryCount()) {

            if(hospitalDrug.getAddInventoryCount() <0){
                hisResponse.setErrorCode(50011L);
                hisResponse.setErrorMessage("增加的库存不是正整数");
                return true;
            }
        } else {
            hospitalDrug.setAddInventoryCount(0D);
        }
        //"进货价",
        if (null != hospitalDrug.getPurchasePrice()) {

            if(hospitalDrug.getPurchasePrice() <0){
                hisResponse.setErrorCode(50012L);
                hisResponse.setErrorMessage("进货价必须是数字,且不能为负数");
                return true;
            }
        } else {
            hospitalDrug.setPurchasePrice(0.00);
        }

        //"处方价",
        if (null != hospitalDrug.getPrescriptionPrice()) {

            if(hospitalDrug.getPrescriptionPrice() <0){
                hisResponse.setErrorCode(50013L);
                hisResponse.setErrorMessage("处方价必须是数字,且不能为负数");
                return true;
            }
        } else {
            hospitalDrug.setPrescriptionPrice(0.00);
        }


        return false;
    }



    protected static  LinkedHashMap<String, String> buildMapExportBatchInstockLogHead() {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String,String>();
        fieldMap.put("drugName","药品名称");
        fieldMap.put("validityDateStr","入库日期");
//        fieldMap.put("specification","规格包装描述");
        fieldMap.put("manufacturer","生产厂家");
        fieldMap.put("supplier","供应商");
        fieldMap.put("instockCount","数量");
        fieldMap.put("instockUnit","单位");
        fieldMap.put("purchasePrice","进货价");
        fieldMap.put("totalPrice","金额");
        return fieldMap;
    }


    protected static  LinkedHashMap<String, String> buildMapExportDrugInventoryListHead() {
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String,String>();
        fieldMap.put("drugName","药品名称");
        fieldMap.put("specification","规格包装描述");
        fieldMap.put("manufacturer","生产厂家");
        fieldMap.put("inventoryCount","库存");
        fieldMap.put("saleUnit","库存单位");
//        fieldMap.put("purchasePrice","进货价");
        return fieldMap;
    }

    /**
     * 检查 excel 文件的表头 是否正确
     *
     * @param rwb
     */
    protected static boolean checkExcel(Workbook rwb) {

        Sheet sheet = rwb.getSheet(0);
        int rows = sheet.getRows();// ????
        int columns = sheet.getColumns();// ????
        if(columns>23){
            columns = columns -1;//使用错误文件 导出时，需要删除错误消息列
        }
 /*       String[] heads = new String[] { "药品名称","药品类型","是否OTC","批准文号","条形码","助记码","单次剂量","使用频次",
                "开药量","用法","医嘱","最小剂量","最小单位","规格单位","换算比","包装单位","生产厂家","进货价",
                "处方价","售药价","库存数量","货架码","支持拆零","直接购药","供应商" };*/

        String[] heads = new String[] {"药品名称","分类","是否OTC","批准文号","条形码","规格包装描述",
                "包装单位","生产厂家","是否支持拆零销售","小包装单位","换算比","库存","货架码","进货价","处方价","供应商",
                "用法","使用频次","单次剂量","单次剂量单位","开药量","开药量单位","医嘱"};


        if (rows > 1 && columns == heads.length) {
            for (int i = 0; i < columns; i++) {
                String contents = sheet.getCell(i, 0).getContents();
                if (contents == null || !contents.equals(heads[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }

}
