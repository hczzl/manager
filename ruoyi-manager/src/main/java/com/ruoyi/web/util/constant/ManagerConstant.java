package com.ruoyi.web.util.constant;

/**
 * 任务、项目、审批模块常量工具类
 *
 * @author zhongzhilong
 * @date 2020/10/16 0016
 */
public class ManagerConstant {
    /**
     * 任务模块
     */
    public static final String TASK_TABLE = "task_table";
    public static final String TASK_NAME = "TaskTable";
    /**
     * 任务完成标识,-1:提交,0:未完成,1:任务完成,2:任务中止,5:任务完成审批中,6:任务中止审批中,7:任务变更审批中,8:任务撤回
     */
    public static final String TASK_NOT_APPROVAL = "-1";
    public static final String TASK_APPROVAL = "0";
    public static final String TASK_FINISH = "1";
    public static final String TASK_NOT_FINISH = "5";
    public static final String TASK_STOP = "2";
    public static final String TASK_NOT_STOP = "6";
    public static final String TASK_CHANGE = "7";
    public static final String TASK_RECALL = "8";
    /**
     * 允许转推的最大次数
     */
    public static final Integer TASK_MAX_PUSH = 50;
    /**
     * 无顺延
     */
    public static final String TASK_NOT_PUTOFF = "13";
    /**
     * 顺延
     */
    public static final String TASK_PUTOFF = "14";

    /**
     * 项目模块
     */
    public static final String PROJECT_TABLE = "project_table";
    /**
     * 已完成
     */
    public static final String PROJECT_FINISH = "1";
    /**
     * 完成审批中
     */
    public static final String PROJECT_NOT_FINISH = "-1";
    /**
     * 已中止
     */
    public static final String PROJECT_STOP = "2";
    /**
     * 中止审批中
     */
    public static final String PROJECT_NOT_STOP = "-2";
    /**
     * 立项中
     */
    public static final String PROJECT_NOT_ESTABLISH = "0";
    /**
     * 立项成功
     */
    public static final String PROJECT_ESTABLISH_SUCCESS = "1";
    /**
     * 立项失败
     */
    public static final String PROJECT_ESTABLISH_FAIL = "2";
    /**
     * 再次提交
     */
    public static final String PROJECT_ESTABLISH_SUMMIT = "3";
    /**
     * 项目撤回
     */
    public static final String PROJECT_ESTABLISH_QUASH = "4";
    /**
     * 科研项目
     */
    public static final Integer PROJECT_RESEARCH = 0;
    /**
     * 市场项目
     */
    public static final Integer PROJECT_MARKET = 1;

    /**
     * 审批模块
     */
    public static final String RESCINDED = "已撤销";
    public static final String NOT_APPROVAL = "审批中";
    /**
     * 审核状态:1已提交,2审批中,3审批不通过，4审批通过,5审批全部通过，6转推中，7：已撤销,8:审批中
     */
    public static final Integer APPROVAL_UNDER_APPROVAL = 2;
    public static final Integer APPROVAL_FAILURE = 3;
    public static final Integer APPROVAL_PASS = 4;
    public static final Integer APPROVAL_SUCCESS = 5;
    public static final Integer APPROVAL_PUSH = 6;
    public static final Integer APPROVAL_RESCINDED = 7;
    public static final Integer APPROVAL_NOT_FINISH = 8;
    /**
     * 审批类型: 1发起任务,3市场立项,7科研立项,8任务中止,9任务完成,10市场项目中止,11市场项目完成,12任务变更,13科研项目中止,14科研项目完成
     */
    public static final Integer APPROVAL_CREATE_TASK = 1;
    public static final Integer APPROVAL_CREATE_MARKET_PROJECT = 3;
    public static final Integer APPROVAL_CREATE_RESEARCH_PROJECT = 7;
    public static final Integer APPROVAL_TASK_BREAK = 8;
    public static final Integer APPROVAL_TASK_FINISH = 9;
    public static final Integer APPROVAL_MARKET_PROJECT_BREAK = 10;
    public static final Integer APPROVAL_MARKET_PROJECT_FINISH = 11;
    public static final Integer APPROVAL_TASK_CHANGE = 12;
    public static final Integer APPROVAL_RESEARCH_PROJECT_BREAK = 13;
    public static final Integer APPROVAL_RESEARCH_PROJECT_FINISH = 14;
    /**
     * 审批通过
     */
    public static final Integer APPROVAL_TYPE_PASS = 1;
    /**
     * 审批不通过
     */
    public static final Integer APPROVAL_TYPE_REFUSE = 0;
    /**
     * 审批转推
     */
    public static final Integer APPROVAL_TYPE_PUSH = 2;
    /**
     * 属于项目的审批类型
     */
    public static final Integer[] PROJECT_AUDIT_TYPE = {3, 7, 10, 11, 13, 14};
    /**
     * 属于任务的审批类型
     */
    public static final Integer[] TASK_AUDIT_TYPE = {1, 8, 9, 12};
    /**
     * 消息模块
     */
    /**
     * 任务到期、逾期、未开始提醒
     */
    public static final Integer MSG_TASK = 4;
    /**
     * 审批提醒
     */
    public static final Integer MSG_APPROVAL = 3;
    /**
     * 填写通知提醒
     */
    public static final Integer MSG_WRITE = 7;
    /**
     * 拒批提醒
     */
    public static final Integer MSG_NOT_APPROVAL = 10;
    /**
     * 抄送提醒
     */
    public static final Integer MSG_COPY = 11;
    /**
     * 项目无进展提醒
     */
    public static final Integer MSG_NO_PROGRESS = 12;

    /**
     * 公共
     */
    public static final String IS_NULL = "";
    /**
     * 每月最大任务周期
     */
    public static final Integer MAX_PERIOD = 25;

}
