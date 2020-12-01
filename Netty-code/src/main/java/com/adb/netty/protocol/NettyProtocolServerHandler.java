package com.adb.netty.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;

public class
NettyProtocolServerHandler extends SimpleChannelInboundHandler<MessageProtocolDto>{

    /**
     * 客户端连接就会调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.println("客户端地址为---"+socketAddress.toString());
    }

    /**
     * 读取通道数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
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

    /**
     * 出现异常后关闭
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        channel.close();
    }
}
