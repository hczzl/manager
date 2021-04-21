package com.ruoyi.quartz.service;

import com.ruoyi.web.domain.MsgEvtInfo;

import java.util.Map;

public interface IMsgEvtInfoService {

	void save(Map<String, Object> map);

	public int insertMessageInfo(MsgEvtInfo msgEvtInfo);
}
