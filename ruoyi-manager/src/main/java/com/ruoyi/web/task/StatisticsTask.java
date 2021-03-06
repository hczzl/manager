package com.ruoyi.web.task;

import com.ruoyi.framework.util.DateUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.SysUserWorks;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.web.service.item.SysUserWorksService;
import com.ruoyi.web.domain.SysHolidays;
import com.ruoyi.web.domain.SysYearMonth;
import com.ruoyi.web.mapper.item.SysUserWorksMapper;
import com.ruoyi.web.mapper.item.SysYearMonthMapper;
import com.ruoyi.web.mapper.item.TaskTableMapper;
import com.ruoyi.web.service.SysHolidaysService;
import com.ruoyi.web.service.item.SysYearMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhongzhilong
 * @date 2020-06-05
 * @description 任务统计模块相关接口
 */
@Component
@Async
public class StatisticsTask {
    @Autowired
    private SysHolidaysService holidaysService;
    @Autowired
    private SysUserWorksService sysUserWorksService;
    @Autowired
    private SysYearMonthService sysYearMonthService;
    @Autowired
    private SysUserWorksMapper sysUserWorksMapper;
    @Autowired
    private TaskTableMapper taskTableMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysYearMonthMapper sysYearMonthMapper;

    // @Scheduled(cron = "0 5/10 * * * ?")
    @Scheduled(cron = "0 0/30 * * * ?")
    public void execute() {
        System.out.println("开始执行任务统计功能");
        SysYearMonth sysYearMonth = new SysYearMonth();
        sysYearMonthService.insertDate(sysYearMonth);
        List<String> users = sysUserMapper.queryUserIds();
        for (String user : users) {
            Integer userId = Integer.parseInt(user);
            // 添加年 月 日，计算每月工作天数、发起任务数
            insertWorkDays(userId);
            // 月平均分、每月的周期
            insertData(userId);
            // 获取每一年的任务完成的情况:完成数量、未完成数量、逾期数量、未逾期数量
            selectPeopleTask(userId);
            // 获得年平均分，年度总综合评分
            selectMyYearScore(userId);
        }
    }

    /**
     * 添加年 月 日，计算每月工作天数、发起任务数
     *
     * @param userId
     */
    public void insertWorkDays(Integer userId) {
        SysUserWorks sysUserWorks = new SysUserWorks();

        SysYearMonth sysYearMonth = new SysYearMonth();
        List<SysYearMonth> yearsList = sysYearMonthService.selectInfos(sysYearMonth);
        for (int i = 0; i < yearsList.size(); i++) {
            String beginTime = yearsList.get(i).getStartTime();
            String finishTime = yearsList.get(i).getEndTime();
            String[] arr = finishTime.split("-");
            //获取年
            String year = arr[0];
            String month = arr[1];
            int workday = 0;
            Integer creatTaskNum = 0;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                List<SysHolidays> sysHolidays = holidaysService.selectSysHolidaysList(null);
                sysHolidays = sysHolidays.stream()
                        .filter(item -> item.getHolidays().compareTo(beginTime) >= 0)
                        .filter(item -> item.getHolidays().compareTo(finishTime) <= 0)
                        .collect(Collectors.toList());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(format.parse(beginTime));
                //获取当月工作天数：当月所有天数-假期数
                workday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - sysHolidays.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("startTime", beginTime + " 00:00:00");
            map.put("endTime", finishTime + " 23:59:59");
            creatTaskNum = taskTableMapper.selectCreateByCount(map);
            //用户id
            sysUserWorks.setUserId(userId);
            //每月应工作天数
            sysUserWorks.setWorkDays(workday);
            //年份
            sysUserWorks.setYear(Integer.parseInt(year));
            //月份
            sysUserWorks.setMonth(Integer.parseInt(month));
            //发起任务数
            sysUserWorks.setCreateTaskNum(creatTaskNum);
            //判断表中是否插入了年份、月份
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userId", userId);
            resultMap.put("year", year);
            resultMap.put("month", month);
            Integer count = sysUserWorksMapper.selectCount(resultMap);
            if (count < 1) {
                sysUserWorks.setCreateTime(new Date());
                sysUserWorksService.insertUserWorks(sysUserWorks);
            }
            if (count > 0) {
                sysUserWorks.setUpdateTime(new Date());
                sysUserWorksService.updateUserWorks(sysUserWorks);
            }
        }
    }

    /**
     * 月平均分、每月的周期
     *
     * @param userId
     */

