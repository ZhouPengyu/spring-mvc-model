package com.hm.his.module.message.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SuShaohua
 * @date 2016/8/2 15:46
 * @description
 */
public enum SmsSendStatusEnum {
    /**
     1     发送成功
     -1    用户名或密码为空
     -2    手机号不正确
     -3    msg参数为空
     -4    短信字数超长
     -5    群发手机号码个数超限（群发一个包最多300个号码）
     -6    黑名单号码
     -8    短信内容含有屏蔽词
     -9    短信账户不存在
     -10   短信账户已经停用
     -11   短信账户余额不足
     -12   密码错误
     -16   IP服务器鉴权错误
     -17   单发手机号码个数超限（单发一次只能提交1个号码）
     -21   提交速度超限
     -22 = 手机号达到当天发送限制
     -99   系统异常
     */
    success(1,"发送成功"),
    failure(2,"发送失败"),
    overlimit(3,"条数超限");

    private Integer sendStatusType;

    private String sendStatusTypeName;

    private static Map<Integer, String> map = new HashMap<Integer, String>();
    private static Map<String, Integer> typeNameMap = new HashMap<String, Integer>();
    static {
        for (SmsSendStatusEnum e : SmsSendStatusEnum.values()) {
            map.put(e.sendStatusType, e.sendStatusTypeName);
            typeNameMap.put(e.sendStatusTypeName,e.sendStatusType);
        }
    }

    private SmsSendStatusEnum(Integer sendStatusType, String sendStatusTypeName) {
        this.sendStatusType = sendStatusType;
        this.sendStatusTypeName = sendStatusTypeName;
    }

    public static String getSendStatusTypeNameByType(Integer sendStatusType) {
        return map.get(sendStatusType);
    }

    public static Integer getSendStatusTypeByName(String sendStatusTypeName) {
        return typeNameMap.get(sendStatusTypeName);
    }

    public Integer getSendStatusType() {
        return sendStatusType;
    }

    public void setSendStatusType(Integer sendStatusType) {
        this.sendStatusType = sendStatusType;
    }

    public String getSendStatusTypeName() {
        return sendStatusTypeName;
    }

    public void setSendStatusTypeName(String sendStatusTypeName) {
        this.sendStatusTypeName = sendStatusTypeName;
    }
}
