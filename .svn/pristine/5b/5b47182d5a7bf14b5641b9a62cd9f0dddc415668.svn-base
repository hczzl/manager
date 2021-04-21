package com.ruoyi.web.service.item.impl;


import com.ruoyi.web.domain.TaskStopMone;
import com.ruoyi.web.mapper.item.TaskStopMoneMapper;
import com.ruoyi.web.service.item.TaskStopMoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class TaskStopMoneServiceImpl implements TaskStopMoneService {
    @Autowired
    private TaskStopMoneMapper taskStopMoneMapper;

    @Override
    public int insertTaskStopMone(TaskStopMone taskStopMone) {
        return taskStopMoneMapper.insertTaskStopMone(taskStopMone);
    }

    @Override
    public int updateTaskStopMone(TaskStopMone taskStopMone) {
        return taskStopMoneMapper.updateTaskStopMone(taskStopMone);
    }

    @Override
    public List<TaskStopMone> selectMax(TaskStopMone taskStopMone) {
        return taskStopMoneMapper.selectMax(taskStopMone);
    }
}
