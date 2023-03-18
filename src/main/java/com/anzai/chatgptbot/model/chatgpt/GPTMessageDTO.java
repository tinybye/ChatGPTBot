package com.anzai.chatgptbot.model.chatgpt;

import lombok.ToString;

/**
 * GPT对话信息
 * 官方接口文档：https://platform.openai.com/docs/guides/chat/introduction
 *
 * @author tinybye
 * @date 2023/3/17
 */
@ToString
public class GPTMessageDTO {
    /**
     * 角色，"system", "user", or "assistant"
     */
    private String role;
    /**
     * 聊天内容
     */
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
