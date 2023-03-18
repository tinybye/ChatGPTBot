package com.anzai.chatgptbot.model.chatgpt;

import lombok.Data;

/**
 * 聊天请求参数
 *
 * @author tinybye
 * @date 2023/3/18
 */
@Data
public class ChatRequestVO {
    /**
     * 聊天内容
     */
    private String content;
    /**
     * 用户名，标识区分请求
     */
    private String userName;
}
