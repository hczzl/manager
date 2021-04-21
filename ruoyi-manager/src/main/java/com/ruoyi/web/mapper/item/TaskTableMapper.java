package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.TaskSubnitPlan;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.domain.UserTaskAttention;
import com.ruoyi.web.domain.vo.AuditFlowCrurrentVo;
import com.ruoyi.web.domain.vo.SysProjectTableVo;
import com.ruoyi.web.domain.vo.TaskTableVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发起任务Mapper接口
 *
 * @author ruoyi
 * @date 2019-08-14
 */
@Component
public interface TaskTableMapper {

    List<TaskTable> selectTaskTableByPlantId(Integer parentid);

    TaskTable selectTaskTableById(Long tId);

    List<TaskTable> selectTaskTableList(TaskTable taskTable);

    int insertTaskTable(TaskTable taskTable);

    int insertTaskTableForPlan(TaskTable taskTable);

    int updateTaskTable(TaskTable taskTable);

    int deleteTaskTableById(Long tId);

    int deleteTaskTableByIds(@Param("tIds") String[] tIds);

    int quashTaskByIds(@Param("tIds") String[] tIds);

    List<TaskTable> selectAllTask(TaskTable taskTable);

    int getAllTitle(TaskTable taskTable);

    int taskMessageRemind(HttpServletResponse response, HttpServletRequest request, TaskTable taskTable);

    List<TaskTable> getAllTaskRemind(TaskTable taskTable);

    List<Map<String, Object>> selectWarnList(Map<String, Object> map);

    void update(Map<String, Object> map);

    Integer selectChargePeopleId(int tId);

    List<TaskTable> selectTemporaryTask(TaskTable taskTable);

    List<TaskTable> selectTaskByTid(TaskTable taskTable);

    TaskTable selectTaskByTid1(TaskTable taskTable);

    List<TaskTable> selectTaskByChargePeoleId(TaskTable taskTable);

    String selectFinishFlagById(TaskTable taskTable);

    List<TaskTable> getAllTaskTable(TaskTable taskTable);

    int selectProjectId(TaskTable taskTable);

    String selectCreateId(TaskTable taskTable);

    TaskTable getLowerTask(int userId);

    List<TaskTable> selectAllChargePeople(TaskTable taskTable);

    List<TaskTable> selectAllTid(TaskTable taskTable);

    Date getStartTime(TaskTable taskTable);

    int selectTaskSchedule(TaskTable taskTable);

    int updateTaskTableByFlag(TaskTable taskTable);

    List<TaskTable> selectListByChargeId(TaskTable taskTable);

    List<TaskTable> selectTaskByTid2(TaskTable taskTable);

    Integer selectUserAttention(UserTaskAttention userTaskAttention);

    String selectTitle(long id);

    List<TaskTable> selectCreateBy(TaskTable taskTable);

    List<TaskTable> selectTaskTableByName(String title);

    List<TaskTable> selectTaskTableByProjectId(Integer pid);

    Integer selectRows();

    List<TaskTable> selectInfoByProjectId(TaskTable taskTable);

    List<TaskTable> selectTaskByTitleAndPid(TaskTable taskTable);

    List<TaskTable> selectTaskByUserId(Long userId);

    void updateWarnInfo(TaskSubnitPlan taskSubnitPlan);

    List<TaskTable> selectCountTask(TaskTable taskTable);

    List<TaskTableVo> selectTaskVos(TaskTableVo taskTableVo);

    List<TaskTable> selectNewInfo(TaskTable taskTable);

    List<TaskTable> selectAttentionlist(TaskTable taskTable);

    void upTaskOverdueState(@Param("state") Integer state, @Param("id") Long id);

    List<TaskTable> selectTaskInfos(long tId);

    Integer selectTechniqueUserId(Integer tId);

    List<Long> selectTaskPlayersIdByTid(TaskTable taskTable);

    Integer selectTaskTableListCount(TaskTable taskTable);

    List<TaskTable> selectTaskByUserIdByParticipants(Long userId);

    /**
     * 根据任务id
     *
     * @param tId
     * @return
     */
    List<SysProjectTableVo> queryProjectBytId(Long tId);

    List<Long> selectTaskUserId(Long tid);

    Map<String, Object> selectApprovalDetail(AuditFlowCrurrentVo vo);

    String selectApprovalCreate(Map<String, Object> map);

    List<String> selectApprovalMemo(Map<String, Object> map);

    Integer selectCreateByCount(Map<String, Object> map);

    /**
     * 根据任务id任务列表
     *
     * @param tId
     * @return
     */
    List<TaskTable> selectTaskList(long tId);

    /**
     * 根据时间获取任务列表
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectTaskListByTime(Map<String, Object> map);

    /**
     * 获取年度数据总量接口
     *
     * @param map
     * @return
     */
    Long selectYearCount(Map<String, Object> map);

    /**
     * 分段获取任务数据，更新其逾期状态
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectPageTaskList(Map<String, Object> map);

    /**
     * 更新逾期状态
     **/
    void modifyOverdueState();

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
     * @param map 参数
     * @return
     */
    Long selectTaskCount(Map<String, Object> map);
}
