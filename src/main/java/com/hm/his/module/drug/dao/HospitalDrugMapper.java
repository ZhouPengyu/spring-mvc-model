package com.hm.his.module.drug.dao;

import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;

import java.util.List;

public interface HospitalDrugMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HospitalDrug record);

    int insertHospitalDrugByList(List<HospitalDrug> hospitalDrugs);

    int insertSelective(HospitalDrug record);

    HospitalDrug selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HospitalDrug record);

    int updateHospitalDrugPrice(HospitalDrug record);



    int updateByPrimaryKey(HospitalDrug record);

    int deletByDrugId(Integer id);

    /**
     *  功能描述：查询诊所药品列表
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    List<HospitalDrug> selectHospitalDrug(DrugRequest drugRequest);

    List<HospitalDrug> selectDrugListByDefault(Long hospitalId);

    List<HospitalDrug> selectDrugListByHospitalId(Long hospitalId);

    /**
     *  功能描述：查询诊所库 根据药物名称搜索药品sug
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    List<HospitalDrug> searchDrugByName(DrugRequest drugRequest);

    /**
     *  功能描述：查询诊所库 根据药物名称搜索药品sug
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    List<HospitalDrug> searchDrugByHospitalId(Long hospitalId);

    List<HospitalDrug>  searchDrugNameList(DrugRequest drugRequest);

    List<HospitalDrug> searchDrugByNameForSale(DrugRequest drugRequest);

    List<HospitalDrug> searchDrugByDrugIds(List<Long> drugIds);

    List<HospitalDrug> searchDrugByBarCodeForSale(DrugRequest drugRequest);

    HospitalDrug searchDrugAndInventory(Long drugId);

    /**
     *  功能描述：查询诊所药品的总数
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/3
     *
     */
    Integer countHospitalDrug(DrugRequest drugRequest);

    Integer countHospitalDrugAllByHospitalId(Long hospitalId);


    Long checkDrugRepeatedByBarCode(HospitalDrug hospitalDrug);

    Integer checkDrugRepeatedByNameAndSpec(HospitalDrug hospitalDrug);
    
    /**
     * <p>Description:检查药品重复性验证，返回ID<p>
     * @author ZhouPengyu
     * @date 2016-5-30 15:42:53
     */
    Integer getRepeatedDrugId(HospitalDrug hospitalDrug);
    
    List<HospitalDrug> searchDrugByInventoryAlarm(DrugRequest drugRequest);

    List<HospitalDrug> searchDrugInventoryMoreThanZero(Long hospitalId);

    List<HospitalDrug> searchHospitalDrugForPinyin(DrugRequest drugRequest);

    int countDrugByInventoryAlarm(DrugRequest drugRequest);

    int batchUpdateDrugPinyin(List<HospitalDrug> hospitalDrugs);

}