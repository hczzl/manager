package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.ProblemFileInfo;
import com.ruoyi.web.service.item.ProblemFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.web.mapper.item.ProblemFileInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemFileInfoServiceImpl implements ProblemFileInfoService {
    @Autowired
    private ProblemFileInfoMapper problemFileInfoMapper;

    @Override
    public int insertProblemFollowTable(ProblemFileInfo problemFileInfo) {
        return problemFileInfoMapper.insertProblemFileInfo(problemFileInfo);
    }

    @Override
    public List<ProblemFileInfo> selectFileInfoList(ProblemFileInfo problemFileInfo) {
        return problemFileInfoMapper.selectFileInfoList(problemFileInfo);
    }

}
