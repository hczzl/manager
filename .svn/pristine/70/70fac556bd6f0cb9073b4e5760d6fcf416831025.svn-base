package com.ruoyi.web.domain;

import com.ruoyi.system.domain.SysUser;
import io.swagger.annotations.ApiModelProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 任务参与人实体类
 *
 * @author zhongzhilong
 * @date 2019/8/20 0015
 */
public class TaskUser extends BaseEntity implements Serializable {
    private static final long SERIAL_VERSION_UID = -5144716066789195299L;

    @ApiModelProperty("主键编号")
    private int id;

    @ApiModelProperty("任务id")
    @Excel(name = "任务id")
    private Long tId;

    @ApiModelProperty("用户id")
    @Excel(name = "用户id")
    private Long userId;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("用户对象")
    private SysUser sysUser;

    @ApiModelProperty("任务对象")
    private TaskTable taskTable;

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public TaskTable getTaskTable() {
        return taskTable;
    }

    public void setTaskTable(TaskTable taskTable) {
        this.taskTable = taskTable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
