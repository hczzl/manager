package com.ruoyi.web.mapper.item;

import com.ruoyi.system.domain.TaskUserFree;

import java.util.List;

/**
 * @author ruoyi
 * @date 2019-08-20
 */
public interface TaskUserFreeMapper {

    int insertTaskUserFree(TaskUserFree taskUserFree);


    int updateTaskUserFree(TaskUserFree taskUserFree);


    List<TaskUserFree> selectUserFreeTime(TaskUserFree taskUserFree);


    int deleteAllInfo(TaskUserFree taskUserFree);


    int deleteInfoById(TaskUserFree taskUserFree);


    List<TaskUserFree> selectUserFreeTime1(TaskUserFree taskUserFree);

}
