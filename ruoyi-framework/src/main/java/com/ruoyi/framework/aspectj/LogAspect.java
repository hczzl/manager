package com.ruoyi.framework.aspectj;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.ruoyi.framework.util.JDBCUtile;
import com.ruoyi.common.utils.MyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessStatus;
import com.ruoyi.common.json.JSON;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.domain.SysUser;

/**
 * 操作日志记录处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.ruoyi.common.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // 获取当前的用户
            SysUser currentUser = ShiroUtils.getSysUser();

            // *========数据库日志=========*//
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = ShiroUtils.getIp();
            operLog.setOperIp(ip);

            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            if (currentUser != null) {
                operLog.setOperName(currentUser.getUserName());
                if (StringUtils.isNotNull(currentUser.getDept())
                        && StringUtils.isNotEmpty(currentUser.getDept().getDeptName())) {
                    operLog.setDeptName(currentUser.getDept().getDeptName());
                }
            }

            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            //设置详情
            getMethodLogInfo(controllerLog, operLog);
            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog, operLog);
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(Log log, SysOperLog operLog) throws Exception {
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(operLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(SysOperLog operLog) throws Exception {
        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
        String params = JSON.marshal(map);
        operLog.setOperParam(StringUtils.substring(params, 0, 2000));
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 设置日志操作的详情
     *
     * @return
     */
    private void getMethodLogInfo(Log log, SysOperLog operLog) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String info = "";
        String beOperObject = "";
        String title = log.title();
        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
        System.out.println();
        //任务相关
        if ("发起任务".equals(title)) {
            String taskTitle = map.get("taskTitle")[0];
            String startTime = sf.format(new Date(Long.parseLong(map.get("startTimes")[0])));
            String endTime = sf.format(new Date(Long.parseLong(map.get("endTimes")[0])));
            info = operLog.getOperName() + " 发起了 任务名称为 " + taskTitle + " 的任务 " + "开始时间:" + startTime + ",结束时间:" + endTime;
        }
        if ("变更任务".equals(title)) {
            String flowld = map.get("flowId")[0];
            String startTime = map.get("startTime")[0];
            String endTime = map.get("endTime")[0];
            String tId = map.get("tId")[0];
            String taskTitle = JDBCUtile.getTaskTitleByTid(tId);
            switch (flowld) {
                case "12":
                    info = operLog.getOperName() + " 对 `名称:" + taskTitle + "` 的任务发起了 `任务延期`," + "开始时间:" + startTime + ",结束时间:" + endTime;
                    break;
                case "13":
                    info = operLog.getOperName() + " 对 `名称`" + taskTitle + "` 的任务发起了 `任务变更`," + "开始时间:" + startTime + ",结束时间:" + endTime;
                    break;
                case "14":
                    info = operLog.getOperName() + " 对 `名称`" + taskTitle + "` 的任务发起了 `任务顺延`," + "开始时间:" + startTime + ",结束时间:" + endTime;
                    break;
                default:
                    break;
            }
        }
        if ("任务中止".equals(title)) {
            String tId = map.get("tId")[0];
            String taskTitle = JDBCUtile.getTaskTitleByTid(tId);
            info = operLog.getOperName() + " 对 `名称:" + taskTitle + "` 的任务发起了 `任务中止`";
        }
        if ("批量任务导入".equals(title)) {
            info = operLog.getOperName() + " 执行了 `批量任务导入` 操作";
        }
        if ("提交进度".equals(title)) {
            String tId = map.get("tId")[0];
            String taskTitle = JDBCUtile.getTaskTitleByTid(tId);
            info = operLog.getOperName() + " 对任务: " + taskTitle + " 提交了进度," + "进度:" + map.get("planRate")[0] + "%";
        }

        if ("新增项目".equals(title)) {
            String taskTitle = map.get("title")[0];
            String startTime = map.get("startTime")[0];
            String endTime = map.get("endTime")[0];
            info = operLog.getOperName() + " 发起了 项目名称为 " + taskTitle + " 的项目 " + "开始时间:" + startTime + ",结束时间:" + endTime;
        }
        //用户相关
        if ("用户管理:重置密码".equals(title)) {
            String userName = JDBCUtile.getUserNameByid(map.get("userId")[0]);
            info = operLog.getOperName() + " 重置了 " + userName + " 的用户密码";
        }
        if ("用户管理:修改密码".equals(title)) {
            String userName = JDBCUtile.getUserNameByid(map.get("userId")[0]);
            info = "用户: " + operLog.getOperName() + " 修改了用户密码";
        }
        if ("用户管理:新增用户".equals(title)) {
            String userName = map.get("userName")[0];
            info = operLog.getOperName() + " 新增了一个,名为：" + userName + " 的用户";
        }
        if ("用户管理:用户编辑".equals(title)) {
            SysUser user = (SysUser) MyUtils.getMapCache(ShiroUtils.getUserId() + "operLogInfo");
            if (user != null) {
                info = ShiroUtils.getSysUser().getUserName() + " 修改了 " + user.getUserName() + " 的用户信息";
            }
        }
        if ("用户管理:删除用户".equals(title)) {
            String userName = JDBCUtile.getUserNameByid(map.get("ids")[0]);
            info = operLog.getOperName() + " 删除了一个,名为：" + userName + " 的用户";
        }

        //审批相关
        if ("提交审批".equals(title)) {
            String type = map.get("type")[0];
            String currentId = map.get("currentId")[0];
            String taskTitle = JDBCUtile.getAdultTaskTitleByCurrentId(currentId);
            //审核通过
            if ("0".equals(type)) {
                info = operLog.getOperName() + " 拒绝了审批单名称:" + taskTitle + ",审核不通过";
            } else if ("1".equals(type)) {
                info = operLog.getOperName() + " 通过了审批单名称:" + taskTitle + ",审核通过";
            } else {
                info = "";
            }

        }
        //里程碑相关
        if ("添加里程碑".equals(title)) {
            String planTtile = map.get("planTitle")[0];
            String projectTitle = JDBCUtile.getProjectNameById(map.get("projectId")[0]);
            info = operLog.getOperName() + " 在项目: " + projectTitle + " 中，添加了名为: " + planTtile + " 的里程碑";
        }
        if ("删除里程碑标题".equals(title)) {
            info = operLog.getOperName() + "删除了一个里程碑";
        }
        if ("审批单撤销".equals(title)) {
            String auditTitle = (String) MyUtils.getMapCache(ShiroUtils.getUserId() + "LogTitle");
            info = operLog.getOperName() + " 将名称为: " + auditTitle + " 的审批单进行撤销操作";
        }
        if ("立项列表删除项目".equals(title)) {
            String projectTitle = (String) MyUtils.getMapCache(ShiroUtils.getUserId() + "LogTitle");
            info = operLog.getOperName() + " 将名称为: " + projectTitle + " 的项目进行删除操作";
        }
        beOperObject = (String) MyUtils.getMapCache(ShiroUtils.getUserId() + "LogObject");
        if (beOperObject != null) {
            operLog.setBeOperObject(beOperObject);
            MyUtils.removeMapCache(ShiroUtils.getUserId() + "LogObject");
            MyUtils.removeMapCache(ShiroUtils.getUserId() + "LogTitle");
        }
        if (info != null) {
            operLog.setInfo(info);
        }
    }
}
