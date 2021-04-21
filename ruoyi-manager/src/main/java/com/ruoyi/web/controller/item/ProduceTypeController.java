package com.ruoyi.web.controller.item;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.service.item.ProduceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zhongzhilong
 * @date 2020-11-18
 * @description 产品线控制器
 */
@RequestMapping("/produceType")
@RestController
@Api(value = "ProduceTypeController", tags = "产品线相关接口")
public class ProduceTypeController extends BaseController {
    @Autowired
    private ProduceTypeService produceTypeService;

    @ApiOperation("获取所有的产品线id及产品线名称")
    @PostMapping("/selectProduceTypeList")
    @ResponseBody
    public AjaxResult selectProduceTypeList() {
        try {
            List<Map<String, Object>> resultList = produceTypeService.selectProduceTypeList();
            return AjaxResult.success("查询成功", resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询失败");
        }
    }

}
