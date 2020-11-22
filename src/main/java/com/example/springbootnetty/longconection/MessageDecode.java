package com.example.springbootnetty.longconection;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author tom
 * @version V1.0
 * @date 2020/11/22 17:20
 */
public class MessageDecode extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
                          List<Object> out) throws Exception {
        final byte[] array;
        //获取可读取的字节数
        final int length = msg.readableBytes();
        //存放数据的字节数组
        array = new byte[length];
        //读取数据到array数组中
        msg.getBytes(msg.readerIndex(), array, 0, length);
        MessagePack pack = new MessagePack();
        //转化为java对象
        out.add(pack.read(array, NettyMessage.class));

    }
}
