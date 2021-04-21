package com.ruoyi.web.service.item;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.TaskProject;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.domain.ResultApprovalRecord;
import com.ruoyi.web.domain.vo.SysProjectTableTechnologyVo;
import com.ruoyi.web.domain.vo.SysProjectTableVo;
import com.ruoyi.web.util.pageutils.PageEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 项目 业务层
 *
 * @author zzl
 */

public interface SysProjectTableService {


    /**
     * 添加市场项目
     *
     * @param sysProjectTable
     * @return
     */
    int insertMarkProject(SysProjectTable sysProjectTable);

    /**
     * 查找所有的id,遍历得到最大的id
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> getBigId(SysProjectTable sysProjectTable);

    /**
     * 市场项目的查询
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> selectAllInfo(SysProjectTable sysProjectTable);

    /**
     * 市场项目的删除
     *
     * @param id
     * @return
     */
    int deleteProject(int id);

    /**
     * 根据类型类查询项目
     *
     * @param projectType
     * @return
     */
    List<SysProjectTable> selectProjectByType(String projectType);

    /**
     * 据项目名称关键字模糊查询立项列表
     *
     * @param title
     * @return
     */
    List<SysProjectTable> selectProjectByTitle(String title);

    /**
     * 根据立项状态查出立项列表
     *
     * @param status
     * @return
     */
    List<SysProjectTable> selectProjectByStatus(String status);

    /**
     * 三条件一起查询，满足其中一个条件就可以查找到
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> selectAllestablish(SysProjectTable sysProjectTable);

    /**
     * 查询我的项目
     *
     * @param id
     * @return
     */
    List<SysProjectTable> getMyProject(int id);

    /**
     * 根据id查询负责人
     *
     * @param sysProjectTable
     * @return
     */
    int getChargeIdById(SysProjectTable sysProjectTable);

    /**
     * 查询项目列表
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> selectProjectList(SysProjectTable sysProjectTable);


    /**
     * 查询项目详细信息
     *
     * @return 项目信息集合信息
     */
    List<SysProjectTable> selectAllProject(SysProjectTable sysProjectTable);

    /**
     * Vo类：查询项目列表
     *
     * @param sysProjectTableVo
     * @return
     */
    List<SysProjectTableVo> selectProjectVos(SysProjectTableVo sysProjectTableVo);

    /**
     * 根据id查询项目
     *
     * @param pId
     * @return
     */
    SysProjectTable getProjectTable(int pId);

    /**
     * 根据id查询项目
     *
     * @param pId
     * @return
     */
    SysProjectTable getProjectTableByid(int pId);

    /**
     * 根据id查询项目2
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> getProjectTableByProjectId(SysProjectTable sysProjectTable);

    /**
     * 添加科研项目
     *
     * @param sysProjectTable
     * @return
     */
    int insertProject(SysProjectTable sysProjectTable);

    /**
     * 立项列表
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> selectEstablish(SysProjectTable sysProjectTable);

    /**
     * 查询立项列表
     *
     * @param id
     * @return
     */
    List<SysProjectTable> selectEstablishById(int id);

    /**
     * 修改项目完成标识,终止项目
     *
     * @param sysProjectTable
     * @return
     */
    int updateProject(SysProjectTable sysProjectTable);

    /**
     * 修改立项状态,使得能够完成立项审批
     *
     * @param sysProjectTable
     * @return
     */
    int updateEstablishStatus(SysProjectTable sysProjectTable);

    /**
     * 查询已经关闭的项目列表
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> endProject(SysProjectTable sysProjectTable);

    /**
     * 根据项目id获得项目的进度plan_rate
     *
     * @param pId
     * @return
     */
    Double getPlanRate(int pId);

    /**
     * 根据id来更新进度条
     *
     * @param sysProjectTable
     * @return
     */
    int updatePlan(SysProjectTable sysProjectTable);

    /**
     * updateParticipantsName
     * 更新参与人名字
     *
     * @param sysProjectTable
     * @return
     */
    int updateParticipantsName(SysProjectTable sysProjectTable);

    /**
     * 根据id来获取参与人
     *
     * @param id
     * @return
     */
    String getParticipants(int id);

