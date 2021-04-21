package com.ruoyi.web.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * springboot对Double类型保留两位小数返回处理
 *
 * @author zhongzhilong
 * @date 2019/8/8
 */
public class CustomerDoubleSerialize extends JsonSerializer<Double> {
    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void serialize(Double arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
        if (arg0 != null) {
            arg1.writeString(df.format(arg0));
        }
    }

}
