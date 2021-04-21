package com.ruoyi.web.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.SysUser;

import java.io.Serializable;
import java.util.List;

/**
 * 参与人表，可以存储参与人id
 *
 * @author zhongzhilong
 * @date 2019/12/4
 */
public class ParticipantsTable extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2801520045637671750L;
    /**
     * 主键id
     */
    private int id;
    /**
     * 参与人id
     */
    private long userId;
    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * user_name用户名
     */
    private String userName;

    private SysUser sysUser;

    private SysProjectTable projectTable;
    /**
     * 参与人列表
     */
    private List<ParticipantsTable> participantsList;
    /**
     * 返回一个审批记录列表
     */
    private List<ResultApprovalRecord> resultApprovalRecordList;

    private Integer chargepeopleId;

    /**
     * 项目的参与人字符串，以逗号隔开
     */
    private String peoples;

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SysProjectTable getProjectTable() {
        return projectTable;
    }

    public void setProjectTable(SysProjectTable projectTable) {
        this.projectTable = projectTable;
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

    public Integer getChargepeopleId() {
        return chargepeopleId;
    }

    public void setChargepeopleId(Integer chargepeopleId) {
        this.chargepeopleId = chargepeopleId;
    }

    public String getPeoples() {
        return peoples;
    }

    public void setPeoples(String peoples) {
        this.peoples = peoples;
    }
}
