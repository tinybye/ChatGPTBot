package com.anzai.chatgptbot.model;

import lombok.Data;
import lombok.ToString;

/**
 * 聊天请求参数
 *
 * @author tinybye
 * @date 2023/3/18
 */
@Data
@ToString
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
