package com.hm.his.module.drug.dao;

import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;

import java.util.List;

public interface DrugTradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DrugTrade record);

    int insertSelective(DrugTrade record);

    DrugTrade selectByPrimaryKey(Long id);

    List<DrugTrade> searchDrugTradeByIds(List<Long> ids);

    int updateByPrimaryKeySelective(DrugTrade record);

    int updateByPrimaryKey(DrugTrade record);

    /**
     *  功能描述：查询惠每库 根据药物名称搜索药品sug
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    List<DrugTrade> searchDrugByName(DrugRequest drugRequest);

    /**
     *  功能描述：查询惠每库 根据药物名称(通用名 和商品名)搜索药品sug
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    List<DrugTrade> searchDrugByDrugName(DrugRequest drugRequest);

    List<DrugTrade> searchDrugTradeList(DrugRequest drugRequest);


    /**
     *  功能描述：查询惠每库 根据药物名称拼音(条形码，通用名拼音和商品名拼音)搜索药品sug
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    List<DrugTrade> searchDrugByDrugNamePinyin(DrugRequest drugRequest);

    List<DrugTrade> findAllDrugTrade();

    /**
     *  功能描述：查询惠每库 根据药物条形码搜索药品sug
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    List<DrugTrade> searchDrugByBarCode(DrugRequest drugRequest);

    int batchUpdateDrugPinyin(List<DrugTrade> trades);


    DrugTrade selectByDrugTrade(DrugTrade trade);
}