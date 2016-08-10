package com.hm.his.module.login.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.his.framework.utils.BeanUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限控制实体类
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-07-26
 * Time: 15:03
 * CopyRight:HuiMei Engine
 */

public class AccessControl {

    public static SystemFrame systemFrame  = new SystemFrame();
    public static SystemFrame supplierFrame  = new SystemFrame();
    static {

        try {
//           Object jsons = JSON.parse(Files.readAllBytes(Paths.get("C:\\hm\\config\\sysFrame.json")));
//           Object jsons = JSON.parse(Files.readAllBytes(Paths.get("/hm/config/sysFrame.json")));
            byte[] jsonByte = Files.readAllBytes(Paths.get("/hm/config/sysFrame.json"));
//            System.out.println("sysFrame.json====\r\n"+jsonByte);
            // 使用 SystemFrame 保存 权限json
            systemFrame = JSON.parseObject(jsonByte,SystemFrame.class);

            byte[] supJsonByte = Files.readAllBytes(Paths.get("/hm/config/supFrame.json"));
            supplierFrame = JSON.parseObject(supJsonByte,SystemFrame.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(parseJsonByMap());


    }

    public static Map<String,List<String>> buildFunction(){
        Map<String,List<String>> accessFunMap = Maps.newHashMap();
        List<String> clinicFuns = Lists.newArrayList();
        clinicFuns.add("newRecord");
        clinicFuns.add("fixSearch");
        accessFunMap.put("clinic",clinicFuns);

        List<String> chargeDrugFuns = Lists.newArrayList();
        chargeDrugFuns.add("hasChargeList");
        chargeDrugFuns.add("purchase");
        accessFunMap.put("chargeDrug",chargeDrugFuns);

        List<String> drugManageFuns = Lists.newArrayList();
        drugManageFuns.add("drugList");
        drugManageFuns.add("orderEarning");
        drugManageFuns.add("dateEarning");
        drugManageFuns.add("orderList");
        accessFunMap.put("drugManage",drugManageFuns);
        return accessFunMap;
    }
    private  static String parseJsonByBean(){
        try {
            Object jsons  = JSON.parse(Files.readAllBytes(Paths.get("C:\\hm\\config\\sysFrame.json")));
            // 使用 SystemFrame 保存 权限json
            SystemFrame systemFrame = JSON.parseObject(jsons.toString(),SystemFrame.class);

            //替换头部的logo 和 诊所名称
            String headerCont  = systemFrame.getHeader_cont().toString();
            headerCont = headerCont.replaceAll("../../images/header_logo.png","http://his.huimei.com/images/header_logo.png").replaceAll("惠每云诊所系统","安和寿连锁诊所管理系统");
            systemFrame.setHeader_cont(headerCont);

            //根据权限定制 权限的json
            Map<String,List<String>> accessFunMap = buildFunction();
            SystemFrame.MenuConf newMenuConfs  = new SystemFrame.MenuConf();

            SystemFrame.MenuConf menuConfs  = systemFrame.getMenu_conf();
            accessFunMap.keySet().forEach(funKey ->{
                try {
                    SystemFrame.FirstMenu firstMenu = (SystemFrame.FirstMenu)BeanUtils.forceGetProperty(menuConfs,funKey);
                    LinkedHashMap<String,Object> secondMenus = firstMenu.getSon_menu();
                    LinkedHashMap<String,Object> newSecondMenus = new LinkedHashMap<String, Object>();

                    List<String> secFuns = accessFunMap.get(funKey);
                    secondMenus.keySet().forEach(secondKey ->{
                        if(secFuns.contains(secondKey.toString())){

                            newSecondMenus.put(secondKey,secondMenus.get(secondKey));
                        }
                    });
                    firstMenu.setSon_menu(newSecondMenus);
                    BeanUtils.forceSetProperty(newMenuConfs,funKey,firstMenu);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            });
            newMenuConfs.setUserCenter(menuConfs.getUserCenter());
            systemFrame.setMenu_conf(newMenuConfs);
            System.out.println("systemFrame============\r\n"+JSON.toJSONString(systemFrame));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String parseJsonByMap(){
        try {
//            List<String> jsons = Files.readAllLines(Paths.get("C:\\hm\\config\\sysFrame.json"),StandardCharsets.UTF_8);
//            jsons.stream().forEach(System.out :: println);

            Object jsons  = JSON.parse(Files.readAllBytes(Paths.get("C:\\hm\\config\\sysFrame.json")));
            // 使用 SystemFrame 保存 权限json
//            SystemFrame systemFrame = JSON.parseObject(jsons.toString(),SystemFrame.class);
//            System.out.println(systemFrame);
            // 使用 Map<String, Object> 保存 权限json
            Map<String, Object> listMap = JSON.parseObject(jsons.toString(), new TypeReference<Map<String,Object>>(){});
            //替换头部的logo 和 诊所名称
            String headerCont  = listMap.get("header_cont").toString();
            headerCont = headerCont.replaceAll("../../images/header_logo.png","http://his.huimei.com/images/header_logo.png").replaceAll("惠每云诊所系统","安和寿连锁诊所管理系统");
            listMap.put("header_cont",headerCont);

            //根据权限定制 权限的json
            Map<String,List<String>> accessFunMap = buildFunction();

            System.out.println("accessFunMap======="+ JSON.toJSONString(accessFunMap));
            JSONObject menuConfs  = JSON.parseObject(listMap.get("menu_conf").toString());
            Set<String> firstSet = menuConfs.keySet().stream().filter(firstKey -> accessFunMap.keySet().contains(firstKey)).collect(Collectors.toSet());
            JSONObject lessMenus = new JSONObject();
            JSONObject secondLessMenus = new JSONObject();
            firstSet.forEach( firstKey ->{
                JSONObject firstMenus = (JSONObject)menuConfs.get(firstKey);
                JSONObject secondMenus = JSON.parseObject(firstMenus.get("son_menu").toString());
                List<String> secFuns = accessFunMap.get(firstKey);
                Set<String> secondSet = secondMenus.keySet().stream().filter(secondKey -> secFuns.contains(firstKey)).collect(Collectors.toSet());
                secondSet.forEach(secondKey ->{
                    JSONObject sonMenus = (JSONObject)secondMenus.get(secondKey);
                    secondLessMenus.put(secondKey,sonMenus);
                });

                lessMenus.put(firstKey,menuConfs.get(firstKey));
            });


//            menuConfs.keySet().forEach(firstKey->{
//                if(accessFunMap.keySet().contains(firstKey)){
//                    JSONObject firstMenus  = JSON.parseObject(menuConfs.get(firstKey).toString());
//                    JSONObject secondMenus = JSON.parseObject(firstMenus.get("son_menu").toString());
//                    List<String> secFuns = accessFunMap.get(firstKey);
//                    secondMenus.keySet().forEach(secondKey ->{
//                        if(!secFuns.contains(secondKey.toString())){
//                            secondMenus.remove(secondKey.toString());
//                        }
//                    });
//                }else{
//                    menuConfs.remove(firstKey);
//                }
//            });
            listMap.put("menu_conf",lessMenus);

            System.out.println("menu_conf============\r\n"+listMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


}
