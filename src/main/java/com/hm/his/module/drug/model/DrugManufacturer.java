package com.hm.his.module.drug.model;

/**
 * 药品厂商信息表
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/26
 * Time: 14:48
 * CopyRight:HuiMei Engine
 */
public class DrugManufacturer {
    private Long id;
    private String cnManufacturer;
    private String cnManufacturerAddr;
    private String enManufacturer;
    private String enManufacturerAddr;

    private String pinyin;
    private String pinyinFirst;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnManufacturer() {
        return cnManufacturer;
    }

    public void setCnManufacturer(String cnManufacturer) {
        this.cnManufacturer = cnManufacturer;
    }

    public String getCnManufacturerAddr() {
        return cnManufacturerAddr;
    }

    public void setCnManufacturerAddr(String cnManufacturerAddr) {
        this.cnManufacturerAddr = cnManufacturerAddr;
    }



    public String getEnManufacturer() {
        return enManufacturer;
    }

    public void setEnManufacturer(String enManufacturer) {
        this.enManufacturer = enManufacturer;
    }

    public String getEnManufacturerAddr() {
        return enManufacturerAddr;
    }

    public void setEnManufacturerAddr(String enManufacturerAddr) {
        this.enManufacturerAddr = enManufacturerAddr;
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

    @Override
    public String toString() {
        return "DrugManufacturer{" +
                "id=" + id +
                ", cnManufacturer='" + cnManufacturer + '\'' +
                ", cnManufacturerAddr='" + cnManufacturerAddr + '\'' +
                ", enManufacturer='" + enManufacturer + '\'' +
                ", enManufacturerAddr='" + enManufacturerAddr + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", pinyinFirst='" + pinyinFirst + '\'' +
                '}';
    }
}
