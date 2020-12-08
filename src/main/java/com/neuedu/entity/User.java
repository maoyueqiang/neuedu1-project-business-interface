package com.neuedu.entity;

import java.util.Date;

/**
 * <p>
 * 用户登录表
 * </p>
 *
 * @author maoyq123
 * @since 2020-11-19
 */
public class User {

    private Integer userId;

    private Integer userinfoId;

    private String userName;

    private String userEmail;

    private String userPhone;

    private String userPwd;

    private String userAuthor;

    private String valiFlag;

    private String remark;

    private String creater;

    private String createrName;

    private Date createTime;

    private String updater;

    private String updaterName;

    private Date updateTime;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserinfoId() {
        return userinfoId;
    }

    public void setUserinfoId(Integer userinfoId) {
        this.userinfoId = userinfoId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserAuthor() {
        return userAuthor;
    }

    public void setUserAuthor(String userAuthor) {
        this.userAuthor = userAuthor;
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
        return "User{" +
        "userId=" + userId +
        ", userinfoId=" + userinfoId +
        ", userName=" + userName +
        ", userEmail=" + userEmail +
        ", userPhone=" + userPhone +
        ", userPwd=" + userPwd +
        ", userAuthor=" + userAuthor +
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
