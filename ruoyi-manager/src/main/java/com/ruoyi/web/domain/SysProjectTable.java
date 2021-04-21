package com.ruoyi.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * sys_ProjectTable
 * 项目表
 *
 * @author zzl
 */
@ApiModel
public class SysProjectTable extends BaseEntity implements Serializable {

    private static final long SERIAL_VERSION_UID = 2415551748925819833L;
    /**
     * 项目编号
     */
    @Excel(name = "项目id")
    @ApiModelProperty("项目id")
    private Integer pId;
    /**
     * 项目文件
     */
    private List<SysFileInfo> fileInfoList;
    /**
     * 部门id
     */
    @ApiModelProperty("项目名字")
    private String title;

    @ApiModelProperty("产品线id")
    private Integer producetypeid;
    /**
     * 项目详细描述
     */
    @ApiModelProperty("项目描述或者项目的负责人")
    private String describeProject;
    /**
     * 项目负责人id
     */
    @ApiModelProperty("商务负责人")
    private Integer chargepeopleId;

    @ApiModelProperty("商务参与人id,多个,隔开")
    private String businessParticipants;

    /**
     * 项目参与人id
     */
    private String participants;
    /**
     * 参与人名字
     */
    private String participantsName;

    /**
     * 科研项目所属公司内外（0:公司内，1：公司外）
     */
    private Integer inOrOut;
    /**
     * 周期
     */
    @ApiModelProperty("周期")
    private String period;
    /**
     * 项目起始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;
    /**
     * 项目结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("开始时间时间戳")
    private String startTimes;
    /**
     * 项目结束时间
     */
    @ApiModelProperty("结束时间时间戳")
    private String endTimes;

    @ApiModelProperty("关键词查询")
    private String queryInfo;
    /**
     * 进度,由double类型改为Integer 类型
     */
    @ApiModelProperty("项目进度")
    private Integer planRate;
    /**
     * 项目备注
     */
    @ApiModelProperty("项目备注")
    private String memo;

    /**
     * 验收时间或鉴定时间
     */
    @ApiModelProperty("验收时间或鉴定时间")
    private Date checktime;

    /**
     * 中止类型
     */
    @ApiModelProperty("中止类型")
    private String stoptype;
    /**
     * 中止原因
     */
    @ApiModelProperty("中止原因")
    private String stopcause;
    /**
     * 项目提交时间
     */
    private Date establishTime;
    /**
     * 部门ID
     */
    private Integer departmentId;

    /**
     * 立项成功标识
     */
    private String establishStatus;
    /**
     * 项目完成标识
     */
    private String projectFinishFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 用来存取部门名称
     */
    private String departmentName;
    /**
     * 用来存取负责人名字
     */
    private String chargePeopleName;
    /**
     * 项目审批创建人
     */
    private String approvalCommitUserName;

    private Integer flowId;
    /**
     * 一级
     */
    private String firstUserId;
    /**
     * 二级
     */
    private String secondUserId;
    /**
     * 三级
     */
    private String thirdUserId;
    /**
     * 四级审批人
     */
    private String fourthUserId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 存储项目项目参与人名字
     */
    private String panticiantName;
    /**
     * 存储项目项目参与人id
     */
    private String participantsId;
    /**
     * 返回一个项目参与人的list
     */
    private List<ParticipantsTable> participantsList;
    /**
     * 返回一个审批记录列表
     */
    private List<ResultApprovalRecord> resultApprovalRecordList;
    /**
     * 关注项目的用户
     */
    private String attention;
    /**
     * 用户关注的项目状态
     */
    private Integer userAttention;
    /**
     * 项目类型
     */
    private Integer projectType;

    private Integer customerIsEstablish;

    private String buildPeriod;

    private Date expectTime;

    private Integer customerIsBudget;

    private Integer budgetAmount;

    private Date budgetTime;

    private Integer budgettimeAndAmcount;

    private Integer techniquePeople;

    private String updateFilePath;

    private Integer clientHighdirectorJob;

    private Integer clientHighDupdirectorJob;

    private Integer clientMidbranchleaderJob;

    private Integer clientMidDupbranchleaderJob;

    private Integer clientMidcommissarjob;

    private Integer clientHighdirectorAuthority;

    private Integer clientHighDupdirectorAuthority;

    private Integer clientMidbranchleaderAuthority;

    private Integer clientMidDupbranchleaderAuthority;

    private Integer clientMidcommissarAnthority;

    private Integer clientHighdirectorReporting;

    private Integer clientHighDupdirectorReporting;

    private Integer clientMidbranchleaderReporting;

    private Integer clientMidDupbranchleaderReporting;

