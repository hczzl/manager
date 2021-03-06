package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.qcloudsms.SmsUtil;
import com.ruoyi.framework.shiro.service.SysLoginService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.domain.SysCode;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysCodeService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Api("登录验证")
@Controller
public class SysLoginController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SysLoginController.class);
    /**
     * 禁用标志
     */
    public static final String STATUS = "1";

    @Autowired
    private SysLoginService loginService;
    @Autowired
    private ISysUserTokenService sysUserTokenService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysCodeService sysCodeService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        return "login";
    }

    @ApiOperation("登录")
    @PostMapping("/dologin")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password) {
        SysUser user = null;
        try {
            user = loginService.login(username, password);
            // statue=1，表示被禁用;del_flag=2,表示用户被删除
            if (user != null && user.getStatus().equals(STATUS)) {
                AjaxResult.error("该用户已被禁用");
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(user.getUserId());
            Integer roleId = sysUserTokenService.selectRoleId(sysUserRole);
            if (roleId == null) {
                roleId = 0;
            }
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getUserId());
            userMap.put("userName", user.getUserName());
            userMap.put("deptId", user.getDeptId());
            userMap.put("deptName", user.getDept().getDeptName());
            userMap.put("avatar", user.getAvatar());
            // 0-普通用戶，1是管理員，2-超级管理员
            userMap.put("userType", roleId);
            userMap.put("rankId", user.getRankId());
            Map<String, Object> tokenInfo = sysUserTokenService.createToken(user.getUserId());
            userMap.putAll(tokenInfo);
            MyUtils.putMapCache(username + "code", password);
            return AjaxResult.success("操作成功", userMap);
        } catch (CaptchaException e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            return error(e.getMessage());
        } catch (UserNotExistsException e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            return error(e.getMessage());
        } catch (UserPasswordNotMatchException e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            return error(e.getMessage());
        } catch (UserPasswordRetryLimitExceedException e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            return error(e.getMessage());
        } catch (UserBlockedException e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            return error(e.getMessage());
        } catch (RoleBlockedException e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            return error(e.getMessage());
        } catch (Exception e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            return error(e.getMessage());
        }
    }

    /**
     * 微信公众号登录
     *
     * @return
     */
    @ApiOperation("根据微信id来验证登录")
    @RequestMapping(value = "/dologinWxId", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxResult ajaxLoginByWxId(String wxCode) {
        //String wxId = "001kmZX10L32JK1VQoY10NjNX10kmZXc";
        SysUser user = new SysUser();
        try {
            Map<String, Object> userMap = new HashMap<>();
            SysUserRole sysUserRole = new SysUserRole();
            //根据微信id获取openId
            String openId = getOpenId(wxCode);
            //之后验证改用户是否存在着openId
            if (ShiroUtils.isNotNull(openId)) {
                user.setOpenId(openId);
                List<SysUser> users = userService.selectInfosByWxId(user);
                if (ShiroUtils.isNotEmpty(users)) {
                    for (SysUser u : users) {
                        sysUserRole.setUserId(u.getUserId());
                        Integer roleId = sysUserTokenService.selectRoleId(sysUserRole);
                        if (roleId == null) {
                            roleId = 0;
                        }
                        userMap.put("userId", u.getUserId());
                        userMap.put("userName", u.getUserName());
                        userMap.put("deptId", u.getDeptId());
                        userMap.put("deptName", u.getDept().getDeptName());
                        userMap.put("avatar", u.getAvatar());
                        // 0-普通用戶，1是管理員，2-超级管理员
                        userMap.put("userType", roleId);
                        userMap.put("rankId", u.getRankId());
                        //获取token
                        Map<String, Object> tokenInfo = sysUserTokenService.createToken(u.getUserId());
                        userMap.putAll(tokenInfo);
                    }
                    return AjaxResult.success("操作成功", userMap);
                }
                //将信息插入中间表里
                SysCode sysCode = new SysCode();
                sysCode.setWxId(wxCode);
                sysCode.setOpenId(openId);
                sysCodeService.insertCoded(sysCode);
            }
            return AjaxResult.error("登录失败");
        } catch (Exception e) {
            log.info("对用户[" + wxCode + "]进行登录验证..验证未通过{}", e.getMessage());
            return error(e.getMessage());
        }
    }

    @ApiOperation("根据用户电话来验证登录")
    @RequestMapping(value = "/dologinPhone", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxResult ajaxLoginByPhone(String phonenumber, String wxCode, String code) {
        //String phonenumber = "15277382876";
        //String wxId = "001kmZX10L32JK1VQoY10NjNX10kmZXc";
        SysUser user = new SysUser();
        try {
            Long currentMills = System.currentTimeMillis();
            Map<String, Object> userMap = new HashMap<>();
            SysUserRole sysUserRole = new SysUserRole();
            user.setPhonenumber(phonenumber);
            List<SysUser> users = userService.selectInfosByWxId(user);
            if (ShiroUtils.isNotEmpty(users)) {
                SysUser u = users.get(0);
                String userVerification = u.getVerificationCode();
                Date verificationPeriod = u.getVerificationPeriod();
                if (null == userVerification || (null != userVerification && !code.equals(userVerification))) {
                    return AjaxResult.error("验证码错误");
                }
                if (null == verificationPeriod || (null != verificationPeriod && currentMills > verificationPeriod.getTime())) {
                    return AjaxResult.error("请重新获取验证码");
                }
                if (code.equals(userVerification)) {
                    sysUserRole.setUserId(u.getUserId());
                    Integer roleId = sysUserTokenService.selectRoleId(sysUserRole);
                    if (roleId == null) {
                        roleId = 0;
                    }
                    userMap.put("userId", u.getUserId());
                    userMap.put("userName", u.getUserName());
                    userMap.put("deptId", u.getDeptId());
                    userMap.put("deptName", u.getDept().getDeptName());
                    userMap.put("avatar", u.getAvatar());
                    // 0-普通用戶，1是管理員，2-超级管理员
                    userMap.put("userType", roleId);
                    userMap.put("rankId", u.getRankId());
                    Map<String, Object> tokenInfo = sysUserTokenService.createToken(u.getUserId());
                    userMap.putAll(tokenInfo);
                    //将微信id插入到数据库表中
                    user.setUserId(u.getUserId());
                    user.setUpdateTime(new Date());
                    //user.setWxId(wxId);
                    SysCode sysCode = new SysCode();
                    sysCode.setWxId(wxCode);
                    //获得open_id
                    String openId = sysCodeService.selectwxId(sysCode);
                    if (ShiroUtils.isNotNull(openId)) {
                        user.setOpenId(openId);
                        int a = userService.updateWxId(user);
                        if (a == 0) {
                            return AjaxResult.error();
                        }
                    }
                    return AjaxResult.success("操作成功", userMap);
                }
                return AjaxResult.error("验证码错误");
            }
            return AjaxResult.numberNotExist();
        } catch (Exception e) {
            log.info("对用户[" + phonenumber + "]进行登录验证..验证未通过{}", e.getMessage());
            return AjaxResult.error();
        }
    }

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getVerificationCode", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxResult getVerificationCode(String phoneNumber) {
        try {
            Long currentMillis = System.currentTimeMillis();
            String verificationCode = SmsUtil.sendVerificationCode(phoneNumber);
            //String verificationCode = "1111";
            SysUser sysUser = userService.selectUserByPhoneNumber(phoneNumber);
            if (null != sysUser) {
                sysUser.setVerificationCode(verificationCode);
                //计算生成验证码后120秒对应的时间，用于判断验证码是否过期
                currentMillis += 120 * 1000;
                sysUser.setVerificationPeriod(new Date(currentMillis));
                userService.updateUser(sysUser);
            }
            return AjaxResult.success("发送成功");
        } catch (Exception e) {
            log.info("获取验证码失败", e.getMessage());
            return AjaxResult.error();
        }
    }


    @GetMapping("/unauth")
    public String unauth() {
        return "/error/unauth";
    }

    /**
     * 用户注销，用户退出
     */
    @PostMapping("/logout")
    @ResponseBody
    public AjaxResult logout(HttpSession session, SysUser sysUser) {
        try {
            int userId = sysUser.getUserId().intValue();
            sysUserTokenService.deleteTokenByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //清空session
        session.invalidate();
        return AjaxResult.success();
    }

    /**
     * 根据微信id获取到openId
     *
     * @param wxCode
     * @return
     */
    @GetMapping("/selectOpendId")
    @ResponseBody
    public String getOpenId(String wxCode) {
        //String code = "061PLoh80gu7hD1FUAg80nYdh80PLohI";
        //这2个值在开通公众号认证之后，可以在微信后台获取-固定的值
        String applyId = "wxf9acfe2e5cb258b7";
        String secret = "bab31c869df7fb047b2cf15eb07bd218";
        String openid = "";
        //微信接口，调用可以获得openId
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + applyId
                + "&secret=" + secret + "&code=" + wxCode + "&grant_type=authorization_code";
        //String aturl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + SECRET;
        try {
            //DefaultHttpClient client = new DefaultHttpClient();//过期的方法
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject jsonResult = new JSONObject(strResult);
                if (jsonResult != null) {
                    openid = (String) jsonResult.get("openid");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return openid;
    }

}
