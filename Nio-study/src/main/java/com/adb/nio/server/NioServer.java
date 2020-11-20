package com.adb.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    /**
     * nio通信的服务端
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //获取服务端
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println("服务器端启动了------");
        //获取选择器
        Selector selector = Selector.open();
        //绑定通道
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        //将该通道设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //将serverSocketChannel注册到selector，该通道用于与客户端的连接事件
        //所有的客户端连接，都通过该通道产生一个连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            //阻塞一秒
            int num = selector.select(2000);
            if(num==0){
                System.out.println("服务器等待2s,无客户端连接");
                continue;
            }
            //获取该选择器中所有连接
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //如果是连接事件
                if(selectionKey.readyOps()==SelectionKey.OP_ACCEPT){
                    SocketChannel accept = serverSocketChannel.accept();
                    accept.configureBlocking(false);
                    System.out.println("服务器收到一个连接-----"+accept.hashCode());
                    //将该连接注册到选择器里面，并标注为读取数据事件
                    accept.register(selector,SelectionKey.OP_READ,ByteBuffer.allocate(1024));
                }
                //如果是读取数据事件
                if(selectionKey.readyOps()==SelectionKey.OP_READ){
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer attachment = (ByteBuffer)selectionKey.attachment();
                    channel.read(attachment);
                    System.out.println("收到数据为-----"+new String(attachment.array()));
                }
                //删除当前  SelectionKey
                iterator.remove();
            }
        }
    }
}
