package com.hm.his.module.template.service;

import com.hm.his.framework.model.HisResponse;
import com.hm.his.module.outpatient.model.request.PatientInquiryRequest;
import com.hm.his.module.template.model.Template;
import com.hm.his.module.template.pojo.TemplateRequest;

import java.util.List;

/**
 * 医生模板增加和修改 服务
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/2/29
 * Time: 10:04
 * CopyRight:HuiMei Engine
 */
public interface TemplateOperService {


    HisResponse saveTemplate(Template template);

    HisResponse modifyTemplate(Template template);
    Integer checkTemplateByName(Template template);

    /**
     *  功能描述：保存模板时，判断 是否合法
     * @param  operType：操作类型    0：新增     1：修改
     * @return
     * @throws
     * @author:  tangww
     * @createDate   2016/4/12
     *
     */
    HisResponse checkTemplateDate(Template template,Integer operType);

}
