package com.adb.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClinet {

    /**
     * nio通信的客户端
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8888);

        //连接服务器端
        boolean connect = socketChannel.connect(inetSocketAddress);
        if(!connect){
            //循环判断，等待连接成功
            while (!socketChannel.finishConnect()){
                System.out.println("客户端还未连接上-------");
            }
        }

        String str ="客户端发来了一个连接";
        ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
        //发送数据
        socketChannel.write(wrap);
        System.out.println("客户端发送完成-----");
        //让程序停在这，不要关闭通道
        System.in.read();
    }

}
