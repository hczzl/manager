package com.ruoyi.web.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import java.io.Serializable;
import java.util.Date;

/**
 * 存储新旧变更信息
 */
public class TaskChange extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6607844406053863443L;

    private long tId;

    private Date startTime;

    private Date endTime;

    private String period;

    private Integer chargeId;

    private String chargeName;

    private String particiants;

    private String particiantsName;

    private String memo;

    private String newMemo;

    private Date newStartTime;

    private Date newEndTime;

    private String newPeriod;

    private Integer newChargeId;

    private String newChargeName;

    private String newParticiants;

    private String newParticiantsName;

    private String newCreateChangeUser;

    private Integer newIsPostpone;

    private String createChangeUser;

    private Integer isPostpone;

    public String getNewMemo() {
        return newMemo;
    }

    public void setNewMemo(String newMemo) {
        this.newMemo = newMemo;
    }

    public String getCreateChangeUser() {
        return createChangeUser;
    }

    public void setCreateChangeUser(String createChangeUser) {
        this.createChangeUser = createChangeUser;
    }

    public void setNewIsPostpone(Integer newIsPostpone) {
        this.newIsPostpone = newIsPostpone;
    }

    public Integer getIsPostpone() {
        return isPostpone;
    }

    public void setIsPostpone(Integer isPostpone) {
        this.isPostpone = isPostpone;
    }

    public String getNewCreateChangeUser() {
        return newCreateChangeUser;
    }

    public void setNewCreateChangeUser(String newCreateChangeUser) {
        this.newCreateChangeUser = newCreateChangeUser;
    }

    public Integer getNewIsPostpone() {
        return newIsPostpone;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getParticiantsName() {
        return particiantsName;
    }

    public void setParticiantsName(String particiantsName) {
        this.particiantsName = particiantsName;
    }

    public String getNewChargeName() {
        return newChargeName;
    }

    public void setNewChargeName(String newChargeName) {
        this.newChargeName = newChargeName;
    }

    public String getNewParticiantsName() {
        return newParticiantsName;
    }

    public void setNewParticiantsName(String newParticiantsName) {
        this.newParticiantsName = newParticiantsName;
    }

    public long gettId() {
        return tId;
    }

    public void settId(long tId) {
        this.tId = tId;
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


    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public String getParticiants() {
        return particiants;
    }

    public void setParticiants(String particiants) {
        this.particiants = particiants;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getNewStartTime() {
        return newStartTime;
    }

    public void setNewStartTime(Date newStartTime) {
        this.newStartTime = newStartTime;
    }

    public Date getNewEndTime() {
        return newEndTime;
    }

    public void setNewEndTime(Date newEndTime) {
        this.newEndTime = newEndTime;
    }


    public Integer getNewChargeId() {
        return newChargeId;
    }

    public void setNewChargeId(Integer newChargeId) {
        this.newChargeId = newChargeId;
    }

    public String getNewParticiants() {
        return newParticiants;
    }

    public void setNewParticiants(String newParticiants) {
        this.newParticiants = newParticiants;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNewPeriod() {
        return newPeriod;
    }

    public void setNewPeriod(String newPeriod) {
        this.newPeriod = newPeriod;
    }
}
