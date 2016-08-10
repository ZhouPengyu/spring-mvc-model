package com.hm.his.module.drug.service;

import com.hm.his.module.drug.model.Dictionary;
import com.hm.his.module.drug.model.DrugManufacturer;

import java.util.List;


/**
 * 字典 服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public interface DictionaryService {

    /**
     *  功能描述： 5.11.字典sug
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    List<Dictionary> searchDictionary(Dictionary dictionary);


    List<Dictionary> searchDictionaryByName(Integer type,String name);

    List<Dictionary> searchDefaultDictionary(Integer type);


}
