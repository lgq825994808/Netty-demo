package com.adb.netty.protocol;

import com.adb.netty.heartbeat.NettyHeartbeatHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class NettyProtocolServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //5秒无数据就发生读空闲， 7秒无数据就发生写空闲，10秒无数据就发生读写空闲
                            pipeline.addLast(new IdleStateHandler(5,7,10, TimeUnit.SECONDS));
                            pipeline.addLast(new NettyProtocolHeartbeatHandler());
                            //添加解码器
                            pipeline.addLast("decoder",new NettyProtocolDecodeHandler());
                            //添加编码器
                            pipeline.addLast("encoder",new NettyProtocolEncodeHandler());
                            //加入自己的业务处理 handler
                            pipeline.addLast(new NettyProtocolServerHandler());
                        }
                    });

            System.out.println(".....服务器 is ready...");

            ChannelFuture cf = bootstrap.bind(8888).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
