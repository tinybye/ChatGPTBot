package com.anzai.chatgptbot.utils;

import com.anzai.chatgptbot.model.GPTMessageDTO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具
 *
 * @author tinybye
 * @date 2023/3/17
 */
public class CacheUtil {

    /**
     * 对话缓存，key为用名，value为一组对话的历史消息列表
     */
    private static final Cache<String, List<GPTMessageDTO>> CHAT_GPT_CACHE;

    static {
        // 设置超时时间
        CHAT_GPT_CACHE = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
    }

    /**
     * 更新消息列表
     *
     * @param userName
     * @param gptMessageDtos
     */
    public static void setGptCache(String userName, List<GPTMessageDTO> gptMessageDtos) {
        CHAT_GPT_CACHE.put(userName, gptMessageDtos);
    }

    /**
     * 获取用户的消息列表
     *
     * @param userName
     * @return
     */
    public static List<GPTMessageDTO> getGptCache(String userName) {
        List<GPTMessageDTO> messageDtos = CHAT_GPT_CACHE.getIfPresent(userName);
        if (CollectionUtils.isEmpty(messageDtos)) {
            messageDtos = Lists.newArrayList();
            GPTMessageDTO gptMessageDTO = new GPTMessageDTO();
            gptMessageDTO.setRole("system");
            gptMessageDTO.setContent("你是一个艺术家，你的名字叫小王");
            messageDtos.add(gptMessageDTO);
        }
        return messageDtos;
    }

    /**
     * 清空用户对应的对话缓存
     *
     * @param userName
     */
    public static void clearGptCache(String userName) {
        List<GPTMessageDTO> messageDtos = CHAT_GPT_CACHE.getIfPresent(userName);
        if (CollectionUtils.isNotEmpty(messageDtos)) {
            messageDtos.clear();
        }
    }
}
