package com.ruoyi.common.utils.qcloudsms;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.ruoyi.common.config.Global;

import java.io.IOException;

/**
 * 短信工具类
 * create by wcc on 2020-03-02
 */
public class SmsUtil {
    /**
     * 短信平台appId
     */
    private static String APP_ID = Global.getConfig("sms.appId");
    /**
     * 短信平台appKey
     */
    private static String APP_KEY = Global.getConfig("sms.appKey");
    /**
     * 短信模板id
     */
    private static String TEMPLATE_ID = Global.getConfig("sms.templateId");
    /**
     * 签名
     */
    private static String SMS_SIGN = Global.getConfig("sms.smsSign");
    /**
     * 验证码位数
     */
    private static int digit = 4;

    /**
     * 发送短信
     *
     * @param prefix
     * @param params
     * @param phoneNumber
     * @param templateId
     * @return
     */
    public static SmsSingleSenderResult smsSingleSender(String prefix, String[] params, String phoneNumber, int templateId) {
        SmsSingleSenderResult result = null;
        try {
            SmsSingleSender sender = new SmsSingleSender(Integer.parseInt(APP_ID), APP_KEY);
            result = sender.sendWithParam(prefix, phoneNumber,
                    templateId, params, SMS_SIGN, "", "");
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param context
     */
    public static void sendMsg(String phoneNumber, String context) {
        String[] contexts = {context};
        SmsSingleSenderResult sms = smsSingleSender("86", contexts, phoneNumber, Integer.parseInt(TEMPLATE_ID));
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber
     */
    public static String sendVerificationCode(String phoneNumber) {
        String code = getVerificationCode(digit);
        sendMsg(phoneNumber, code);
        return code;
    }

    /**
     * 获取验证码
     *
     * @param digit 验证码位数
     * @return
     */
    public static String getVerificationCode(int digit) {
        String x = "1";
        for (int i = 1; i < digit; i++) {
            x += "0";
        }
        int random = (int) ((Math.random() * 9 + 1) * Integer.parseInt(x));
        return String.valueOf(random);
    }

    public static void main(String[] args) {
        sendVerificationCode("13558033600");
    }
}
