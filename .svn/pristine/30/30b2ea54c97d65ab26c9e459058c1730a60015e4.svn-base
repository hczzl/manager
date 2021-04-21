package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.domain.SysMenu;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysMenuService;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页 业务处理
 *
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController {
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysUserService userService;

    /**
     * 系统首页
     *
     * @return
     */
    @GetMapping("/index")
    @ResponseBody
    public ModelMap index() {
        ModelMap mmap = new ModelMap();
        // 取身份信息
        /* SysUser user = ShiroUtils.getSysUser();*/
        SysUser user = userService.selectUserById(1L);
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return mmap;
    }

    /**
     * 系统介绍
     *
     * @param mmap
     * @return
     */
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "main";
    }
}
