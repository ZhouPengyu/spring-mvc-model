package com.hm.his.module.template.service.impl;

import com.google.common.collect.Maps;
import com.hm.his.framework.log.annotation.SystemLogAnno;
import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.Operation;
import com.hm.his.framework.model.HisResponse;
import com.hm.his.framework.utils.SessionUtils;
import com.hm.his.module.template.dao.TemplateMapper;
import com.hm.his.module.template.model.Template;
import com.hm.his.module.template.service.TemplateOperService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/7
 * Time: 15:36
 * CopyRight:HuiMei Engine
 */
@Service
public class TemplateOperServiceImpl implements TemplateOperService {

    @Autowired(required = false)
    TemplateMapper templateMapper;

    @Override
    public HisResponse checkTemplateDate(Template template,Integer operType) {
        template.setHospitalId(SessionUtils.getHospitalId());
        template.setDoctorId(SessionUtils.getDoctorId());
        HisResponse hisResponse = new HisResponse();
        if (StringUtils.isBlank(template.getTempName()) || template.getTempType() == null) {
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("模板名称为空，请填写模板名称");
            return hisResponse;
        }

        if (checkTemplateByName(template) > operType) {
            hisResponse.setErrorCode(50001L);
            hisResponse.setErrorMessage("模板名称已存在，请修改模板名称");
            return hisResponse;
        }

        if(operType.equals(1)&& template.getId() ==null){
            hisResponse.setErrorCode(50004L);
            hisResponse.setErrorMessage("模板ID为空，请重试");
            return hisResponse;
        }
        return hisResponse;
    }

    @Override
    @SystemLogAnno(functionId = FunctionConst.TEMPLATE_LOG,operationId = Operation.TEMPLATE_ADD)
    public HisResponse saveTemplate(Template template) {
        template.setHospitalId(SessionUtils.getHospitalId());
        template.setDoctorId(SessionUtils.getDoctorId());

        HisResponse hisResponse = new HisResponse();

        template.setStatus(1);
        template.setCreateDate(new Date());
        template.setCreater(SessionUtils.getDoctorId());
        Integer result = templateMapper.insert(template);

        if(result >0){
            Map<String,Long> body = Maps.newHashMap();
            body.put("id",template.getId());
            hisResponse.setBody(body);
            return hisResponse;
        }else{
            hisResponse.setErrorCode(60001L);
            hisResponse.setErrorMessage("保存模板失败，请重试.");
            return hisResponse;
        }
    }

    @Override
    @SystemLogAnno(functionId = FunctionConst.TEMPLATE_LOG,operationId = Operation.TEMPLATE_MODIFY)
    public HisResponse modifyTemplate(Template template) {
        template.setHospitalId(SessionUtils.getHospitalId());
        template.setDoctorId(SessionUtils.getDoctorId());

        HisResponse hisResponse = new HisResponse();

        template.setModifyDate(new Date());
        template.setModifier(SessionUtils.getDoctorId());
        int result = templateMapper.updateByPrimaryKeySelective(template);
        if(result >0){
            Map<String,Long> body = Maps.newHashMap();
            body.put("id",template.getId());
            hisResponse.setBody(body);
            return hisResponse;
        }else{
            hisResponse.setErrorCode(60001L);
            hisResponse.setErrorMessage("修改模板失败，请重试.");
            return hisResponse;
        }
    }

    @Override
    public Integer checkTemplateByName(Template template){
        return templateMapper.checkTemplateByName(template);
    }
}
