package com.ruoyi.web.controller.item;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.SysUserWorks;
import com.ruoyi.web.mapper.item.SysUserWorksMapper;
import com.ruoyi.web.service.item.SysUserWorksService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/userworks")
@Api(value = "SysUserWorksController", tags = "统计员工任务工作相关接口")
public class SysUserWorksController extends BaseController {


    @Autowired
    private SysUserWorksService sysUserWorksService;
    @Autowired
    private SysUserWorksMapper sysUserWorksMapper;

    /**
     * 查询
     *
     * @param sysUserWorks
     * @return
     */
    @PostMapping("/selectInfos")
    @ResponseBody
    public List<SysUserWorks> selectInfos(SysUserWorks sysUserWorks) {
        List<SysUserWorks> list = sysUserWorksService.selectUserWorks(sysUserWorks);
        return list;
    }

    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(SysUserWorks sysUserWorks) {
        if (sysUserWorks.getMonth() == null || sysUserWorks.getYear() == null) {
            return AjaxResult.error("年份或月份不能为空");
        }
        sysUserWorks.setMonthScore(sysUserWorks.getTaskMonthScore());
        sysUserWorks.setUpdateTime(new Date());
        Double score = 0.0;
        Map<String, Object> map = new HashMap<>();
        map.put("userId", sysUserWorks.getUserId());
        map.put("year", sysUserWorks.getYear());
        map.put("month", sysUserWorks.getMonth());
        List<Map<String, Object>> list = sysUserWorksMapper.selectMonthScore(map);
        if (sysUserWorks.getMultipleMonthScore() != null) {
            sysUserWorks.setMultipleMonthScore(sysUserWorks.getMultipleMonthScore());
            Double taskAverageScore = 0.0;
            for (Map<String, Object> map2 : list) {
                taskAverageScore = (Double) map2.get("taskAverageScore");
            }
            Double taskScore = Double.valueOf(String.format("%.2f", taskAverageScore * 0.7));
            Double mutipleScore = Double.valueOf(String.format("%.2f", sysUserWorks.getMultipleMonthScore() * 0.3));
            Double monthscore = taskScore + mutipleScore;
            sysUserWorks.setMonthScore(monthscore);
        }
        if (sysUserWorks.getPersonalScore() != null) {
            sysUserWorks.setPersonalScore(sysUserWorks.getPersonalScore());
            Double yearScore = 0.0;
            for (Map<String, Object> map2 : list) {
                yearScore = (Double) map2.get("yearScore");
            }
            yearScore = Double.valueOf(String.format("%.2f", yearScore * 0.7));
            Double personalScore = Double.valueOf(String.format("%.2f", sysUserWorks.getPersonalScore() * 0.3));
            Double yearAverageScore = yearScore + personalScore;
            sysUserWorks.setYearAverageScore(yearAverageScore);
        }
        return toAjax(sysUserWorksService.updateUserWorks(sysUserWorks));
    }

    @PostMapping("/selectScore")
    @ResponseBody
    public AjaxResult selectScore(SysUserWorks sysUserWorks) {
        try {
            if (sysUserWorks.getMonth() == null || sysUserWorks.getYear() == null) {
                return AjaxResult.error("年份或月份不能为空");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("userId", sysUserWorks.getUserId());
            map.put("year", sysUserWorks.getYear());
            map.put("month", sysUserWorks.getMonth());
            Map<String, Object> resultMap = sysUserWorksMapper.selectScoreByTime(map);
            if (resultMap == null || resultMap.size() < 1) {
                resultMap.put("monthScore", 0.0);
                resultMap.put("yearAverageScore", 0.0);
                resultMap.put("multipleMonthScore", 0.0);
                resultMap.put("personalScore", 0.0);
            } else {
                Double monthScore = (Double) resultMap.get("monthScore");
                Double yearAverageScore = (Double) resultMap.get("yearAverageScore");
                Double multipleMonthScore = (Double) resultMap.get("multipleMonthScore");
                Double personalScore = (Double) resultMap.get("personalScore");
                if (monthScore == null) {
                    resultMap.put("monthScore", 0.0);
                }
                if (yearAverageScore == null) {
                    resultMap.put("yearAverageScore", 0.0);
                }
                if (multipleMonthScore == null) {
                    resultMap.put("multipleMonthScore", 0.0);
                }
                if (personalScore == null) {
                    resultMap.put("personalScore", 0.0);
                }
            }
            return AjaxResult.success("查询成功", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询失败");
        }

    }
}
