package com.hm.his.drug.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.BaseControllerTest;
import com.hm.his.framework.utils.DateUtil;
import com.hm.his.module.drug.model.Dictionary;
import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.drug.pojo.DrugRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/3/1
 * Time: 15:12
 * CopyRight:HuiMei Engine
 */
public class DrugControllerTest  extends BaseControllerTest {

    @Test
    public void verifyExamName() throws Exception {
        Map<String, String> requestParams = Maps.newHashMap();
//        request.setDrugName("板蓝根");
//        request.setDrugType(3);
        requestParams.put("examName","常规");
        super.testRole("/manage/verifyExamName", JSON.toJSONString(requestParams));
    }

    @Test
    public void searchDrugList() throws Exception {
        DrugRequest request = new DrugRequest();
        request.setDrugName("大力神丸");
//        request.setDrugType(2);
//        request.setStatus(1);
//        request.setDrugType(3);
        request.setCurrentPage(1);
        request.setPageSize(20);
        super.testRole("/inventory/searchDrugList", JSON.toJSONString(request));
    }

    /**
     *  功能描述： 5.2.	药品搜索sug根据药物名称  --用户在搜索框中输入“通用名/商品名/药品条码/批准文号”信息
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void searchDrugByName() throws Exception {
        DrugRequest request = new DrugRequest();
        //测试用例1: 仅搜索 诊所药物库
//        request.setDrugName("六味地黄丸");
//        request.setDataSource(0);
        //测试用例2: 仅搜索 惠每药物库
//        request.setDrugName("控释");
//        request.setDataSource(1);
        //测试用例3: 当诊所药物库为空时搜索 惠每药物库
//        request.setDrugName("控释");
        //6939030230047 HM有多个药物
//        request.setDrugName("6939030230047");
//        request.setDataSource(1);

        //6943297600109 HM有一个药物
//        request.setDrugName("逍遥");
//        request.setDrugName("ej");
        request.setDrugName("ejs");
//        request.setDrugName("erjias");
//        request.setDrugName("erjias");
        request.setDataSource(1);
//        request.setDrugName("8690529000");
//        request.setDataSource(0);

        super.testRole("/inventory/searchDrugByName", JSON.toJSONString(request));
    }

    /**
     *  功能描述： 5.3.	药品搜索sug （处方，售药模块）
     *  医生或售药人员在药品搜索框中输入“通用名/商品名/药品条码/批准文号”信息时，调用此接口，返回信息中包括药品的价格信息
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void searchDrugByNameForSale() throws Exception{
        DrugRequest request = new DrugRequest();
        //诊所 只有一个药品
//        request.setDrugName("6939261900221");
//        request.setDataSource(2);

        //诊所没有，惠每有三个药
//        request.setDrugName("6936693500173");
//        request.setDataSource(2);

        //诊所 有多个药品
        request.setDrugName("ys");
        request.setDataSource(2);
        request.setDrugType(100);
        super.testRole("/inventory/searchDrugByNameForSaleDrug", JSON.toJSONString(request));
    }

    @Test
    public void searchDrugByNameForInsock() throws Exception{
        DrugRequest request = new DrugRequest();
        //诊所 只有一个药品
//        request.setDrugName("6939261900221");
//        request.setDataSource(2);

        //诊所没有，惠每有三个药
//        request.setDrugName("6936693500173");
//        request.setDataSource(2);

        //诊所 有多个药品
        request.setDrugName("6907406680109");
        request.setDataSource(0);
        super.testRole("/inventory/searchDrugByNameForInsock", JSON.toJSONString(request));
    }



    @Test
    public void testSearchDrugNameList() throws Exception{
        DrugRequest request = new DrugRequest();
        request.setDrugName("逍遥");
        super.testRole("/inventory/searchDrugNameList", JSON.toJSONString(request));
    }




    /**
     *  功能描述： 5.4.	新增药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void saveDrug() throws Exception{
        HospitalDrug hDrug = new HospitalDrug();
        hDrug.setDrugName("阿莫西林胶囊文武");
        hDrug.setDrugType(3);
        hDrug.setIsOtc(1);
        hDrug.setStatus(1);

        hDrug.setApprovalNumber("国药准字J20150030");
        hDrug.setBarCode("86900639000658");
        hDrug.setMnemoniCode("8690");
        hDrug.setSingleDosage("1");
        hDrug.setSingleDosageUnit("粒");
        hDrug.setFrequency("每天三次");
        hDrug.setPrescribeAmount(2D);
        hDrug.setPrescribeAmountUnit("粒");
        hDrug.setInstruction("口服");
        hDrug.setDoctorAdvice("");

        hDrug.setSpecMinimumDosage(0.5);
        hDrug.setSpecMinimumUnit("mg");
        hDrug.setSpecUnit("粒");
        hDrug.setSpecUnitaryRatio(24);
        hDrug.setSpecPackageUnit("盒");

        hDrug.setManufacturer("上海信谊黄河制药有限公司");
        hDrug.setPurchasePrice(23.5);
        hDrug.setPrescriptionPrice(25.5);
        hDrug.setSalePrice(35.5);
        hDrug.setInventoryCount(10D);
        hDrug.setInventoryThreshold(30);
        hDrug.setValidityDate(DateUtil.addDay(720));
        hDrug.setGoodsShelfCode("1号柜3排");
        hDrug.setOpenStock(0);
        hDrug.setDirectDrugPurchase(1);
        hDrug.setSupplier("北京药局");
        super.testRole("/inventory/saveDrug", JSON.toJSONString(hDrug));
    }


    /**
     *  功能描述：5.5.	生产厂家sug
     *  用户在添加药物的生产厂家时，sug层提示，最多5条
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void searchManufacturer() throws Exception{
        DrugRequest request = new DrugRequest();
        request.setManufacturer("安徽");

        super.testRole("/inventory/searchManufacturer", JSON.toJSONString(request));
    }

    /**
     *  功能描述：5.5.	生产厂家sug
     *  用户在添加药物的生产厂家时，sug层提示，最多5条
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void searchDictionary() throws Exception{
        Dictionary dictionary = new Dictionary();
        dictionary.setType(4);
        super.testRole("/dict/searchDictionary", JSON.toJSONString(dictionary));
    }

    /**
     *  功能描述：5.6.	查询药品详细信息
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void drugDetail() throws IOException{
        DrugRequest request = new DrugRequest();
        request.setDrugId(9012L);
        request.setDataSource(0);
        super.testRole("/inventory/drugDetail", JSON.toJSONString(request));
    }

    @Test
    public void checkDrugRepeatedByBarCode() throws IOException{
        HospitalDrug hDrug = new HospitalDrug();
        hDrug.setBarCode("86900639000658");
//        hDrug.setBarCode("86905858100878");
        hDrug.setHospitalId(1L);
        super.testRole("/inventory/checkDrugRepeatedByBarCode", JSON.toJSONString(hDrug));
    }


    /**
     *  功能描述：5.7.	导入药品excel
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void importDrugs() throws IOException{
        DrugRequest request = new DrugRequest();
        request.setDrugName("");
        request.setDrugType(3);
        request.setStatus(1);
        request.setCurrentPage(1);
        request.setPageSize(15);
        super.testRole("/inventory/importDrugs", JSON.toJSONString(request));
    }

    /**
     *  功能描述：5.8.	下载导入失败药品excel
     *  当导入药品接口中 failCount 大于 0 时，需要调用此接口，下载失败详情的excel
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void downloadFailDrugs() throws IOException{
        DrugRequest request = new DrugRequest();
        super.testRole("/inventory/downloadFailDrugs", JSON.toJSONString(request));
    }

    /**
     *  功能描述：5.9.	修改保存药品
     *
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void modifyDrug() throws IOException{
        HospitalDrug hDrug = new HospitalDrug();
        hDrug.setId(2606L);
        hDrug.setDrugName("苗夫人");
        hDrug.setDrugType(5);
        hDrug.setIsOtc(0);
        hDrug.setStatus(1);

        hDrug.setApprovalNumber("国药准字J20150030");
        hDrug.setBarCode("86900639000429");
        hDrug.setMnemoniCode("8690");
        hDrug.setSingleDosage("1");
        hDrug.setFrequency("每天三次");
        hDrug.setPrescribeAmount(2D);
        hDrug.setInstruction("口服");
        hDrug.setDoctorAdvice("");

        hDrug.setSpecMinimumDosage(0.5);
        hDrug.setSpecMinimumUnit("mg");
        hDrug.setSpecUnit("支");
        hDrug.setSpecUnitaryRatio(24);
        hDrug.setSpecPackageUnit("盒");


        hDrug.setManufacturer("玉林市苗夫人抗菌制品有限公司");
        hDrug.setPurchasePrice(23.5);
        hDrug.setPrescriptionPrice(25.5);
        hDrug.setSalePrice(35.5);
        hDrug.setInventoryCount(20D);
        hDrug.setGoodsShelfCode("1号柜3排");
        hDrug.setInventoryThreshold(100);
        hDrug.setValidityDate(DateUtil.addDay(365));
        hDrug.setOpenStock(0);
        hDrug.setDirectDrugPurchase(1);
        hDrug.setSupplier("北京药局");

        super.testRole("/inventory/modifyDrug", JSON.toJSONString(hDrug));
    }

    /**
     *  功能描述：5.10.	删除药品
     *  用户删除药品，逻辑删除
     * @author:  tangwenwu
     * @createDate   2016/3/1
     *
     */
    @Test
    public void deleteDrug() throws IOException{
        DrugRequest request = new DrugRequest();
        List<Long> drugIds = Lists.newArrayList();
//        drugIds.add(1L);
//        drugIds.add(3L);
        request.setDrugIds(drugIds);
        super.testRole("/inventory/deleteDrug", JSON.toJSONString(request));
    }


