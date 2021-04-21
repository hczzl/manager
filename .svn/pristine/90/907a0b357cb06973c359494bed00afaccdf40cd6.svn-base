package com.ruoyi.web.domain;

import io.swagger.annotations.ApiModelProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.io.Serializable;

/**
 * 任务关注实体类
 *
 * @author zhongzhilong
 * @date 2019/8/20 0015
 */
public class UserTaskAttention extends BaseEntity implements Serializable {

    private static final long SERIAL_VERSION_UID = 8483340508599266276L;

    @ApiModelProperty("用户id")
    @Excel(name = "用户id")
    private Long userId;

    @ApiModelProperty("任务id")
    @Excel(name = "任务id")
    private Long tId;

    @ApiModelProperty("关注状态")
    private Integer state;

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }


}
