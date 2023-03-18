package com.anzai.chatgptbot.service;

import com.alibaba.fastjson.*;
import com.anzai.chatgptbot.config.BaseConfig;
import com.anzai.chatgptbot.model.GPTMessageDTO;
import com.anzai.chatgptbot.model.GPTResponseDTO;
import com.anzai.chatgptbot.utils.CacheUtil;
import com.anzai.chatgptbot.utils.OkHttpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * api服务类
 *
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
     * 使用GPT3.5模型
     * 接口文档：https://platform.openai.com/docs/api-reference/chat/create
     *
     * @return
     */
    public String chatToGPT(String text, String fromUser) {
        // 构造请求
        Map<String, String> header = getGPTHeader();
        List<GPTMessageDTO> msgs = getMsgs(text, fromUser);
        Map<String, Object> body = getGPTRequestBody(msgs);
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON.toJSONString(body), mediaType);

        // 请求接口，并返回信息
        String response;
        try {
            response = OkHttpUtil.post(COMPLETION_URL, requestBody, header);
        } catch (Exception e) {
            return "哎呀，网络不好，请稍后重试";
        }

        GPTResponseDTO gptResponse = transformGPTResponse(response);
        GPTMessageDTO message = gptResponse.getMessage();

        // 对话次数校验，毕竟对话次数越多，消耗的token也会增多
        if (msgs.size() > baseConfig.getMaxChatNum()) {
            // 清空之前的消息记录
            CacheUtil.clearGptCache(fromUser);
            return message.getContent() + "\n\n【注意咯】连续对话超过" + baseConfig.getMaxChatNum() + "次了，累了，下一次提问将开启新的对话";
        } else if (baseConfig.isContinuous()) {
            // 将本次对话信息添加到缓存列表
            msgs.add(message);
            CacheUtil.setGptCache(fromUser, msgs);
        }
        return message.getContent();
    }

    /**
     * 从响应值里解析返回的对话内容
     *
     * @param response
     * @return
     */
    private GPTResponseDTO transformGPTResponse(String response) {
        GPTResponseDTO responseDTO = new GPTResponseDTO();
        JSONObject responseJson = JSONObject.parseObject(response);
        JSONArray choices = responseJson.getJSONArray("choices");
        JSONObject choiceJson = (JSONObject) choices.get(0);
        GPTMessageDTO message = JSON.parseObject(choiceJson.getString("message"), GPTMessageDTO.class);
        responseDTO.setMessage(message);
        responseDTO.setUsage(responseJson.getJSONObject("usage"));
        return responseDTO;
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
            msgs = CacheUtil.getGptCache(fromUser);
        }
        // 将本次用户的消息追加到message列表里
        GPTMessageDTO gptMessageDTO = new GPTMessageDTO();
        gptMessageDTO.setRole("user");
        gptMessageDTO.setContent(text);
        msgs.add(gptMessageDTO);
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
        // 指定为gpt-3.5-turbo模型
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
        // 自己账户申请的APIkey
        header.put("Authorization", "Bearer " + baseConfig.getApiKey());
        header.put("Content-Type", "application/json");
        return header;
    }

}
