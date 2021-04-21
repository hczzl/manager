package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.ProjectPlanTable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * mapper接口
 *
 * @author zhongzhilong
 * @date 2019/7/30
 */
@Component
public interface ProjectPlanMapper {

    int insertPlan(ProjectPlanTable projectPlanTable);

    List<ProjectPlanTable> selectAllPlan(ProjectPlanTable projectPlanTable);

    int deletePlanTitle(ProjectPlanTable projectPlanTable);

    List<ProjectPlanTable> selectAllByProjectId(Integer id);

    ProjectPlanTable selectProjectPlanTableById(Long planId);

    Integer selectProjectPlanTableIdByTitle(ProjectPlanTable projectPlanTable);
}