    /**
     * 添加保存项目
     *
     * @param sysProjectTable
     * @param projectMarketStageTable
     * @param projectMarkTypeInfoTable
     * @return
     */
    int insertMarketProject(SysProjectTable sysProjectTable, ProjectMarketStageTable projectMarketStageTable, ProjectMarkTypeInfoTable
            projectMarkTypeInfoTable);

    /**
     * 立项状态变为1
     *
     * @param sysProjectTable
     * @return
     */
    int updateEstablishStatu(SysProjectTable sysProjectTable);

    /**
     * 获得项目的审批记录
     *
     * @return
     */
    List<ResultApprovalRecord> selectByCurrentId(int projectId);

    /**
     * 获得项目的负责人
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> selectByChargePeople(SysProjectTable sysProjectTable);

    /**
     * 获得项目负责人id
     *
     * @param projectId
     * @return
     */
    int selectChargePeopleById(int projectId);

    /**
     * 查询我的项目
     *
     * @param sysProjectTable
     * @return
     */
    SysProjectTable selectMyProject(SysProjectTable sysProjectTable);

    /**
     * 获得项目完成标识
     *
     * @param sysProjectTable
     * @return
     */
    String selectFinishByProjectId(SysProjectTable sysProjectTable);

    /**
     * 更新用户得关注项目的状态
     *
     * @param sysProjectTable
     * @return
     */
    int updateUserAttention(SysProjectTable sysProjectTable);

    /**
     * 更新技术负责人写的部分
     *
     * @param sysProjectTable
     * @return
     */
    int updateMarkProject(SysProjectTable sysProjectTable);

    /**
     * 获取所有存在里程碑的项目的id以及名称
     *
     * @return
     */
    List<SysProjectTable> selectExsitPlantProjet();

    /**
     * 查询技术负责人id
     *
     * @param currentId
     * @return
     */
    Map<String, Object> selectTechniquePeopleId(Integer currentId);

    /**
     * 获取登录用户关注项目的状态
     *
     * @param u
     * @return
     */
    Integer selectUserAention(UserProjectAttention u);

    /**
     * 修改商务负责人部分内容
     *
     * @param sysProjectTable
     * @return
     */
    int updateChargePeoplePart(SysProjectTable sysProjectTable);

    /**
     * 查询项目标题
     *
     * @param id
     * @return
     */
    String selectProjectNameById(long id);

    /**
     * 更新市场立项成功的项目信息
     *
     * @param sysProjectTable
     * @return
     */
    Integer updateMarkProjectForDate(SysProjectTable sysProjectTable);

    /**
     * 根据技术负责人id来获取对应的项目
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> selectByTechniquePeople(SysProjectTable sysProjectTable);

    /**
     * 科研项目的修改
     *
     * @param sysProjectTable
     * @return
     */
    int updateByProjectId(SysProjectTable sysProjectTable);

    /**
     * 根据开始时间和和周期获得结束时间
     *
     * @param startTime
     * @param period
     * @return
     * @throws ParseException
     */
    String getFinallyDate(String startTime, String period) throws ParseException;

    /**
     * 根据pid查询项目信息
     *
     * @param pid
     * @return
     */
    SysProjectTable selectProjectById(Long pid);

    /**
     * 根据项目名称模糊查询获取项目信息
     *
     * @param title
     * @return
     */
    List<SysProjectTable> selectProjectInfoByTitle(String title);

    /**
     * 添加拒批然后再次编辑的项目
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> insertFailProject(SysProjectTable sysProjectTable);

    /**
     * 根据立项状态、完成标识、项目id查询项目
     *
     * @param sysProjectTable
     * @return
     */
    List<SysProjectTable> selectInfosByProjectId(SysProjectTable sysProjectTable);

    /**
     * 完成编辑再次提交的功能
     * 删除信息的操作
     *
     * @param sysProjectTable
     * @param projectMarketStageTable
     * @return
     */
    AjaxResult updateApprocalMarkProject(SysProjectTable sysProjectTable, ProjectMarketStageTable
            projectMarketStageTable);

    /**
     * 完成编辑再次提交的功
     * 保留原信息，并且改变项目的，名称
     *
     * @param sysProjectTable
     * @param projectMarketStageTable
     * @return
     */
    AjaxResult updateMarkProject(SysProjectTable sysProjectTable, ProjectMarketStageTable projectMarketStageTable);

    /**
     * 实现项目相关列表的分页的功能
     *
     * @param sysProjectTable
     * @param list
     * @return
     */
    List<SysProjectTable> paging(SysProjectTable sysProjectTable, List<SysProjectTable> list);

