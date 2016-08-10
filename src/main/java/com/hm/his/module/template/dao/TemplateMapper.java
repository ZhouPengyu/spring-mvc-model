package com.hm.his.module.template.dao;

import com.hm.his.module.drug.model.HospitalDrug;
import com.hm.his.module.template.model.Template;
import com.hm.his.module.template.pojo.TemplateRequest;

import java.util.List;

public interface TemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int deletByTempId(Integer id);

    int insert(Template record);

    int insertSelective(Template record);

    int countTemplates(TemplateRequest templateRequest);
    int countTemplatesForRecipe(TemplateRequest templateRequest);

    Integer checkTemplateByName(Template record);

    Template selectByPrimaryKey(Integer id);

    List<Template> selectTemplateList(TemplateRequest templateRequest);

    List<Template> selectTemplateListForRecipe(TemplateRequest templateRequest);

    int updateByPrimaryKeySelective(Template record);

    int updateByPrimaryKey(Template record);
}