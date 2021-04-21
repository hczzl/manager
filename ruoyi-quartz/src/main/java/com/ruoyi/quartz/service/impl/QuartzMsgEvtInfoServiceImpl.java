package com.ruoyi.quartz.service.impl;

import java.util.Map;

import com.ruoyi.web.domain.MsgEvtInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.quartz.service.IMsgEvtInfoService;
import com.ruoyi.web.mapper.item.MsgEvtInfoMapper;

@Service
public class QuartzMsgEvtInfoServiceImpl implements IMsgEvtInfoService {

	@Autowired
	private MsgEvtInfoMapper msgEvtInfoMapper;
	
	@Override
	public void save(Map<String, Object> map) {
		msgEvtInfoMapper.save(map);
	}

	@Override
	public int insertMessageInfo(MsgEvtInfo msgEvtInfo) {
		return msgEvtInfoMapper.insertMessageInfo(msgEvtInfo);
	}

}
