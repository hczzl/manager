package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysCode;
import com.ruoyi.system.mapper.SysCodeMapper;
import com.ruoyi.system.service.ISysCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SysCodeServiceImpl implements ISysCodeService {
    @Autowired
    private SysCodeMapper sysCodeMapper;

    @Override
    public List<SysCode> selectCodes(SysCode sysCode) {
        return sysCodeMapper.selectCodes(sysCode);
    }

    @Override
    public int insertCoded(SysCode sysCode) {
        return sysCodeMapper.insertCoded(sysCode);
    }

    @Override
    public int updateCode(SysCode sysCode) {
        return sysCodeMapper.updateCode(sysCode);
    }

    @Override
    public String selectwxId(SysCode sysCode) {
        return sysCodeMapper.selectwxId(sysCode);
    }
}
