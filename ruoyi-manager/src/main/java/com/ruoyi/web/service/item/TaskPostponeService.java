package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.TaskPostpone;
import com.ruoyi.web.domain.TaskTable;

import java.util.List;

/**
 * 延期Service接口
 *
 * @author ruoyi
 * @date 2019-08-27
 */
public interface TaskPostponeService {
    /**
     * 查询延期
     *
     * @param id 延期ID
     * @return 延期
     */
    TaskPostpone selectTaskPostponeById(Long id);

    /**
     * 查询延期列表
     *
     * @param taskPostpone 延期
     * @return 延期集合
     */
    List<TaskPostpone> selectTaskPostponeList(TaskPostpone taskPostpone);

    /**
     * 新增延期
     *
     * @param taskPostpone 延期
     * @return 结果
     */
    int insertTaskPostpone(TaskPostpone taskPostpone);

    /**
     * 修改延期
     *
     * @param taskPostpone 延期
     * @return 结果
     */
    int updateTaskPostpone(TaskPostpone taskPostpone);

    /**
     * 批量删除延期
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteTaskPostponeByIds(String ids);

    /**
     * 删除延期信息
     *
     * @param id 延期ID
     * @return 结果
     */
    int deleteTaskPostponeById(Long id);

    /**
     * 查询最大的id
     *
     * @param taskPostpone
     * @return
     */
    Integer selectMaxId(TaskPostpone taskPostpone);

    /**
     * 根据id查询信息
     *
     * @param taskPostpone
     * @return
     */
    List<TaskPostpone> selectById(TaskPostpone taskPostpone);

    /**
     * 根据任务id和是否延期状态来获取对应的信息
     *
     * @param taskPostpone
     * @return
     */
    List<TaskPostpone> selectTaskChange(TaskPostpone taskPostpone);

    /**
     * 任务顺延
     *
     * @param taskTable
     */
    void taskPostpone(TaskTable taskTable);
}
