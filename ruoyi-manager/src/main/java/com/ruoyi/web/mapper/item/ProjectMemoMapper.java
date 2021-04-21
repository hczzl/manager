package com.ruoyi.web.mapper.item;


import com.ruoyi.web.domain.ProjectMemo;

import java.util.List;

/**
 * mapper接口
 *
 * @author zhongzhilong
 * @date 2020/3/10
 */
public interface ProjectMemoMapper {

    int insertMemo(ProjectMemo projectMemo);

    List<ProjectMemo> selectMaxId(ProjectMemo projectMemo);

    int updateState(ProjectMemo projectMemo);

    List<ProjectMemo> selectByProjectId(ProjectMemo projectMemo);

}
