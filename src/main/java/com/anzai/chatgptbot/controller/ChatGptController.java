package com.anzai.chatgptbot.controller;

import com.anzai.chatgptbot.model.chatgpt.ChatRequestVO;
import com.anzai.chatgptbot.service.ChatGPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天请求Controller
 *
 * @author tinybye
 * @date 2023/3/17
 */
@RestController
@Slf4j
public class ChatGptController {
    @Autowired
    private ChatGPTService chatGptService;

    @PostMapping("/chat")
    public String receiveMsgFromDd(@RequestBody ChatRequestVO chatRequestVO) throws Exception {
        return chatGptService.chatToGPT(chatRequestVO.getContent(), chatRequestVO.getUserName());
    }

}
