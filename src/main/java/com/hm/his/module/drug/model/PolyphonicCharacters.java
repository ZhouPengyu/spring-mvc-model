package com.hm.his.module.drug.model;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 药品名称 -多音字对象
 * User: TangWenWu
 * Date: 2016-07-08
 * Time: 16:46
 * CopyRight:HuiMei Engine
 */
public class PolyphonicCharacters {

    public static Map<String,PolyphonicCharacters>  polyCharaMap = Maps.newHashMap();
    static {
        List<String> polyCharas = Lists.newArrayList();
        polyCharas.add("人参,renshen,rs");
        polyCharas.add("阿胶,ejiao,ej");
        polyCharas.stream().forEach(polyChara ->{
            String[] charArr =polyChara.split(",");
            PolyphonicCharacters polyphonicCharacter = new PolyphonicCharacters();
            polyphonicCharacter.setCnName(charArr[0]);
            polyphonicCharacter.setPinyin(charArr[1]);
            polyphonicCharacter.setPinyinFirst(charArr[2]);
            polyphonicCharacter.setErrorPinyin(PinyinHelper.convertToPinyinString(polyphonicCharacter.getCnName(), "", PinyinFormat.WITHOUT_TONE));
            polyphonicCharacter.setErrorPinyinFirst(PinyinHelper.getShortPinyin(polyphonicCharacter.getCnName()));
            polyCharaMap.put(polyphonicCharacter.getCnName(),polyphonicCharacter);
        });
    }

    private String cnName;
    private String pinyin;
    private String pinyinFirst;

    private String errorPinyin;
    private String errorPinyinFirst;

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
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

    public String getErrorPinyin() {
        return errorPinyin;
    }

    public void setErrorPinyin(String errorPinyin) {
        this.errorPinyin = errorPinyin;
    }

    public String getErrorPinyinFirst() {
        return errorPinyinFirst;
    }

    public void setErrorPinyinFirst(String errorPinyinFirst) {
        this.errorPinyinFirst = errorPinyinFirst;
    }

    @Override
    public String toString() {
        return "PolyphonicCharacters{" +
                "cnName='" + cnName + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", pinyinFirst='" + pinyinFirst + '\'' +
                ", errorPinyin='" + errorPinyin + '\'' +
                ", errorPinyinFirst='" + errorPinyinFirst + '\'' +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(polyCharaMap);
    }
}
