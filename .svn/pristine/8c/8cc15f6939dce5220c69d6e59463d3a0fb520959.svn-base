package com.ruoyi.web.domain.news;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 新闻信息实体类
 *
 * @author zhongzhilong
 * @date 2020/10/9 0009
 */
@ApiModel
public class NewsMsg implements Serializable {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("新闻类别id")
    private Integer typeId;

    @ApiModelProperty("新闻标题")
    private String title;

    @ApiModelProperty("新闻链接")
    private String newUrl;

    @ApiModelProperty("图片链接")
    private String picturesUrl;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("删除时，可以传多个id批量删除")
    private List<String> idsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getPicturesUrl() {
        return picturesUrl;
    }

    public void setPicturesUrl(String picturesUrl) {
        this.picturesUrl = picturesUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getIdsList() {
        return idsList;
    }

    public void setIdsList(List<String> idsList) {
        this.idsList = idsList;
    }
}
