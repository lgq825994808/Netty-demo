package com.adb.netty.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyProtocolClientHandler extends SimpleChannelInboundHandler<MessageProtocolDto> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        for(int i=0;i<10;i++){
            String str="客户端发送数据"+i;
            byte[] bytes = str.getBytes();
            MessageProtocolDto dto = new MessageProtocolDto();
            dto.setLen(bytes.length);
            dto.setContent(bytes);
            channel.writeAndFlush(dto);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocolDto msg) throws Exception {
        System.out.println("客户端收到数据为--"+msg.toString());
    }
}
