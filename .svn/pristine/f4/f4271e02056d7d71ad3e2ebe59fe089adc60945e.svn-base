package com.ruoyi.web.controller.item;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.web.domain.SysCompetitor;
import com.ruoyi.web.service.item.SysCompetitorService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ruoyi
 * @date 2019-08-20
 */
@Controller
@RequestMapping("/syscompete")
@Api(value = "SysCompetitorController", tags = "竞争对手相关接口")
public class SysCompetitorController extends BaseController {

    @Autowired
    private SysCompetitorService sysCompetitorService;

    /**
     * 查询竞争对手
     *
     * @param sysCompetitor
     * @return
     */
    @PostMapping("/selectCompete")
    @ResponseBody
    public List<SysCompetitor> selectCompete(SysCompetitor sysCompetitor) {
        return sysCompetitorService.selectCompete(sysCompetitor);
    }

    /**
     * 更心竞争对手
     * @param sysCompetitor
     * @return
     */
    @PostMapping("updateCompete")
    @ResponseBody
    public int updateCompete(SysCompetitor sysCompetitor) {
        return sysCompetitorService.updateCompete(sysCompetitor);
    }
}
