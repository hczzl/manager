package com.ruoyi.web.controller.excel.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhongzhilong
 * @date 2019/9/8
 */
public class DictData implements Serializable {
    private static final long serialVersionUID = -1955155854992815269L;
    public static Map<String, Object> dict = null;

    /**
     * 获取数据字典中的值
     */
    public static Object getDict(String dict) {
        return DictData.dict.get(dict);
    }

    /**
     * 添加数据字典中的值
     */
    public static void setDict(Map<String, Object> map) {
        DictData.dict = map;
    }
}
