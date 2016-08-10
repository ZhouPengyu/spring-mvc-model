package com.hm.his.module.log.model;

import com.hm.his.framework.log.constant.FunctionConst;
import com.hm.his.framework.log.constant.OperResultConst;
import com.hm.his.framework.log.constant.Operation;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: TangWenWu
 * Date: 2016/4/5
 * Time: 11:33
 * CopyRight:HuiMei Engine
 */
public class SystemLog {


    /**
     * IAM3.3普通的操作日志描述信息模板
     */
    private static final String TEMPLATE = "{0}：操作人：{1}，所属医院：{2}，操作对象ID：{3}，操作对象名：{4}";


    private Long id;

    /**
     * 登录人名称，必填
     */
    private String userName;
    /**
     * 医院名称，必填
     */
    private String hospitalName;

    /**
     * 登录用户IP，必填
     */
    private String loginIp;

    /**
     * 操作功能号，必填 格式：Operation中的静态常量
     */
    private int operationId;

    /**
     * 模块ID，必填 格式：FunctionConst中的静态常量
     */
    private int functionId;

    /**
     * 操作结果，必填 格式：OperResultConst中的静态常量
     */
    private int operationResult;




    /**
     * 操作功能名称，必填 格式：Operation中的静态常量
     */
    private String operationName;

    /**
     * 模块名称D，必填 格式：FunctionConst中的静态常量
     */
    private String functionName;

    /**
     * 操作结果，必填 格式：OperResultConst中的静态常量
     */
    private String operationResultStr;

    /**
     * 操作对象的ID
     */
    private Long operObjId;

    /**
     * 操作对象的名
     */
    private String operObjName;

    private String description;

    private String params;

    /**
     * 医院ID
     */
    private Long hospitalId;

    /**
     * 医生ID
     */
    private Long doctorId;
    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人id
     */
    private Long creater;

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }


    public String getDescription() {
        if (StringUtils.isBlank(description)) {
            MessageFormat mf = new MessageFormat(TEMPLATE);
            Object[] o = {Operation.getOperationName(getOperationId()), getUserName(), getHospitalName(), getOperObjId(), getOperObjName()};
            String descrs = mf.format(o);
            return descrs;
        } else {
            return description;
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperObjName() {
        return operObjName;
    }

    public void setOperObjName(String operObjName) {
        this.operObjName = operObjName;
    }

    public Long getOperObjId() {
        return operObjId;
    }

    public void setOperObjId(Long operObjId) {
        this.operObjId = operObjId;
    }

    public int getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(int operationResult) {
        this.operationResult = operationResult;
        setOperationResultStr(OperResultConst.getOperResultName(this.operationResult));
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
        setOperationName(Operation.getOperationName(this.operationId));
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
        setFunctionName(FunctionConst.getLogTypeName(this.functionId));
    }

    public String getOperationName() {

        return Operation.getOperationName(this.operationId);
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getFunctionName() {
        return FunctionConst.getLogTypeName(this.functionId);
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getOperationResultStr() {
        return OperResultConst.getOperResultName(this.operationResult);
    }

    public void setOperationResultStr(String operationResultStr) {
        this.operationResultStr = operationResultStr;
    }


    @Override
    public String toString() {
        return "SystemLog{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", operationId=" + operationId +
                ", functionId=" + functionId +
                ", operationResult=" + operationResult +
                ", operationName='" + operationName + '\'' +
                ", functionName='" + functionName + '\'' +
                ", operationResultStr='" + operationResultStr + '\'' +
                ", operObjId=" + operObjId +
                ", operObjName='" + operObjName + '\'' +
                ", description='" + description + '\'' +
                ", params='" + params + '\'' +
                ", hospitalId=" + hospitalId +
                ", doctorId=" + doctorId +
                ", createDate=" + createDate +
                ", creater=" + creater +
                '}';
    }
}
