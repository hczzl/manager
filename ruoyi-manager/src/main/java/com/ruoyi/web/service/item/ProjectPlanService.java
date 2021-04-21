package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.ProjectPlanTable;

import java.util.List;

public interface ProjectPlanService {
    /**
     * 添加里程碑
     *
     * @param projectPlanTable
     * @return
     */
    int insertPlan(ProjectPlanTable projectPlanTable);

    /**
     * 查询里程碑信息
     *
     * @param projectPlanTable
     * @return
     */
    List<ProjectPlanTable> selectAllPlan(ProjectPlanTable projectPlanTable);

    /**
     * 删除里程碑标题
     *
     * @param projectPlanTable
     * @return
     */
    int deletePlanTitle(ProjectPlanTable projectPlanTable);

    /**
     * 根据项目id获取里程碑信息
     *
     * @param id
     * @return
     */
    List<ProjectPlanTable> selectAllByProjectId(Integer id);

    /**
     * 根据里程碑Id获取里程碑的信息
     *
     * @param planId
     * @return
     */
    ProjectPlanTable selectProjectPlanTableById(Long planId);

    /**
     * 根据标题返回里程碑id
     *
     * @param projectPlanTable
     * @return
     */
    Integer selectProjectPlanTableIdByTitle(ProjectPlanTable projectPlanTable);
}
