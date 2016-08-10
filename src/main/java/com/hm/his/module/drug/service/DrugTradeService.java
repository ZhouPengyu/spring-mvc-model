package com.hm.his.module.drug.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;

import java.util.List;
import java.util.Map;

/**
 * 惠每药品 库服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/21
 * Time: 10:59
 * CopyRight:HuiMei Engine
 */
public interface DrugTradeService {

    DrugTrade getDrugById(Long drugId);

    Map<Long,DrugTrade> searchDrugByIds(List<Long> drugsId);

    List<DrugTrade> searchDrugByName(DrugRequest drugRequest);

    HospitalDrug getDrugByHMDrugId(Long drugId);

    HospitalDrug hmDrugToHospitalDrug(DrugTrade drugTrade);

    //根据条形码精确匹配药物信息 找惠每库
    List<DrugTrade> searchHMDrugByBarCode(DrugRequest drugRequest);

    List<DrugTrade> searchDrugList(DrugRequest drugRequest);

    List<DrugTrade> findAllDrugTrade();

    DrugTrade selectByDrugTrade(DrugTrade trade);

    /**
     *  功能描述： 5.4.	新增药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */

    HisResponse saveDrug(DrugTrade drugTrade);





    /**
     *  功能描述：5.9.	修改保存药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    HisResponse modifyDrug(DrugTrade drugTrade);

    /**
     *  功能描述：5.10.	删除药品
     *  用户删除药品，逻辑删除
     *  return :删除成功的总数
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    Integer deleteDrug(List<Long> drugsId);

}
