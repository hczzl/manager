package com.ruoyi.web.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zhongzhilong
 * @date 2019/9/10
 */
public class EmployeeHoliday implements Serializable {

    private static final long serialVersionUID = 7038069593386110902L;
    private Integer id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 返回前端的时间集
     */
    private List<Date> dateList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Date> getDateList() {
        return dateList;
    }

    public void setDateList(List<Date> dateList) {
        this.dateList = dateList;
    }
}
