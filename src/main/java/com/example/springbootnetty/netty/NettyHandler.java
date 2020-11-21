package com.example.springbootnetty.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * ChannelInboundHandler，输入数据处理器
 *
 * @author tom
 * @version V1.0
 * @date 2020/11/21 16:08
 */
public class NettyHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * 输入数据处理器
     *
     * @param context 通信管道的上下文
     * @param request netty处理channel的时候，只处理消息是FullHttpRequest的Channel，
     *                这样我们就能在一个ChannelHandler中处理一个完整的Http请求了。
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, FullHttpRequest request) throws Exception {
        System.out.println("class:" + request.getClass().getName());
        // 生成response，这里使用的DefaultFullHttpResponse，同FullHttpRequest类似，通过这个我们就不用将response拆分成多个channel返回给请求端了。
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("hello my netty".getBytes()));

        HttpHeaders heads = response.headers();
        // 响应类型
        heads.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + "; charset=UTF-8");
        // 添加header描述length，没有这一步，你会发现用postman发出请求之后就一直在刷新，因为http请求方不知道返回的数据到底有多长。
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 设置保持连接
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        // channel读取完成之后需要输出缓冲流。如果没有这一步，你会发现postman同样会一直在刷新。
        context.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception {
        System.out.println("通道数据读取完毕");
        super.channelReadComplete(context);
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        System.out.println("异常抓取");
        if(null != cause) cause.printStackTrace();
        if(null != context) context.close();
    }

}
