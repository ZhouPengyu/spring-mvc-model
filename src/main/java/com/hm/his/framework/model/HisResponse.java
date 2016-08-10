package com.hm.his.framework.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wangjialin on 15/12/10.
 */
public class HisResponse implements Serializable{

    private static final long serialVersionUID = 3151836054335979057L;

    private Map<String, Object> head = new HashMap<>();
    private Object body = new Object();
    @JSONField(serialize=false)
    private Boolean hasError=false;

    public HisResponse(){
        this.head.put("error", 0);
    }

    public void setErrorCode(long errorCode){
        this.hasError = true;
        this.head.put("error", errorCode);
    }

    public void setErrorMessage(String message){
        this.head.put("message", message);
    }

    public void addHead(String key, Object obj){
        if (key != null){
            head.put(key, obj);
        }
    }

    public void setBody(Object body){
        this.body = body;
    }

    public Map<String, Object> getHead() {
		return head;
	}

	public void setHead(Map<String, Object> head) {
		this.head = head;
	}

	public Object getBody() {
		return body;
	}

	public String toString(){
		return JSON.toJSONString(this);
    }

    public static HisResponse getInstance(){
        return new HisResponse();
    }
    public static HisResponse getInstance(Object body){
        HisResponse response=new HisResponse();
        response.setBody(body);
        return response;
    }

    public Boolean getHasError() {
        return hasError;
    }
}
