package com.hm.his.module.login.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.*;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016-07-26
 * Time: 15:48
 * CopyRight:HuiMei Engine
 */
public class SystemFrame implements Serializable {

    /**
     *  功能描述：利用串行化来做深复制（主要是为了避免重写比较复杂对象的深复制的clone（）方法，也可以程序实现断点续传等等功能）
     * @author:  tangww
     * @createDate   2016-08-01
     *
     */
    public Object deepClone() throws IOException, OptionalDataException, ClassNotFoundException {//将对象写到流里
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(this);//从流里读出来
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (oi.readObject());
    }

    @JSONField(ordinal = 1)
    private String header_cont;
    @JSONField(ordinal = 2)
    private String system_flag;
    @JSONField(ordinal = 3)
    private MenuConf menu_conf;

    public String getHeader_cont() {
        return header_cont;
    }

    public void setHeader_cont(String header_cont) {
        this.header_cont = header_cont;
    }

    public MenuConf getMenu_conf() {
        return menu_conf;
    }

    public String getSystem_flag() {
        return system_flag;
    }

    public void setSystem_flag(String system_flag) {
        this.system_flag = system_flag;
    }

    public void setMenu_conf(MenuConf menu_conf) {
        this.menu_conf = menu_conf;
    }

    //@JSONType(orders ={"clinic","chargeDrug","drugManage","template","statistics","clinicMana","userCenter"} )
    public static class MenuConf  implements Serializable{

        @JSONField(ordinal = 1)
        private FirstMenu order;
        @JSONField(ordinal = 1)
        private FirstMenu clinic;
        @JSONField(ordinal = 2)
        private FirstMenu chargeDrug;
        @JSONField(ordinal = 3)
        private FirstMenu drugManage;
        @JSONField(ordinal = 4)
        private FirstMenu template;
        @JSONField(ordinal = 5)
        private FirstMenu statistics;
        @JSONField(ordinal = 6)
        private FirstMenu clinicMana;
        @JSONField(ordinal = 7)
        private FirstMenu userHelp;
        @JSONField(ordinal = 8)
        private FirstMenu userCenter;

        @JSONField(ordinal = 9)
        private FirstMenu systemMessage;



        public FirstMenu getClinic() {
            return clinic;
        }

        public void setClinic(FirstMenu clinic) {
            this.clinic = clinic;
        }

        public FirstMenu getChargeDrug() {
            return chargeDrug;
        }

        public void setChargeDrug(FirstMenu chargeDrug) {
            this.chargeDrug = chargeDrug;
        }

        public FirstMenu getDrugManage() {
            return drugManage;
        }

        public void setDrugManage(FirstMenu drugManage) {
            this.drugManage = drugManage;
        }

        public FirstMenu getTemplate() {
            return template;
        }

        public void setTemplate(FirstMenu template) {
            this.template = template;
        }

        public FirstMenu getStatistics() {
            return statistics;
        }

        public void setStatistics(FirstMenu statistics) {
            this.statistics = statistics;
        }

        public FirstMenu getClinicMana() {
            return clinicMana;
        }

        public void setClinicMana(FirstMenu clinicMana) {
            this.clinicMana = clinicMana;
        }

        public FirstMenu getUserCenter() {
            return userCenter;
        }

        public void setUserCenter(FirstMenu userCenter) {
            this.userCenter = userCenter;
        }

        public FirstMenu getUserHelp() {
            return userHelp;
        }

        public void setUserHelp(FirstMenu userHelp) {
            this.userHelp = userHelp;
        }

        public FirstMenu getOrder() {
            return order;
        }

        public void setOrder(FirstMenu order) {
            this.order = order;
        }

        public FirstMenu getSystemMessage() {
            return systemMessage;
        }

        public void setSystemMessage(FirstMenu systemMessage) {
            this.systemMessage = systemMessage;
        }
    }


    public static class FirstMenu  implements Serializable{
        private String des;
        private String def_url;
        private String hide;
        private String add_cont;
        private LinkedHashMap<String,Object> son_menu;




        public LinkedHashMap<String, Object> getSon_menu() {
            return son_menu;
        }

        public void setSon_menu(LinkedHashMap<String, Object> son_menu) {
            this.son_menu = son_menu;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getDef_url() {
            return def_url;
        }

        public void setDef_url(String def_url) {
            this.def_url = def_url;
        }

        public String getHide() {
            return hide;
        }

        public void setHide(String hide) {
            this.hide = hide;
        }

        public String getAdd_cont() {
            return add_cont;
        }

        public void setAdd_cont(String add_cont) {
            this.add_cont = add_cont;
        }
    }

//    public static class SecondMenu{
//        private String des;
//        private String son_menu;
//
//        public String getDes() {
//            return des;
//        }
//
//        public void setDes(String des) {
//            this.des = des;
//        }
//
//        public String getSon_menu() {
//            return son_menu;
//        }
//
//        public void setSon_menu(String son_menu) {
//            this.son_menu = son_menu;
//        }
//    }
}
