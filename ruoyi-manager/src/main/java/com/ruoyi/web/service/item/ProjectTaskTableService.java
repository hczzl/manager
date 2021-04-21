package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.ProjectTaskTable;

import java.util.List;

public interface ProjectTaskTableService {

    /**
     * 添加信息
     *
     * @param projectTaskTable
     * @return
     */
    int insertProjectTask(ProjectTaskTable projectTaskTable);

    /**
     * 更新信息
     *
     * @param projectTaskTable
     * @return
     */
    int updateProjectTask(ProjectTaskTable projectTaskTable);

    /**
     * 查询详细信息
     *
     * @param projectTaskTable
     * @return
     */
    List<ProjectTaskTable> selectInfos(ProjectTaskTable projectTaskTable);

    /**
     * 删除
     *
     * @param projectTaskTable
     * @return
     */
    int deleteInfos(ProjectTaskTable projectTaskTable);
}
