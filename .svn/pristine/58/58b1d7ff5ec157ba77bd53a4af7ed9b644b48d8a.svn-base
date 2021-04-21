package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.SysCompetitor;
import com.ruoyi.web.domain.SysProjectTable;
import com.ruoyi.web.mapper.item.SysCompetitorMapper;
import com.ruoyi.web.service.item.SysCompetitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务参与人Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Service
public class SysCompetitorServiceImpl implements SysCompetitorService {
    @Autowired
    private SysCompetitorMapper sysCompetitorMappere;

    @Override
    public Integer insertCompetitor(SysCompetitor sysCompetitor) {
        return sysCompetitorMappere.insertCompetitor(sysCompetitor);
    }

    @Override
    public List<SysCompetitor> selectCompete(SysCompetitor sysCompetitor) {
        if (SysProjectTable.getYes() == 0) {
            return null;
        }
        return sysCompetitorMappere.selectCompete(sysCompetitor);
    }

    @Override
    public int updateCompete(SysCompetitor sysCompetitor) {
        return sysCompetitorMappere.updateCompete(sysCompetitor);
    }

    @Override
    public int deleteCompete(Integer projectId) {
        return sysCompetitorMappere.deleteCompete(projectId);
    }
}
