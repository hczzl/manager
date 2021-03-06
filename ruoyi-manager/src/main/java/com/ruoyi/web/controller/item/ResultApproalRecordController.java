package com.ruoyi.web.controller.item;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.web.domain.ResultApprovalRecord;
import com.ruoyi.web.service.item.ResultApprovalRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@Controller
@RequestMapping("/approval")
@Api(value = "ResultApproalRecordController", tags = "审批记录查询相关接口")
public class ResultApproalRecordController extends BaseController {

    @Autowired
    private ResultApprovalRecordService resultApprovalRecordService;

    @PostMapping("/selectApproval")
    @ResponseBody
    public List<ResultApprovalRecord> selectApproval(ResultApprovalRecord resultApprovalRecord) {
        return resultApprovalRecordService.selectApprovalRecord(resultApprovalRecord);
    }

    @PostMapping("/selectApprovalGroupBy")
    @ResponseBody
    public List<ResultApprovalRecord> selectApprovalRecordByAuditId(ResultApprovalRecord resultApprovalRecord) {
        return resultApprovalRecordService.selectApprovalRecordByAuditId(resultApprovalRecord);
    }

}
