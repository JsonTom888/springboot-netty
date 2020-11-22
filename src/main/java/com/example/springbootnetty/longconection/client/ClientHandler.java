package com.example.springbootnetty.longconection.client;

import com.example.springbootnetty.longconection.MessageHandler;
import com.example.springbootnetty.longconection.NettyMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author tom
 * @version V1.0
 * @date 2020/11/22 17:33
 */
public class ClientHandler extends MessageHandler {

    private Client client;

    public ClientHandler(Client client) {
        super("client");
        this.client = client;
    }

    @Override
    protected void handlerData(ChannelHandlerContext ctx, Object msg) {
        NettyMessage message = (NettyMessage) msg;
        System.out.println("client  收到 server 业务逻辑处理结果： " + message.toString());
    }
    @Override
    protected void handlerAllIdle(ChannelHandlerContext ctx) {
        super.handlerAllIdle(ctx);
        sendPingMsg(ctx);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.doConnect();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println(name + "exception :"+ cause.toString());
    }
}
