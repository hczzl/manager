package com.ruoyi.web.domain;


import java.io.Serializable;
import java.util.List;

public class ResultInfo<T> implements Serializable {
    private static final long serialVersionUID = 6293218893461228679L;
    /**
     * 当前页
     */
    private Integer pageNumber;
    /**
     * 每页显示数
     */
    private Integer total;
    /**
     * 总数
     */
    private Integer pages;
    /**
     * 数据
     */
    private List<T> list;

    public ResultInfo() {
    }

    public ResultInfo(Integer pageNumber, Integer total) {
        this.pageNumber = pageNumber;
        this.total = total;
    }


    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public ResultInfo(Integer pageNumber, Integer total, Integer pages, List<T> list) {
        this.pageNumber = pageNumber;
        this.total = total;
        this.pages = pages;
        this.list = list;
    }
}
