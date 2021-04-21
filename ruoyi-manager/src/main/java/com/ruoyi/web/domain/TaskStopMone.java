package com.ruoyi.web.domain;


import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 任务中止的原因
 *
 * @author zhongzhilong
 * @date 2019/8/20 0015
 */
public class TaskStopMone extends BaseEntity {

    private static final long SERIAL_VERSION_UID = -205286739521914509L;
    private int id;

    private Integer tId;

    @ApiModelProperty("任务中止的原因")
    private String stopMone;

    @ApiModelProperty("创建者")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新者")
    private String uppdateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUppdateBy() {
        return uppdateBy;
    }

    public void setUppdateBy(String uppdateBy) {
        this.uppdateBy = uppdateBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public String getStopMone() {
        return stopMone;
    }

    public void setStopMone(String stopMone) {
        this.stopMone = stopMone;
    }
}
