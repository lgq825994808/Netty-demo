package com.adb.netty.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 事件处理类 （心跳事件）
 */
public class NettyProtocolHeartbeatHandler extends ChannelInboundHandlerAdapter {

    //检测到了心跳事件，就触发该方法
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Channel channel = ctx.channel();
        if(evt instanceof IdleStateEvent){
            IdleStateEvent ise=(IdleStateEvent)evt;
            IdleState state = ise.state();
            String data="";
            switch (state){
                case READER_IDLE:
                    data="读空闲";
                    break;
                case WRITER_IDLE:
                    data="写空闲";
                    break;
                case ALL_IDLE:
                    data="读写空闲";
                    break;
                default:
                    break;
            }
            System.out.println(channel.remoteAddress()+"---发生了---"+data);
        }
    }
}
