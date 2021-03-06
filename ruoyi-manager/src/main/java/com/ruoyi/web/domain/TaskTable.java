package com.ruoyi.web.domain;

import com.ruoyi.system.domain.SysUser;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.vo.SysUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务对象实体类
 *
 * @author zhongzhilong
 * @date 2019/8/14 0015
 */
public class TaskTable extends BaseEntity implements Serializable {

    private static final long SERIAL_VERSION_UID = -2561528138301797672L;

    @ApiModelProperty("任务id")
    private Long tId;

    @ApiModelProperty("所属项目id,临时任务为空或者0")
    @Excel(name = "所属项目id,可以为空")
    private Long projectId;

    @ApiModelProperty("所属项目里程碑")
    @Excel(name = "所属项目里程碑，可为空")
    private Long planId;

    @ApiModelProperty("任务标题")
    @Excel(name = "任务标题")
    private String taskTitle;

    @ApiModelProperty("任务详细描述")
    @Excel(name = "任务详细描述")
    private String taskDescribe;

    @ApiModelProperty("即里程碑id")
    private Integer parentId;

    @ApiModelProperty("任务负责人id")
    @Excel(name = "任务负责人id")
    private Long chargepeopleId;

    @ApiModelProperty("任务开始时间")
    @Excel(name = "任务开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("任务结束时间")
    @Excel(name = "任务结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("任务周期")
    @Excel(name = "周期")
    private String period;

    @Excel(name = "紧急程度: 0-正常，1-紧急")
    private String urgencyLevel;

    @ApiModelProperty("任务进度进度")
    @Excel(name = "进度")
    private Integer scheduleRate;

    @ApiModelProperty("是否需要提前提醒，0：否，1：是")
    @Excel(name = "是否需要提前提醒，0：否，1：是")
    private String warnFlag;

    @ApiModelProperty("是否已经提醒过，0：未提醒，1：已经提醒")
    @Excel(name = "是否已经提醒过，0：未提醒，1：已经提醒")
    private String warnDays;

    @ApiModelProperty("提醒给谁，0：负责人")
    @Excel(name = "提醒给谁，0：负责人")
    private String warnToobject;

    @ApiModelProperty("提醒给负责人=1")
    private String remindChargePeople;

    @ApiModelProperty("提醒给参与人=1")
    private String remindPanticiants;

    @ApiModelProperty("自定义提醒谁=1")
    private String remindOthars;

    @ApiModelProperty("是否已经提醒过，0：未提醒，1：已经提醒=1")
    @Excel(name = "是否已经提醒过，0：未提醒，1：已经提醒")
    private String warnStatus;

    @ApiModelProperty("任务进度提交时间，多时间以分号隔开")
    @Excel(name = "任务进度提交时间，多时间以分号隔开", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskSubmittime;

    @ApiModelProperty("格式：时间+进度+说明文字")
    @Excel(name = "格式：时间+进度+说明文字")
    private String taskSubmitremark;

    @ApiModelProperty("上传的文档ftp地址")
    @Excel(name = "上传的文档ftp地址")
    private String taskUpdatefileftp;

    @ApiModelProperty("任务完成标识，0：未完成，1：完成，2:任务中止")
    @Excel(name = "任务完成标识，0：未完成，1：完成，2:任务中止")
    private String taskFinishflag;

    @ApiModelProperty("任务完成提交时间")
    @Excel(name = "任务完成提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskFinishsubmittime;
    private String taskFinishsubmittimes;

    @ApiModelProperty("中止备注")
    @Excel(name = "中止备注")
    private String stopMone;

    @ApiModelProperty("里程碑标题")
    private String projectPlanName;

    @ApiModelProperty("审批创建人")
    private String approvalCommitUserName;

    @ApiModelProperty("项目标题")
    private String projectName;

    @ApiModelProperty("任务负责人名字")
    private String chargePeopleName;

    @ApiModelProperty("关注人")
    private String attention;

    @ApiModelProperty("任务开始时间字符串")
    private long startTimes;

    @ApiModelProperty("任务结束时间字符串")
    private long endTimes;

    @ApiModelProperty("一级审批人")
    private String firstUserId;

    @ApiModelProperty("二级审批人")
    private String secondUserId;

    @ApiModelProperty("审批类型")
    private String flowId;

    @ApiModelProperty("查询下属标识，0不查，1查询")
    private String searchUnderling;

    @ApiModelProperty("查询下属任务的一个关键字段")
    private String searchDepeId;

    @ApiModelProperty("任务变更对象")
    private TaskPostpone taskPostpone;

    @ApiModelProperty("审批记录对象集合")
    private List<ResultApprovalRecord> resultApprovalRecordList;

    @ApiModelProperty("里程碑对象")
    private ProjectPlanTable projectPlanTable;

    @ApiModelProperty("任务进度提交对象集合")
    private List<TaskSubnitPlan> taskSubnitPlan;

    @ApiModelProperty("项目对象")
    private SysProjectTable projectTable;

    @ApiModelProperty("文件对象集合")
    private List<SysFileInfo> fileInfoList;

    @ApiModelProperty("任务参与人对象集合")
    private List<TaskUser> taskUserList;

    @ApiModelProperty("负责人姓名")
    private String chargepeopleName;

    @ApiModelProperty("发起人姓名")
    private String createName;

    @ApiModelProperty("任务逾期提醒状态")
    private String taskOverdueState;

    @ApiModelProperty("任务未开始状态")
    private String taskNotstartState;

    @ApiModelProperty("用户id")
    private long userId;

    @ApiModelProperty("职务id")
    private int postId;

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("用户关注的任务状态")
    private Integer userAttention;

    @ApiModelProperty("开始时间")
    private String beginDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("任务进度说明")
    private String taskMemo;

    @ApiModelProperty("任务负责人和参与人字符串，以逗号隔开")
    private List<Integer> peopleList;

    @ApiModelProperty("负责人和参与人")
    private String peopleString;

    @ApiModelProperty("下属id")
    private List<Integer> lowerPeopleId;

    @ApiModelProperty("临时参数")
    private Integer allow;

    @ApiModelProperty("中止原因")
    private String taskStopMemo;

    @ApiModelProperty("用来获取逾期且是本月未完成的任务的模块")
    private Integer overdue;

    @ApiModelProperty("开始时间")
    private Date begin;

    @ApiModelProperty("结束时间")
    private Date end;

    @ApiModelProperty("负责人对象")
    private SysUser chargePeopleUsers;

    @ApiModelProperty("用户对象")
    private SysUser sysUser;

    @ApiModelProperty("变更原因")
    private String changeTimeMemo;

    @ApiModelProperty("是否计划内")
    private Integer planInOut;

    @ApiModelProperty("关键字")
    private Integer isKey;

    @ApiModelProperty("任务分数")
    private Double taskScore;

    @ApiModelProperty("uuId")
    private Integer uuId;

    @ApiModelProperty("抄送参数")
    private String ccIds;

    @ApiModelProperty("项目类型")
    private Integer projectType;

    @ApiModelProperty("产品经理")
    private List<SysUserVo> managersList;

    @ApiModelProperty("taskImportedBy")
    private String taskImportedBy;

    @ApiModelProperty("任务中止发起人")
    private String stopUser;

    @ApiModelProperty("任务评分的审批人")
    private String taskRater;

    @ApiModelProperty("当前最新的审核用户")
    private String currentAuditUser;

    @ApiModelProperty("申请完成发起人、申请中止发起人 、申请变更审批发起人")
    private String approvalName;

    @ApiModelProperty("已完成、已终止原因")
    private String approvalMemo;

    @ApiModelProperty("审批流id")
    private Integer currentId;

    public Integer getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public String getApprovalMemo() {
        return approvalMemo;
    }

    public void setApprovalMemo(String approvalMemo) {
        this.approvalMemo = approvalMemo;
    }

    public String getCurrentAuditUser() {
        return currentAuditUser;
    }

    public void setCurrentAuditUser(String currentAuditUser) {
        this.currentAuditUser = currentAuditUser;
    }

    public String getTaskRater() {
        return taskRater;
    }

    public void setTaskRater(String taskRater) {
        this.taskRater = taskRater;
    }

    public String getStopUser() {
        return stopUser;
    }

    public void setStopUser(String stopUser) {
        this.stopUser = stopUser;
    }

    public String getTaskImportedBy() {
        return taskImportedBy;
    }

    public void setTaskImportedBy(String taskImportedBy) {
        this.taskImportedBy = taskImportedBy;
    }

    public Integer getScheduleRate() {
        return scheduleRate;
    }

    public void setScheduleRate(Integer scheduleRate) {
        this.scheduleRate = scheduleRate;
    }

    public List<SysUserVo> getManagersList() {
        return managersList;
    }

    public void setManagersList(List<SysUserVo> managersList) {
        this.managersList = managersList;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public String getCcIds() {
        return ccIds;
    }

    public void setCcIds(String ccIds) {
        this.ccIds = ccIds;
    }

    public Integer getUuId() {
        return uuId;
    }

    public void setUuId(Integer uuId) {
        this.uuId = uuId;
    }

    public Double getTaskScore() {
        return taskScore;
    }

    public void setTaskScore(Double taskScore) {
        this.taskScore = taskScore;
    }

    public Integer getIsKey() {
        return isKey;
    }

    public void setIsKey(Integer isKey) {
        this.isKey = isKey;
    }

    public Integer getPlanInOut() {
        return planInOut;
    }

    public void setPlanInOut(Integer planInOut) {
        this.planInOut = planInOut;
    }

    public String getChangeTimeMemo() {
        return changeTimeMemo;
    }

    public void setChangeTimeMemo(String changeTimeMemo) {
        this.changeTimeMemo = changeTimeMemo;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public SysUser getChargePeopleUsers() {
        return chargePeopleUsers;
    }

    public void setChargePeopleUsers(SysUser chargePeopleUsers) {
        this.chargePeopleUsers = chargePeopleUsers;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }

    public String getTaskStopMemo() {
        return taskStopMemo;
    }

    public void setTaskStopMemo(String taskStopMemo) {
        this.taskStopMemo = taskStopMemo;
    }

    public Integer getAllow() {
        return allow;
    }

    public void setAllow(Integer allow) {
        this.allow = allow;
    }

    public List<Integer> getLowerPeopleId() {
        return lowerPeopleId;
    }

    public void setLowerPeopleId(List<Integer> lowerPeopleId) {
        this.lowerPeopleId = lowerPeopleId;
    }

    public List<Integer> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(List<Integer> peopleList) {
        this.peopleList = peopleList;
    }

    public String getPeopleString() {
        return peopleString;
    }

    public void setPeopleString(String peopleString) {
        this.peopleString = peopleString;
    }

    public String getTaskMemo() {
        return taskMemo;
    }

    public void setTaskMemo(String taskMemo) {
        this.taskMemo = taskMemo;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getUserAttention() {
        return userAttention;
    }

    public void setUserAttention(Integer userAttention) {
        this.userAttention = userAttention;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRemindChargePeople() {
        return remindChargePeople;
    }

    public void setRemindChargePeople(String remindChargePeople) {
        this.remindChargePeople = remindChargePeople;
    }

    public String getRemindPanticiants() {
        return remindPanticiants;
    }

    public void setRemindPanticiants(String remindPanticiants) {
        this.remindPanticiants = remindPanticiants;
    }

    public String getRemindOthars() {
        return remindOthars;
    }

    public void setRemindOthars(String remindOthars) {
        this.remindOthars = remindOthars;
    }

    public List<ResultApprovalRecord> getResultApprovalRecordList() {
        return resultApprovalRecordList;
    }

    public void setResultApprovalRecordList(List<ResultApprovalRecord> resultApprovalRecordList) {
        this.resultApprovalRecordList = resultApprovalRecordList;
    }

    public String getApprovalCommitUserName() {
        return approvalCommitUserName;
    }

    public void setApprovalCommitUserName(String approvalCommitUserName) {
        this.approvalCommitUserName = approvalCommitUserName;
    }

    public String getTaskOverdueState() {
        return taskOverdueState;
    }

    public void setTaskOverdueState(String taskOverdueState) {
        this.taskOverdueState = taskOverdueState;
    }

    public String getTaskNotstartState() {
        return taskNotstartState;
    }

    public void setTaskNotstartState(String taskNotstartState) {
        this.taskNotstartState = taskNotstartState;
    }

    public String getChargepeopleName() {
        return chargepeopleName;
    }

    public void setChargepeopleName(String chargepeopleName) {
        this.chargepeopleName = chargepeopleName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public List<TaskUser> getTaskUserList() {
        return taskUserList;
    }

    public void setTaskUserList(List<TaskUser> taskUserList) {
        this.taskUserList = taskUserList;
    }

    public List<SysFileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<SysFileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    public String getChargePeopleName() {
        return chargePeopleName;
    }

    public void setChargePeopleName(String chargePeopleName) {
        this.chargePeopleName = chargePeopleName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescribe() {
        return taskDescribe;
    }

    public void setTaskDescribe(String taskDescribe) {
        this.taskDescribe = taskDescribe;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Long getChargepeopleId() {
        return chargepeopleId;
    }

    public void setChargepeopleId(Long chargepeopleId) {
        this.chargepeopleId = chargepeopleId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getWarnFlag() {
        return warnFlag;
    }

    public void setWarnFlag(String warnFlag) {
        this.warnFlag = warnFlag;
    }

    public String getWarnDays() {
        return warnDays;
    }

    public void setWarnDays(String warnDays) {
        this.warnDays = warnDays;
    }

    public String getWarnToobject() {
        return warnToobject;
    }

    public void setWarnToobject(String warnToobject) {
        this.warnToobject = warnToobject;
    }

    public String getWarnStatus() {
        return warnStatus;
    }

    public void setWarnStatus(String warnStatus) {
        this.warnStatus = warnStatus;
    }

    public Date getTaskSubmittime() {
        return taskSubmittime;
    }

    public void setTaskSubmittime(Date taskSubmittime) {
        this.taskSubmittime = taskSubmittime;
    }

    public String getTaskSubmitremark() {
        return taskSubmitremark;
    }

    public void setTaskSubmitremark(String taskSubmitremark) {
        this.taskSubmitremark = taskSubmitremark;
    }

    public String getTaskUpdatefileftp() {
        return taskUpdatefileftp;
    }

    public void setTaskUpdatefileftp(String taskUpdatefileftp) {
        this.taskUpdatefileftp = taskUpdatefileftp;
    }

    public String getTaskFinishflag() {
        return taskFinishflag;
    }

    public void setTaskFinishflag(String taskFinishflag) {
        this.taskFinishflag = taskFinishflag;
    }

    public Date getTaskFinishsubmittime() {
        return taskFinishsubmittime;
    }

    public void setTaskFinishsubmittime(Date taskFinishsubmittime) {
        this.taskFinishsubmittime = taskFinishsubmittime;
    }

    public String getTaskFinishsubmittimes() {
        return taskFinishsubmittimes;
    }

    public void setTaskFinishsubmittimes(String taskFinishsubmittimes) {
        this.taskFinishsubmittimes = taskFinishsubmittimes;
    }

    public String getProjectPlanName() {
        return projectPlanName;
    }

    public void setProjectPlanName(String projectPlanName) {
        this.projectPlanName = projectPlanName;
    }

    public String getStopMone() {
        return stopMone;
    }

    public void setStopMone(String stopMone) {
        this.stopMone = stopMone;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public long getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(long startTimes) {
        this.startTimes = startTimes;
    }

    public long getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(long endTimes) {
        this.endTimes = endTimes;
    }

    public String getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(String firstUserId) {
        this.firstUserId = firstUserId;
    }

    public String getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(String secondUserId) {
        this.secondUserId = secondUserId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getSearchUnderling() {
        return searchUnderling;
    }

    public void setSearchUnderling(String searchUnderling) {
        this.searchUnderling = searchUnderling;
    }

    public String getSearchDepeId() {
        return searchDepeId;
    }

    public void setSearchDepeId(String searchDepeId) {
        this.searchDepeId = searchDepeId;
    }

    public TaskPostpone getTaskPostpone() {
        return taskPostpone;
    }

    public void setTaskPostpone(TaskPostpone taskPostpone) {
        this.taskPostpone = taskPostpone;
    }

    public ProjectPlanTable getProjectPlanTable() {
        return projectPlanTable;
    }

    public void setProjectPlanTable(ProjectPlanTable projectPlanTable) {
        this.projectPlanTable = projectPlanTable;
    }

    public List<TaskSubnitPlan> getTaskSubnitPlan() {
        return taskSubnitPlan;
    }

    public void setTaskSubnitPlan(List<TaskSubnitPlan> taskSubnitPlan) {
        this.taskSubnitPlan = taskSubnitPlan;
    }

    public SysProjectTable getProjectTable() {
        return projectTable;
    }

    public void setProjectTable(SysProjectTable projectTable) {
        this.projectTable = projectTable;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
