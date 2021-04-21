package com.ruoyi.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@ApiModel
public class SysProjectTableFail extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 40234470242806408L;
    private int id;
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
    /**
     * 项目详细描述
     */
    @ApiModelProperty("项目描述或者项目的负责人")
    private String describeProject;
    /**
     * 项目负责人id
     */
    @ApiModelProperty("商务负责人")
    private int chargepeopleId;
    /**
     * 项目参与人id
     */

    private String participants;
    /**
     * 参与人名字
     */
    private String participantsName;
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
    /**
     * 进度,由double类型改为int 类型
     */
    @ApiModelProperty("项目进度")
    private int planRate;
    /**
     * 项目备注
     */
    @ApiModelProperty("项目备注")
    private String memo;
    /**
     * 项目提交时间
     */
    private Date establishTime;
    /**
     * 部门ID
     */
    private int departmentId;

    /**
     * 立项成功标识
     */
    private String establishStatus;
    /**
     * 项目完成标识
     */
    private String projectFinishFlag;
    //创建者
    private String createBy;
    //创建时间
    private Date createTime;
    //更新人
    private String updateBy;
    //更新时间
    private Date updateTime;
    //用来存取部门名称
    private String departmentName;
    //用来存取负责人名字
    private String chargePeopleName;
    /*项目审批创建人**/
    private String approvalCommitUserName;
    private Integer flowId;
    // 一级
    private String firstUserId;
    // 二级
    private String secondUserId;
    // 三级
    private String thirdUserId;
    // 四级审批人
    private String fourthUserId;
    // 用户id
    private Integer userId;
    // 存储项目项目参与人名字
    private String panticiantName;
    // 存储项目项目参与人id
    private String participantsId;
    // 返回一个项目参与人的list
    private List<ParticipantsTable> participantsList;
    // 返回一个审批记录列表
    private List<ResultApprovalRecord> resultApprovalRecordList;
    // 关注项目的用户
    private String attention;
    // 用户关注的项目状态
    private Integer userAttention;
    // 以下为涉及市场项目的数据库字段
    // 项目类型
    private int projectType;

    private int customerIsEstablish;

    private String buildPeriod;

    private Date expectTime;

    private int customerIsBudget;

    private int  budgetAmount;

    private Date budgetTime;

    private int  budgettimeAndAmcount;

    private int techniquePeople;

    private String updateFilePath;

    private int clientHighdirectorJob;

    private int clientHighDupdirectorJob;

    private int clientMidbranchleaderJob;

    private int clientMidDupbranchleaderJob;

    private int clientMidcommissarjob;

    private int clientHighdirectorAuthority;

    private int clientHighDupdirectorAuthority;

    private int clientMidbranchleaderAuthority;

    private int clientMidDupbranchleaderAuthority;

    private int clientMidcommissarAnthority;

    private int clientHighdirectorReporting;

    private int clientHighDupdirectorReporting;

    private int clientMidbranchleaderReporting;

    private int clientMidDupbranchleaderReporting;

    private int clientMidcommissarReporting;

    private int clientHighdirectorMeet;

    private int clientHighDupdirectorMeet;

    private int clientMidbranchleaderMeet;

    private int clientMidDupbranchleaderMeet;

    private int clientMidcommissarMeet;

    private int clientHighdirectorTransmit;

    private int clientHighDupdirectorTransmit;

    private int clientMidbranchleaderTransmit;

    private int clientMidDupbranchleaderTransmit;

    private int clientMidcommissarTransmit;

    private int clientHighdirectorWill;

    private int clientHighDupdirectorWill;

    private int clientMidbranchleaderWill;

    private int clientMidDupbranchleaderWill;

    private int clientMidcommissarWill;

    private int clientLowinfluence;

    private String needBackground;

    private String needApplication;

    private int needUnderstand;

    private int needFeasible;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date needEndTime;
    //是否垫资
    private int isDianzi;
    //周期
    private int dianziPeriod;
    //垫资金额
    private int dianziMoney;
    //是否试用
    private int isShiyong;

    private Integer shiyongPeriod;

    private String riskDescribeBusiness;

    private String riskSolutionsBusiness;

    private String riskDescribeTechnique;

    private String riskSolutionsTechnique;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getChargepeopleId() {
        return chargepeopleId;
    }

    public void setChargepeopleId(int chargepeopleId) {
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

    public int getPlanRate() {
        return planRate;
    }

    public void setPlanRate(int planRate) {
        this.planRate = planRate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
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

    public int getProjectType() {
        return projectType;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    public int getCustomerIsEstablish() {
        return customerIsEstablish;
    }

    public void setCustomerIsEstablish(int customerIsEstablish) {
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

    public int getCustomerIsBudget() {
        return customerIsBudget;
    }

    public void setCustomerIsBudget(int customerIsBudget) {
        this.customerIsBudget = customerIsBudget;
    }

    public int getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(int budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public Date getBudgetTime() {
        return budgetTime;
    }

    public void setBudgetTime(Date budgetTime) {
        this.budgetTime = budgetTime;
    }

    public int getBudgettimeAndAmcount() {
        return budgettimeAndAmcount;
    }

    public void setBudgettimeAndAmcount(int budgettimeAndAmcount) {
        this.budgettimeAndAmcount = budgettimeAndAmcount;
    }

    public int getTechniquePeople() {
        return techniquePeople;
    }

    public void setTechniquePeople(int techniquePeople) {
        this.techniquePeople = techniquePeople;
    }

    public String getUpdateFilePath() {
        return updateFilePath;
    }

    public void setUpdateFilePath(String updateFilePath) {
        this.updateFilePath = updateFilePath;
    }

    public int getClientHighdirectorJob() {
        return clientHighdirectorJob;
    }

    public void setClientHighdirectorJob(int clientHighdirectorJob) {
        this.clientHighdirectorJob = clientHighdirectorJob;
    }

    public int getClientHighDupdirectorJob() {
        return clientHighDupdirectorJob;
    }

    public void setClientHighDupdirectorJob(int clientHighDupdirectorJob) {
        this.clientHighDupdirectorJob = clientHighDupdirectorJob;
    }

    public int getClientMidbranchleaderJob() {
        return clientMidbranchleaderJob;
    }

    public void setClientMidbranchleaderJob(int clientMidbranchleaderJob) {
        this.clientMidbranchleaderJob = clientMidbranchleaderJob;
    }

    public int getClientMidDupbranchleaderJob() {
        return clientMidDupbranchleaderJob;
    }

    public void setClientMidDupbranchleaderJob(int clientMidDupbranchleaderJob) {
        this.clientMidDupbranchleaderJob = clientMidDupbranchleaderJob;
    }

    public int getClientMidcommissarjob() {
        return clientMidcommissarjob;
    }

    public void setClientMidcommissarjob(int clientMidcommissarjob) {
        this.clientMidcommissarjob = clientMidcommissarjob;
    }

    public int getClientHighdirectorAuthority() {
        return clientHighdirectorAuthority;
    }

    public void setClientHighdirectorAuthority(int clientHighdirectorAuthority) {
        this.clientHighdirectorAuthority = clientHighdirectorAuthority;
    }

    public int getClientHighDupdirectorAuthority() {
        return clientHighDupdirectorAuthority;
    }

    public void setClientHighDupdirectorAuthority(int clientHighDupdirectorAuthority) {
        this.clientHighDupdirectorAuthority = clientHighDupdirectorAuthority;
    }

    public int getClientMidbranchleaderAuthority() {
        return clientMidbranchleaderAuthority;
    }

    public void setClientMidbranchleaderAuthority(int clientMidbranchleaderAuthority) {
        this.clientMidbranchleaderAuthority = clientMidbranchleaderAuthority;
    }

    public int getClientMidDupbranchleaderAuthority() {
        return clientMidDupbranchleaderAuthority;
    }

    public void setClientMidDupbranchleaderAuthority(int clientMidDupbranchleaderAuthority) {
        this.clientMidDupbranchleaderAuthority = clientMidDupbranchleaderAuthority;
    }

    public int getClientMidcommissarAnthority() {
        return clientMidcommissarAnthority;
    }

    public void setClientMidcommissarAnthority(int clientMidcommissarAnthority) {
        this.clientMidcommissarAnthority = clientMidcommissarAnthority;
    }

    public int getClientHighdirectorReporting() {
        return clientHighdirectorReporting;
    }

    public void setClientHighdirectorReporting(int clientHighdirectorReporting) {
        this.clientHighdirectorReporting = clientHighdirectorReporting;
    }

    public int getClientHighDupdirectorReporting() {
        return clientHighDupdirectorReporting;
    }

    public void setClientHighDupdirectorReporting(int clientHighDupdirectorReporting) {
        this.clientHighDupdirectorReporting = clientHighDupdirectorReporting;
    }

    public int getClientMidbranchleaderReporting() {
        return clientMidbranchleaderReporting;
    }

    public void setClientMidbranchleaderReporting(int clientMidbranchleaderReporting) {
        this.clientMidbranchleaderReporting = clientMidbranchleaderReporting;
    }

    public int getClientMidDupbranchleaderReporting() {
        return clientMidDupbranchleaderReporting;
    }

    public void setClientMidDupbranchleaderReporting(int clientMidDupbranchleaderReporting) {
        this.clientMidDupbranchleaderReporting = clientMidDupbranchleaderReporting;
    }

    public int getClientMidcommissarReporting() {
        return clientMidcommissarReporting;
    }

    public void setClientMidcommissarReporting(int clientMidcommissarReporting) {
        this.clientMidcommissarReporting = clientMidcommissarReporting;
    }

    public int getClientHighdirectorMeet() {
        return clientHighdirectorMeet;
    }

    public void setClientHighdirectorMeet(int clientHighdirectorMeet) {
        this.clientHighdirectorMeet = clientHighdirectorMeet;
    }

    public int getClientHighDupdirectorMeet() {
        return clientHighDupdirectorMeet;
    }

    public void setClientHighDupdirectorMeet(int clientHighDupdirectorMeet) {
        this.clientHighDupdirectorMeet = clientHighDupdirectorMeet;
    }

    public int getClientMidbranchleaderMeet() {
        return clientMidbranchleaderMeet;
    }

    public void setClientMidbranchleaderMeet(int clientMidbranchleaderMeet) {
        this.clientMidbranchleaderMeet = clientMidbranchleaderMeet;
    }

    public int getClientMidDupbranchleaderMeet() {
        return clientMidDupbranchleaderMeet;
    }

    public void setClientMidDupbranchleaderMeet(int clientMidDupbranchleaderMeet) {
        this.clientMidDupbranchleaderMeet = clientMidDupbranchleaderMeet;
    }

    public int getClientMidcommissarMeet() {
        return clientMidcommissarMeet;
    }

    public void setClientMidcommissarMeet(int clientMidcommissarMeet) {
        this.clientMidcommissarMeet = clientMidcommissarMeet;
    }

    public int getClientHighdirectorTransmit() {
        return clientHighdirectorTransmit;
    }

    public void setClientHighdirectorTransmit(int clientHighdirectorTransmit) {
        this.clientHighdirectorTransmit = clientHighdirectorTransmit;
    }

    public int getClientHighDupdirectorTransmit() {
        return clientHighDupdirectorTransmit;
    }

    public void setClientHighDupdirectorTransmit(int clientHighDupdirectorTransmit) {
        this.clientHighDupdirectorTransmit = clientHighDupdirectorTransmit;
    }

    public int getClientMidbranchleaderTransmit() {
        return clientMidbranchleaderTransmit;
    }

    public void setClientMidbranchleaderTransmit(int clientMidbranchleaderTransmit) {
        this.clientMidbranchleaderTransmit = clientMidbranchleaderTransmit;
    }

    public int getClientMidDupbranchleaderTransmit() {
        return clientMidDupbranchleaderTransmit;
    }

    public void setClientMidDupbranchleaderTransmit(int clientMidDupbranchleaderTransmit) {
        this.clientMidDupbranchleaderTransmit = clientMidDupbranchleaderTransmit;
    }

    public int getClientMidcommissarTransmit() {
        return clientMidcommissarTransmit;
    }

    public void setClientMidcommissarTransmit(int clientMidcommissarTransmit) {
        this.clientMidcommissarTransmit = clientMidcommissarTransmit;
    }

    public int getClientHighdirectorWill() {
        return clientHighdirectorWill;
    }

    public void setClientHighdirectorWill(int clientHighdirectorWill) {
        this.clientHighdirectorWill = clientHighdirectorWill;
    }

    public int getClientHighDupdirectorWill() {
        return clientHighDupdirectorWill;
    }

    public void setClientHighDupdirectorWill(int clientHighDupdirectorWill) {
        this.clientHighDupdirectorWill = clientHighDupdirectorWill;
    }

    public int getClientMidbranchleaderWill() {
        return clientMidbranchleaderWill;
    }

    public void setClientMidbranchleaderWill(int clientMidbranchleaderWill) {
        this.clientMidbranchleaderWill = clientMidbranchleaderWill;
    }

    public int getClientMidDupbranchleaderWill() {
        return clientMidDupbranchleaderWill;
    }

    public void setClientMidDupbranchleaderWill(int clientMidDupbranchleaderWill) {
        this.clientMidDupbranchleaderWill = clientMidDupbranchleaderWill;
    }

    public int getClientMidcommissarWill() {
        return clientMidcommissarWill;
    }

    public void setClientMidcommissarWill(int clientMidcommissarWill) {
        this.clientMidcommissarWill = clientMidcommissarWill;
    }

    public int getClientLowinfluence() {
        return clientLowinfluence;
    }

    public void setClientLowinfluence(int clientLowinfluence) {
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

    public int getNeedUnderstand() {
        return needUnderstand;
    }

    public void setNeedUnderstand(int needUnderstand) {
        this.needUnderstand = needUnderstand;
    }

    public int getNeedFeasible() {
        return needFeasible;
    }

    public void setNeedFeasible(int needFeasible) {
        this.needFeasible = needFeasible;
    }

    public Date getNeedEndTime() {
        return needEndTime;
    }

    public void setNeedEndTime(Date needEndTime) {
        this.needEndTime = needEndTime;
    }

    public int getIsDianzi() {
        return isDianzi;
    }

    public void setIsDianzi(int isDianzi) {
        this.isDianzi = isDianzi;
    }

    public int getDianziPeriod() {
        return dianziPeriod;
    }

    public void setDianziPeriod(int dianziPeriod) {
        this.dianziPeriod = dianziPeriod;
    }

    public int getDianziMoney() {
        return dianziMoney;
    }

    public void setDianziMoney(int dianziMoney) {
        this.dianziMoney = dianziMoney;
    }

    public int getIsShiyong() {
        return isShiyong;
    }

    public void setIsShiyong(int isShiyong) {
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
}
