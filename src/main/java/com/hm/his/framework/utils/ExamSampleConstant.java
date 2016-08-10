package com.hm.his.framework.utils;

import java.util.ArrayList;
import java.util.List;

import com.hm.his.module.outpatient.model.ExamSample;

/**
 * Created by wangjialin on 15/12/21.
 */
public class ExamSampleConstant {
	
    public static List<ExamSample> getDefaultSampleList(){
        List<ExamSample> sampleList = new ArrayList<>();
        ExamSample examSample1 = new ExamSample("b", "血液");
        ExamSample examSample2 = new ExamSample("s", "血清");
        ExamSample examSample3 = new ExamSample("p", "血浆");
        ExamSample examSample4 = new ExamSample("u", "尿液");
        ExamSample examSample5 = new ExamSample("salivary", "唾液");
        ExamSample examSample6 = new ExamSample("sF", "滑膜液");
        ExamSample examSample7 = new ExamSample("t", "痛风石");
        ExamSample examSample8 = new ExamSample("s_p", "血浆或血清");
        ExamSample examSample9 = new ExamSample("ery", "红细胞");
        sampleList.add(examSample1);
        sampleList.add(examSample2);
        sampleList.add(examSample3);
        sampleList.add(examSample4);
        sampleList.add(examSample5);
        sampleList.add(examSample6);
        sampleList.add(examSample7);
        sampleList.add(examSample8);
        sampleList.add(examSample9);
        return sampleList;
    }
    
    public static String getSampleName(String sampleId){
    	if(sampleId==null || sampleId.equals(""))
    		return null;
    	if(sampleId.equals("b"))
    		return "血液";
    	else if(sampleId.equals("s"))
    		return "血清";
    	else if(sampleId.equals("p"))
    		return "血浆";
    	else if(sampleId.equals("u"))
    		return "尿液";
    	else if(sampleId.equals("salivary"))
    		return "唾液";
    	else if(sampleId.equals("sF"))
    		return "滑膜液";
    	else if(sampleId.equals("t"))
    		return "痛风石";
    	else if(sampleId.equals("s_p"))
    		return "血浆或血清";
    	else if(sampleId.equals("ery"))
    		return "红细胞";
    	else if(sampleId.equals("NEFA"))
    		return "游离脂肪酸";
    	else if(sampleId.equals("PL"))
    		return "血磷脂";
    	else if(sampleId.equals("p(EDTA)"))
    		return "EDTA抗凝血浆";
    	else
    		return "未知";
    }
}