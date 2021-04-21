package com.ruoyi.web.service.item.impl;

import com.ruoyi.system.domain.TaskUserFree;
import com.ruoyi.web.mapper.item.TaskUserFreeMapper;
import com.ruoyi.web.service.item.TaskUserFreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务参与人Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Service
public class TaskUserFreeServiceImpl implements TaskUserFreeService {
    @Autowired
    private TaskUserFreeMapper taskUserFreeMapper;

    /**
     * 新增用户空闲时间
     *
     * @param taskUserFree
     * @return
     */
    @Override
    public int insertTaskUserFree(TaskUserFree taskUserFree) {
        return taskUserFreeMapper.insertTaskUserFree(taskUserFree);
    }

    /**
     * 更新表信息
     *
     * @param taskUserFree
     * @return
     */
    @Override
    public int updateTaskUserFree(TaskUserFree taskUserFree) {
        return taskUserFreeMapper.updateTaskUserFree(taskUserFree);
    }

    /**
     * 查询信息
     *
     * @param taskUserFree
     * @return
     */
    @Override
    public List<TaskUserFree> selectUserFreeTime(TaskUserFree taskUserFree) {
        return taskUserFreeMapper.selectUserFreeTime(taskUserFree);
    }

    /**
     * 删除表信息
     *
     * @param taskUserFree
     * @return
     */
    @Override
    public int deleteAllInfo(TaskUserFree taskUserFree) {
        return taskUserFreeMapper.deleteAllInfo(taskUserFree);
    }

    /**
     * 根据id删除记录
     *
     * @param taskUserFree
     * @return
     */
    @Override
    public int deleteInfoById(TaskUserFree taskUserFree) {
        return taskUserFreeMapper.deleteInfoById(taskUserFree);
    }

    @Override
    public List<TaskUserFree> selectUserFreeTime1(TaskUserFree taskUserFree) {
        return taskUserFreeMapper.selectUserFreeTime1(taskUserFree);
    }

}
