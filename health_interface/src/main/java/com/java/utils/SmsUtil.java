package com.java.utils;

import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 合一云短信工具类
 */
public class SmsUtil {

    public static String ACCESS_KEY_ID = "PCBbuzbeVd2KAJuAzZVUDFJLKSNBoQCyCLYQfLAwMsbTWRRa9";
    private static String ACCESS_KEY_SECRET = "your access key secret";

    public static void sendShortMessage(String code, String telephone){
        // 初始化
        Uni.init(ACCESS_KEY_ID); // 若使用简易验签模式仅传入第一个参数即可

        // 设置自定义参数 (变量短信)
        Map<String, String> templateData = new HashMap<String, String>();
        templateData.put("code",code);
        templateData.put("ttl","3");

        // 构建信息
        UniMessage message = UniSMS.buildMessage()
                .setTo(telephone)
                .setSignature("chkly")
                .setTemplateId("pub_verif_ttl4")
                .setTemplateData(templateData);

        // 发送短信
        try {
            UniResponse res = message.send();
            System.out.println(res);
        } catch (UniException e) {
            System.out.println("Error: " + e);
            System.out.println("RequestId: " + e.requestId);
        }
    }
}


