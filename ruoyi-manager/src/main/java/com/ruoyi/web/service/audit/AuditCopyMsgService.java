package com.ruoyi.web.service.audit;

import com.ruoyi.web.domain.AuditCopyMsg;

import java.util.List;

/**
 * @author zhongzhilong
 * @date 2019/8/12
 */
public interface AuditCopyMsgService {
    List<AuditCopyMsg> selectInfos(AuditCopyMsg auditCopyMsg);

    int insertCopys(AuditCopyMsg auditCopyMsg);

    int updateInfos(AuditCopyMsg auditCopyMsg);

    int deleteInfos(AuditCopyMsg auditCopyMsg);

    void insertCopysBytId(String ccIds, long eventId, Integer auditId);
}
