package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.ProjectTaskTable;
import com.ruoyi.web.domain.TaskUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface ProjectTaskTableMapper {

    int insertProjectTask(ProjectTaskTable projectTaskTable);

    int updateProjectTask(ProjectTaskTable projectTaskTable);

    List<ProjectTaskTable> selectInfos(ProjectTaskTable projectTaskTable);

    int deleteInfos(ProjectTaskTable projectTaskTable);

}
