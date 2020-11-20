package com.adb.nio.groupchat;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * 聊天系统（群聊）的服务端
 */
public class GroupChatClient {

    private static String host="127.0.0.1";

    private static Integer prot=8888;

    public static void main(String[] args) throws IOException {
        SocketChannel chatClientChannel = SocketChannel.open();
        chatClientChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, prot);
        System.out.println("客户端启动了。。。。");
        //连接客户端
        boolean connect = chatClientChannel.connect(inetSocketAddress);
        if(!connect){
            //循环判断连接
            boolean finish = chatClientChannel.finishConnect();
            while (!finish){
                System.out.println("客户端还未连接上-------");
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        String msg = ChannelUtil.readData(chatClientChannel);
                        if(StringUtils.isNotBlank(msg)){
                            System.out.println(msg);
                        }
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            sendInfo(chatClientChannel,s);
        }
    }

    /**
     * 发送数据
     * @param chatClientChannel
     * @param str
     */
    public static void sendInfo(SocketChannel chatClientChannel,String str){
        try {
            String addr = chatClientChannel.getLocalAddress().toString();
            str=addr+"  说:"+str;
            ChannelUtil.writeData(chatClientChannel,str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
