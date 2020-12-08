package com.neuedu.entity;

import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author maoyq123
 * @since 2020-11-19
 */
public class Userinfo{

    private Integer userinfoId;

    private String userinfoName;

    private String userinfoIdcard;

    private String userinfoSex;

    private Date userinfoBirth;

    private String userinfoAddr;

    private String valiFlag;

    private String remark;

    private String creater;

    private String createrName;

    private Date createTime;

    private String updater;

    private String updaterName;

    private Date updateTime;


    public Integer getUserinfoId() {
        return userinfoId;
    }

    public void setUserinfoId(Integer userinfoId) {
        this.userinfoId = userinfoId;
    }

    public String getUserinfoName() {
        return userinfoName;
    }

    public void setUserinfoName(String userinfoName) {
        this.userinfoName = userinfoName;
    }

    public String getUserinfoIdcard() {
        return userinfoIdcard;
    }

    public void setUserinfoIdcard(String userinfoIdcard) {
        this.userinfoIdcard = userinfoIdcard;
    }

    public String getUserinfoSex() {
        return userinfoSex;
    }

    public void setUserinfoSex(String userinfoSex) {
        this.userinfoSex = userinfoSex;
    }

    public Date getUserinfoBirth() {
        return userinfoBirth;
    }

    public void setUserinfoBirth(Date userinfoBirth) {
        this.userinfoBirth = userinfoBirth;
    }

    public String getUserinfoAddr() {
        return userinfoAddr;
    }

    public void setUserinfoAddr(String userinfoAddr) {
        this.userinfoAddr = userinfoAddr;
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
        return "Userinfo{" +
        "userinfoId=" + userinfoId +
        ", userinfoName=" + userinfoName +
        ", userinfoIdcard=" + userinfoIdcard +
        ", userinfoSex=" + userinfoSex +
        ", userinfoBirth=" + userinfoBirth +
        ", userinfoAddr=" + userinfoAddr +
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
