package com.ruoyi.web.mapper.news;

import com.ruoyi.web.domain.news.NewsMsg;
import com.ruoyi.web.domain.news.vo.NewsMsgVo;

import java.util.List;
import java.util.Map;

/**
 * @author zhongzhilong
 * @date 2020/10/9 0009
 */
public interface NewsMsgMapper {
    /**
     * 添加新闻信息
     *
     * @param newsMsg
     * @return
     */
    int insertNewsMsg(NewsMsg newsMsg);

    /**
     * 分页获取新闻列表
     *
     * @param vo
     * @return
     */
    List<Map<String, Object>> selectNewsMsgList(NewsMsgVo vo);

    /**
     * 获取新闻类别集合
     *
     * @return
     */
    List<Map<String, Object>> selectTypeList();

    /**
     * 修改信息信息
     *
     * @param vo
     * @return
     */
    int modifyNewsMsg(NewsMsg vo);

    /**
     * 删除新闻信息
     *
     * @param vo
     * @return
     */
    int deleteNewsMsg(NewsMsg vo);
}
