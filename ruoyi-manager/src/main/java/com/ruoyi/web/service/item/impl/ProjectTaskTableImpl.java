package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.ProjectTaskTable;
import com.ruoyi.web.mapper.item.ProjectTaskTableMapper;
import com.ruoyi.web.service.item.ProjectTaskTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskTableImpl implements ProjectTaskTableService {
    @Autowired
    private ProjectTaskTableMapper projectTaskTableMapper;

    @Override
    public int insertProjectTask(ProjectTaskTable projectTaskTable) {
        return projectTaskTableMapper.insertProjectTask(projectTaskTable);
    }

    @Override
    public int updateProjectTask(ProjectTaskTable projectTaskTable) {
        return projectTaskTableMapper.updateProjectTask(projectTaskTable);
    }

    @Override
    public List<ProjectTaskTable> selectInfos(ProjectTaskTable projectTaskTable) {
        return projectTaskTableMapper.selectInfos(projectTaskTable);
    }

    @Override
    public int deleteInfos(ProjectTaskTable projectTaskTable) {
        return projectTaskTableMapper.deleteInfos(projectTaskTable);
    }
}
