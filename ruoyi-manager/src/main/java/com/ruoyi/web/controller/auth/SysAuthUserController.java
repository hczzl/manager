package com.ruoyi.web.controller.auth;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.auth.SysAuthUser;
import com.ruoyi.web.service.auth.SysAuthUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author zhongzhilong
 * @date 2020/11/9 0009
 * @description 权限类信息控制器
 */
@Controller
@RequestMapping("/authority")
@Api(value = "AuthorityController", tags = "权限模块")
public class SysAuthUserController extends BaseController {

    @Autowired
    private SysAuthUserService sysAuthRankService;

    @PostMapping("/selectAuthList")
    @ResponseBody
    @ApiOperation("根据userId获取权限列表")
    public AjaxResult selectList(SysAuthUser sysAuthUser) {
        Map<String, Object> resultMap= sysAuthRankService.selectList(sysAuthUser);
        return AjaxResult.success("查询成功", resultMap);
    }
}
