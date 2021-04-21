package com.ruoyi.framework.util;

import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.shiro.realm.UserRealm;
import com.ruoyi.system.domain.SysUser;

import java.util.*;

/**
 * shiro 工具类
 *
 * @author ruoyi
 */
public class ShiroUtils {
    private static Integer chargeId;
    private static String participant;
    private static Integer flowId;
    private static Integer currentId;
    private static Integer taskScore;

    public static Integer getTaskScore() {
        return taskScore;
    }

    public static void setTaskScore(Integer taskScore) {
        ShiroUtils.taskScore = taskScore;
    }

    public static Integer getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(Integer currentId) {
        ShiroUtils.currentId = currentId;
    }

    public static Integer getFlowId() {
        return flowId;
    }

    public static void setFlowId(Integer flowId) {
        ShiroUtils.flowId = flowId;
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout() {
        getSubject().logout();
    }

    public static SysUser getSysUser() {
        SysUser user = null;
        Object obj = getSubject().getPrincipal();
        if (StringUtils.isNotNull(obj)) {
            user = new SysUser();
            BeanUtils.copyBeanProp(user, obj);
        }
        return user;
    }

    public static void setSysUser(SysUser user) {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    public static void clearCachedAuthorizationInfo() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm realm = (UserRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static Long getUserId() {
        return getSysUser().getUserId().longValue();
    }

    public static String getLoginName() {
        return getSysUser().getLoginName();
    }

    public static String getIp() {
        return getSubject().getSession().getHost();
    }

    public static String getSessionId() {
        return String.valueOf(getSubject().getSession().getId());
    }

    /**
     * 生成随机盐
     *
     * @return
     */
    public static String randomSalt() {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }

    public static Integer getChargeId() {
        return chargeId;
    }

    public static void setChargeId(Integer chargeId) {
        ShiroUtils.chargeId = chargeId;
    }

    public static String getParticipant() {
        return participant;
    }

    public static void setParticipant(String participant) {
        ShiroUtils.participant = participant;
    }

    public static void setValue(Integer chargeId, String participant, Integer flowId) {
        ShiroUtils.participant = participant;
        ShiroUtils.chargeId = chargeId;
        ShiroUtils.flowId = flowId;
    }

    /**
     * 判断集合是否为空
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List<?> list) {
        if (list != null && !list.isEmpty() && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     *
     * @param arr
     * @return
     */
    public static boolean isNotNull(String arr) {
        if (arr != null && arr.length() > 0 && arr != "") {
            return true;
        }
        return false;
    }

    public static boolean isNull(String arr) {
        if (arr == null || arr.length() < 1 || arr == "") {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty() || list.size() < 1) {
            return true;
        }
        return false;
    }

    /**
     * 字符串转整形
     *
     * @param arr
     * @return
     */
    public static Integer turnInteger(String arr) {
        if (ShiroUtils.isNotNull(arr)) {
            return Integer.parseInt(arr);
        }
        return 0;
    }

    /**
     * 把map的key转换成驼峰命名
     *
     * @param map
     * @return
     */
    public static Map<String, Object> toReplaceKeyLow(Map<String, Object> map) {
        Map reMap = new HashMap();
        if (reMap != null) {
            Iterator var2 = map.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) var2.next();
                reMap.put(underlineToCamel((String) entry.getKey()), map.get(entry.getKey()));
            }
            map.clear();
        }
        return reMap;
    }

    public static final char UNDERLINE = '_';

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(Character.toLowerCase(param.charAt(i)));
            }
        }
        return sb.toString();
    }
}
