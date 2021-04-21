package com.ruoyi.web.controller.item;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.domain.ParticipantsTable;
import com.ruoyi.web.service.item.ParticipantsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zhongzhilong
 * @date 2019/12/4
 */
@Controller
@Api(value = "ParticipantsController", tags = "参与人模块相关接口")
public class ParticipantsController extends BaseController {

    @Autowired
    private ParticipantsService participantsService;

    /**
     * 新增内容
     *
     * @param
     * @return
     */
    @PostMapping("addParticipants")
    @ResponseBody
    @Log(title = "新增内容", businessType = BusinessType.INSERT)
    public int insertParticipants(int pid, String participants, ParticipantsTable participantsTable) {
        int d = 0;
        String[] arr;
        //分隔符
        arr = participants.split(",");
        for (int i = 0; i < arr.length; i++) {
            int id = Integer.parseInt(arr[i]);
            participantsTable.setUserId(id);
            participantsTable.setProjectId(pid);
            d = participantsService.insertparticipants(participantsTable);
        }
        return d;
    }

    /**
     * 查询参与人信息
     *
     * @param participantsTable
     * @return
     */
    @GetMapping("/selectInfo")
    @ResponseBody
    public TableDataInfo listEstablish(ParticipantsTable participantsTable) {
        startPage();
        List<ParticipantsTable> listea = participantsService.selectParticients(participantsTable);
        return getDataTable(listea);
    }

    @PostMapping("/selectProjectId")
    @ResponseBody
    public List<ParticipantsTable> selectProjectId(ParticipantsTable p) {
        return participantsService.selectInfo(p);
    }

    /**
     * 根据用户名查询用户参与了多少个项目
     *
     * @param participantsTable
     * @return
     */
    @GetMapping("/selectAllParticipantsId")
    @ResponseBody
    public List<ParticipantsTable> getUserJoin(ParticipantsTable participantsTable) {
        List<ParticipantsTable> allListId = participantsService.selectUserJoin(participantsTable);
        for (int i = 0; i < allListId.size(); i++) {
            int d = getAlltimes((int) allListId.get(i).getUserId());
        }
        return allListId;
    }

    @GetMapping("/getAllTimeas/{userId}")
    @ResponseBody
    public int getAlltimes(@PathVariable("userId") int userId) {
        return participantsService.getAlltimes(userId);
    }

    /**
     * 获得所有的用户id
     */
    @PostMapping("/getAllParticiantsUserId")
    @ResponseBody
    public List<ParticipantsTable> selectAllUserId(ParticipantsTable participantsTable) {
        return participantsService.selectAllUserId(participantsTable);
    }

    @PostMapping("/getProjectByProjectId")
    @ResponseBody
    public List<ParticipantsTable> selectProjectByProjectId(ParticipantsTable participantsTable) {
        return participantsService.getProjectByProjectId(participantsTable);
    }
}
