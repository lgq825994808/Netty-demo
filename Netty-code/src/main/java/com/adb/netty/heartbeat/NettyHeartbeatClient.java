package com.adb.netty.heartbeat;

import com.adb.netty.http.groupchat.NettyGroupChatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * Netty编写的聊天室  客户端
 */
public class NettyHeartbeatClient {

    //属性
    private static String host="127.0.0.1";
    private static int port=8888;

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //添加解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //添加编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new NettyGroupChatClientHandler());
                        }
                    });
            System.out.println(".....客户端 is ready...");
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch(Exception e){
                bossGroup.shutdownGracefully();
        }
    }
}
