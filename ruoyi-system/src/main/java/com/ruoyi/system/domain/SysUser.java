package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
public class SysUser extends BaseEntity {
    private static final long SERIAL_VERSION_UID = 1L;

    /**
     * 用户ID
     */
    @Excel(name = "用户序号", prompt = "用户编号")
    private Long userId;

    /**
     * 部门ID
     */
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    /**
     * 部门父ID
     */
    private Long parentId;

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 岗位ID
     */
    private Long postId;
    /**
     * 登录名称
     */
    @Excel(name = "登录名称")
    private String loginName;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称")
    private String userName;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phonenumber;

    /**
     * 用户性别
     */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 密码
     */
    private String password;

    /**
     * 要修改的新密码
     */
    private String newPassword;

    /**
     * 盐加密
     */
    private String salt;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登陆IP
     */
    @Excel(name = "最后登陆IP", type = Type.EXPORT)
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @Excel(name = "最后登陆时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /**
     * 部门对象
     */
    @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT)
    private SysDept dept;
    private String userSn;//成员编码


    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;
    /**
     * 部门对象
     */
    private List<SysUserPost> postList;
    /**
     * 特长
     */
    private String speciality;
    /**
     * 输入的审核类型
     */
    private String searchFlowId;

    private String rankId;

    private String rankName;

    private List<String> notbusyList;

    private List<TaskUserFree> taskUserFrees;
    /**
     * 审批类型
     */
    private int auditId;
    /**
     * 用户类型
     */
    private String userType;

    private String updateBy;

    private Date updateTime;

    private SysPost[] sysPost;
    /**
     * 发起任务数
     */
    private Integer creatTaskNum;
    /**
     * 任务天数
     */
    private String period;
    /**
     * 当月应工作天数
     */
    private Integer workDays;
    /**
     * 工作饱和度
     */
    private double saturation;

    private Integer year;

    private Integer month;

    private Double taskAverageScore;

    private Double multipleMonthScore;

    private Double monthScore;

    private Integer allow;

    private Integer[] ids;

    private String roleName;

    private SysUserRole sysUserRole;

    private Integer taskAlreadyFinish;

    private Integer taskNotFinish;

    private Integer taskOverdue;

    private Integer taskNotOverdue;

    private Double yearScore;

    private Double personalScore;

    private Double yearAverageScore;
    /**
     * openId，用来验证登录微信公众号
     */
    private String openId;
    /**
     * 验证码
     */
    private String verificationCode;
    /**
     * 验证码有效期限
     */
    private Date verificationPeriod;
    /**
     * 默认初始密码
     */
    private String initPassWord;
    /**
     * 非超级管理员查询用户列表时传该参数
     */
    private String notStatus;

    public String getNotStatus() {
        return notStatus;
    }

    public void setNotStatus(String notStatus) {
        this.notStatus = notStatus;
    }

    public String getInitPassWord() {
        return initPassWord;
    }

    public void setInitPassWord(String initPassWord) {
        this.initPassWord = initPassWord;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Double getMultipleMonthScore() {
        return multipleMonthScore;
    }

    public void setMultipleMonthScore(Double multipleMonthScore) {
        this.multipleMonthScore = multipleMonthScore;
    }

    public Double getMonthScore() {
        return monthScore;
    }

    public void setMonthScore(Double monthScore) {
        this.monthScore = monthScore;
    }

    public Integer getTaskNotOverdue() {
        return taskNotOverdue;
    }

    public void setTaskNotOverdue(Integer taskNotOverdue) {
        this.taskNotOverdue = taskNotOverdue;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getTaskAverageScore() {
        return taskAverageScore;
    }

    public void setTaskAverageScore(Double taskAverageScore) {
        this.taskAverageScore = taskAverageScore;
    }

    public SysUserRole getSysUserRole() {
        return sysUserRole;
    }

    public void setSysUserRole(SysUserRole sysUserRole) {
        this.sysUserRole = sysUserRole;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Integer workDays) {
        this.workDays = workDays;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public Integer getAllow() {
        return allow;
    }

    public void setAllow(Integer allow) {
        this.allow = allow;
    }

    public Integer getCreatTaskNum() {
        return creatTaskNum;
    }

    public void setCreatTaskNum(Integer creatTaskNum) {
        this.creatTaskNum = creatTaskNum;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public SysPost[] getSysPost() {
        return sysPost;
    }

    public void setSysPost(SysPost[] sysPost) {
        this.sysPost = sysPost;
    }

    @Override
    public String getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    //用户空闲时间接口
    //private List<TaskUserFree> taskUserFrees;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<TaskUserFree> getTaskUserFrees() {
        return taskUserFrees;
    }

    public void setTaskUserFrees(List<TaskUserFree> taskUserFrees) {
        this.taskUserFrees = taskUserFrees;
    }


    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public List<String> getNotbusyList() {
        return notbusyList;
    }

    public void setNotbusyList(List<String> notbusyList) {
        this.notbusyList = notbusyList;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    /**
     * 手动设置为管理员
     *
     * @param userId
     * @return
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public List<SysUserPost> getPostList() {
        return postList;
    }

    public void setPostList(List<SysUserPost> postList) {
        this.postList = postList;
    }

    public SysDept getDept() {
        if (dept == null) {
            dept = new SysDept();
        }
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }


    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getSearchFlowId() {
        return searchFlowId;
    }

    public void setSearchFlowId(String searchFlowId) {
        this.searchFlowId = searchFlowId;
    }

    public String getUserSn() {
        return userSn;
    }

    public void setUserSn(String userSn) {
        this.userSn = userSn;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Integer getTaskAlreadyFinish() {
        return taskAlreadyFinish;
    }

    public void setTaskAlreadyFinish(Integer taskAlreadyFinish) {
        this.taskAlreadyFinish = taskAlreadyFinish;
    }

    public Integer getTaskNotFinish() {
        return taskNotFinish;
    }

    public void setTaskNotFinish(Integer taskNotFinish) {
        this.taskNotFinish = taskNotFinish;
    }

    public Integer getTaskOverdue() {
        return taskOverdue;
    }

    public void setTaskOverdue(Integer taskOverdue) {
        this.taskOverdue = taskOverdue;
    }

    public Double getYearScore() {
        return yearScore;
    }

    public void setYearScore(Double yearScore) {
        this.yearScore = yearScore;
    }

    public Double getPersonalScore() {
        return personalScore;
    }

    public void setPersonalScore(Double personalScore) {
        this.personalScore = personalScore;
    }

    public Double getYearAverageScore() {
        return yearAverageScore;
    }

    public void setYearAverageScore(Double yearAverageScore) {
        this.yearAverageScore = yearAverageScore;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Date getVerificationPeriod() {
        return verificationPeriod;
    }

    public void setVerificationPeriod(Date verificationPeriod) {
        this.verificationPeriod = verificationPeriod;
    }


}
