package com.ruoyi.web.controller.news;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.SysFileInfo;
import com.ruoyi.web.domain.news.NewsMsg;
import com.ruoyi.web.domain.news.vo.NewsMsgVo;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.web.service.news.NewsMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新闻类控制器
 *
 * @author zhongzhilong
 * @date 2020/10/9 0009
 */
@RequestMapping("/newsMsg")
@Controller
@Api(value = "NewsMsgController", tags = "新闻类相关接口")
public class NewsMsgController extends BaseController {
    @Autowired
    private NewsMsgService newsMsgService;
    @Autowired
    private SysFileInfoService fileInfoService;
    @Value("${ruoyi.profile}")
    private String profile;

    @ApiOperation("上传图片接口-返回图片路径")
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxResult fileUpload(@RequestParam("file") MultipartFile[] file) {
        Map<String, Object> map = new HashMap<>();
        MyUtils.removeMapCache("fileDoc");
        for (int i = 0; i < file.length; i++) {
            if (!file[i].isEmpty()) {
                String fileName = "";
                try {
                    fileName = FileUploadUtils.upload(FileUploadUtils.defaultBaseDir, file[i]);
                    SysFileInfo sysFileInfo = new SysFileInfo();
                    sysFileInfo.setFileName(file[i].getOriginalFilename());
                    sysFileInfo.setFilePath(fileName);
                    sysFileInfo.setFileType(5);
                    fileInfoService.insertFileInfo(sysFileInfo);
                    map.put("picturesUrl", profile + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    return AjaxResult.error("上传失败");
                }
            }
        }
        return AjaxResult.success("上传成功", map);
    }

    @ApiOperation("添加新闻具体信息接口")
    @ResponseBody
    @RequestMapping(value = "/addNewsMsg", method = {RequestMethod.POST})
    public AjaxResult addNewsMsg(@RequestBody NewsMsg newsMsg) {
        try {
            if (newsMsg == null || ShiroUtils.isNull(newsMsg.getTitle())) {
                return AjaxResult.error("参数有误，添加失败");
            }
            return toAjax(newsMsgService.insertNewsMsg(newsMsg));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("添加失败");
        }
    }

    @ApiOperation("分页查询新闻信息列表")
    @ResponseBody
    @RequestMapping(value = "/selectNewsMsgList", method = {RequestMethod.POST})
    public AjaxResult selectNewsMsgList(@RequestBody NewsMsgVo vo) {
        try {
            if (vo.getPageNumber() == null || vo.getTotal() == null) {
                return AjaxResult.error("分页参数不能为空");
            }
            PageEntity pageEntity = newsMsgService.selectNewsMsgList(vo);
            return AjaxResult.success("查询成功", pageEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询失败");
        }
    }

    @ApiOperation("查询新闻类别列表-添加新闻列表中下拉框选择新闻类别")
    @ResponseBody
    @RequestMapping(value = "/selectTypeList", method = {RequestMethod.POST})
    public AjaxResult selectTypeList() {
        try {
            List<Map<String, Object>> resultList = newsMsgService.selectTypeList();
            return AjaxResult.success("查询成功", resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询失败");
        }
    }

    @ApiOperation("修改新闻信息")
    @ResponseBody
    @RequestMapping(value = "/modifyNewsMsg", method = {RequestMethod.POST})
    public AjaxResult modifyNewsMsg(@RequestBody NewsMsg vo) {
        try {
            return toAjax(newsMsgService.modifyNewsMsg(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("修改失败");
        }
    }

    @ApiOperation("删除新闻信息")
    @ResponseBody
    @RequestMapping(value = "/deleteNewsMsg", method = {RequestMethod.POST})
    public AjaxResult deleteNewsMsg(@RequestBody NewsMsg vo) {
        try {
            return toAjax(newsMsgService.deleteNewsMsg(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("删除失败");
        }
    }
}
