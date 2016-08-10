package com.hm.his.module;

public class Constants {
    /**
	 * 应用ID
	 */
    public static final String AppId = "wx6473dd7247cc2434";
    //   public static final String AppId = "wx4aa4a2e07a641e6e";  //测试环境
    
    /**
	 * 应用密钥
	 */
    public static final String AppSecret = "c97627a148a07948ef64f63bc6555a13";
    //   public static final String AppSecret = "bcafed3eadb7d5607625bf6f5f808ba6"; //测试环境
    
    /**
     * 前端应用路径
     */
//    public static final String WebUrl = "http://wxtest.huimeionline.com/"; //测试环境

    public static final String WebUrl = "http://weixin.huimei.com/";

    /* 缓存生命周期等级控制 */
    /** 3秒 */
    public final static int EXPIRES_LEVEL_1 = 3;
    /** 5分钟 **/
    public final static int EXPIRES_LEVEL_2 = 5 * 60;
    /** 1小时 **/
    public final static int EXPIRES_LEVEL_3 = 60 * 60;

    /** 2小时 **/
    public final static int EXPIRES_TWO_HOUR = 2 * 60 * 60;

    /** 8小时 **/
    public final static int EXPIRES_EIGHT_HOUR = 8 * 60 * 60;
    /** 1天 **/
    public final static int EXPIRES_LEVEL_4 = 60 * 60 * 24;
    /** 7天 **/
    public final static int EXPIRES_LEVEL_5 = 60 * 60 * 24 * 7;
    /** 30天 **/
    public final static int EXPIRES_LEVEL_6 = 60 * 60 * 24 * 30;
    
}