    public void insertData(Integer userId) {
        SysUserWorks sysUserWorks = new SysUserWorks();
        SysYearMonth sysYearMonth = new SysYearMonth();
        List<SysYearMonth> yearsList = sysYearMonthService.selectInfos(sysYearMonth);
        for (int m = 0; m < yearsList.size(); m++) {
            String beginTime = yearsList.get(m).getStartTime();
            String finishTime = yearsList.get(m).getEndTime();
            String[] arr = finishTime.split("-");
            String year = arr[0];
            String month = arr[1];
            //月任务天数
            Double periodSum = 0.0;
            //月任务总分
            Double taskScoreSum = 0.0;
            //任务月平均分
            Double taskAverageScore = 0.0;
            //月综合表现分
            Double multipleMonthScore = 0.0;
            //月总评分
            Double monthScore = 0.0;
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("startTime", beginTime + " 00:00:00");
            map.put("endTime", finishTime + " 23:59:59");
            List<Map<String, Object>> resultList = taskTableMapper.selectTaskListByTime(map);
            if (ShiroUtils.isEmpty(resultList)) {
                continue;
            }
            //月任务数
            int taskNum = resultList.size();
            for (Map<String, Object> map2 : resultList) {
                String period = (String) map2.get("period");
                Double taskScore = (Double) map2.get("taskScore");
                if (!ShiroUtils.isNull(period)) {
                    periodSum += Double.parseDouble(period);
                }
                if (taskScore != null) {
                    taskScoreSum += taskScore;
                }
            }
            map.put("year", year);
            map.put("month", month);
            List<Map<String, Object>> list = sysUserWorksMapper.selectMonthScore(map);
            for (Map<String, Object> map2 : list) {
                multipleMonthScore = (Double) map2.get("multipleMonthScore");
            }
            if (taskNum > 0 && taskScoreSum > 0) {
                taskAverageScore = Double.valueOf(String.format("%.2f", taskScoreSum / taskNum));
            }
            if (taskAverageScore != null) {
                monthScore += taskAverageScore * 0.7;
            }
            if (multipleMonthScore != null) {
                monthScore += multipleMonthScore * 0.3;
            }
            sysUserWorks.setUserId(userId.intValue());
            sysUserWorks.setPeriod(periodSum.toString());
            sysUserWorks.setTaskAverageScore(taskAverageScore);
            sysUserWorks.setYear(Integer.parseInt(year));
            sysUserWorks.setMonth(Integer.parseInt(month));
            sysUserWorks.setMonthScore(monthScore);
            List<SysUserWorks> userWorksList = sysUserWorksService.selectUserWorks(sysUserWorks);
            if (userWorksList.size() < 1) {
                sysUserWorks.setCreateTime(new Date());
                sysUserWorks.setSaturation(0.0);
                sysUserWorksService.insertUserWorks(sysUserWorks);
            }
            if (userWorksList.size() > 0) {
                for (SysUserWorks userWorks : userWorksList) {
                    Integer workDays = userWorks.getWorkDays();
                    if (workDays != null && workDays > 0) {
                        Double saturation = Double.valueOf(String.format("%.2f", periodSum / workDays));
                        sysUserWorks.setSaturation(saturation);
                    }
                }
                sysUserWorks.setUpdateTime(new Date());
                sysUserWorksService.updateUserWorks(sysUserWorks);
            }
        }
    }


