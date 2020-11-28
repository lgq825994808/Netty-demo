package com.adb.netty.protocol;

import com.adb.netty.heartbeat.NettyHeartbeatHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
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
                            //FixedLengthFrameDecoder  对于使用固定长度的粘包和拆包场景
                            //LineBasedFrameDecoder   行分割帧解码器
                            //DelimiterBasedFrameDecoder   自定义分隔符帧解码器
                            //LengthFieldBasedFrameDecoder   自定义长度帧解码器
                                //---maxFrameLength - 发送的数据包最大长度；
                                //---lengthFieldOffset - 长度域偏移量，指的是长度域位于整个数据包字节数组中的下标；
                                //---lengthFieldLength - 长度域的自己的字节数长度。
                                //---lengthAdjustment – 长度域的偏移量矫正。 如果长度域的值，除了包含有效数据域的长度外，还包含了其他域（如长度域自身）长度，那么，就需要进行矫正。矫正的值为：包长 - 长度域的值 – 长度域偏移 – 长度域长。
                                //---initialBytesToStrip – 丢弃的起始字节数。丢弃处于有效数据前面的字节数量。比如前面有4个节点的长度域，则它的值为4。
                            //设置每次读取数据包的大小（通过数据包中的表示包体长度的字节）
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 0, true));
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
