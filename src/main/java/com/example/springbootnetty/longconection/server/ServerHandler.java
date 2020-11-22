package com.example.springbootnetty.longconection.server;

import com.example.springbootnetty.longconection.NettyMessage;
import com.example.springbootnetty.longconection.MessageType;
import com.example.springbootnetty.longconection.MessageHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author tom
 * @version V1.0
 * @date 2020/11/22 17:34
 */
public class ServerHandler extends MessageHandler {

    public ServerHandler() {
        super("server");
    }
    @Override
    protected void handlerData(ChannelHandlerContext ctx, Object msg) {
        NettyMessage message  = (NettyMessage) msg;
        System.out.println("server 接收业务请求并进行业务逻辑处理 ： " +  message.toString());
        message.setType(MessageType.CUSTOMER);
        message.setBody("server 业务逻辑处理结果");
        ctx.channel().writeAndFlush(message);
        System.out.println("server 发送业务逻辑处理结果： " + message.toString());
    }
    @Override
    protected void handlerReaderIdle(ChannelHandlerContext ctx) {
        super.handlerReaderIdle(ctx);
        System.err.println(" ---- client "+ ctx.channel().remoteAddress().toString() + " reader timeOut, --- close it");
        ctx.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.err.println( name +"  exception" + cause.toString());
    }
}