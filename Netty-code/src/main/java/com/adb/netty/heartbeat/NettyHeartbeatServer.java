package com.adb.netty.heartbeat;

import com.adb.netty.http.groupchat.NettyGroupChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 心跳处理
 */
public class NettyHeartbeatServer {

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
                            //加入一个 netty 提供 IdleStateHandler
                            /*
                            说明
                            1. IdleStateHandler 是 netty 提供的处理空闲状态的处理器
                            2. long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接
                            3. long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
                            4. long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接
                            5. 当 IdleStateEvent 触发后 , 就会传递给管道 的下一个 handler 去处理
                            6. 通过调用(触发)下一个 handler 的 userEventTiggered , 在该方法中去处理 IdleStateEvent(读
                            空闲，写空闲，读写空闲)
                            */
                            pipeline.addLast(new IdleStateHandler(5,7,10, TimeUnit.SECONDS));
                            //加入自己的业务处理 handler
                            pipeline.addLast(new NettyHeartbeatHandler());
                        }
                    });

            System.out.println(".....服务器 is ready...");
            //绑定一个端口并且同步, 生成了一个 ChannelFuture 对象
            //启动服务器(并绑定端口)
            ChannelFuture cf = bootstrap.bind(8888).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
