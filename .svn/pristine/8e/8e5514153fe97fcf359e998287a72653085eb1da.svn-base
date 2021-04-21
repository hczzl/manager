package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.ProduceType;
import com.ruoyi.web.mapper.item.ProduceTypeMapper;
import com.ruoyi.web.service.item.ProduceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProduceTypeImpl implements ProduceTypeService {
    @Autowired
    private ProduceTypeMapper produceTypeMapper;

    @Override
    public List<Integer> selectproductypeidlist() {
        return produceTypeMapper.selectproductypeidlist();
    }

    @Override
    public String getprojectmanagerbyid(Integer id) {
        return produceTypeMapper.getprojectmanagerbyid(id);
    }

    @Override
    public List<ProduceType> selectproducetype() {
        List<ProduceType> produceTypeList = produceTypeMapper.selectproducetype();
        return produceTypeList;
    }

    @Override
    public List<Map<String, Object>> selectProduceTypeList() {
        return produceTypeMapper.selectProduceTypeList();
    }
}
