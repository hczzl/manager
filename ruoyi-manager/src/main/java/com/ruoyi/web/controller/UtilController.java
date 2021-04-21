package com.ruoyi.web.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.web.domain.SysFileInfo;
import com.ruoyi.web.service.SysFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnv on 2019/8/8.
 */
@Controller
@RequestMapping("/file")
public class UtilController {
    @Autowired
    private SysFileInfoService fileInfoService;
    @Value("${ruoyi.profile}")
    private String profile;
    @RequestMapping("/upload")
    @ResponseBody
    @Log(title = "上传文件", businessType = BusinessType.INSERT)
    public  List<SysFileInfo> handleFileUpload(@RequestParam("file") MultipartFile[] file ,SysFileInfo sysFileInfo1 ) {
        MyUtils.removeMapCache("fileDoc");
        List<SysFileInfo> fileInfoList=new ArrayList<>();
        for(int i = 0;i<file.length;i++){
            if (!file[i].isEmpty()) {
                String fileName="";
                try {
                    fileName= FileUploadUtils.upload(FileUploadUtils.defaultBaseDir,file[i]);
                    SysFileInfo sysFileInfo=new SysFileInfo();
                    sysFileInfo.setFileName(file[i].getOriginalFilename());
                    sysFileInfo.setFilePath(fileName);
                    sysFileInfo.setFileType(sysFileInfo1.getFileType());
                    fileInfoList.add(sysFileInfo);
                    fileInfoService.insertFileInfo(sysFileInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        return fileInfoList;

    }
    @GetMapping("/downloadFile/{fileId}")
    @Log(title = "下载文件", businessType = BusinessType.OTHER)
    public void downloadFile(@PathVariable("fileId") Integer fileId, HttpServletResponse response,
                             HttpServletRequest request) throws Exception
    {

        String realFileName = "e4a32fa0e262c01a77af3225f9261072.jpg";
        String path = "D:\\profile\\2019\\08\\08";
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
        FileUtils.writeBytes(path, response.getOutputStream());
    }
}