    /**
     * 获取每一年的任务完成的情况:完成数量、未完成数量、逾期数量、未逾期数量
     *
     * @param userId
     */
    public void selectPeopleTask(Integer userId) {
        try {
            // 完成数
            Long taskAlreadyFinish = 0L;
            // 未完成数
            Long taskNotFinish = 0L;
            // 逾期数
            Long taskOverdue = 0L;
            // 未逾期数
            Long taskNotOverdue = 0L;
            List<Integer> yearLsit = sysYearMonthMapper.selectYearList();
            for (Integer year : yearLsit) {
                Map<String, Object> map = new HashMap<>();
                // 获得今年第一天日期
                String startDate = selectMinDate(year);
                // 获得今年最后一天日期
                String endDate = selectMaxDate(year);
                // 当前月最后一天
                String currentDate = DateUtil.getCurrentMonthLastDay();
                if (endDate.equals(currentDate)) {
                    // 如果今年最后一个月等于当月最后一天，则将结束日期设置为上一个月的最后一天
                    String lastDaty = DateUtil.getLastMonthLastDay();
                    if (startDate.compareTo(lastDaty) < 0) {
                        endDate = lastDaty;
                    } else {
                        continue;
                    }
                }
                map.put("userId", userId);
                map.put("startTime", startDate + " 00:00:00");
                map.put("endTime", endDate + " 23:59:59");
                // 计算年度完成任务数taskFinishFlag为1的数据
                map.put("finish", "1");
                taskAlreadyFinish = taskTableMapper.selectYearCount(map);
                map.put("finish", "");
                // 计算年度未完成任务数-去掉taskFinishFlag为-1,8,2的任务数据
                String[] arr = {"-1", "1", "8", "2"};
                map.put("notFinish", Arrays.asList(arr));
                taskNotFinish = taskTableMapper.selectYearCount(map);
                List<String> nullList = new ArrayList<>();
                map.put("notFinish", nullList);
                // 计算年度逾期数-判断任务的逾期状态
                map.put("taskOverdueState", "0");
                taskOverdue = taskTableMapper.selectYearCount(map);
                // 计算年度未逾期数
                map.put("taskOverdueState", "1");
                taskNotOverdue = taskTableMapper.selectYearCount(map);

                SysUserWorks sysUserWorks = new SysUserWorks();
                sysUserWorks.setTaskAlreadyFinish(taskAlreadyFinish.intValue());
                sysUserWorks.setTaskNotFinish(taskNotFinish.intValue());
                sysUserWorks.setTaskOverdue(taskOverdue.intValue());
                sysUserWorks.setTaskNotOverdue(taskNotOverdue.intValue());

                sysUserWorks.setUserId(userId);
                sysUserWorks.setYear(year);
                sysUserWorks.setMonth(13);
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("userId", userId);
                resultMap.put("year", year);
                resultMap.put("month", 13);
                Integer count = sysUserWorksMapper.selectCount(resultMap);
                if (count < 1) {
                    sysUserWorks.setCreateTime(new Date());
                    sysUserWorksService.insertUserWorks(sysUserWorks);
                }
                if (count > 0) {
                    sysUserWorks.setUpdateTime(new Date());
                    sysUserWorksService.updateUserWorks(sysUserWorks);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户id得到该用户的平均分数=
     * （各个月份平均分数之和）/12
     * 获得年平均分，年度总综合评分
     *
     * @param userId
     */
    public void selectMyYearScore(Integer userId) {
        List<Integer> yearLsit = sysYearMonthMapper.selectYearList();
        for (Integer year : yearLsit) {
            List<Integer> monthList = new ArrayList<>();
            Integer thisYear = DateUtil.selectYear();
            Integer yearCount = this.selectMonthCount(year);
            if (yearCount == null || yearCount == 0 || year == 1) {
                continue;
            }
            if (thisYear.equals(year)) {
                yearCount = yearCount - 1;
                Integer thisMonth = DateUtil.selectMonth();
                monthList.add(thisMonth);
            }
            double monthScoreSum = 0.0;
            Map<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("year", year);
            map.put("yearAverage", 13);
            map.put("monthList", monthList);
            SysUserWorks sysUserWorks = new SysUserWorks();
            sysUserWorks.setUserId(userId);
            sysUserWorks.setYear(year);
            sysUserWorks.setMonth(13);
            // 判断年度数据是否存在，如果否，则添加数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userId", userId);
            resultMap.put("year", year);
            resultMap.put("month", 13);
            Integer count = sysUserWorksMapper.selectCount(resultMap);
            if (count < 1) {
                sysUserWorks.setCreateTime(new Date());
                sysUserWorksService.insertUserWorks(sysUserWorks);
            }
            if (count > 0) {
                sysUserWorks.setUpdateTime(new Date());
                sysUserWorksService.updateUserWorks(sysUserWorks);
            }
            sysUserWorks.setUpdateTime(new Date());
            // 根据userId，获取1-12个月的任务总分数
            List<Map<String, Object>> list = sysUserWorksMapper.selectMonthScore(map);
            for (Map<String, Object> map2 : list) {
                Double monthScore = (Double) map2.get("monthScore");
                if (monthScore != null) {
                    monthScoreSum += monthScore;
                }
            }
            // 求平均数
            monthScoreSum = Double.valueOf(String.format("%.2f", monthScoreSum / yearCount));
            sysUserWorks.setYearScore(monthScoreSum);
            // 更新年度任务平均分
            sysUserWorksService.updateUserWorks(sysUserWorks);
            map.put("yearAverage", null);
            map.put("month", 13);
            list = sysUserWorksMapper.selectMonthScore(map);
            for (Map<String, Object> map2 : list) {
                Double yearScore = (Double) map2.get("yearScore");
                Double personalScore = (Double) map2.get("personalScore");
                if (yearScore != null) {
                    yearScore = Double.valueOf(String.format("%.2f", yearScore * 0.7));
                }
                if (personalScore != null) {
                    personalScore = Double.valueOf(String.format("%.2f", personalScore * 0.3));
                }
                Double yearAverageScore = yearScore + personalScore;
                sysUserWorks.setYearAverageScore(yearAverageScore);
                // 更新年度总分
                sysUserWorksService.updateUserWorks(sysUserWorks);
            }
        }
    }

    /**
     * 获取各年的实际月份
     *
     * @param year
     * @return
     */
    public int selectMonthCount(Integer year) {
        StringBuffer build = new StringBuffer();
        build.append(year).append("-");
        return sysYearMonthMapper.selectMonthCount(build.toString());
    }

    /**
     * 获取对应年份下最小月份
     *
     * @param year
     * @return
     */
    public String selectMinDate(Integer year) {
        StringBuffer build = new StringBuffer();
        build.append(year).append("-");
        return sysYearMonthMapper.selectMinDate(build.toString());
    }

    /**
     * 获取对应年份下最大月份
     *
     * @param year
     * @return
     */
    public String selectMaxDate(Integer year) {
        StringBuffer build = new StringBuffer();
        build.append(year).append("-");
        return sysYearMonthMapper.selectMaxDate(build.toString());
    }
}
