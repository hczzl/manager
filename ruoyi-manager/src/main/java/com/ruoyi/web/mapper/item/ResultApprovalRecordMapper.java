package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.ResultApprovalRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

/**
 * 里程碑mapper
 */
public interface ResultApprovalRecordMapper {
    /**
     * @param resultApprovalRecord
     * @return
     */
    int insertApprovalRecord(ResultApprovalRecord resultApprovalRecord);

    /**
     * @param resultApprovalRecord
     * @return
     */
    List<ResultApprovalRecord> selectApprovalRecord(ResultApprovalRecord resultApprovalRecord);

    /**
     * @param resultApprovalRecord
     * @return
     */
    int updateApprovalRecord(ResultApprovalRecord resultApprovalRecord);

    /**
     * @param resultApprovalRecord
     * @return
     */
    int deleteApprovalRecord(ResultApprovalRecord resultApprovalRecord);

    /**
     * @param resultApprovalRecord
     * @return
     */
    List<ResultApprovalRecord> selectApprovalRecordByAuditId(ResultApprovalRecord resultApprovalRecord);

    /**
     * @param r
     * @return
     */
    Integer selectNumber(ResultApprovalRecord r);

    /**
     * @param r
     * @return
     */
    List<ResultApprovalRecord> selectApprovalInfo(ResultApprovalRecord r);

    /**
     * @param vo
     * @return
     */
    List<Integer> selectUserIdList(ResultApprovalRecord vo);


}
