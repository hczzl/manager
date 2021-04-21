package com.ruoyi.web.controller.monitor;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.web.domain.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.service.ISysLogininforService;

/**
 * 系统访问记录
 * 
 * @author ruoyi
 */
    @RestController
    @RequestMapping("/monitor/logininfor")
    public class SysLogininforController extends BaseController
    {
        @Autowired
        private ISysLogininforService logininforService;
        @Autowired
        private ISysRoleService sysRoleService;

    @PostMapping("/list")
    public ResultInfo list(SysLogininfor logininfor, ResultInfo resultInfo)
    {
        if(logininfor.getBeTime()!=null){
            logininfor.setBeginTime(new Date(logininfor.getBeTime()));
        }
        if(logininfor.getEnTime()!=null){
            logininfor.setEndTime(new Date(logininfor.getEnTime()));
        }
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        resultInfo.setPages(list.size());
        //数据分页
        list=list.stream()
                .skip((resultInfo.getPageNumber()-1)*resultInfo.getTotal())
                .sorted(Comparator.comparing(SysLogininfor::getLoginTime).reversed())
                .limit(resultInfo.getTotal())
                .collect(Collectors.toList());
        resultInfo.setList(list);
        return resultInfo;
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult export(SysLogininfor logininfor)
    {
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setUserId(ShiroUtils.getUserId());
        Integer userRole = sysRoleService.selectRoleId(sysUserRole);
        if(userRole==null){
            userRole=0;
        }
        if(userRole==1||userRole==2){
            List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
            ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
            AjaxResult result = util.exportExcel(list, "登录日志");
            return result;
        }
        else {
            return AjaxResult.error("没有权限");
        }

    }

    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(String ids)
    {
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setUserId(ShiroUtils.getUserId());
        Integer userRole = sysRoleService.selectRoleId(sysUserRole);
        if(userRole==1||userRole==2){
            return toAjax(logininforService.deleteLogininforByIds(ids));
        }
        else {
            return AjaxResult.error("没有权限");
        }
    }
    
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    public AjaxResult clean()
    {
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setUserId(ShiroUtils.getUserId());
        Integer userRole = sysRoleService.selectRoleId(sysUserRole);
        if(userRole==1||userRole==2){
            logininforService.cleanLogininfor();
            return success();
        }
        else {
            return AjaxResult.error("没有权限");
        }

    }
}