    private Integer clientMidcommissarReporting;

    private Integer clientHighdirectorMeet;

    private Integer clientHighDupdirectorMeet;

    private Integer clientMidbranchleaderMeet;

    private Integer clientMidDupbranchleaderMeet;

    private Integer clientMidcommissarMeet;

    private Integer clientHighdirectorTransmit;

    private Integer clientHighDupdirectorTransmit;

    private Integer clientMidbranchleaderTransmit;

    private Integer clientMidDupbranchleaderTransmit;

    private Integer clientMidcommissarTransmit;

    private Integer clientHighdirectorWill;

    private Integer clientHighDupdirectorWill;

    private Integer clientMidbranchleaderWill;

    private Integer clientMidDupbranchleaderWill;

    private Integer clientMidcommissarWill;

    private Integer clientLowinfluence;

    private String needBackground;

    private String needApplication;

    private Integer needUnderstand;

    private Integer needFeasible;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date needEndTime;
    /**
     * 是否垫资
     */
    private Integer isDianzi;
    /**
     * 周期
     */
    private Integer dianziPeriod;
    /**
     * 垫资金额
     */
    private Integer dianziMoney;
    /**
     * 是否试用
     */
    private Integer isShiyong;

    private Integer shiyongPeriod;

    private String riskDescribeBusiness;

    private String riskSolutionsBusiness;

    private String riskDescribeTechnique;

    private String riskSolutionsTechnique;
    /**
     * 阶段list
     */
    private List<ProjectMarketStageTable> projectMarketStageTable;
    /**
     * 硬件信息表
     */
    private List<ProjectMarkTypeInfoTable> projectMarkTypeInfoTable;
    /**
     * 技术负责人名字
     */
    private String techniquePeopleName;
    /**
     * 用户对象
     */
    private SysUser chargePeopleUsers;
    /**
     * techniqueUsers
     */
    private SysUser techniqueUsers;
    /**
     * 项目完成的说明：
     */
    @ApiModelProperty("项目完成备注")
    private String finishMemo;
    /**
     * 发起人名字
     */
    private String createByName;

    private SysCompetitor sysCompetitor;

    private String competeJson;

    private Integer agree;

    private Integer isFinish;
    /**
     * 当前审批流
     */
    private Integer projectCurrentId;
    /**
     * 当前审批人
     */
    private String userIds;

    private List<ProjectPlanTable> projectPlanTableList;

    private String ccIds;

    private Integer currentId;

    private Integer parameter = 0;

    private Integer parameter2 = 0;

    private static Integer yes = 0;

    /**
     * 表示审批到哪一级
     */
    private Integer approvalStage;
    /**
     * 0有权限，1是没有权限
     */
    private Integer isFirstAuthority;

    private Integer isSecondAuthority;
    /**
     * 是否为暂存项目(0:不暂存,1:暂存)
     */
    private Integer projectCache = 0;

    private String projectManager;
    /**
     * 垫资描述
     */
    private String advancedfundsDescribe;
    /**
     * 预计建设周期
     */
    private Integer estimatedBuildperiod;

    private String currentApprovelUserIds;

    private String keyword;

    private List<String> peopleList;

    @ApiModelProperty("立项列表判定是否显示删除按钮：true:显示，false:不显示")
    private Boolean deletable;

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    public Integer getProducetypeid() {
        return producetypeid;
    }

    public void setProducetypeid(Integer producetypeid) {
        this.producetypeid = producetypeid;
    }

    public String getStoptype() {
        return stoptype;
    }

    public String getStopcause() {
        return stopcause;
    }

    public void setStoptype(String stoptype) {
        this.stoptype = stoptype;
    }

