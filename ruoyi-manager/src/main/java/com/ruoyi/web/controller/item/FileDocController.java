package com.ruoyi.web.controller.item;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.web.service.item.ProjectPlanService;
import com.ruoyi.web.service.item.SysProjectTableService;
import com.ruoyi.web.service.item.TaskTableService;
import com.ruoyi.common.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author suqiuzhao
 * @date 2019/7/30
 */
@Api(value = "FileDocController",tags = "文档模块相关接口")
@RestController
@RequestMapping(value = "/fileDoc")
public class FileDocController {
    @Autowired
    SysFileInfoService sysFileInfoService;
    @Autowired
    TaskTableService taskTableService;
    @Autowired
    SysProjectTableService sysProjectTableService;
    @Autowired
    ProjectPlanService projectPlanService;
    @Autowired
    ISysUserService sysUserService;

    /**
     * 查询所有文件
     * @return
     */
    @ApiOperation("全部文档（查询所有的文件）")
    @PostMapping(value = "/getAllFileInfo")
    public ResultInfo<SysFileInfo> getAllFileInfo(ResultInfo resultInfo){
        List<SysFileInfo> fileDoc=new ArrayList<>();
        List<SysFileInfo> sysFileInfos = sysFileInfoService.selectAllFileInfo();
        if(MyUtils.getMapCache("fileDoc")!=null){
            fileDoc= (List<SysFileInfo>) MyUtils.getMapCache("fileDoc");
        }else {
            //查询信息
            fileDoc = selectAllInfo(sysFileInfos, fileDoc);
        }
        resultInfo.setPages(fileDoc.size());
        fileDoc=fileDoc.stream()
                .skip((resultInfo.getPageNumber()-1)*resultInfo.getTotal())
                .limit(resultInfo.getTotal())
                .collect(Collectors.toList());
        resultInfo.setList(fileDoc);
        return resultInfo;
    }

    /**
     * 获取我的所有文件信息
     * @return
     */
    @ApiOperation("我的文档（查询我上传的文件）")
    @PostMapping(value = "/getMyAllFileInfoByUserId")
    public ResultInfo getMyAllFileInfoByUserId(ResultInfo resultInfo){
        List<SysFileInfo> fileDoc=new ArrayList<>();
        List<SysFileInfo> sysFileInfos = sysFileInfoService.selectAllFileInfo();
        if(MyUtils.getMapCache("fileDoc")!=null){
            fileDoc= (List<SysFileInfo>) MyUtils.getMapCache("fileDoc");
        }else {
            //查询信息
            fileDoc = selectAllInfo(sysFileInfos, fileDoc);
        }
        resultInfo.setPages(fileDoc.stream().filter(item->item.getUserId().equals(ShiroUtils.getUserId())).collect(Collectors.toList()).size());
        fileDoc=fileDoc.stream()
                .filter(item->item.getUserId().equals(ShiroUtils.getUserId()))
                .skip((resultInfo.getPageNumber()-1)*resultInfo.getTotal())
                .limit(resultInfo.getTotal())
                .collect(Collectors.toList());
        resultInfo.setList(fileDoc);
        return resultInfo;
    }

