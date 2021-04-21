package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.ProduceType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * mapper接口
 *
 * @author sanglili
 * @date 2020/9/10
 */
@Component
public interface ProduceTypeMapper {
    /**
     * @return
     */
    List<Integer> selectproductypeidlist();

    /**
     * @param id
     * @return
     */
    String getprojectmanagerbyid(Integer id);

    /**
     * @return
     */
    List<ProduceType> selectproducetype();

    /**
     * @param userId
     * @return
     */
    Integer selectTypeByUserId(String userId);

    /**
     * 获取所有的产品id及产品线名称
     *
     * @return
     */
    List<Map<String, Object>> selectProduceTypeList();
}
