package com.hm.his.module.drug.model;

public class DrugTradeSimple {
    private Integer id;

    private String commonName;

    private String tradeName;

    private String commonPinyin;

    private String commonPinyinFirst;

    private String tradePinyin;

    private String tradePinyinFirst;

    private String drugNames;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName == null ? null : commonName.trim();
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName == null ? null : tradeName.trim();
    }

    public String getCommonPinyin() {
        return commonPinyin;
    }

    public void setCommonPinyin(String commonPinyin) {
        this.commonPinyin = commonPinyin == null ? null : commonPinyin.trim();
    }

    public String getCommonPinyinFirst() {
        return commonPinyinFirst;
    }

    public void setCommonPinyinFirst(String commonPinyinFirst) {
        this.commonPinyinFirst = commonPinyinFirst == null ? null : commonPinyinFirst.trim();
    }

    public String getTradePinyin() {
        return tradePinyin;
    }

    public void setTradePinyin(String tradePinyin) {
        this.tradePinyin = tradePinyin == null ? null : tradePinyin.trim();
    }

    public String getTradePinyinFirst() {
        return tradePinyinFirst;
    }

    public void setTradePinyinFirst(String tradePinyinFirst) {
        this.tradePinyinFirst = tradePinyinFirst == null ? null : tradePinyinFirst.trim();
    }

    public String getDrugNames() {
        return drugNames;
    }

    public void setDrugNames(String drugNames) {
        this.drugNames = drugNames == null ? null : drugNames.trim();
    }
}