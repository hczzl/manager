package com.ruoyi.web.util.pageutils;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页实体类
 *
 * @author zhongzhilong
 * @date 2019/9/16 0016
 */
public class PageEntity<T> implements Serializable {

    private static final long SERIAL_VERSION_UID = -1943678992543759453L;
    /**
     * 每页总条数
     */
    private Integer total;
    /**
     * 总条数
     */
    private Integer pages;
    /**
     * 当前页-页码
     */
    private Integer pageNumber;
    /**
     * 结果集
     */
    private List<T> list;


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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public PageEntity(int total, int pages, int pageNumber, List<T> list) {
        this.total = total;
        this.pages = pages;
        this.pageNumber = pageNumber;
        this.list = list;
    }

    public PageEntity(int total, int pages) {
        this.total = total;
        this.pages = pages;
    }

    public PageEntity() {
    }

    /**
     * 自定义构造方法
     *
     * @param pageInfo
     */
    public PageEntity(PageInfo pageInfo) {
        // 每页的条数
        this.total = pageInfo.getPageSize();
        // 页码
        this.pageNumber = pageInfo.getPageNum();
        // 总条数
        this.pages = (int) pageInfo.getTotal();
        // 返回值
        this.list = pageInfo.getList();
    }
}