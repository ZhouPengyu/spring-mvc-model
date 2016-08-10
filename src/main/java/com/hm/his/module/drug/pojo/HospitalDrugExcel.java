package com.hm.his.module.drug.pojo;

import com.hm.his.module.drug.model.HospitalDrug;

import java.util.Date;
import java.util.List;

/**
 * 诊所药品--仅用于Excel导入导出使用
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/26
 * Time: 14:42
 * CopyRight:HuiMei Engine
 */
public class HospitalDrugExcel extends HospitalDrug{

    private String drugTypeStr;//	int	是	药品分类   枚举值  饮片：1  中成药：2 西药：3 耗材:4

    private String isOtcStr;//	int	否	是否OTC 1：是 0：否

    private String openStockStr;//	int	是	支持拆零   1:支持  0：不支持


    public String getDrugTypeStr() {
        return drugTypeStr;
    }

    public void setDrugTypeStr(String drugTypeStr) {
        this.drugTypeStr = drugTypeStr;
    }

    public String getIsOtcStr() {
        return isOtcStr;
    }

    public void setIsOtcStr(String isOtcStr) {
        this.isOtcStr = isOtcStr;
    }

    public String getOpenStockStr() {
        return openStockStr;
    }

    public void setOpenStockStr(String openStockStr) {
        this.openStockStr = openStockStr;
    }


}
