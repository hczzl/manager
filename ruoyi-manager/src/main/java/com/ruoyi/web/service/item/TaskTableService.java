package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.*;
import com.ruoyi.web.domain.vo.SysProjectTableVo;
import com.ruoyi.web.domain.vo.TaskQuashVo;
import com.ruoyi.web.domain.vo.TaskTableVo;
import com.ruoyi.web.util.pageutils.PageEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发起任务Service接口
 *
 * @author ruoyi
 * @date 2019-08-14
 */
public interface TaskTableService {

    /**
     * 全部任务
     * 紧急任务
     * 逾期任务
     */
    PageEntity lists(TaskTable taskTable);

    /**
     * 根据里程碑id查询任务
     *
     * @param parentid
     * @return
     */
    List<TaskTable> selectTaskTableByPlantId(Integer parentid);

    /**
     * 查询发起任务
     *
     * @param tId 发起任务ID
     * @return 发起任务
     */
    TaskTable selectTaskTableById(Long tId);

    /**
     * 查询发起任务列表
     *
     * @param taskTable 发起任务
     * @return 发起任务集合
     */
    List<TaskTable> selectTaskTableList(TaskTable taskTable);

    /**
     * 新增发起任务
     *
     * @param taskTable 发起任务,需要进行审批
     * @return 结果
     */
    int insertTaskTable(TaskTable taskTable);

    /**
     * 新增发起任务
     * 里程碑中的添加任务，不要进行审批
     */
    int insertTaskTableForPlan(TaskTable taskTable);

    /**
     * Excel新增发起任务
     * 里程碑中的添加任务，不要进行审批
     */
    int insertExcelTaskTableForPlan(TaskTable taskTable);

    /**
     * 修改发起任务
     *
     * @param taskTable 发起任务
     * @return 结果
     */
    int updateTaskTable(TaskTable taskTable);

    /**
     * 批量删除发起任务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteTaskTableByIds(String ids);

    /**
     * 删除发起任务信息
     *
     * @param tId 发起任务ID
     * @return 结果
     */
    int deleteTaskTableById(Long tId);

    /**
     * 批量撤回发起任务
     *
     * @param ids 需要撤回的任务ID
     * @return 结果
     */
    int quashTaskByIds(String ids, Integer currentId);

    /**
     * 完成里程碑和相关任务的全部查询
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectAllTask(TaskTable taskTable);

    /**
     * 计算任务总数
     *
     * @param taskTable
     * @return
     */
    int getAllTitle(TaskTable taskTable);

    /**
     * 查询该时间段无工作的日期
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Map<String, List<String>> getBusy(String startTime, String endTime);

    /**
     * 获取两个时间点之内的工作日
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getworkday(String startTime, String endTime);

    String getFinallyDate(String startTime, String period) throws ParseException;

    /**
     * 获取饱和度的接口
     *
     * @param taskTable
     * @return
     */
    Map saturation(TaskTable taskTable);

    /**
     * 任务到期提醒
     *
     * @param response
     * @param request
     * @param taskTable
     * @return
     */
    int taskMessageRemind(HttpServletResponse response, HttpServletRequest request, TaskTable taskTable);

    /**
     * 获取taskTable的list
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> getAllTaskRemind(TaskTable taskTable);

    /**
     * 获取负责人id
     *
     * @param tId
     * @return
     */
    Integer selectChargePeopleId(int tId);

    /**
     * 临时任务接口方法
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectTemporaryTask(TaskTable taskTable);

    /**
     * 任务审批记录接口
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectTaskByTid(TaskTable taskTable);

    /**
     * 根据任务id,任务遍历
     *
     * @param taskTable
     * @return
     */
    TaskTable selectTaskByTid1(TaskTable taskTable);

    /**
     * 根据负责人查任务
     */
    List<TaskTable> selectTaskByChargePeoleId(TaskTable taskTable);

    /**
     * 获得任务完成标识
     *
     * @param taskTable
     * @return
     */
    String selectFinishFlagById(TaskTable taskTable);

    /**
     * 获得任务提醒的列表
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectWarnList(Map<String, Object> map);

    /**
     * 更新列表
     *
     * @param map
     */
    void update(Map<String, Object> map);

    /**
     * 获得任务列表
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> getAllTaskTable(TaskTable taskTable);

    /**
     * 获得项目id
     *
     * @param taskTable
     * @return
     */
    int selectProjectId(TaskTable taskTable);

    /**
     * 获得任务发起人
     *
     * @param taskTable
     * @return
     */
    String selectCreateId(TaskTable taskTable);

