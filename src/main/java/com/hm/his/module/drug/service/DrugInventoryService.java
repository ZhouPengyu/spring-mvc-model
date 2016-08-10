package com.hm.his.module.drug.service;

import com.hm.his.module.drug.model.DrugInventory;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.InventoryOperatePojo;

/**
 * 库存服务类
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/14
 * Time: 10:11
 * CopyRight:HuiMei Engine
 */
public interface DrugInventoryService {

    public DrugInventory selectByDrugId(Long drugId);

    /**
     *  功能描述：初始库存
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    Integer initInventory(HospitalDrug hospitalDrug);

    /**
     *  功能描述：修改库存
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    Integer modifyInventory(DrugInventory inventory);

    /**
     *  功能描述：删减库存
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    InventoryOperatePojo cutInventory(InventoryOperatePojo inventoryOperatePojo);


    /**
     *  功能描述：诊所药品删减库存
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    InventoryOperatePojo cutHospitalDrugInventory(InventoryOperatePojo inventoryOperatePojo);

    /**
     *  功能描述：退还库存
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    InventoryOperatePojo returnInventory(InventoryOperatePojo inventoryOperatePojo);


    /**
     *  功能描述：诊所药品退还库存
     * @param
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/3/14
     *
     */
    InventoryOperatePojo returnHospitalDrugInventory(InventoryOperatePojo inventoryOperatePojo);

    Integer instockInventory(HospitalDrug hospDrug,Double addInventoryCount);
}
