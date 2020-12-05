package com.adb.netty.dubbo.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyDubboServerHandler extends SimpleChannelInboundHandler<String>{

    String str="nettyDubbo->demoServer->";

    int i=0;
    int t=0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        i++;
        System.out.println("i=============="+i);
        System.out.println("客服端连接来了-----"+ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        t++;
        System.out.println("t=============="+t);
        if(msg.startsWith(str)){
            String substring = msg.substring(str.length());
            DemoServerImpl demoServer = new DemoServerImpl();
            String data = demoServer.getData(substring);
            ctx.channel().writeAndFlush(data);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
