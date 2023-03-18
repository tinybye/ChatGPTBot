package com.anzai.chatgptbot.service;

import com.alibaba.fastjson.*;
import com.anzai.chatgptbot.config.BaseConfig;
import com.anzai.chatgptbot.model.chatgpt.GPTMessageDTO;
import com.anzai.chatgptbot.utils.CacheHelper;
import com.anzai.chatgptbot.utils.OkHttpUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * api服务类
 * @author tinybye
 * @date 2023/3/17
 */
@Service
@Slf4j
public class ChatGPTService {
    private static final String COMPLETION_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    private BaseConfig baseConfig;


    /**
     * openai GPT 3.5 chat功能
     * 接口文档：https://platform.openai.com/docs/api-reference/chat/create
     *
     * @return
     */
    public String chatToGPT(String text, String fromUser) {
        log.info("调用GPT3.5模型对话, fromUser: {}, text: {}", fromUser, text);

        // 构造请求体
        Map<String, String> header = getGPTHeader();
        List<GPTMessageDTO> msgs = getMsgs(text, fromUser);
        Map<String, Object> body = getGPTRequestBody(msgs);
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON.toJSONString(body), mediaType);

        // 请求接口，并返回信息
        String response;
        try {
            response = OkHttpUtils.post(COMPLETION_URL, "", requestBody, header);
        } catch (Exception e) {
            return "哎呀，网络不好，请稍后重试";
        }

        if (StringUtils.isBlank(response)) {
            return "哎呀，网络不好，请稍后重试";
        }

        String content = getContentFromResponse(response);

        log.info("gptNewComplete content:{}", content);

        // 对话次数校验，毕竟对话次数越多，消耗的token也会增多
        if (msgs.size() > baseConfig.getMaxChatNum()) {
            CacheHelper.clearGptCache(fromUser);
            return content + "\n\n【注意咯】连续对话超过" + baseConfig.getMaxChatNum() + "次了，累了，请开启新的对话吧";
        } else if (baseConfig.isContinuous()) {
            // 将本次对话信息添加到缓存列表
            GPTMessageDTO asistantMsg = new GPTMessageDTO();
            asistantMsg.setRole("assistant");
            asistantMsg.setContent(content);
            msgs.add(asistantMsg);
            CacheHelper.setGptCache(fromUser,msgs);
        }
        return content;
    }

    /**
     * 从响应值里解析返回的对话内容
     *
     * @param response
     * @return
     */
    private String getContentFromResponse(String response) {
        JSONObject responseJson = JSONObject.parseObject(response);
        JSONArray choices = responseJson.getJSONArray("choices");
        JSONObject choiceJson = (JSONObject) choices.get(0);
        JSONObject message = (JSONObject) choiceJson.get("message");
        String content = (String) message.get("content");
        return content;
    }

    /**
     * 获取本次请求的消息记录
     *
     * @param text
     * @param fromUser
     * @return
     */
    private List<GPTMessageDTO> getMsgs(String text, String fromUser) {
        List<GPTMessageDTO> msgs = Lists.newArrayList();
        if (baseConfig.isContinuous()) {
            msgs = CacheHelper.getGptCache(fromUser);
        }
        GPTMessageDTO gptMessageDto = new GPTMessageDTO();
        gptMessageDto.setRole("user");
        gptMessageDto.setContent(text);
        msgs.add(gptMessageDto);
        return msgs;
    }

    /**
     * 初始化请求体
     *
     * @param msgs
     * @return
     */
    private Map<String, Object> getGPTRequestBody(List<GPTMessageDTO> msgs) {
        Map<String, Object> body = Maps.newHashMap();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", msgs);
        body.put("max_tokens", baseConfig.getMaxTokens());
        body.put("temperature", baseConfig.getTemperature());
        return body;
    }

    /**
     * 初始化请求头
     *
     * @return
     */
    private Map<String, String> getGPTHeader() {
        Map<String, String> header = Maps.newHashMap();
        header.put("Authorization", "Bearer " + baseConfig.getChatGptApiKey());
        header.put("Content-Type", "application/json");
        return header;
    }

}
