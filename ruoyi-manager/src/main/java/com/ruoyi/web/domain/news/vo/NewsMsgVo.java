package com.ruoyi.web.domain.news.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Date;

/**
 * @author zhongzhilong
 * @date 2020/10/9 0009
 */
@ApiModel
public class NewsMsgVo extends BaseEntity {
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

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
