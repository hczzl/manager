package com.ruoyi.common.core.domain;

import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 操作消息提醒
 *
 * @author ruoyi
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";

    public static final String MSG_TAG = "msg";

    public static final String DATA_TAG = "data";

    /**
     * 状态类型
     */
    public enum Type {
        /**
         * 成功
         */
        SUCCESS(0),
        /**
         * 警告
         */
        WARN(301),
        /**
         * 错误
         */
        ERROR(500),
        /**
         * 审批不存在
         */
        APPROVAL_DOES_NOT_EXIST(4001),
        /**
         * 号码不存在
         */
        NUMBER_DOES_NOT_EXIST(4002),
        /**
         * 禁止用户进行项目完成、项目中止
         */
        NOT_OPEARTION(4003),
        /**
         * 失败信息(前端根据该code，自动将后台返回的错误信息显示到前端界面)
         */
        FAILURE(4004);

        /**
         * value值
         */
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    /**
     * 状态类型
     */
    private Type type;

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private Object data;

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     */
    public AjaxResult(Type type, String msg) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param type 状态类型
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(Type type, String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        super.put(DATA_TAG, data);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(Type.SUCCESS, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult warn(String msg) {
        return AjaxResult.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult warn(String msg, Object data) {
        return new AjaxResult(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }

    public static AjaxResult numberNotExist() {
        return AjaxResult.numberNotExist("号码不存在");
    }

    public static AjaxResult approvalNotExist() {
        return AjaxResult.approvalNotExist("该审批不存在,正在为您更新列表");
    }

    public static AjaxResult noEdit() {
        return AjaxResult.noEdit("无权修改管理员用户类型");
    }

    public static AjaxResult notOperation() {
        return AjaxResult.notOperation("无法进行项目完成或者项目中止操作");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    public static AjaxResult numberNotExist(String msg) {
        return AjaxResult.numberNotExist(msg, null);
    }

    public static AjaxResult approvalNotExist(String msg) {
        return AjaxResult.approvalNotExist(msg, null);
    }

    public static AjaxResult noEdit(String msg) {
        return AjaxResult.noEdit(msg, null);
    }

    public static AjaxResult notOperation(String msg) {
        return AjaxResult.notOperation(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(Type.ERROR, msg, data);
    }

    public static AjaxResult numberNotExist(String msg, Object data) {
        return new AjaxResult(Type.NUMBER_DOES_NOT_EXIST, msg, data);
    }

    public static AjaxResult approvalNotExist(String msg, Object data) {
        return new AjaxResult(Type.APPROVAL_DOES_NOT_EXIST, msg, data);
    }

    public static AjaxResult notOperation(String msg, Object data) {
        return new AjaxResult(Type.NOT_OPEARTION, msg);
    }

    public static AjaxResult noEdit(String msg, Object data) {
        return new AjaxResult(Type.ERROR, msg, data);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 返回处理失败的信息
     *
     * @param msg
     * @return
     */
    public static AjaxResult failure(String msg) {
        return AjaxResult.failure(msg, null);
    }

    public static AjaxResult failure(String msg, Object data) {
        return new AjaxResult(Type.FAILURE, msg, data);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("code", getCode())
                .append("msg", getMsg())
                .append("data", getData())
                .toString();
    }
}
