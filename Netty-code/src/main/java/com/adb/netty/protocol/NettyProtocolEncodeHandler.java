package com.adb.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyProtocolEncodeHandler extends MessageToByteEncoder<MessageProtocolDto>{

    /**
     *此方法将要发送的数据按先发送数据内容的长度，再发送数据的规则一直发送数据
     * 发送数据长度的原因是因为解码的时候需要知道数据内容的长度（粘包和拆包原因）
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocolDto msg, ByteBuf out) throws Exception {
        System.out.println("编码器编码数据为---"+msg.toString());
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
