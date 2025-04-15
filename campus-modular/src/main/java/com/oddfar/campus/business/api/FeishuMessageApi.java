package com.oddfar.campus.business.api;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeishuMessageApi {
    private final static String BOT_TOKEN = "1ab201d5-7430-4098-9d5b-8185795778c3";


    public static void sendMessage(String message) {
        try {
            String url = "https://open.feishu.cn/open-apis/bot/v2/hook/" + BOT_TOKEN;
            JSONObject content = new JSONObject();
            content.put("text", "【提醒】" + message);
            JSONObject params = new JSONObject();
            params.put("msg_type", "text");
            params.put("content", content);
            HttpUtil.post(url, JSONUtil.toJsonStr(params));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
