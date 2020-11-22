package com.example.springbootnetty.longconection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author tom
 * @version V1.0
 * @date 2020/11/22 17:30
 */
public abstract class MessageHandler extends ChannelInboundHandlerAdapter {

    protected String name;
    //记录次数
    private int heartbeatCount = 0;

    //获取server and client 传入的值
    public MessageHandler(String name) {
        this.name = name;
    }
    /**
     * 输入数据业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage m = (NettyMessage) msg;
        int type = m.getType();
        switch (type) {
            case 1:
                //收到心跳消息，进行响应
                sendPongMsg(ctx);
                break;
            case 2:
                //发送心跳消息后收到响应
                System.out.println(name + " 收到心跳响应消息 " + ctx.channel().remoteAddress());
                break;
            case 3:
                handlerData(ctx,msg);
                break;
            default:
                break;
        }
    }

    protected abstract void handlerData(ChannelHandlerContext ctx,Object msg);

    protected void sendPingMsg(ChannelHandlerContext ctx){
        NettyMessage message = new NettyMessage();

        message.setType(MessageType.PING);

        ctx.channel().writeAndFlush(message);

        heartbeatCount++;

        System.out.println(name + " 发送心跳消息 " + ctx.channel().remoteAddress() + "count :" + heartbeatCount);
    }

    private void sendPongMsg(ChannelHandlerContext ctx) {

        NettyMessage message = new NettyMessage();

        message.setType(MessageType.PONG);

        ctx.channel().writeAndFlush(message);

        heartbeatCount++;

        System.out.println(name +" 收到心跳发送响应消息 "+ctx.channel().remoteAddress() +" , count :" + heartbeatCount);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        IdleStateEvent stateEvent = (IdleStateEvent) evt;

        switch (stateEvent.state()) {
            case READER_IDLE:
                handlerReaderIdle(ctx);
                break;
            case WRITER_IDLE:
                handlerWriterIdle(ctx);
                break;
            case ALL_IDLE:
                handlerAllIdle(ctx);
                break;
            default:
                break;
        }
    }

    /**
     * 客户端与服务端在指定时间内没有任何读写请求，就会认为连接是idle的
     *
     * @param ctx
     */
    protected void handlerAllIdle(ChannelHandlerContext ctx) {
        System.err.println("---ALL_IDLE---");
    }

    protected void handlerWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("---WRITER_IDLE---");
    }


    protected void handlerReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---READER_IDLE---");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(" ---"+ctx.channel().remoteAddress() +"----- is  active" );
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(" ---"+ctx.channel().remoteAddress() +"----- is  no active");
    }
}
