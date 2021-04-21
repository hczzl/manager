package com.ruoyi.web.util;

/**
 * 结果集响应代码
 *
 * @author zhongzhilong
 */
public enum ResultEnum {
    UNKNOWN_ERROR(-1, "未知错误"),
    SUCCESS(10000, "成功"),
    USER_NOT_EXIST(1, "用户不存在"),
    USER_IS_EXISTS(2, "用户已存在"),
    DATA_IS_NULL(3, "数据为空"),
    ;
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
