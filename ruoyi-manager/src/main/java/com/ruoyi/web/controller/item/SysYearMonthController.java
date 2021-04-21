package com.ruoyi.web.controller.item;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.web.domain.SysYearMonth;
import com.ruoyi.web.service.item.SysYearMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zhongzhilong
 * @date 2020/9/15 0015
 */
@Controller
@RequestMapping("/yearmonth")
@Api(value = "SysYearMonthController", tags = "获取日期相关接口")
public class SysYearMonthController extends BaseController {


    @Autowired
    private SysYearMonthService sysYearMonthService;

    @ResponseBody
    @PostMapping("/insertDate")
    public int insertDate(SysYearMonth sysYearMonth) {
        return sysYearMonthService.insertDate(sysYearMonth);
    }

    @ResponseBody
    @PostMapping("selectInfos")
    public List<SysYearMonth> selectInfos(SysYearMonth sysYearMonth) {
        return sysYearMonthService.selectInfos(sysYearMonth);
    }

    @ResponseBody
    @PostMapping("test")
    @ApiOperation("测试接口")
    public List<String> selectMonthList(int year) {
        return sysYearMonthService.selectMonthList(year);
    }
}
