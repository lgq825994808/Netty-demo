package com.adb.netty.protocol;

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
 *
 */
public class NettyProtocolClient {

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
                            pipeline.addLast("decoder",new NettyProtocolDecodeHandler());
                            //添加编码器
                            pipeline.addLast("encoder",new NettyProtocolEncodeHandler());
                            pipeline.addLast(new NettyProtocolClientHandler());
                        }
                    });
            System.out.println(".....客户端 is ready...");
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //得到 channel
            Channel channel = channelFuture.channel();
            System.out.println("-------" + channel.localAddress() + "--------");
            //客户端需要输入信息，创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                //通过 channel 发送到服务器端
                channel.writeAndFlush(msg);
            }
        } catch(Exception e){
                bossGroup.shutdownGracefully();
        }
    }
}
