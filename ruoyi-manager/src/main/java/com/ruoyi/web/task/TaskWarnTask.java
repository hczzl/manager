package com.ruoyi.web.task;


import com.ruoyi.web.domain.MsgEvtInfo;
import com.ruoyi.web.mapper.item.MsgEvtInfoMapper;
import com.ruoyi.web.mapper.item.TaskTableMapper;
import com.ruoyi.web.mapper.item.TaskUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * 任务相关提醒操作
 */
@Component
@Async
public class TaskWarnTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskTableMapper taskTableMapper;
    @Autowired
    private TaskUserMapper taskUserMapper;
    @Autowired
    private MsgEvtInfoMapper msgEvtInfoMapper;

    /**
     * 一天执行一次
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void execute() {
        System.out.println("开始执行任务相关提醒");
        Map<String, Object> map = new HashMap<>();
        int page = 1;
        int limit = 100;
        while (true) {
            //分页信息以字符串的形式存储
            map.put("start", (page - 1) * limit);
            map.put("end", limit);
            List<Map<String, Object>> taskList = taskTableMapper.selectWarnList(map);
            if (taskList.size() < 1 || taskList == null || taskList.isEmpty()) {
                break;
            }
            for (Map<String, Object> m : taskList) {
                long tId = Long.valueOf(String.valueOf(m.get("tId")));
                Map<String, Object> uMap = new HashMap<>();
                // 未逾期的情况
                if (checkNowAndEnd(m.get("endTime"))) {
                    // 当前时间在结束时间之前
                    if ("0".equals(m.get("warnStatus"))) {
                        // 任务到期前提醒
                        if (checkWarnTimeBetweenNowAndEndTime(m.get("endTime"), m.get("warnDays"))) {
                            logger.info("任务ID:" + m.get("tId") + "需要任务到期前提醒");
                            String warnUserId = null;
                            // 获得负责人
                            String chargepeopleId = String.valueOf(m.get("chargepeopleId"));
                            List<String> userList = new ArrayList<>();
                            if ("1".equals(m.get("remindChargePeople"))) {
                                //给负责人提醒
                                warnUserId = chargepeopleId;
                                userList.add(warnUserId);
                            } else if ("1".equals(m.get("remindPanticiants"))) {
                                //给参与人提醒
                                List<String> list = taskUserMapper.getUserByTid(m);
                                userList.addAll(list);
                            }
                            // type=0 表示任务到期
                            saveWarnMsg(tId, userList, "0");
                            uMap.put("warnStatus", "1");
                        }
                    }
                } else {
                    // now().getTime>end.getTime() --逾期的情况
                    // 当前时间在结束时间之后
                    if ("0".equals(m.get("overdueState")) && "1".equals(m.get("urgencyLevel"))) {
                        // 任务逾期提醒
                        logger.info("任务ID:" + m.get("tId") + "需要任务逾期提醒");
                        String chargepeopleId = String.valueOf(m.get("chargepeopleId"));
                        List<String> userList = new ArrayList<>();
                        userList.add(chargepeopleId);
                        System.out.println("需要逾期提醒");
                        // 保存需要提醒的人，type=1表示任务逾期
                        saveWarnMsg(tId, userList, "1");
                        uMap.put("overdueState", "1");
                    }
                }
                // 更新提醒状态
                if (!uMap.isEmpty()) {
                    uMap.put("tId", m.get("tId"));
                    taskTableMapper.update(uMap);
                }
            }
            if (taskList.size() < limit) {
                break;
            }
            page++;
        }
        System.out.println("跑批结束");
    }

    /**
     * 判断是否逾期的情况
     *
     * @param end
     * @return
     */
    public boolean checkNowAndEnd(Object end) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime((Date) end);
        java.util.Date endTime = calendar.getTime();
        java.util.Date now = new java.util.Date();
        if (now.getTime() < endTime.getTime()) {
            // 现在时间小于结束时间的情况
            return true;
        } else {
            return false;
        }
    }

    public boolean checkBetweenTwoDay(Object start) {
        Calendar calendar = new GregorianCalendar();
        // 开始时间
        calendar.setTime((Date) start);
        calendar.add(Calendar.DATE, 1);
        java.util.Date sta = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        java.util.Date end = calendar.getTime();
        java.util.Date now = new java.util.Date();
        // 现在时间
        if (now.getTime() >= sta.getTime() && now.getTime() < end.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断提前几天进行提醒
     *
     * @param end
     * @param wDays
     * @return
     */
    public boolean checkWarnTimeBetweenNowAndEndTime(Object end, Object wDays) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime((Date) end);
        java.util.Date endTime = calendar.getTime();
        calendar.add(Calendar.DATE, -Integer.valueOf((String) wDays));
        java.util.Date warnTime = calendar.getTime();
        java.util.Date now = new java.util.Date();
        if (now.getTime() >= warnTime.getTime() && now.getTime() <= endTime.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存需要进行任务提醒的用户
     *
     * @param userList
     * @param type
     */
    public void saveWarnMsg(long tId, List<String> userList, String type) {
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        if (!userList.isEmpty() && userList != null) {
            for (int i = 0; i < userList.size(); i++) {
                msgEvtInfo.setType(Integer.parseInt(type));
                msgEvtInfo.setEventId(tId);
                msgEvtInfo.setUserId(Integer.parseInt(userList.get(i)));
                msgEvtInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
                msgEvtInfoMapper.insertMessageInfo(msgEvtInfo);
            }
        }
    }

}
