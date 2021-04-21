package com.ruoyi.web.util;


import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * @author zhongzhilong
 * @date 2020/10/8 0008
 */
public class JsonObjectUtil {
    public static JSONObject getRequestJsonObject(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        BufferedReader in;
        try {
            in = request.getReader();
            StringBuffer bf = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                bf.append(line);
            }
            String data = bf.toString();
            if (data != null && data.length() > 0) {
                json = JSONObject.parseObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
