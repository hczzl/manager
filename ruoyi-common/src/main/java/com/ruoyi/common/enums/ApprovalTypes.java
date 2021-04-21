package com.ruoyi.common.enums;

/**
 * 用户状态
 *
 * @author zzl
 */
public enum ApprovalTypes {
    //审核状态:1已提交,2审批中,3审批不通过，4审批通过,5审批全部通过，6转推中，7：已撤销,8:审批中
    NOT_APPROVAL(2, "审批中"),
    APPROVAL_NOT_PASS(3, "审批不通过");

    private final int state;
    private final String info;

    ApprovalTypes(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return info;
    }
}
