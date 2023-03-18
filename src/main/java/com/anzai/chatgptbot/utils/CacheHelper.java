package com.anzai.chatgptbot.utils;

import com.anzai.chatgptbot.model.chatgpt.GPTMessageDTO;
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
public class CacheHelper {

    /**
     * 对话缓存
     */
    private static Cache<String, List<GPTMessageDTO>> chatGptCache;

    static {
        // 设置超时时间
        chatGptCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
    }

    public static void setGptCache(String userName, List<GPTMessageDTO> gptMessageDtos) {
        chatGptCache.put(userName, gptMessageDtos);
    }

    public static List<GPTMessageDTO> getGptCache(String userName) {
        List<GPTMessageDTO> messageDtos = chatGptCache.getIfPresent(userName);
        if (CollectionUtils.isEmpty(messageDtos)) {
            return Lists.newArrayList();
        }
        return messageDtos;
    }

    /**
     * 清空用户对应的对话缓存
     *
     * @param userName
     */
    public static void clearGptCache(String userName) {
        List<GPTMessageDTO> messageDtos = chatGptCache.getIfPresent(userName);
        if (CollectionUtils.isNotEmpty(messageDtos)) {
            messageDtos.clear();
        }
    }
}