    @Test
    public void testUploadFile() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
//        File file = new File("C:\\Users\\tangww\\Downloads\\drug_import_template.xls");
        File file = new File("C:\\Users\\tangww\\Downloads\\drug_import_template_with_branch2.xls");
        HttpPost post = new HttpPost("http://localhost:8080/his/inventory/importDrugsWithBranch");
        FileBody fileBody = new FileBody(file, ContentType.create("multipart/form-data", Charset.defaultCharset()));
//        StringBody author = new StringBody("马晓超", ContentType.APPLICATION_JSON);
//        StringBody name = new StringBody("测试", ContentType.APPLICATION_JSON);
//        StringBody docActionType = new StringBody("1", ContentType.MULTIPART_FORM_DATA);
//        List list = Lists.newArrayList();
//        list.add(6);
//        StringBody productIds = new StringBody("6,", ContentType.APPLICATION_JSON);
//        StringBody disaseIds = new StringBody("6,8", ContentType.APPLICATION_JSON);

        //
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("excelFile", fileBody);
//        builder.addPart("author", author);
//        builder.addPart("name", name);
//        builder.addPart("docActionType", docActionType);
//        builder.addPart("productIds", productIds);
//        builder.addPart("diseaseIds", disaseIds);
        HttpEntity entity = builder.build();
        //
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        String re = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
        System.out.print(re);
    }

}
