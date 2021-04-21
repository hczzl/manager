package com.ruoyi.web.controller.item;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.UserProjectAttention;
import com.ruoyi.web.service.item.UserProjectAttentionService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 关注任务Controller
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Controller
@RequestMapping("/userprojectattention")
@Api(value = "UserProjectAttentionController", tags = "项目关注相关接口")
public class UserProjectAttentionController extends BaseController {
    private String prefix = "system/attention";

    @Autowired
    private UserProjectAttentionService userProjectAttentionService;

    /**
     * 查询关注项目
     *
     * @param userProjectAttention
     * @return
     */
    @RequiresPermissions("system:attention:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserProjectAttention userProjectAttention) {
        startPage();
        List<UserProjectAttention> list = userProjectAttentionService.selectUserProjectAttentionList(userProjectAttention);
        return getDataTable(list);
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
     * 新增保存关注项目
     *
     * @param userProjectAttention
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(UserProjectAttention userProjectAttention) {
        // 传入userId
        userProjectAttention.setUserId(ShiroUtils.getUserId());
        List<UserProjectAttention> list = userProjectAttentionService.selectUserProjectAttentionList(userProjectAttention);
        if (list.size() > 0) {
            return AjaxResult.error("您已经关注！");
        } else {
            userProjectAttention.setState(1);
            int a = userProjectAttentionService.insertUserProjectAttention(userProjectAttention);
            return toAjax(a);
        }

    }

    /**
     * 取消关注
     *
     * @param u
     * @return
     */
    @ResponseBody
    @PostMapping("/deleteUserAttention")
    public AjaxResult deleteUserAttention(UserProjectAttention u) {
        int a = userProjectAttentionService.deleteUserAttention(u);
        return toAjax(a);
    }


}