    /**
     * @param title
     * @return 根据项目标题返回指定的项目信息
     */
    SysProjectTable selectInfoByTitle(String title);

    /**
     * @param ids 根据项目标题返回指定的项目信息
     */
    void deleteByIdEstablishProject(String ids);

    /**
     * @param ids 根据id删除与项目表有外键关联的数据
     */
    void deleteByIdEstablishProjectLinkInfo(String ids);

    /**
     * @param ids 根据id删除与项目表有关的审核信息
     */
    void deleteByIdEstablishProjectLinkAuditInfo(String ids);

    /**
     * 根据项目id获取信息
     *
     * @param pId
     * @return
     */
    SysProjectTable selectInfosBypId(int pId);

    /**
     * @param ids 根据id删除与项目立项表有关的提醒消息
     */
    void deleteByIdProjectInMsg(String ids);

    List<SysProjectTable> selectAllCacheProject(SysProjectTable sysProjectTable);

    void inserts(Integer projectId, Integer userId);

    List<SysProjectTable> selectUserIdsBypId(Integer pId);

    Integer selectState(Integer projectId);

    Integer selectFinishState(Integer projectId);

    SysProjectTableTechnologyVo selectTechnology(Integer pId);

    Integer selectProjectVosCount(SysProjectTableVo sysProjectTableVo);

    Integer selectProjectCountByTitle(String title);

    List<SysProjectTableVo> selectManager(SysProjectTableVo sysProjectTableVo);

    List<SysProjectTableTechnologyVo> selectTitle(SysProjectTableTechnologyVo sysProjectTableTechnologyVo);

    AjaxResult isRepeat(String title);

    /**
     * 获取有里程碑执行中的项目
     *
     * @return
     */
    List<SysProjectTable> selectProjectInPlanList(TaskProject taskProject);

    /**
     * 项目文件上传
     *
     * @param fileIds
     * @param pid
     * @return
     */
    AjaxResult addProjectFile(String fileIds, Integer pid);

    /**
     * 批量撤回立项
     *
     * @param ids 需要撤回的立项ID
     * @return 结果
     */
    int quashProjectByIds(String ids, Integer currentId);

    /**
     * 批量删除立项
     *
     * @param ids 需要删除的立项ID
     * @return 结果
     */
    int deleteProjectByIds(String ids);

    /**
     * 项目查询
     *
     * @param keyword        关键字
     * @param chargePeopleid 负责人id
     * @return 结果
     */
    List<SysProjectTable> querylist(String keyword, Long chargePeopleid);

    /**
     * 更新验收时间或鉴定时间
     *
     * @param id        项目ID
     * @param checktime 验收时间
     * @return 结果
     */
    int updatetimeById(int id, String checktime, String finishMemo);

    /**
     * 更新中止原因
     *
     * @param pId       项目ID
     * @param stopcause 中止原因
     * @return 结果
     */
    int updatestopcauseById(Integer pId, String stoptype, String stopcause);

    /**
     * 科研项目的添加
     *
     * @param sysProjectTable
     * @return
     */
    AjaxResult insertResearchProject(SysProjectTable sysProjectTable);

    /**
     * 添加市场项目
     *
     * @param sysProjectTable
     * @param projectMarketStageTable
     * @return
     */
    AjaxResult insertMarkProject(SysProjectTable sysProjectTable, ProjectMarketStageTable projectMarketStageTable);

    /**
     * 获取我的项目
     *
     * @param sysProjectTable
     * @return
     */
    PageEntity queryMyProject(SysProjectTable sysProjectTable);

    /**
     * 添加资源信息的接口
     *
     * @param vo
     * @return
     */
    int addMarkInfo(ProjectMarkTypeInfoTable vo);

    /**
     * 更新资源表信息
     *
     * @param projectMarkTypeInfoTable
     * @return
     */
    int updateMarkInfo(ProjectMarkTypeInfoTable projectMarkTypeInfoTable);

    /**
     * 我关注的项目列表
     *
     * @return
     */
    PageEntity attentionlist(SysProjectTable sysProjectTable);

    /**
     * 项目立项列表查询
     *
     * @param sysProjectTable
     * @return
     */
    PageEntity establishList(SysProjectTable sysProjectTable);
}