    /**
     * 获取所有共享类型文件信息
     * @return
     */
    @ApiOperation("共享文档（查询所有共享文档）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber",value = "当前页数",type = "Integer",paramType = "query",required = true),
            @ApiImplicitParam(name = "total",value = "每页显示数",type = "Integer",paramType = "query",required = true)
    })
    @PostMapping(value = "/selectAllShareFileInfo")
    public ResultInfo selectAllShareFileInfo(ResultInfo resultInfo){
        List<SysFileInfo> fileDoc=new ArrayList<>();
        List<SysFileInfo> sysFileInfos = sysFileInfoService.selectAllFileInfo();
        if(MyUtils.getMapCache("fileDoc")!=null){
            fileDoc= (List<SysFileInfo>) MyUtils.getMapCache("fileDoc");
        }else {
            //查询信息
            fileDoc = selectAllInfo(sysFileInfos, fileDoc);
        }
        resultInfo.setPages(fileDoc.stream().filter(item->item.getFileType()==3).collect(Collectors.toList()).size());
        fileDoc=fileDoc.stream()
                .filter(item->item.getFileType()==3)
                .skip((resultInfo.getPageNumber()-1)*resultInfo.getTotal())
                .limit(resultInfo.getTotal())
                .collect(Collectors.toList());
        resultInfo.setList(fileDoc);
        return resultInfo;
    }


    /**
     * 所有任务查询接口
     */
    @ApiOperation("全部文档内条件查询(根据关键词查询)")
    @PostMapping(value = "/selectFileInfo")
    public ResultInfo selectFileInfo(FileSelectInfo fileSelectInfo){
        return searchFiles(getAllFileInfo(new ResultInfo(fileSelectInfo.getPageNumber(),fileSelectInfo.getTotal())),fileSelectInfo);
    }

    /**
     * 我的任务查询接口
     */
    @ApiOperation("我的文档内条件查询(根据关键词查询)")
    @PostMapping(value = "/selectMyFileInfo")
    public ResultInfo selectMyFileInfo(FileSelectInfo fileSelectInfo){
        return searchFiles(getMyAllFileInfoByUserId(new ResultInfo(fileSelectInfo.getPageNumber(),fileSelectInfo.getTotal())),fileSelectInfo);
    }


    /**
     * 共享文档查询接口
     */
    @ApiOperation("共享文档内条件查询（根据关键词查询）")
    @PostMapping(value = "/selectShareFileInfo")
    public ResultInfo selectShareFileInfo(FileSelectInfo fileSelectInfo){
        return searchFiles(selectAllShareFileInfo(new ResultInfo(fileSelectInfo.getPageNumber(),fileSelectInfo.getTotal())),fileSelectInfo);
    }
    /**
     * 共享文件上传
     * @return
     */
    @Log(title = "文档模块:共享文件上传", businessType = BusinessType.INSERT)
    @ApiOperation(value = "共享文件上传",notes = "把文件上传后点保存，将上传文件的id发送该接口，接口修改文件信息，完成项目文件上传")
    @PostMapping(value = "/saveShareFile")
    public AjaxResult saveShareFile(String fileIds){
        try {
            String[] fileId = fileIds.split(",");
            for (int i = 0; i < fileId.length; i++) {
                SysFileInfo sysFileInfo=new SysFileInfo();
                sysFileInfo.setFileId(Long.parseLong(fileId[i]));
                sysFileInfo.setFileType(3);
                sysFileInfoService.updateFileInfo(sysFileInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.success();
    }

    public List<SysFileInfo> selectAllInfo(List<SysFileInfo> sysFileInfos,List<SysFileInfo> fileDoc){
        //将所有文件信息中存在的任务id存在taskMap中
        for (int i = 0; i < sysFileInfos.size(); i++) {
            fileDoc.add(sysFileInfos.get(i));
            if(sysFileInfos.get(i).getWorkId()!=null){
                //0表示任务文件，3任务共享文件
                if(sysFileInfos.get(i).getFileType()==0||sysFileInfos.get(i).getFileType()==3){
                    //根据tid查询任务信息
                    TaskTable taskTable = taskTableService.selectTaskTableById(Long.parseLong(sysFileInfos.get(i).getWorkId().toString()));
                    fileDoc.get(i).setTaskTable(taskTable);
                    //根据项目id查询项目信息
                    if(taskTable!=null&&taskTable.getProjectId()!=null&&!"".equals(taskTable.getProjectId())) {
                        SysProjectTable sysProjectTable = sysProjectTableService.selectProjectById(taskTable.getProjectId());
                        fileDoc.get(i).setProjectTable(sysProjectTable);
                    }
                    if(taskTable!=null&&taskTable.getParentId()!=null&&!"".equals(taskTable.getParentId())) {
                        //根据里程碑Id查询里程碑信息
                        ProjectPlanTable projectPlanTable = projectPlanService.selectProjectPlanTableById(Long.parseLong(taskTable.getParentId().toString()));
                        fileDoc.get(i).setProjectPlanTable(projectPlanTable);
                    }
                }
                //1表示项目文件，4任务共享文件
                else if(sysFileInfos.get(i).getFileType()==1||sysFileInfos.get(i).getFileType()==4){
                    SysProjectTable sysProjectTable = sysProjectTableService.selectProjectById(Long.parseLong(sysFileInfos.get(i).getWorkId().toString()));
                    fileDoc.get(i).setProjectTable(sysProjectTable);
                }
            }
        }
        MyUtils.putMapCache("fileDoc",fileDoc);
        return fileDoc;
    }

    public ResultInfo searchFiles(ResultInfo resultInfo, FileSelectInfo fileSelectInfo){
        String queryInfo=fileSelectInfo.getQueryInfo();
        if(MyUtils.isEmpty(queryInfo)){
            return resultInfo;
        }
        //用于存储返回的数据
        List<SysFileInfo> fileInfos=new ArrayList<>();
        List<SysFileInfo> fileDoc = resultInfo.getList();
        //根据文件名称过滤数据
        fileInfos.addAll(
                fileDoc.stream()
                        .filter(item->item.getFileName()!=null)
                        .filter(item->item.getFileName().contains(queryInfo))
                        .collect(Collectors.toList())
        );
        //根据上传人姓名过滤数据
        fileInfos.addAll(
                fileDoc.stream()
                        .filter(item->item.getUserName()!=null)
                        .filter(item->item.getUserName().contains(queryInfo))
                        .collect(Collectors.toList())
        );
        //根据项目名称过滤数据
        fileInfos.addAll(
                fileDoc.stream()
                        .filter(item->item.getProjectTable()!=null)
                        .filter(item->item.getProjectTable().getTitle()!=null)
                        .filter(item->item.getProjectTable().getTitle().contains(queryInfo))
                        .collect(Collectors.toList())
        );
        //根据里程碑名称过滤数据
        fileInfos.addAll(
                fileDoc.stream()
                        .filter(item->item.getProjectPlanTable()!=null)
                        .filter(item->item.getProjectPlanTable().getPlanTitle()!=null)
                        .filter(item->item.getProjectPlanTable().getPlanTitle().contains(queryInfo))
                        .collect(Collectors.toList())
        );
        //根据任务名称过滤数据
        fileInfos.addAll(
                fileDoc.stream()
                        .filter(item->item.getTaskTable()!=null)
                        .filter(item->item.getTaskTable().getTaskTitle()!=null)
                        .filter(item->item.getTaskTable().getTaskTitle().contains(queryInfo))
                        .collect(Collectors.toList())
        );
        //过滤重复数据
        fileInfos=fileInfos.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(o-> o.getFileId()))), ArrayList::new))
                .stream().sorted(Comparator.comparing(SysFileInfo::getFileId))
                .collect(Collectors.toList());
        resultInfo.setPages(fileInfos.size());
        //分页
        fileInfos=fileInfos.stream()
                .skip((fileSelectInfo.getPageNumber()-1)*fileSelectInfo.getTotal())
                .limit(fileSelectInfo.getTotal())
                .collect(Collectors.toList());
        resultInfo.setList(fileInfos);
        return resultInfo;
    }
}
