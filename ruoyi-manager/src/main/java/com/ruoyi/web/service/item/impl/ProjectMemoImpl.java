package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.ProjectMemo;
import com.ruoyi.web.mapper.item.ProjectMemoMapper;
import com.ruoyi.web.service.item.ProjectMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemoImpl implements ProjectMemoService {

    @Autowired
    private ProjectMemoMapper projectMemoMapper;

    @Override
    public int insertMemo(ProjectMemo projectMemo) {
        return projectMemoMapper.insertMemo(projectMemo);
    }

    @Override
    public List<ProjectMemo> selectMaxId(ProjectMemo projectMemo) {
        return projectMemoMapper.selectMaxId(projectMemo);
    }

    @Override
    public int updateState(ProjectMemo projectMemo) {
        return projectMemoMapper.updateState(projectMemo);
    }

    @Override
    public List<ProjectMemo> selectByProjectId(ProjectMemo projectMemo) {
        return projectMemoMapper.selectByProjectId(projectMemo);
    }
}
