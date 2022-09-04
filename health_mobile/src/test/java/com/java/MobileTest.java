package com.java;

import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import com.java.utils.ValidateCodeUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MobileTest {

    public static String ACCESS_KEY_ID = "PCBbuzbeVd2KAJuAzZVUDFJLKSNBoQCyCLYQfLAwMsbTWRRa9";
    private static String ACCESS_KEY_SECRET = "your access key secret";

    @Test
    public void test1(){

            // 初始化
            Uni.init(ACCESS_KEY_ID); // 若使用简易验签模式仅传入第一个参数即可

            // 设置自定义参数 (变量短信)
            Map<String, String> templateData = new HashMap<String, String>();

            Integer i = ValidateCodeUtil.generateValidateCode(6);
            templateData.put("code",String.valueOf(i));
            templateData.put("ttl","3");

            // 构建信息
            UniMessage message = UniSMS.buildMessage()
                    .setTo("18327871761")
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

