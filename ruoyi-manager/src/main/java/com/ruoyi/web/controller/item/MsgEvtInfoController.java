package com.ruoyi.web.controller.item;

import java.sql.Timestamp;
import java.util.*;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.service.item.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 消息模块控制器
 *
 * @author zhongzhilong
 * @date 2020/8/15 0015
 */
@Controller
@RequestMapping("/msgEvtInfo")
@Api(value = "MsgEvtInfoController", tags = "消息模块相关接口")
public class MsgEvtInfoController extends BaseController {

    @Autowired
    private MsgEvtInfoService msgEvtInfoService;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @ApiOperation("查询消息列表")
    @ResponseBody
    @PostMapping("list")
    public PageEntity list(MsgEvtInfo msgEvtInfo) {
        try {
            return msgEvtInfoService.selectMsgEvtList(msgEvtInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation("获得各类消息的未读条数")
    @PostMapping("/getReadMark")
    @ResponseBody
    public Map<String, Integer> selectReadMark(MsgEvtInfo msgEvtInfo) {
        Map map = new HashMap();
        long userId = ShiroUtils.getUserId();
        msgEvtInfo.setUserId((int) userId);
        int approcalReadMark = 0;
        int taskReadMark = 0;
        // 填写通知提醒未读条数
        int noticeReadMark = 0;
        // 未审批的未读条数
        int notpassReadMark = 0;
        // 抄送的未读条数
        int notCopyReadMark = 0;
        // 项目进度提醒的未读条数
        int projectProgressReminderReadMark = 0;
        List<MsgEvtInfo> list = msgEvtInfoService.selectMsgList(msgEvtInfo);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getReadMark().equals(0)) {
                if (list.get(i).getType() == 3) {
                    approcalReadMark++;
                }
                if (list.get(i).getType() == 0 | list.get(i).getType() == 1 | list.get(i).getType() == 2) {
                    taskReadMark++;
                }
                if (list.get(i).getType() == 5 | list.get(i).getType() == 6) {
                    noticeReadMark++;
                }
                if (list.get(i).getType() == 8 | list.get(i).getType() == 9) {
                    notpassReadMark++;
                }
                if (list.get(i).getType() == 11) {
                    notCopyReadMark++;
                }
                if (list.get(i).getType() == 12) {
                    projectProgressReminderReadMark++;
                }
            }
        }
        map.put("approcalReadMark", approcalReadMark);
        map.put("taskReadMark", taskReadMark);
        map.put("noticeReadMark", noticeReadMark);
        map.put("notpassReadMark", notpassReadMark);
        map.put("notCopyReadMark", notCopyReadMark);
        map.put("projectProgressReminderReadMark", projectProgressReminderReadMark);
        return map;
    }


    @ApiOperation("将未读消息置为已读")
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public AjaxResult update(MsgEvtInfo msgEvtInfo) {
        msgEvtInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return toAjax(msgEvtInfoService.update(msgEvtInfo));
    }

}