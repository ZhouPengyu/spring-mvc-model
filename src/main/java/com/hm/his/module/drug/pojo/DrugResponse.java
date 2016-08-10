package com.hm.his.module.drug.pojo;

import com.hm.his.module.drug.model.HospitalDrug;

import java.util.List;

/**
 *  药品管理 response 对象
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/29
 * Time: 15:09
 * CopyRight:HuiMei Engine
 */
public class DrugResponse {
    private Integer version;
    private Integer matchRule; //匹配规则： 1：精确匹配  2：多个结果,sug层选择
    private Object drugInfo; //PC端药品信息
    private List<HospitalDrugSug> drugList;//PC端药品列表
    private List<HospitalDrug> hospitalDrugs;// WX端针药品列表
    private Integer dataSource;//检索的药品数据源类型  1：惠每 –新增药品自动载入信息   0：诊所 –已有药品 转到药品入库 -1：没有找到结果 –完全新增


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(Integer matchRule) {
        this.matchRule = matchRule;
    }

    public Object getDrugInfo() {
        return drugInfo;
    }

    public void setDrugInfo(Object drugInfo) {
        this.drugInfo = drugInfo;
    }

    public List<HospitalDrugSug> getDrugList() {
        return drugList;
    }

    public void setDrugList(List<HospitalDrugSug> drugList) {
        this.drugList = drugList;
    }

    public List<HospitalDrug> getHospitalDrugs() {
        return hospitalDrugs;
    }

    public void setHospitalDrugs(List<HospitalDrug> hospitalDrugs) {
        this.hospitalDrugs = hospitalDrugs;
    }

    public Integer getDataSource() {
        return dataSource;
    }

    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }
}
