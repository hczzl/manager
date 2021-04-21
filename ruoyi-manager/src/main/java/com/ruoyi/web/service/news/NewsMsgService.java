package com.ruoyi.web.service.news;

import com.ruoyi.web.domain.news.NewsMsg;
import com.ruoyi.web.domain.news.vo.NewsMsgVo;
import com.ruoyi.web.util.pageutils.PageEntity;

import java.util.List;
import java.util.Map;

/**
 * @author zhongzhilong
 * @date 2020/10/9 0009
 */
public interface NewsMsgService {


    /**
     * 添加新闻信息
     *
     * @param newsMsg
     * @return
     */
    int insertNewsMsg(NewsMsg newsMsg);

    /**
     * 分页查询新闻信息列表
     *
     * @param vo
     * @return
     */
    PageEntity selectNewsMsgList(NewsMsgVo vo);

    /**
     * 获取新闻类别列表
     *
     * @return
     */
    List<Map<String, Object>> selectTypeList();

    /**
     * 修改新闻信息
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

