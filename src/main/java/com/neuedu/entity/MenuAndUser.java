package com.neuedu.entity;

import java.util.Date;

/**
 * <p>
 * 菜单和用户登录关联表
 * </p>
 *
 * @author maoyq123
 * @since 2020-11-19
 */

public class MenuAndUser {


    private Integer id;

    private Integer userId;

    private Integer menuId;

    private String valiFlag;

    private String remark;

    private String creater;

    private String createrName;

    private Date createTime;

    private String updater;

    private String updaterName;

    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
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
        return "MenuAndUser{" +
        "id=" + id +
        ", userId=" + userId +
        ", menuId=" + menuId +
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
