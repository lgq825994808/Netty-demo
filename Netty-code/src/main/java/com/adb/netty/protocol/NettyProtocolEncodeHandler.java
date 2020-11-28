package com.adb.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyProtocolEncodeHandler extends MessageToByteEncoder<String>{
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        System.out.println("编码器编码数据为---"+msg);
        out.writeBytes(msg.getBytes());
    }
}
