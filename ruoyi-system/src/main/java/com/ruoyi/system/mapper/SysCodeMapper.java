package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysCode;

import java.util.List;

public interface SysCodeMapper {

    List<SysCode> selectCodes(SysCode sysCode);

    int insertCoded(SysCode sysCode);

    int updateCode(SysCode sysCode);

    String selectwxId(SysCode sysCode);
}
