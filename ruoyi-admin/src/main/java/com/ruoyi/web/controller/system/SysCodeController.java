package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.domain.SysCode;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/system/code")
public class SysCodeController extends BaseController {

    @Autowired
    private ISysCodeService sysCodeService;

    @PostMapping("insert")
    @ResponseBody
    public int insert() {
        SysCode sysCode = new SysCode();
        sysCode.setWxId("22S");
        sysCode.setOpenId("222");
        int a = sysCodeService.insertCoded(sysCode);
        return a;
    }
    @PostMapping("select")
    @ResponseBody
    public String list(SysCode sysCode){
        sysCode.setWxId("001kmZX10L32JK1VQoY10NjNX10kmZXc");
        return sysCodeService.selectwxId(sysCode);
    }
}