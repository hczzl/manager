package com.ruoyi.web.controller.item;

import java.util.List;

import com.ruoyi.framework.util.ShiroUtils;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.domain.UserTaskAttention;
import com.ruoyi.web.service.item.UserTaskAttentionService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 关注任务Controller
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Controller
@RequestMapping("/usertaskattention")
@Api(value = "UserTaskAttentionController", tags = "任务关注接口")
public class UserTaskAttentionController extends BaseController {
    private String prefix = "system/attention";

    @Autowired
    private UserTaskAttentionService userTaskAttentionService;

    @RequiresPermissions("system:attention:view")
    @GetMapping()
    public String attention() {
        return prefix + "/attention";
    }

    /**
     * 查询关注任务列表
     *
     * @param userTaskAttention
     * @return
     */
    @RequiresPermissions("system:attention:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserTaskAttention userTaskAttention) {
        startPage();
        List<UserTaskAttention> list = userTaskAttentionService.selectUserTaskAttentionList(userTaskAttention);
        return getDataTable(list);
    }

    /**
     * 导出关注任务列表
     *
     * @param userTaskAttention
     * @return
     */
    @RequiresPermissions("system:attention:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(UserTaskAttention userTaskAttention) {
        List<UserTaskAttention> list = userTaskAttentionService.selectUserTaskAttentionList(userTaskAttention);
        ExcelUtil<UserTaskAttention> util = new ExcelUtil<UserTaskAttention>(UserTaskAttention.class);
        return util.exportExcel(list, "attention");
    }

    /**
     * 新增关注任务
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存关注任务
     *
     * @param userTaskAttention
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(UserTaskAttention userTaskAttention) {
        // 传入userId
        userTaskAttention.setUserId(ShiroUtils.getUserId());
        userTaskAttention.setState(1);
        List<UserTaskAttention> userTaskAttentionList = userTaskAttentionService.selectUserTaskAttentionList(userTaskAttention);
        if (userTaskAttentionList.size() > 0) {
            return AjaxResult.error("您已经关注！");
        } else {
            return toAjax(userTaskAttentionService.insertUserTaskAttention(userTaskAttention));
        }

    }

    /**
     * 修改关注任务
     *
     * @param userId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
        UserTaskAttention userTaskAttention = userTaskAttentionService.selectUserTaskAttentionById(userId);
        mmap.put("userTaskAttention", userTaskAttention);
        return prefix + "/edit";
    }

    /**
     * 修改保存关注任务
     *
     * @param userTaskAttention
     * @return
     */
    @RequiresPermissions("system:attention:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(UserTaskAttention userTaskAttention) {
        return toAjax(userTaskAttentionService.updateUserTaskAttention(userTaskAttention));
    }

    /**
     * 删除关注任务
     *
     * @param ids
     * @return
     */
    @RequiresPermissions("system:attention:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(userTaskAttentionService.deleteUserTaskAttentionByIds(ids));
    }

    /**
     * 取消对任务的关注
     *
     * @param u
     * @return
     */
    @Log(title = "关注模块:任务关注", businessType = BusinessType.DELETE)
    @ResponseBody
    @PostMapping("/deleteUserAttention")
    public AjaxResult deleteUserAttention(UserTaskAttention u) {
        int a = userTaskAttentionService.deleteUserAttention(u);
        return toAjax(a);
    }

}