    public void setStopcause(String stopcause) {
        this.stopcause = stopcause;
    }

    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }

    public Date getChecktime() {
        return checktime;
    }

    public List<String> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(List<String> peopleList) {
        this.peopleList = peopleList;
    }

    public String getAdvancedfundsDescribe() {
        return advancedfundsDescribe;
    }

    public void setAdvancedfundsDescribe(String advancedfundsDescribe) {
        this.advancedfundsDescribe = advancedfundsDescribe;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public Integer getEstimatedBuildperiod() {
        return estimatedBuildperiod;
    }

    public void setEstimatedBuildperiod(Integer estimatedBuildperiod) {
        this.estimatedBuildperiod = estimatedBuildperiod;
    }

    public String getCurrentApprovelUserIds() {
        return currentApprovelUserIds;
    }

    public void setCurrentApprovelUserIds(String currentApprovelUserIds) {
        this.currentApprovelUserIds = currentApprovelUserIds;
    }

    public Integer getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(Integer inOrOut) {
        this.inOrOut = inOrOut;
    }


    public Integer getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Integer isFinish) {
        this.isFinish = isFinish;
    }

    public Integer getProjectCache() {
        return projectCache;
    }

    public void setProjectCache(Integer projectCache) {
        this.projectCache = projectCache;
    }

    public Integer getIsFirstAuthority() {
        return isFirstAuthority;
    }

    public void setIsFirstAuthority(Integer isFirstAuthority) {
        this.isFirstAuthority = isFirstAuthority;
    }

    public Integer getIsSecondAuthority() {
        return isSecondAuthority;
    }

    public void setIsSecondAuthority(Integer isSecondAuthority) {
        this.isSecondAuthority = isSecondAuthority;
    }

    public Integer getApprovalStage() {
        return approvalStage;
    }

    public void setApprovalStage(Integer approvalStage) {
        this.approvalStage = approvalStage;
    }

    public Integer getParameter() {
        return parameter;
    }

    public void setParameter(Integer parameter) {
        this.parameter = parameter;
    }

    public Integer getParameter2() {
        return parameter2;
    }

    public void setParameter2(Integer parameter2) {
        this.parameter2 = parameter2;
    }

    public static Integer getYes() {
        return yes;
    }

    public static void setYes(Integer yes) {
        SysProjectTable.yes = yes;
    }

    public String getBusinessParticipants() {
        return businessParticipants;
    }

    public void setBusinessParticipants(String businessParticipants) {
        this.businessParticipants = businessParticipants;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public String getCcIds() {
        return ccIds;
    }

    public void setCcIds(String ccIds) {
        this.ccIds = ccIds;
    }

    public List<ProjectPlanTable> getProjectPlanTableList() {
        return projectPlanTableList;
    }

    public void setProjectPlanTableList(List<ProjectPlanTable> projectPlanTableList) {
        this.projectPlanTableList = projectPlanTableList;
    }

    public SysProjectTable() {

    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public List<SysFileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<SysFileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribeProject() {
        return describeProject;
    }

    public void setDescribeProject(String describeProject) {
        this.describeProject = describeProject;
    }

    public Integer getChargepeopleId() {
        return chargepeopleId;
    }

    public void setChargepeopleId(Integer chargepeopleId) {
        this.chargepeopleId = chargepeopleId;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getParticipantsName() {
        return participantsName;
    }

    public void setParticipantsName(String participantsName) {
        this.participantsName = participantsName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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

    public Integer getPlanRate() {
        return planRate;
    }

    public void setPlanRate(Integer planRate) {
        this.planRate = planRate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getQueryInfo() {
        return queryInfo;
    }

    public void setQueryInfo(String queryInfo) {
        this.queryInfo = queryInfo;
    }

    public Date getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getEstablishStatus() {
        return establishStatus;
    }

    public void setEstablishStatus(String establishStatus) {
        this.establishStatus = establishStatus;
    }

    public String getProjectFinishFlag() {
        return projectFinishFlag;
    }

    public void setProjectFinishFlag(String projectFinishFlag) {
        this.projectFinishFlag = projectFinishFlag;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getChargePeopleName() {
        return chargePeopleName;
    }

    public void setChargePeopleName(String chargePeopleName) {
        this.chargePeopleName = chargePeopleName;
    }

    public String getApprovalCommitUserName() {
        return approvalCommitUserName;
    }

    public void setApprovalCommitUserName(String approvalCommitUserName) {
        this.approvalCommitUserName = approvalCommitUserName;
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
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

    public String getThirdUserId() {
        return thirdUserId;
    }

    public void setThirdUserId(String thirdUserId) {
        this.thirdUserId = thirdUserId;
    }

    public String getFourthUserId() {
        return fourthUserId;
    }

    public void setFourthUserId(String fourthUserId) {
        this.fourthUserId = fourthUserId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPanticiantName() {
        return panticiantName;
    }

    public void setPanticiantName(String panticiantName) {
        this.panticiantName = panticiantName;
    }

    public String getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(String participantsId) {
        this.participantsId = participantsId;
    }

    public List<ParticipantsTable> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(List<ParticipantsTable> participantsList) {
        this.participantsList = participantsList;
    }

    public List<ResultApprovalRecord> getResultApprovalRecordList() {
        return resultApprovalRecordList;
    }

    public void setResultApprovalRecordList(List<ResultApprovalRecord> resultApprovalRecordList) {
        this.resultApprovalRecordList = resultApprovalRecordList;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public Integer getUserAttention() {
        return userAttention;
    }

    public void setUserAttention(Integer userAttention) {
        this.userAttention = userAttention;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public Integer getCustomerIsEstablish() {
        return customerIsEstablish;
    }

    public void setCustomerIsEstablish(Integer customerIsEstablish) {
        this.customerIsEstablish = customerIsEstablish;
    }

    public String getBuildPeriod() {
        return buildPeriod;
    }

    public void setBuildPeriod(String buildPeriod) {
        this.buildPeriod = buildPeriod;
    }

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public Integer getCustomerIsBudget() {
        return customerIsBudget;
    }

    public void setCustomerIsBudget(Integer customerIsBudget) {
        this.customerIsBudget = customerIsBudget;
    }

    public Integer getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Integer budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public Date getBudgetTime() {
        return budgetTime;
    }

    public void setBudgetTime(Date budgetTime) {
        this.budgetTime = budgetTime;
    }

    public Integer getBudgettimeAndAmcount() {
        return budgettimeAndAmcount;
    }

    public void setBudgettimeAndAmcount(Integer budgettimeAndAmcount) {
        this.budgettimeAndAmcount = budgettimeAndAmcount;
    }

    public Integer getTechniquePeople() {
        return techniquePeople;
    }

    public void setTechniquePeople(Integer techniquePeople) {
        this.techniquePeople = techniquePeople;
    }

    public String getUpdateFilePath() {
        return updateFilePath;
    }

    public void setUpdateFilePath(String updateFilePath) {
        this.updateFilePath = updateFilePath;
    }

    public Integer getClientHighdirectorJob() {
        return clientHighdirectorJob;
    }

    public void setClientHighdirectorJob(Integer clientHighdirectorJob) {
        this.clientHighdirectorJob = clientHighdirectorJob;
    }

    public Integer getClientHighDupdirectorJob() {
        return clientHighDupdirectorJob;
    }

    public void setClientHighDupdirectorJob(Integer clientHighDupdirectorJob) {
        this.clientHighDupdirectorJob = clientHighDupdirectorJob;
    }

    public Integer getClientMidbranchleaderJob() {
        return clientMidbranchleaderJob;
    }

    public void setClientMidbranchleaderJob(Integer clientMidbranchleaderJob) {
        this.clientMidbranchleaderJob = clientMidbranchleaderJob;
    }

    public Integer getClientMidDupbranchleaderJob() {
        return clientMidDupbranchleaderJob;
    }

    public void setClientMidDupbranchleaderJob(Integer clientMidDupbranchleaderJob) {
        this.clientMidDupbranchleaderJob = clientMidDupbranchleaderJob;
    }

    public Integer getClientMidcommissarjob() {
        return clientMidcommissarjob;
    }

    public void setClientMidcommissarjob(Integer clientMidcommissarjob) {
        this.clientMidcommissarjob = clientMidcommissarjob;
    }

    public Integer getClientHighdirectorAuthority() {
        return clientHighdirectorAuthority;
    }

    public void setClientHighdirectorAuthority(Integer clientHighdirectorAuthority) {
        this.clientHighdirectorAuthority = clientHighdirectorAuthority;
    }

    public Integer getClientHighDupdirectorAuthority() {
        return clientHighDupdirectorAuthority;
    }

    public void setClientHighDupdirectorAuthority(Integer clientHighDupdirectorAuthority) {
        this.clientHighDupdirectorAuthority = clientHighDupdirectorAuthority;
    }

    public Integer getClientMidbranchleaderAuthority() {
        return clientMidbranchleaderAuthority;
    }

    public void setClientMidbranchleaderAuthority(Integer clientMidbranchleaderAuthority) {
        this.clientMidbranchleaderAuthority = clientMidbranchleaderAuthority;
    }

    public Integer getClientMidDupbranchleaderAuthority() {
        return clientMidDupbranchleaderAuthority;
    }

    public void setClientMidDupbranchleaderAuthority(Integer clientMidDupbranchleaderAuthority) {
        this.clientMidDupbranchleaderAuthority = clientMidDupbranchleaderAuthority;
    }

    public Integer getClientMidcommissarAnthority() {
        return clientMidcommissarAnthority;
    }

    public void setClientMidcommissarAnthority(Integer clientMidcommissarAnthority) {
        this.clientMidcommissarAnthority = clientMidcommissarAnthority;
    }

    public Integer getClientHighdirectorReporting() {
        return clientHighdirectorReporting;
    }

    public void setClientHighdirectorReporting(Integer clientHighdirectorReporting) {
        this.clientHighdirectorReporting = clientHighdirectorReporting;
    }

    public Integer getClientHighDupdirectorReporting() {
        return clientHighDupdirectorReporting;
    }

    public void setClientHighDupdirectorReporting(Integer clientHighDupdirectorReporting) {
        this.clientHighDupdirectorReporting = clientHighDupdirectorReporting;
    }

    public Integer getClientMidbranchleaderReporting() {
        return clientMidbranchleaderReporting;
    }

    public void setClientMidbranchleaderReporting(Integer clientMidbranchleaderReporting) {
        this.clientMidbranchleaderReporting = clientMidbranchleaderReporting;
    }

    public Integer getClientMidDupbranchleaderReporting() {
        return clientMidDupbranchleaderReporting;
    }

    public void setClientMidDupbranchleaderReporting(Integer clientMidDupbranchleaderReporting) {
        this.clientMidDupbranchleaderReporting = clientMidDupbranchleaderReporting;
    }

    public Integer getClientMidcommissarReporting() {
        return clientMidcommissarReporting;
    }

    public void setClientMidcommissarReporting(Integer clientMidcommissarReporting) {
        this.clientMidcommissarReporting = clientMidcommissarReporting;
    }

    public Integer getClientHighdirectorMeet() {
        return clientHighdirectorMeet;
    }

    public void setClientHighdirectorMeet(Integer clientHighdirectorMeet) {
        this.clientHighdirectorMeet = clientHighdirectorMeet;
    }

    public Integer getClientHighDupdirectorMeet() {
        return clientHighDupdirectorMeet;
    }

    public void setClientHighDupdirectorMeet(Integer clientHighDupdirectorMeet) {
        this.clientHighDupdirectorMeet = clientHighDupdirectorMeet;
    }

    public Integer getClientMidbranchleaderMeet() {
        return clientMidbranchleaderMeet;
    }

    public void setClientMidbranchleaderMeet(Integer clientMidbranchleaderMeet) {
        this.clientMidbranchleaderMeet = clientMidbranchleaderMeet;
    }

    public Integer getClientMidDupbranchleaderMeet() {
        return clientMidDupbranchleaderMeet;
    }

    public void setClientMidDupbranchleaderMeet(Integer clientMidDupbranchleaderMeet) {
        this.clientMidDupbranchleaderMeet = clientMidDupbranchleaderMeet;
    }

    public Integer getClientMidcommissarMeet() {
        return clientMidcommissarMeet;
    }

    public void setClientMidcommissarMeet(Integer clientMidcommissarMeet) {
        this.clientMidcommissarMeet = clientMidcommissarMeet;
    }

    public Integer getClientHighdirectorTransmit() {
        return clientHighdirectorTransmit;
    }

    public void setClientHighdirectorTransmit(Integer clientHighdirectorTransmit) {
        this.clientHighdirectorTransmit = clientHighdirectorTransmit;
    }

    public Integer getClientHighDupdirectorTransmit() {
        return clientHighDupdirectorTransmit;
    }

    public void setClientHighDupdirectorTransmit(Integer clientHighDupdirectorTransmit) {
        this.clientHighDupdirectorTransmit = clientHighDupdirectorTransmit;
    }

    public Integer getClientMidbranchleaderTransmit() {
        return clientMidbranchleaderTransmit;
    }

    public void setClientMidbranchleaderTransmit(Integer clientMidbranchleaderTransmit) {
        this.clientMidbranchleaderTransmit = clientMidbranchleaderTransmit;
    }

    public Integer getClientMidDupbranchleaderTransmit() {
        return clientMidDupbranchleaderTransmit;
    }

    public void setClientMidDupbranchleaderTransmit(Integer clientMidDupbranchleaderTransmit) {
        this.clientMidDupbranchleaderTransmit = clientMidDupbranchleaderTransmit;
    }

    public Integer getClientMidcommissarTransmit() {
        return clientMidcommissarTransmit;
    }

    public void setClientMidcommissarTransmit(Integer clientMidcommissarTransmit) {
        this.clientMidcommissarTransmit = clientMidcommissarTransmit;
    }

    public Integer getClientHighdirectorWill() {
        return clientHighdirectorWill;
    }

    public void setClientHighdirectorWill(Integer clientHighdirectorWill) {
        this.clientHighdirectorWill = clientHighdirectorWill;
    }

    public Integer getClientHighDupdirectorWill() {
        return clientHighDupdirectorWill;
    }

    public void setClientHighDupdirectorWill(Integer clientHighDupdirectorWill) {
        this.clientHighDupdirectorWill = clientHighDupdirectorWill;
    }

    public Integer getClientMidbranchleaderWill() {
        return clientMidbranchleaderWill;
    }

    public void setClientMidbranchleaderWill(Integer clientMidbranchleaderWill) {
        this.clientMidbranchleaderWill = clientMidbranchleaderWill;
    }

    public Integer getClientMidDupbranchleaderWill() {
        return clientMidDupbranchleaderWill;
    }

    public void setClientMidDupbranchleaderWill(Integer clientMidDupbranchleaderWill) {
        this.clientMidDupbranchleaderWill = clientMidDupbranchleaderWill;
    }

    public Integer getClientMidcommissarWill() {
        return clientMidcommissarWill;
    }

    public void setClientMidcommissarWill(Integer clientMidcommissarWill) {
        this.clientMidcommissarWill = clientMidcommissarWill;
    }

    public Integer getClientLowinfluence() {
        return clientLowinfluence;
    }

    public void setClientLowinfluence(Integer clientLowinfluence) {
        this.clientLowinfluence = clientLowinfluence;
    }

    public String getNeedBackground() {
        return needBackground;
    }

    public void setNeedBackground(String needBackground) {
        this.needBackground = needBackground;
    }

    public String getNeedApplication() {
        return needApplication;
    }

    public void setNeedApplication(String needApplication) {
        this.needApplication = needApplication;
    }

    public Integer getNeedUnderstand() {
        return needUnderstand;
    }

    public void setNeedUnderstand(Integer needUnderstand) {
        this.needUnderstand = needUnderstand;
    }

    public Integer getNeedFeasible() {
        return needFeasible;
    }

    public void setNeedFeasible(Integer needFeasible) {
        this.needFeasible = needFeasible;
    }

    public Date getNeedEndTime() {
        return needEndTime;
    }

    public void setNeedEndTime(Date needEndTime) {
        this.needEndTime = needEndTime;
    }

    public Integer getIsDianzi() {
        return isDianzi;
    }

    public void setIsDianzi(Integer isDianzi) {
        this.isDianzi = isDianzi;
    }

    public Integer getDianziPeriod() {
        return dianziPeriod;
    }

    public void setDianziPeriod(Integer dianziPeriod) {
        this.dianziPeriod = dianziPeriod;
    }

    public Integer getDianziMoney() {
        return dianziMoney;
    }

    public void setDianziMoney(Integer dianziMoney) {
        this.dianziMoney = dianziMoney;
    }

    public Integer getIsShiyong() {
        return isShiyong;
    }

    public void setIsShiyong(Integer isShiyong) {
        this.isShiyong = isShiyong;
    }

    public Integer getShiyongPeriod() {
        return shiyongPeriod;
    }

    public void setShiyongPeriod(Integer shiyongPeriod) {
        this.shiyongPeriod = shiyongPeriod;
    }

    public String getRiskDescribeBusiness() {
        return riskDescribeBusiness;
    }

    public void setRiskDescribeBusiness(String riskDescribeBusiness) {
        this.riskDescribeBusiness = riskDescribeBusiness;
    }

    public String getRiskSolutionsBusiness() {
        return riskSolutionsBusiness;
    }

    public void setRiskSolutionsBusiness(String riskSolutionsBusiness) {
        this.riskSolutionsBusiness = riskSolutionsBusiness;
    }

    public String getRiskDescribeTechnique() {
        return riskDescribeTechnique;
    }

    public void setRiskDescribeTechnique(String riskDescribeTechnique) {
        this.riskDescribeTechnique = riskDescribeTechnique;
    }

    public String getRiskSolutionsTechnique() {
        return riskSolutionsTechnique;
    }

    public void setRiskSolutionsTechnique(String riskSolutionsTechnique) {
        this.riskSolutionsTechnique = riskSolutionsTechnique;
    }

    public List<ProjectMarketStageTable> getProjectMarketStageTable() {
        return projectMarketStageTable;
    }

    public void setProjectMarketStageTable(List<ProjectMarketStageTable> projectMarketStageTable) {
        this.projectMarketStageTable = projectMarketStageTable;
    }

    public List<ProjectMarkTypeInfoTable> getProjectMarkTypeInfoTable() {
        return projectMarkTypeInfoTable;
    }

    public void setProjectMarkTypeInfoTable(List<ProjectMarkTypeInfoTable> projectMarkTypeInfoTable) {
        this.projectMarkTypeInfoTable = projectMarkTypeInfoTable;
    }

    public String getTechniquePeopleName() {
        return techniquePeopleName;
    }

    public void setTechniquePeopleName(String techniquePeopleName) {
        this.techniquePeopleName = techniquePeopleName;
    }

    public SysUser getChargePeopleUsers() {
        return chargePeopleUsers;
    }

    public void setChargePeopleUsers(SysUser chargePeopleUsers) {
        this.chargePeopleUsers = chargePeopleUsers;
    }

    public SysUser getTechniqueUsers() {
        return techniqueUsers;
    }

    public void setTechniqueUsers(SysUser techniqueUsers) {
        this.techniqueUsers = techniqueUsers;
    }

    public String getFinishMemo() {
        return finishMemo;
    }

    public void setFinishMemo(String finishMemo) {
        this.finishMemo = finishMemo;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public SysCompetitor getSysCompetitor() {
        return sysCompetitor;
    }

    public void setSysCompetitor(SysCompetitor sysCompetitor) {
        this.sysCompetitor = sysCompetitor;
    }

    public String getCompeteJson() {
        return competeJson;
    }

    public void setCompeteJson(String competeJson) {
        this.competeJson = competeJson;
    }

    public Integer getAgree() {
        return agree;
    }

    public void setAgree(Integer agree) {
        this.agree = agree;
    }

    public Integer getProjectCurrentId() {
        return projectCurrentId;
    }

    public void setProjectCurrentId(Integer projectCurrentId) {
        this.projectCurrentId = projectCurrentId;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(String startTimes) {
        this.startTimes = startTimes;
    }

    public String getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(String endTimes) {
        this.endTimes = endTimes;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SysProjectTable(Integer pId, List<SysFileInfo> fileInfoList, String title, String describeProject, Integer chargepeopleId, String participants, String participantsName, String period, Date startTime, Date endTime, Integer planRate, String memo, Date establishTime, Integer departmentId, String establishStatus, String projectFinishFlag, String createBy, Date createTime, String updateBy, Date updateTime, String departmentName, String chargePeopleName, String approvalCommitUserName, Integer flowId, String firstUserId, String secondUserId, String thirdUserId, String fourthUserId, Integer userId, String panticiantName, String participantsId, List<ParticipantsTable> participantsList, List<ResultApprovalRecord> resultApprovalRecordList, String attention, Integer userAttention, Integer projectType, Integer customerIsEstablish, String buildPeriod, Date expectTime, Integer customerIsBudget, Integer budgetAmount, Date budgetTime, Integer budgettimeAndAmcount, Integer techniquePeople, String updateFilePath, Integer clientHighdirectorJob, Integer clientHighDupdirectorJob, Integer clientMidbranchleaderJob, Integer clientMidDupbranchleaderJob, Integer clientMidcommissarjob, Integer clientHighdirectorAuthority, Integer clientHighDupdirectorAuthority, Integer clientMidbranchleaderAuthority, Integer clientMidDupbranchleaderAuthority, Integer clientMidcommissarAnthority, Integer clientHighdirectorReporting, Integer clientHighDupdirectorReporting, Integer clientMidbranchleaderReporting, Integer clientMidDupbranchleaderReporting, Integer clientMidcommissarReporting, Integer clientHighdirectorMeet, Integer clientHighDupdirectorMeet, Integer clientMidbranchleaderMeet, Integer clientMidDupbranchleaderMeet, Integer clientMidcommissarMeet, Integer clientHighdirectorTransmit, Integer clientHighDupdirectorTransmit, Integer clientMidbranchleaderTransmit, Integer clientMidDupbranchleaderTransmit, Integer clientMidcommissarTransmit, Integer clientHighdirectorWill, Integer clientHighDupdirectorWill, Integer clientMidbranchleaderWill, Integer clientMidDupbranchleaderWill, Integer clientMidcommissarWill, Integer clientLowinfluence, String needBackground, String needApplication, Integer needUnderstand, Integer needFeasible, Date needEndTime, Integer isDianzi, Integer dianziPeriod, Integer dianziMoney, Integer isShiyong, Integer shiyongPeriod, String riskDescribeBusiness, String riskSolutionsBusiness, String riskDescribeTechnique, String riskSolutionsTechnique, List<ProjectMarketStageTable> projectMarketStageTable, List<ProjectMarkTypeInfoTable> projectMarkTypeInfoTable, String techniquePeopleName, SysUser chargePeopleUsers, SysUser techniqueUsers, String finishMemo, String createByName, SysCompetitor sysCompetitor, String competeJson, Integer agree, Integer projectCurrentId, String userIds) {
        this.pId = pId;
        this.fileInfoList = fileInfoList;
        this.title = title;
        this.describeProject = describeProject;
        this.chargepeopleId = chargepeopleId;
        this.participants = participants;
        this.participantsName = participantsName;
        this.period = period;
        this.startTime = startTime;
        this.endTime = endTime;
        this.planRate = planRate;
        this.memo = memo;
        this.establishTime = establishTime;
        this.departmentId = departmentId;
        this.establishStatus = establishStatus;
        this.projectFinishFlag = projectFinishFlag;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
        this.departmentName = departmentName;
        this.chargePeopleName = chargePeopleName;
        this.approvalCommitUserName = approvalCommitUserName;
        this.flowId = flowId;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.thirdUserId = thirdUserId;
        this.fourthUserId = fourthUserId;
        this.userId = userId;
        this.panticiantName = panticiantName;
        this.participantsId = participantsId;
        this.participantsList = participantsList;
        this.resultApprovalRecordList = resultApprovalRecordList;
        this.attention = attention;
        this.userAttention = userAttention;
        this.projectType = projectType;
        this.customerIsEstablish = customerIsEstablish;
        this.buildPeriod = buildPeriod;
        this.expectTime = expectTime;
        this.customerIsBudget = customerIsBudget;
        this.budgetAmount = budgetAmount;
        this.budgetTime = budgetTime;
        this.budgettimeAndAmcount = budgettimeAndAmcount;
        this.techniquePeople = techniquePeople;
        this.updateFilePath = updateFilePath;
        this.clientHighdirectorJob = clientHighdirectorJob;
        this.clientHighDupdirectorJob = clientHighDupdirectorJob;
        this.clientMidbranchleaderJob = clientMidbranchleaderJob;
        this.clientMidDupbranchleaderJob = clientMidDupbranchleaderJob;
        this.clientMidcommissarjob = clientMidcommissarjob;
        this.clientHighdirectorAuthority = clientHighdirectorAuthority;
        this.clientHighDupdirectorAuthority = clientHighDupdirectorAuthority;
        this.clientMidbranchleaderAuthority = clientMidbranchleaderAuthority;
        this.clientMidDupbranchleaderAuthority = clientMidDupbranchleaderAuthority;
        this.clientMidcommissarAnthority = clientMidcommissarAnthority;
        this.clientHighdirectorReporting = clientHighdirectorReporting;
        this.clientHighDupdirectorReporting = clientHighDupdirectorReporting;
        this.clientMidbranchleaderReporting = clientMidbranchleaderReporting;
        this.clientMidDupbranchleaderReporting = clientMidDupbranchleaderReporting;
        this.clientMidcommissarReporting = clientMidcommissarReporting;
        this.clientHighdirectorMeet = clientHighdirectorMeet;
        this.clientHighDupdirectorMeet = clientHighDupdirectorMeet;
        this.clientMidbranchleaderMeet = clientMidbranchleaderMeet;
        this.clientMidDupbranchleaderMeet = clientMidDupbranchleaderMeet;
        this.clientMidcommissarMeet = clientMidcommissarMeet;
        this.clientHighdirectorTransmit = clientHighdirectorTransmit;
        this.clientHighDupdirectorTransmit = clientHighDupdirectorTransmit;
        this.clientMidbranchleaderTransmit = clientMidbranchleaderTransmit;
        this.clientMidDupbranchleaderTransmit = clientMidDupbranchleaderTransmit;
        this.clientMidcommissarTransmit = clientMidcommissarTransmit;
        this.clientHighdirectorWill = clientHighdirectorWill;
        this.clientHighDupdirectorWill = clientHighDupdirectorWill;
        this.clientMidbranchleaderWill = clientMidbranchleaderWill;
        this.clientMidDupbranchleaderWill = clientMidDupbranchleaderWill;
        this.clientMidcommissarWill = clientMidcommissarWill;
        this.clientLowinfluence = clientLowinfluence;
        this.needBackground = needBackground;
        this.needApplication = needApplication;
        this.needUnderstand = needUnderstand;
        this.needFeasible = needFeasible;
        this.needEndTime = needEndTime;
        this.isDianzi = isDianzi;
        this.dianziPeriod = dianziPeriod;
        this.dianziMoney = dianziMoney;
        this.isShiyong = isShiyong;
        this.shiyongPeriod = shiyongPeriod;
        this.riskDescribeBusiness = riskDescribeBusiness;
        this.riskSolutionsBusiness = riskSolutionsBusiness;
        this.riskDescribeTechnique = riskDescribeTechnique;
        this.riskSolutionsTechnique = riskSolutionsTechnique;
        this.projectMarketStageTable = projectMarketStageTable;
        this.projectMarkTypeInfoTable = projectMarkTypeInfoTable;
        this.techniquePeopleName = techniquePeopleName;
        this.chargePeopleUsers = chargePeopleUsers;
        this.techniqueUsers = techniqueUsers;
        this.finishMemo = finishMemo;
        this.createByName = createByName;
        this.sysCompetitor = sysCompetitor;
        this.competeJson = competeJson;
        this.agree = agree;
        this.projectCurrentId = projectCurrentId;
        this.userIds = userIds;
    }
}
