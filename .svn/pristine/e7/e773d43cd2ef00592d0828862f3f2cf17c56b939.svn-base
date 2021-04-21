package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.ResultApprovalRecord;
import com.ruoyi.web.mapper.item.ResultApprovalRecordMapper;
import com.ruoyi.web.service.item.ResultApprovalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultApprovalRecordServiceImpl implements ResultApprovalRecordService {
    @Autowired
    private ResultApprovalRecordMapper resultApprovalRecordMapper;

    /**
     * 添加审批记录
     *
     * @param resultApprovalRecord
     * @return
     */
    @Override
    public int insertApprovalRecord(ResultApprovalRecord resultApprovalRecord) {
        return resultApprovalRecordMapper.insertApprovalRecord(resultApprovalRecord);
    }

    @Override
    public List<ResultApprovalRecord> selectApprovalRecord(ResultApprovalRecord resultApprovalRecord) {
        return resultApprovalRecordMapper.selectApprovalRecord(resultApprovalRecord);
    }

    /**
     * 更新审批表
     *
     * @param resultApprovalRecord
     * @return
     */
    @Override
    public int updateApprovalRecord(ResultApprovalRecord resultApprovalRecord) {
        return resultApprovalRecordMapper.updateApprovalRecord(resultApprovalRecord);
    }

    /**
     * 删除记录
     *
     * @param resultApprovalRecord
     * @return
     */
    @Override
    public int deleteApprovalRecord(ResultApprovalRecord resultApprovalRecord) {
        return resultApprovalRecordMapper.deleteApprovalRecord(resultApprovalRecord);
    }

    @Override
    public List<ResultApprovalRecord> selectApprovalRecordByAuditId(ResultApprovalRecord resultApprovalRecord) {
        return resultApprovalRecordMapper.selectApprovalRecordByAuditId(resultApprovalRecord);
    }

    @Override
    public Integer selectNumber(ResultApprovalRecord r) {
        return resultApprovalRecordMapper.selectNumber(r);
    }

    @Override
    public List<ResultApprovalRecord> selectApprovalInfo(ResultApprovalRecord r) {
        return resultApprovalRecordMapper.selectApprovalInfo(r);
    }
}
