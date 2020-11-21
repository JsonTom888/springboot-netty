package com.example.springbootnetty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * netty服务端
 *
 * @author tom
 * @version V1.0
 * @date 2020/11/21 16:06
 */
public class NettyServer {

    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        int port = 1234;
        new NettyServer(port).start();
    }

    public void start() throws Exception {
        ServerBootstrap server = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        server.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel){
                        System.out.println("initChannel ch:" + channel);
                        channel.pipeline() // ChannelPipeline用于存放ChannelHandler的容器
                                // 解码request，数据流进来的时候将字节码转换为消息对象
                                .addLast("decoder", new HttpRequestDecoder())
                                // 编码response，数据流出去的时候将消息对象转换为字节码
                                .addLast("encoder", new HttpResponseEncoder())
                                // 消息聚合器
                                .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                // 请求处理器
                                .addLast("handler", new NettyHandler());
                    }
                })
                //存放已完成三次握手的请求的队列的最大长度
                .option(ChannelOption.SO_BACKLOG, 128)
                // 保持连接
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        //绑定端口号
        server.bind(port).sync();
    }
}
