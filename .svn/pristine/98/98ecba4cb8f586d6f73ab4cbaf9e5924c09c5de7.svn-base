package com.ruoyi.web.controller.audit;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.service.audit.AuditService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhongzhilong
 * @date 2019/8/10
 */
@RequestMapping("/audit")
@Controller
@Api(value = "auditController", tags = "提交审批相关接口")
public class AuditController {
    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "/commit", method = {RequestMethod.POST})
    @ResponseBody
    @Log(title = "提交审批", businessType = BusinessType.INSERT)
    public AjaxResult audit(String currentId, String memo, Integer type, String userId, String score) {
        return auditService.audit(currentId, memo, type, userId, score);
    }


}
