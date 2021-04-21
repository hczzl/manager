package com.ruoyi.web.controller.monitor;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.web.domain.FileSelectInfo;
import com.ruoyi.web.domain.ResultInfo;
import com.ruoyi.web.domain.SysFileInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.service.ISysOperLogService;

/**
 * 操作日志记录
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController
{
    @Autowired
    private ISysOperLogService operLogService;
    @Autowired
    private ISysRoleService sysRoleService;

    @PostMapping("/list")
    @ResponseBody
    public ResultInfo list(SysOperLog operLog, ResultInfo resultInfo)
    {
        return searchOperLog(operLog,resultInfo);
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysOperLog operLog)
    {
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setUserId(ShiroUtils.getUserId());
        Integer userRole = sysRoleService.selectRoleId(sysUserRole);
        if(userRole==null){
            userRole=0;
        }
        if(userRole==1||userRole==2){
            List<SysOperLog> list = operLogService.selectOperLogList(operLog);
            ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
            return util.exportExcel(list, "操作日志");
        }
        else {
            return AjaxResult.error("没有权限");
        }

    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setUserId(ShiroUtils.getUserId());
        Integer userRole = sysRoleService.selectRoleId(sysUserRole);
        if(userRole==null){
            userRole=0;
        }
        if(userRole==1||userRole==2){
            return toAjax(operLogService.deleteOperLogByIds(ids));
        }
        else {
            return AjaxResult.error("没有权限");
        }
    }

    @PostMapping("/detail")
    public SysOperLog detail(Long operId)
    {
        return operLogService.selectOperLogById(operId);
    }
    
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean()
    {
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setUserId(ShiroUtils.getUserId());
        Integer userRole = sysRoleService.selectRoleId(sysUserRole);
        if(userRole==null){
            userRole=0;
        }
        if(userRole==1||userRole==2){
            operLogService.cleanOperLog();
            return success();
        }
        else {
            return AjaxResult.error("没有权限");
        }

    }

    public ResultInfo searchOperLog(SysOperLog operLog, ResultInfo resultInfo){
        if(operLog.getBeTime()!=null){
            operLog.setBeginTime(new Date(operLog.getBeTime()));
        }
        if(operLog.getEnTime()!=null){
            operLog.setEndTime(new Date(operLog.getEnTime()));
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SysOperLog> searchLogList=new ArrayList<>();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        resultInfo.setPages(list.size());

        if (operLog.getTitle()!=null){
            Integer businessType = changeSearchInfoInBusinessType(operLog.getTitle());
            if(businessType!=10){
                operLog.setBusinessType(businessType);
            }
            if(operLog.getBusinessType()!=null){
                //根据操作类型
                searchLogList.addAll(
                        list.stream()
                                .filter(item->item.getBusinessType()!=null)
                                .filter(item->item.getBusinessType().equals(operLog.getBusinessType()))
                                .collect(Collectors.toList())
                );
            }

            Integer status=changeSearchInfoInStatus(operLog.getTitle());
            if(businessType!=3){
                operLog.setStatus(status);
            }
            if(operLog.getStatus()!=null){
                //根据状态
                searchLogList.addAll(
                        list.stream()
                                .filter(item->item.getBusinessType()!=null)
                                .filter(item->item.getStatus().equals(operLog.getStatus()))
                                .collect(Collectors.toList())
                );
            }
            //根据编号
            searchLogList.addAll(
                    list.stream()
                            .filter(item->item.getOperId()!=null)
                            .filter(item->item.getOperId().toString().contains(operLog.getTitle()))
                            .collect(Collectors.toList())
            );
            //操作名称
            searchLogList.addAll(
                    list.stream()
                            .filter(item->item.getTitle()!=null)
                            .filter(item->item.getTitle().contains(operLog.getTitle()))
                            .collect(Collectors.toList())
            );
            //操作人员
            searchLogList.addAll(
                    list.stream()
                            .filter(item->item.getOperName()!=null)
                            .filter(item->item.getOperName().contains(operLog.getTitle()))
                            .collect(Collectors.toList())
            );
            //根据操作时间
            searchLogList.addAll(
                    list.stream()
                            .filter(item->item.getOperTime()!=null)
                            .filter(item->simpleDateFormat.format(item.getOperTime()).equals(operLog.getTitle()))
                            .collect(Collectors.toList())
            );
            //过滤重复数据
            searchLogList=searchLogList.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(o-> o.getOperId()))), ArrayList::new))
                    .stream().sorted(Comparator.comparing(SysOperLog::getOperId))
                    .collect(Collectors.toList());
            resultInfo.setPages(list.size());

            //数据分页
            list=searchLogList.stream()
                    .sorted(Comparator.comparing(SysOperLog::getOperTime).reversed())
                    .skip((resultInfo.getPageNumber()-1)*resultInfo.getTotal())
                    .limit(resultInfo.getTotal())
                    .collect(Collectors.toList());
            resultInfo.setList(list);
            return resultInfo;
        }

        //数据分页
        list=list.stream()
                .sorted(Comparator.comparing(SysOperLog::getOperTime).reversed())
                .skip((resultInfo.getPageNumber()-1)*resultInfo.getTotal())
                .limit(resultInfo.getTotal())
                .collect(Collectors.toList());
        resultInfo.setList(list);
        return resultInfo;
    }

    public Integer changeSearchInfoInBusinessType(String title){
        Integer businessType;
        switch (title){
            case "其它":
                businessType=0;
                break;
            case "新增":
                businessType=1;
                break;
            case "修改":
                businessType=2;
                break;
            case "删除":
                businessType=3;
                break;
            case "授权":
                businessType=4;
                break;
            case "导出":
                businessType=5;
                break;
            case "导入":
                businessType=6;
                break;
            case "强退":
                businessType=7;
                break;
            case "生成代码":
                businessType=8;
                break;
            case "清空数据":
                businessType=9;
                break;
            default:
                businessType=10;
        }
        return businessType;
    }
    public Integer changeSearchInfoInStatus (String title){
        Integer status;
        switch (title){
            case "成功":
                status=0;
                break;
            case "失败":
                status=1;
                break;
            default:
                status=3;
        }
        return status;
    }
}
