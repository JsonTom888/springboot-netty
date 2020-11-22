package com.example.springbootnetty.longconection;

/**
 * @author tom
 * @version V1.0
 * @date 2020/11/22 17:17
 */
public interface MessageType {
    //心跳消息
    byte PING = 1;
    //响应消息
    byte PONG = 2;
    //业务消息
    byte CUSTOMER = 3;

}
