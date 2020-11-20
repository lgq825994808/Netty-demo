package com.adb.netty.http.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyGroupChatServerHandler extends SimpleChannelInboundHandler<String>{


    //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
    //可以保存全局的通道 channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //调用顺序 1   客户端连上netty服务器后，马上调用handlerAdded方法完成channel的添加操作(所谓channel可以理解为一个客户端)
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //该方法会给channelGroup里面的所有channel发送消息
        channelGroup.writeAndFlush(channel.remoteAddress()+" 加入了聊天");
        System.out.println(channel.remoteAddress()+" 加入了聊天");
        channelGroup.add(channel);
        System.out.println("连接个数："+channelGroup.size());
    }

    //调用顺序 2  添加操作执行完成以后立马调用channelRegistered方法将channel注入到netty中管理起来
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 注册了");
    }

    //调用顺序 3 注册好以后调用服务器端的channelActive方法，让其处于激活状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //Channel channel = ctx.channel();
        //channelGroup.writeAndFlush(channel.remoteAddress()+"  上线了");
        System.out.println(ctx.channel().remoteAddress()+" 上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //Channel channel = ctx.channel();
        //channelGroup.writeAndFlush(channel.remoteAddress()+"  离线了");
        System.out.println(ctx.channel().remoteAddress()+" 离线了");
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 关闭注册了");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress()+" 离开了聊天");
        System.out.println(channel.remoteAddress()+" 离开了聊天");
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"关闭了连接");
    }

    /**
     * 调用顺序 4   读取数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        String str = channel.remoteAddress() + "  说:" + msg;
        System.out.println(str);
        channelGroup.forEach(t ->{
            if(t!=channel){
                t.writeAndFlush(str);
            }else {
                channel.writeAndFlush("自己以发送消息为:"+msg);
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+" 读取数据完成");
    }
}