    /**
     * 获得下属的任务
     *
     * @param userId
     * @return
     */
    TaskTable getLowerTask(int userId);

    /**
     * 获得所有的负责人
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectAllChargePeople(TaskTable taskTable);

    /**
     * 获得t_id
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectAllTid(TaskTable taskTable);

    /**
     * 获得开始时间
     *
     * @param taskTable
     * @return
     */
    Date getStartTime(TaskTable taskTable);

    /**
     * 获取任务进度
     *
     * @param taskTable
     * @return
     */
    int selectTaskSchedule(TaskTable taskTable);

    /**
     * 更新任务完成标识
     *
     * @param taskTable
     * @return
     */
    int updateTaskTableByFlag(TaskTable taskTable);

    /**
     * 根据负责人得到list,完成饱和度的计算
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectListByChargeId(TaskTable taskTable);

    /**
     * 根据id查询任务，并且合并
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectTaskByTid2(TaskTable taskTable);

    /**
     * 根据用户id和任务id得到用户关注任务的状态
     *
     * @param userTaskAttention
     * @return
     */
    Integer selectUserAttention(UserTaskAttention userTaskAttention);

    /**
     * 根据id查询任务标题
     *
     * @param id
     * @return
     */
    String selectTitle(long id);

    /**
     * 获得任务发起人
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectCreateBy(TaskTable taskTable);

    /**
     * 根据标题名称模糊查询获取任务信息
     *
     * @param title
     * @return
     */
    List<TaskTable> selectTaskTableByName(String title);

    /**
     * 根据项目id查询任务信息
     *
     * @param pid
     * @return
     */
    List<TaskTable> selectTaskTableByProjectId(Integer pid);

    /**
     * 获得对应的总数
     *
     * @return
     */
    Integer selectRows();

    /**
     * 根据项目id获取任务信息
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectInfoByProjectId(TaskTable taskTable);

    /**
     * 根据里程碑ID以及标题获取任务
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectTaskByTitleAndPid(TaskTable taskTable);

    /**
     * 根据用户id查询用户参与负责的任务
     *
     * @param userId
     * @return
     */
    List<TaskTable> selectTaskByUserId(Long userId);

    /**
     * 任务修改提醒
     *
     * @param taskSubnitPlan
     */
    void updateWarnInfo(TaskSubnitPlan taskSubnitPlan);

    /**
     * 获取任务的个数
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectCountTask(TaskTable taskTable);

    /**
     * Vo类
     *
     * @param taskTableVo
     * @return
     */
    List<TaskTableVo> selectTaskVos(TaskTableVo taskTableVo);

    /**
     * 获取任务变更消息
     *
     * @param currentId
     * @return
     */
    List<TaskChange> selectTaskChanes(Integer currentId);

    /**
     * @param taskTable
     * @return
     */
    List<TaskTable> selectNewInfo(TaskTable taskTable);

    /**
     * 任务中止
     *
     * @param taskTable
     * @return
     */
    int editTask(TaskTable taskTable);

    /**
     * 任务完成
     *
     * @param taskTable
     * @return
     */
    int accomplish(TaskTable taskTable, SysFileInfo sysFileInfo1);

    int changePeriod(TaskTable taskTable, TaskPostpone taskPostpone);

    /**
     * 查询关注任务的列表
     *
     * @param taskTable
     * @return
     */
    List<TaskTable> selectAttentionlist(TaskTable taskTable);

    /**
     * 根据id更新任务逾期状态
     *
     * @param id
     */
    void upTaskOverdueState(Integer state, Long id);

    List<TaskTable> selectTaskInfos(long tId);

    Integer selectTechniqueUserId(Integer tId);

    Integer selectTaskTableListCount(TaskTable taskTable);

    /**
     * 根据任务id
     *
     * @param tId
     * @return
     */
    List<SysProjectTableVo> queryProjectBytId(Long tId);


    Integer isAttention(Long tId);

    /**
     * 添加任务
     *
     * @param taskTable
     * @return
     */
    int saveTask(TaskTable taskTable);

    /**
     * 再次提交任务
     *
     * @param vo
     * @return
     */
    int insertQushTask(TaskQuashVo vo);

    /**
     * 获取用户参与的项目信息
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectJoinProjectList(Map<String, Object> map);

    /**
     * 获取月度、年度参与任务总数、完成总数、中止总数
     *
     * @param map
     * @return
     */
    Map<String, Object> selectTaskCount(Map<String, Object> map);

}
