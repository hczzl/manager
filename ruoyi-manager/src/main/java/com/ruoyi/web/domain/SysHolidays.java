package com.ruoyi.web.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 发定节假日对象 sys_holidays
 *
 * @author ruoyi
 * @date 2019-08-28
 */
public class SysHolidays extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8746563763904818499L;
    private Long id;

    @Excel(name = "日期")
    private String holidays;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public String getHolidays() {
        return holidays;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("holidays", getHolidays())
                .toString();
    }
}
