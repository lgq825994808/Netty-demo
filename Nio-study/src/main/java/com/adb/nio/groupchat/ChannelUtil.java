package com.adb.nio.groupchat;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ChannelUtil {

    /**
     * 读取通道里面的数据
     * @param socketChannel
     */
    public static String readData(SocketChannel socketChannel) throws IOException {
        StringBuffer sb = new StringBuffer();
        ByteBuffer allocate = ByteBuffer.allocate(3);
        while (true){
            int read = socketChannel.read(allocate);
            if(read<=0){
                break;
            }
            allocate.flip();
            String msg = new String(allocate.array(),0,allocate.limit(),"utf-8");
            sb.append(msg);
            allocate.clear();
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 将数据写入通道
     * @param socketChannel
     * @param msg
     */
    public static void writeData(SocketChannel socketChannel,String msg) throws IOException {
        if(StringUtils.isBlank(msg)){
            System.out.println("发送消息不能为空。。。。。");
            return;
        }
        ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
        socketChannel.write(wrap);
    }
}
