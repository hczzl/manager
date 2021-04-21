package com.ruoyi.web.service.news.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.web.domain.news.NewsMsg;
import com.ruoyi.web.domain.news.vo.NewsMsgVo;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.mapper.news.NewsMsgMapper;
import com.ruoyi.web.service.news.NewsMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 新闻实现类
 *
 * @author zhongzhilong
 * @date 2020/10/9 0009
 */
@Service
public class NewsMsgServiceImpl implements NewsMsgService {
    @Autowired
    private NewsMsgMapper newsMsgMapper;



    @Override
    public int insertNewsMsg(NewsMsg newsMsg) {
        newsMsg.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        newsMsg.setCreateTime(new Date());
        int a = newsMsgMapper.insertNewsMsg(newsMsg);
        return a;
    }

    @Override
    public PageEntity selectNewsMsgList(NewsMsgVo vo) {
        int pageIndex = vo.getPageNumber();
        int pageSize = vo.getTotal();
        pageSize = pageSize <= 0 ? 10 : pageSize;
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, Object>> list = newsMsgMapper.selectNewsMsgList(vo);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return new PageEntity(pageInfo);
    }

    @Override
    public List<Map<String, Object>> selectTypeList() {
        return newsMsgMapper.selectTypeList();
    }

    @Override
    public int modifyNewsMsg(NewsMsg vo) {
        return newsMsgMapper.modifyNewsMsg(vo);
    }

    @Override
    public int deleteNewsMsg(NewsMsg vo) {
        return newsMsgMapper.deleteNewsMsg(vo);
    }
}
