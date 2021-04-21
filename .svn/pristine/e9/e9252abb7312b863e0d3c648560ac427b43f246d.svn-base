package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.ResultApprovalRecord;

import java.util.List;

public interface ResultApprovalRecordService {
    /**
     * 添加审批记录
     *
     * @param resultApprovalRecord
     * @return
     */
    int insertApprovalRecord(ResultApprovalRecord resultApprovalRecord);

    /**
     * 查询审批记录
     *
     * @param resultApprovalRecord
     * @return
     */
    List<ResultApprovalRecord> selectApprovalRecord(ResultApprovalRecord resultApprovalRecord);

    /**
     * 更新审批表
     *
     * @param resultApprovalRecord
     * @return
     */
    int updateApprovalRecord(ResultApprovalRecord resultApprovalRecord);

    /**
     * 删除记录
     *
     * @param resultApprovalRecord
     * @return
     */
    int deleteApprovalRecord(ResultApprovalRecord resultApprovalRecord);

    /**
     * 根据项目或者任务id审批记录
     *
     * @param resultApprovalRecord
     * @return
     */
    List<ResultApprovalRecord> selectApprovalRecordByAuditId(ResultApprovalRecord resultApprovalRecord);

    /**
     * 查询审批级数
     *
     * @param r
     * @return
     */
    Integer selectNumber(ResultApprovalRecord r);

    /**
     * 查询审批记录
     *
     * @param r
     * @return
     */
    List<ResultApprovalRecord> selectApprovalInfo(ResultApprovalRecord r);
}
