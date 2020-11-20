package com.adb.udp;

import java.io.IOException;
import java.net.*;

public class UDPSocketSendUtil {

    /**
     * 使用UDP协议
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        System.out.println("Socket 发送端启动了---------");
        DatagramSocket datagramSocket = new DatagramSocket(10000);
        String str ="scoket UDP 来了,这个是数据";
        byte[] bytes = str.getBytes();
        //构建发送地址 和数据
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
                InetAddress.getByName("127.0.0.1"), 8888);

        //发送数据
        datagramSocket.send(datagramPacket);

        //关闭
        datagramSocket.close();
    }


}
