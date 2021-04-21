package com.ruoyi.web.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 项目关注实体类
 *
 * @author zhongzhilong
 * @date 2019/8/20 0015
 */
public class UserProjectAttention extends BaseEntity implements Serializable {

    private static final long SERIAL_VERSION_UID = -5442904275275593607L;
    private int id;

    @ApiModelProperty("用户id")
    @Excel(name = "用户id")
    private Long userId;

    @ApiModelProperty("任务id")
    @Excel(name = "任务id")
    private Long pId;

    @ApiModelProperty("状态state")
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }
}
