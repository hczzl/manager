package com.ruoyi.web.service.audit.impl;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.AuditCopyMsg;
import com.ruoyi.web.domain.MsgEvtInfo;
import com.ruoyi.web.mapper.audit.AuditCopyMsgMapper;
import com.ruoyi.web.mapper.item.MsgEvtInfoMapper;
import com.ruoyi.web.service.audit.AuditCopyMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 文档模块查询条件
 *
 * @author zhongzhilong
 * @date 2019/11/3
 */
@Service
public class IAuditCopyMsgServiceImpl implements AuditCopyMsgService {

    @Autowired
    private AuditCopyMsgMapper auditCopyMsgMapper;
    @Autowired
    private MsgEvtInfoMapper msgEvtInfoMapper;

    @Override
    public List<AuditCopyMsg> selectInfos(AuditCopyMsg auditCopyMsg) {
        return auditCopyMsgMapper.selectInfos(auditCopyMsg);
    }

    @Override
    public int insertCopys(AuditCopyMsg auditCopyMsg) {
        return auditCopyMsgMapper.insertCopys(auditCopyMsg);
    }

    @Override
    public int updateInfos(AuditCopyMsg auditCopyMsg) {
        return auditCopyMsgMapper.updateInfos(auditCopyMsg);
    }

    @Override
    public int deleteInfos(AuditCopyMsg auditCopyMsg) {
        return auditCopyMsgMapper.deleteInfos(auditCopyMsg);
    }

    @Override
    public void insertCopysBytId(String ccIds, long eventId, Integer auditId) {
        MsgEvtInfo msg = new MsgEvtInfo();
        if (!ShiroUtils.isNull(ccIds)) {
            String[] arr = ccIds.split(",");
            for (String userId : arr) {
                if (Integer.parseInt(userId) != ShiroUtils.getUserId()) {
                    msg.setType(11);
                    msg.setEventId(eventId);
                    msg.setAuditId(auditId);
                    msg.setUserId(Integer.parseInt(userId));
                    msg.setCreateTime(new Date());
                    msgEvtInfoMapper.insertMessageInfo(msg);
                }
            }
        }
    }
}
