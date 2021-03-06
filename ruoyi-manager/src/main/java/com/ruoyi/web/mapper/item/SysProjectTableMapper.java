package com.ruoyi.web.mapper.item;


import com.ruoyi.web.domain.TaskProject;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.domain.ResultApprovalRecord;
import com.ruoyi.web.domain.vo.AuditFlowCrurrentVo;
import com.ruoyi.web.domain.vo.SysProjectTableTechnologyVo;
import com.ruoyi.web.domain.vo.SysProjectTableVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 项目表 数据层
 *
 * @author zzl
 */
@Component
public interface SysProjectTableMapper {


    int insertMarkProject(SysProjectTable sysProjectTable);

    List<SysProjectTable> getBigId(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectAllInfo(SysProjectTable sysProjectTable);

    int deleteProject(int id);

    List<SysProjectTable> selectProjectByType(String projectType);

    List<SysProjectTable> selectProjectByTitle(String title);

    List<SysProjectTable> selectProjectByStatus(String status);

    List<SysProjectTable> selectAllestablish(SysProjectTable sysProjectTable);

    List<SysProjectTable> getMyProject(int id);

    int getChargeIdById(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectProjectList(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectAllProject(SysProjectTable sysProjectTable);

    List<SysProjectTableVo> selectProjectVos(SysProjectTableVo sysProjectTableVo);

    SysProjectTable getProjectTable(int pId);

    SysProjectTable getProjectTableByid(int pId);

    int insertProject(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectEstablish(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectEstablishById(int id);

    int updateProject(SysProjectTable sysProjectTable);

    List<SysProjectTable> endProject(SysProjectTable sysProjectTable);

    Double getPlanRate(int pId);

    int updatePlan(SysProjectTable sysProjectTable);

    int updateParticipantsName(SysProjectTable sysProjectTable);

    int updateEstablishStatus(SysProjectTable sysProjectTable);

    String getParticipants(int id);

    int updateEstablishStatu(SysProjectTable sysProjectTable);

    List<ResultApprovalRecord> selectByCurrentId(int projectId);

    int selectChargePeopleById(int projectId);

    List<SysProjectTable> getProjectTableByProjectId(SysProjectTable sysProjectTable);

    SysProjectTable selectMyProject(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectByChargePeople(SysProjectTable sysProjectTable);

    String selectFinishByProjectId(SysProjectTable sysProjectTable);

    int updateUserAttention(SysProjectTable sysProjectTable);

    int updateMarkProject(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectExsitPlantProjet();

    Map<String, Object> selectTechniquePeopleId(Integer currentId);

    Integer selectUserAention(UserProjectAttention u);

    int updateChargePeoplePart(SysProjectTable sysProjectTable);

    String selectProjectNameById(long id);

    Integer updateMarkProjectForDate(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectByTechniquePeople(SysProjectTable sysProjectTable);

    int updateByProjectId(SysProjectTable sysProjectTable);

    String getFinallyDate(String startTime, String period) throws ParseException;

    SysProjectTable selectProjectById(Long pid);

    List<SysProjectTable> selectProjectInfoByTitle(String title);

    List<SysProjectTable> insertFailProject(SysProjectTable sysProjectTable);

    List<SysProjectTable> selectInfosByProjectId(SysProjectTable sysProjectTable);

    /**
     * @param title
     * @return 根据项目标题返回指定的项目信息
     */
    SysProjectTable selectInfoByTitle(String title);

    /**
     * @param idList 根据项目id删除指定项目
     */
    void deleteByIdEstablishProject(List idList);

    /**
     * @param idList 根据id删除与项目表有外键关联UserProjectAttention表的数据
     */
    void deleteByIdEstablishProjectLinkUserProjectAttentionInfo(List idList);

    /**
     * @param idList 根据id删除与项目表有外键关联ProjectPlanTable表的数据
     */
    void deleteByIdEstablishProjectLinkProjectPlanTableInfo(List idList);

    /**
     * @param idList 根据id删除与项目表有外键关联ParticipantsTable表的数据
     */
    void deleteByIdEstablishProjectLinkParticipantsTableInfo(List idList);

    /**
     * @param idList 根据id删除与项目表有外键关联ProjectTaskTable表的数据
     */
    void deleteByIdEstablishProjectLinkProjectTaskTableInfo(List idList);

    /**
     * @param idList 根据项目id获取audit_flow_current表审核的currentId
     */
    List selectAuditCurrentIdByApplyId(List idList);

    /**
     * @param idList 根据currentIds删除audit_flow_current表的数据
     */
    void deleteByIdEstablishProjectLinkAuditFlowCurrentInfo(List idList);

    /**
     * @param idList 根据currentIds删除audit_flow_node_role表的数据
     */
    void deleteByIdEstablishProjectLinkAuditFlowNodeRoleInfo(List idList);

    /**
     * @param idList 根据currentIds删除audit_flow_oper_record表的数据
     */
    void deleteByIdEstablishProjectLinkAuditFlowOperRecordInfo(List idList);

    SysProjectTable selectInfosBypId(int pId);

    List<String> selectAuditFlowCurrentByPid(List idList);

    void deleteAuditFlowCurrentByPid(List idList);

    void deleteByIdProjectInMsg(List idList);

    void deleteByIdProjectInProjectMarketStageTable(List idList);

    void deleteByIdProjectInProjectMarkTypeInfoTable(List idList);

    void deleteByIdProjectInSysCompetitor(List idList);

    List<SysProjectTable> selectAllCacheProject();

    List<SysProjectTable> selectUserIdsBypId(Integer pId);

    void deleteByIdProjectInMsgByType(List idList);

    SysProjectTableTechnologyVo selectTechnology(Integer pId);

    Integer selectProjectVosCount(SysProjectTableVo sysProjectTableVo);

    Integer selectProjectCountByTitle(String title);

    List<SysProjectTableVo> selectManager(SysProjectTableVo sysProjectTableVo);

    List<SysProjectTableTechnologyVo> selectTitle(SysProjectTableTechnologyVo sysProjectTableTechnologyVo);

    List<SysProjectTable> selectProjectInPlanList(TaskProject taskProject);

    int quashProjectByIds(@Param("ids") String[] ids);

    int deleteProjectByIds(@Param("ids") String[] ids);

    List<SysProjectTable> querylist(@Param("keyword") String keyword, @Param("chargePeopleid") Long chargePeopleid);

    int updatetimeById(@Param("id") int id, @Param("checktime") String checktime, @Param("finishMemo") String finishMemo);

    int updatestopcauseById(@Param("id") Integer id, @Param("stoptype") String stoptype, @Param("stopcause") String stopcause);

    List<Integer> querypIdByUid(Map map);

    Integer selectTypeId(Integer projectId);

    Map<String, Object> selectApprovalDetail(AuditFlowCrurrentVo vo);

    List<Map<String, Object>> selectTitleBypId(Map<String, Object> map);

    String selectApplyName(Integer pId);

    Integer selectFollowUserId(@Param("userId") Long userId, @Param("pId") Integer pId);

    List<Integer> selectIdList(Long userId);

    Map<String, Object> selectChargeName(Integer pId);

    /**
     * 删除项目及关联信息
     *
     * @param pId
     * @return
     */
    int deleteRelationProjectMsg(Integer pId);
}
