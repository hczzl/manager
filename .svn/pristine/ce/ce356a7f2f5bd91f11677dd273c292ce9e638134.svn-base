package com.ruoyi.web.controller.audit;

import com.ruoyi.web.domain.AuditCopyMsg;
import com.ruoyi.web.service.audit.AuditCopyMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/copymsg")
@Controller
@Api(value = "AuditCopyMsgController", tags = "抄送信息相关接口")
public class AuditCopyMsgController {
    @Autowired
    private AuditCopyMsgService auditCopyMsgService;

    @ApiOperation("查询抄送信息列表")
    @PostMapping("selectInfos")
    @ResponseBody
    public List<AuditCopyMsg> selectInfos(AuditCopyMsg auditCopyMsg) {
        return auditCopyMsgService.selectInfos(auditCopyMsg);
    }

    @PostMapping("insertInfos")
    @ResponseBody
    public int insertCopys(AuditCopyMsg auditCopyMsg) {

        return auditCopyMsgService.insertCopys(auditCopyMsg);
    }

    @PostMapping("updateInfos")
    @ResponseBody
    public int updateInfos(AuditCopyMsg auditCopyMsg) {
        int a = auditCopyMsgService.updateInfos(auditCopyMsg);
        return a;
    }

    @PostMapping("deleteInfos")
    @ResponseBody
    public int deleteInfos(AuditCopyMsg auditCopyMsg) {
        return auditCopyMsgService.deleteInfos(auditCopyMsg);
    }
}
