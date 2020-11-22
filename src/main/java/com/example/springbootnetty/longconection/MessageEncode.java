package com.example.springbootnetty.longconection;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author tom
 * @version V1.0
 * @date 2020/11/22 17:24
 */
public class MessageEncode extends MessageToByteEncoder<NettyMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf buf) throws Exception {
        MessagePack pack = new MessagePack();
        //把java对象转化为字节
        byte[] write = pack.write(msg);
        buf.writeBytes(write);

    }
}
