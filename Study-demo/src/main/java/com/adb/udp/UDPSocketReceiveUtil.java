package com.adb.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSocketReceiveUtil {

    /**
     * 使用UDP协议
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        System.out.println("socket 接受数据开始了---------");
        DatagramSocket datagramSocket = new DatagramSocket(8888);

        //构建接受数据的包
        byte[] bytes = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        //接受 数据
        datagramSocket.receive(datagramPacket);
        InetAddress address = datagramPacket.getAddress();
        int port = datagramPacket.getPort();
        byte[] data = datagramPacket.getData();

        System.out.println("接受到的数据  地址:"+address.getHostAddress()+"--端口:"+port+"--数据为:"+new String(data));
        //关闭
        datagramSocket.close();
    }
}
