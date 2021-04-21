package com.ruoyi.web.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件上传对象 file_info
 */
public class SysFileInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1813993274127088655L;
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 文件名称
     */
    @Excel(name = "文件名称")
    private String fileName;
    /**
     * 文件路径
     */
    @Excel(name = "文件路径")
    private String filePath;
    /**
     * 存储业务的类型
     */
    @Excel(name = "存储业务的类型")
    private Integer fileType;
    /**
     * 业务的id
     */
    @Excel(name = "业务id")
    private Integer workId;

    private String fileIds;
    /**
     * 文件创建时间
     */
    @Excel(name = "上传时间")
    private Date fileTime;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 上传的用户名
     */
    private String userName;
    /**
     * 所属项目的信息
     */
    private SysProjectTable projectTable;
    /**
     * 所属里程碑信息
     */
    private ProjectPlanTable projectPlanTable;
    /**
     * 所属任务信息
     */
    private TaskTable taskTable;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    public Date getFileTime() {
        return fileTime;
    }

    public void setFileTime(Date fileTime) {
        this.fileTime = fileTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SysProjectTable getProjectTable() {
        return projectTable;
    }

    public void setProjectTable(SysProjectTable projectTable) {
        this.projectTable = projectTable;
    }

    public ProjectPlanTable getProjectPlanTable() {
        return projectPlanTable;
    }

    public void setProjectPlanTable(ProjectPlanTable projectPlanTable) {
        this.projectPlanTable = projectPlanTable;
    }

    public TaskTable getTaskTable() {
        return taskTable;
    }

    public void setTaskTable(TaskTable taskTable) {
        this.taskTable = taskTable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fileId", getFileId())
                .append("fileName", getFileName())
                .append("filePath", getFilePath())
                .toString();
    }
}
