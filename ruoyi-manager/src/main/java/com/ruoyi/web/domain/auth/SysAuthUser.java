package com.ruoyi.web.domain.auth;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhongzhilong
 * @date 2020/11/9 0009
 * @descrription 权限职级权限实体类
 */
public class SysAuthUser {

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("权限id")
    private Integer authId;

    @ApiModelProperty("用户编号")
    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
