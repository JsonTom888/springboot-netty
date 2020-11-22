package com.example.springbootnetty.longconection;

import lombok.Data;
import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * @author tom
 * @version V1.0
 * @date 2020/11/22 17:16
 */
@Data
@Message
public class NettyMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    // 消息类型
    private int type;

    // 消息内容
    private String body;

}
