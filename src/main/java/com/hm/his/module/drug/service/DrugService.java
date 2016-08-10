package com.hm.his.module.drug.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.drug.model.DrugTrade;
import com.hm.his.module.drug.pojo.DrugRequest;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugResponse;
import com.hm.his.module.drug.pojo.HospitalDrugSug;
import com.hm.his.module.drug.pojo.SaleWayPojo;
import com.hm.his.module.order.model.SellDrug;

import java.util.List;
import java.util.Map;

/**
 * 诊所药品 服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public interface DrugService {
    //其他模块调用此接口 --规格信息：规格包装描述＋“空格”＋包装单位
    HospitalDrug getDrugById(Long drugId);
    //诊所药品详细接口   --不组合规格信息
    HospitalDrug getDrugDetailById(Long drugId);


    List<HospitalDrug> searchDrugList(DrugRequest drugRequest);


    Integer countHospitalDrug(DrugRequest drugRequest);

    Integer countHospitalDrugAllByHospitalId(Long hospitalId);
    /**
     *  功能描述： 5.2.	药品搜索sug根据药物名称  --用户在搜索框中输入“通用名/商品名/药品条码”信息
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    List<HospitalDrugSug> searchDrugByName(DrugRequest drugRequest);


    /**
     *  功能描述： 根据条形码查询药品信息
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    DrugResponse searchDrugByBarCodeForWX(DrugRequest drugRequest);






    List<HospitalDrug> searchDrugNameList(DrugRequest drugRequest);


    /**
     *  功能描述： 5.3.	药品搜索sug （处方，售药模块）
     *  医生或售药人员在药品搜索框中输入“通用名/商品名/药品条码/批准文号”信息时，调用此接口，返回信息中包括药品的价格信息
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    List<HospitalDrugSug> searchDrugByNameForSale(DrugRequest drugRequest);

    /**
     *  功能描述： 5.3.	根据条形码准确匹配药品搜索sug （处方，售药模块）
     *  医生或售药人员在药品搜索框中通过 条形码检索，调用此接口，返回信息中包括药品的价格信息
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    List<HospitalDrugSug> searchDrugByBarCodeForSale(DrugRequest drugRequest);


    /**
     *  功能描述： 根据药品ID 和销售渠道 查询 售药方式
     *  saleChannel ： 销售渠道  1：处方 2：直接售药
     * @author:  tangwenwu
     * @createDate   2016/3/21
     *
     */
    List<SaleWayPojo>  searchDrugSaleWayById(Integer saleChannel, Long drugId);


    Map<Long,HospitalDrug> searchDrugByDrugIds(List<Long> drugIds);
    /**
     *  功能描述： 5.4.	新增药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */

    HisResponse saveDrug(HospitalDrug hospitalDrug);

    /**
     *  功能描述：功能描述： 直接售药时，将惠每药物库的药物 添加到 诊所的蒶库中
     * @param
     * @return  : 默认返回 诊所药品ID，订单中新的药品ID，并将 dataSource 设置 为 0  ，
     * 如果根据HM药物ID 查询 不到HM药品时，返回 -1，直接售药将原来记录的HM药品ID和dataSource 设置为 1 保存到订单中
     * @throws
     * @author:  tangww
     * @createDate   2016-07-01
     *
     */
    Long addDrugByHMDrug(SellDrug sellDrug);


    /**
     *  功能描述： 5.4.	weixin 新增药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */

    HisResponse saveDrugByWeixin(HospitalDrug hospitalDrug);


    /**
     *  功能描述：5.9.	修改保存药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    HisResponse modifyDrug(HospitalDrug hospitalDrug);

    /**
     *  功能描述：	修改药品库存和价格
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    HisResponse modifyInventoryAndPrice(HospitalDrug hospitalDrug);

    /**
     *  功能描述：5.10.	删除药品
     *  用户删除药品，逻辑删除
     *  return :删除成功的总数
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    Integer deleteDrug(List<Long> drugsId);


    //检查药品重复性验证
    //6.5.2.2重复性验证
    //2.条形码重复时，认为重复
    //以上两种逻辑，满足任意一条时，认为重复
    HospitalDrug checkDrugRepeatedByBarCode(HospitalDrug hospitalDrug);

    //检查药品重复性验证
    //6.5.2.2重复性验证
    //1.名称、规格包装、厂家名称 相同时认为重复
    Integer checkDrugRepeatedByNameAndSpec(HospitalDrug hospitalDrug);
    
    /**
     * <p>Description:检查药品重复性验证，返回ID<p>
     * @author ZhouPengyu
     * @date 2016-5-30 15:42:53
     */
    Integer getRepeatedDrugId(HospitalDrug hospitalDrug) throws Exception;




    /**
     *  功能描述： 向新注册诊所药物中添加的默认药物
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
     HisResponse addDefaultDrugForNewHospital(Long  hospitalId,Long doctorId);

     HospitalDrugSug processHospitalDrugSugByHMDrug(DrugTrade drugTrade,boolean hasManufacturer);




    Integer batchModifyDrugInventoryThreshold(DrugRequest drugRequest);
    
    /**
     * <p>Description:查询诊所下库存告警药品列表<p>
     * @author ZhouPengyu
     * @date 2016年6月2日 上午10:17:51
     */
    List<HospitalDrug> searchDrugByInventoryAlarm(DrugRequest drugRequest);
    
    /**
     * <p>Description:查询诊所下库存告警药品总数<p>
     * @author ZhouPengyu
     * @date 2016年6月2日 上午10:17:51
     */
    Integer searchDrugByInventoryAlarmCount(DrugRequest drugRequest);
    
    /**
     * <p>Description:根据采购订单药品ID查询药品<p>
     * @author ZhouPengyu
     * @date 2016年6月2日 上午10:17:51
     */
    HospitalDrug getDrugByPurchaseOrderDrugId(Long drugId);
}
