package com.neuedu.entity;

import java.util.Date;

/**
 * <p>
 * 二级代码表
 * </p>
 *
 * @author maoyq123
 * @since 2020-11-19
 */
public class Constant {

    private String constantId;
    
    private String constantName;

    private String constantCode;

    private String constantValue;

    private String valiFlag;

    private String remark;

    private String creater;

    private String createrName;

    private Date createTime;

    private String updater;

    private String updaterName;

    private Date updateTime;


    public String getConstantId() {
        return constantId;
    }

    public void setConstantId(String constantId) {
        this.constantId = constantId;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public String getConstantCode() {
        return constantCode;
    }

    public void setConstantCode(String constantCode) {
        this.constantCode = constantCode;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public String getValiFlag() {
        return valiFlag;
    }

    public void setValiFlag(String valiFlag) {
        this.valiFlag = valiFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Constant{" +
        "constantId=" + constantId +
        ", constantName=" + constantName +
        ", constantCode=" + constantCode +
        ", constantValue=" + constantValue +
        ", valiFlag=" + valiFlag +
        ", remark=" + remark +
        ", creater=" + creater +
        ", createrName=" + createrName +
        ", createTime=" + createTime +
        ", updater=" + updater +
        ", updaterName=" + updaterName +
        ", updateTime=" + updateTime +
        "}";
    }
}
