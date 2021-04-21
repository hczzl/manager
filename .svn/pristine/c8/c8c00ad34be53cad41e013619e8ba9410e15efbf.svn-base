package com.ruoyi.quartz.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.quartz.service.ITaskUserService;
import com.ruoyi.web.mapper.item.TaskUserMapper;

@Service
public class QuartzTaskUserServiceImpl implements ITaskUserService{

	@Autowired
	private TaskUserMapper taskUserMapper;
	
	@Override
	public List<String> getUserByTid(Map<String, Object> map) {
		return taskUserMapper.getUserByTid(map);
	}

}
