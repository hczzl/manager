package com.ruoyi.web.domain;


import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 存储年月日
 */
public class SysYearMonth extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7045556011629665363L;
    private int id;

    private String startTime;

    private String endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
