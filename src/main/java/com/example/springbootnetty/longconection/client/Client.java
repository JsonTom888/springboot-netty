package com.example.springbootnetty.longconection.client;

import com.example.springbootnetty.longconection.MessageDecode;
import com.example.springbootnetty.longconection.MessageEncode;
import com.example.springbootnetty.longconection.MessageType;
import com.example.springbootnetty.longconection.NettyMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author tom
 * @version V1.0
 * @date 2020/11/22 17:28
 */
public class Client {

    private NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    private Bootstrap bootstrap;

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        client.start();
        client.sendData("Hello netty 这是一条业务处理请求");
    }

    private void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 0, 5));
                        pipeline.addLast(new MessageDecode());
                        pipeline.addLast(new MessageEncode());
                        pipeline.addLast(new ClientHandler(Client.this));
                    }
                });
        doConnect();
    }

    /**
     * 连接服务端 and 重连
     */
    protected void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture connect = bootstrap.connect("127.0.0.1", 8081);
        // 实现监听通道连接的方法
        connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("连接状态正常---------" + channel.isActive());
                } else {
                    System.out.println("每隔2s重连....");
                    channelFuture.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 2, TimeUnit.SECONDS);
                }
            }
        });
        channel = connect.channel(); // 作用域问题，分包处理会拿不到 channel,赋值一下下面的 sendData 才可以拿到正确的channel
    }

    /**
     * 向服务端发送消息
     */
    private void sendData(String msg) {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(5000);
                if (channel != null && channel.isActive()) {
                    //获取一个键盘扫描器
                    NettyMessage message = new NettyMessage();
                    message.setType(MessageType.CUSTOMER);
                    message.setBody(msg);
                    System.out.println("第" + (i + 1) + "次向server发送业务处理请求");
                    channel.writeAndFlush(message);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("线程被中断");
        }
    }
}
