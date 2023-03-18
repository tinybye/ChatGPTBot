package com.anzai.chatgptbot.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 接口响应信息
 *
 * @author tinybye
 * @date 2023/3/18
 */
@Data
public class GPTResponseDTO {
    private JSON usage;
    private GPTMessageDTO message;
}
