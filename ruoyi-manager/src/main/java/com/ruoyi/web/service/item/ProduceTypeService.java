package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.ProduceType;

import java.util.List;
import java.util.Map;

public interface ProduceTypeService {
    List<Integer> selectproductypeidlist();

    /**
     * 根据id获取产品经理
     *
     * @param id
     * @return 结果
     */
    String getprojectmanagerbyid(Integer id);

    /**
     * @return
     */
    List<ProduceType> selectproducetype();

    /**
     * 获取所有的产品id及产品线名称
     *
     * @return
     */
    List<Map<String, Object>> selectProduceTypeList();
}
