package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.ProjectMarkTypeInfoTable;
import com.ruoyi.web.mapper.item.ProjectMarkTypeMapper;
import com.ruoyi.web.service.item.ProjectMarkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMarkTypeServiceImpl implements ProjectMarkTypeService {

    @Autowired
    private ProjectMarkTypeMapper projectMarkTypeMapper;

    /**
     * 添加硬件信息
     */
    @Override
    public int insertMarkType(ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        return projectMarkTypeMapper.insertMarkType(projectMarkTypeInfoTable);
    }

    /**
     * 查询全部硬件信息
     *
     * @param projectMarkTypeInfoTable
     * @return
     */
    @Override
    public List<ProjectMarkTypeInfoTable> selectAll(ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        return projectMarkTypeMapper.selectAll(projectMarkTypeInfoTable);
    }

    @Override
    public int deleteType(int id) {
        return projectMarkTypeMapper.deleteType(id);
    }

    /**
     * 更新资源表
     *
     * @param p
     * @return
     */
    @Override
    public int updateMarkInfo(ProjectMarkTypeInfoTable p) {
        return projectMarkTypeMapper.updateMarkInfo(p);
    }
}
