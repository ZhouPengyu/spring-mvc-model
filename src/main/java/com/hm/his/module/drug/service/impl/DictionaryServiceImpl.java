package com.hm.his.module.drug.service.impl;

import com.google.common.collect.Lists;
import com.hm.his.module.drug.dao.DictionaryMapper;
import com.hm.his.module.drug.dao.DrugManufacturerMapper;
import com.hm.his.module.drug.model.Dictionary;
import com.hm.his.module.drug.model.DrugManufacturer;
import com.hm.his.module.drug.service.DictionaryService;
import com.hm.his.module.drug.service.DrugManufacturerService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/4
 * Time: 18:04
 * CopyRight:HuiMei Engine
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired(required = false)
    DictionaryMapper dictionaryMapper;

    private static  List<Dictionary> dictionaries = null;

    @Override
    public List<Dictionary> searchDictionary(Dictionary dictionary) {
        if(CollectionUtils.isEmpty(dictionaries)){
            dictionaries =  dictionaryMapper.selectDictionary(null);
        }
        return dictionaries;
    }

    @Override
    public List<Dictionary> searchDefaultDictionary(Integer type){
        dictionaries = this.searchDictionary(null);
        List<Dictionary> dictionaryList = dictionaries.stream()
                .filter(dictionary -> dictionary.getType().equals(type))
                .sorted(Comparator.comparing(Dictionary::getSort))
                .collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(dictionaryList)&& dictionaryList.size()>5){
//            return dictionaryList.subList(0,5);
//        }
        return dictionaryList;
    }

    @Override
    public List<Dictionary> searchDictionaryByName(Integer type,String name){
        dictionaries = this.searchDictionary(null);
        List<Dictionary> dictionaryList = dictionaries.stream()
                .filter(dictionary -> dictionary.getType().equals(type))
                .filter(dict ->dict.getCnName().contains(name.trim()))
                .sorted(Comparator.comparing(Dictionary::getSort))
                .collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(dictionaryList)&& dictionaryList.size()>5){
//            return dictionaryList.subList(0,5);
//        }
        return dictionaryList;
    }
}
