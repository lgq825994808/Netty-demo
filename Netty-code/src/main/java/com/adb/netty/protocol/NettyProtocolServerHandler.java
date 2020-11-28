package com.adb.netty.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class
NettyProtocolServerHandler extends SimpleChannelInboundHandler<MessageProtocolDto>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocolDto msg) throws Exception {
        System.out.println("服务端收到的数据为---"+msg.toString());
        Channel channel = ctx.channel();
        String str="客户端你好";
        byte[] bytes = str.getBytes();
        MessageProtocolDto dto = new MessageProtocolDto();
        dto.setLen(bytes.length);
        dto.setContent(bytes);
        channel.writeAndFlush(dto);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        channel.close();
    }
}