package com.adb.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 聊天系统（群聊）的服务端
 */
public class GroupChatServer {

    private Integer prot=8888;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public GroupChatServer() {
        try {
            //开启服务器serverSocketChannel
            serverSocketChannel=ServerSocketChannel.open();
            //开启选择器
            selector=Selector.open();
            //绑定监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress(prot));
            //设置该通道为非阻塞
            serverSocketChannel.configureBlocking(false);
            //将该通道注册到选择器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("服务端启动了");
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listener();
    }

    /**
     * 监听端口下面所有通道，并获取客户端通道的数据
     * @param
     * @param
     */
    public  void listener(){
        try {
            while (true){
                //阻塞2秒
                int num = selector.select(2000);
                if(num >0){
                    //获取当前有事件发生的selentionKey,也是获取通道
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        //注册事件
                        if(selectionKey.isAcceptable()){
                            //获取通道
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            //将该通道设置为非阻塞
                            socketChannel.configureBlocking(false);
                            String msg=socketChannel.getRemoteAddress().toString()+"上线了";
                            System.out.println(msg);
                            socketChannel.register(selector,SelectionKey.OP_READ);

                            //发送消息给所有客户端
                            pullAll(msg,socketChannel);
                        }
                        //读取事件
                        if(selectionKey.isReadable()){
                            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                            try {
                                //读取通道里面的数据
                                String msg = ChannelUtil.readData(socketChannel);
                                System.out.println(msg);
                                //发送消息给所有客户端
                                pullAll(msg,socketChannel);
                            } catch (IOException e) {
                                try {
                                    String str=socketChannel.getRemoteAddress()+"  离线了";
                                    System.out.println(str);
                                    //发送离线消息给所有客户端
                                    pullAll(str,socketChannel);
                                    //清除selectionKey的注册
                                    selectionKey.cancel();
                                    //关闭通道
                                    socketChannel.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        //删除该selectionKey  防止重复处理
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待。。。。。");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息给所有客户端
     * @param msg
     */
    public void pullAll(String msg,SocketChannel socketChannel){
        //获取所有客户端的连接
        Set<SelectionKey> keys = selector.keys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()){
            try {
                SelectionKey next = iterator.next();
                Channel channel = next.channel();
                //排除掉自己
                if(channel instanceof SocketChannel && channel!=socketChannel){
                    SocketChannel tagerSocketChannel = (SocketChannel) channel;
                    //往通道里面写数据
                    ChannelUtil.writeData(tagerSocketChannel,msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
