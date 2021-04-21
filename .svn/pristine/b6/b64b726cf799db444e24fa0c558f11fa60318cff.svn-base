package com.ruoyi.quartz.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.quartz.service.ITaskTableService;
import com.ruoyi.web.mapper.item.TaskTableMapper;

@Service
public class QuartzTaskTableServiceImpl implements ITaskTableService{

	@Autowired
	private TaskTableMapper taskTableMapper;

	@Override
	public List<Map<String, Object>> selectWarnList(Map<String, Object> map) {
		return taskTableMapper.selectWarnList(map);
	}

	@Override
	public void update(Map<String, Object> map) {
		taskTableMapper.update(map);
	}
	

}
