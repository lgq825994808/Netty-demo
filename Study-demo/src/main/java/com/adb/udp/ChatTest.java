package com.adb.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * 简单聊天模型
 *
 * 使用UDP协议
 */
public class ChatTest {

    public static void main(String[] args) throws SocketException {

        //发送端socket
        DatagramSocket sendSocket = new DatagramSocket(10000);
        //接受端socket
        DatagramSocket recevieSocket = new DatagramSocket(8888);

        Thread send = new Thread(new Send(sendSocket));
        Thread recevie = new Thread(new Receive(recevieSocket));
        send.start();
        recevie.start();
    }

}

/**
 * 聊天发送端
 */
class Send implements Runnable{

   private DatagramSocket ds;

    public Send(DatagramSocket ds) {
        this.ds = ds;
    }

    @Override
    public void run() {
        try {
            //获取键盘输入
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String data;
            while ((data=bufferedReader.readLine())!=null){
                byte[] bytes = data.getBytes();
                //构建发送数据
                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
                        InetAddress.getByName("127.0.0.1"), 8888);
                //发送数据
                ds.send(datagramPacket);
                //停止发送
                if("over".equals(data)){
                    break;
                }
            }
            //关闭
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

/**
 * 聊天接受端
 */
class Receive implements Runnable{

    private DatagramSocket ds;

    public Receive(DatagramSocket ds) {
        this.ds = ds;
    }

    @Override
    public void run() {

        try {
            while (true){
                byte[] bytes = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);

                //接受 数据
                ds.receive(datagramPacket);

                InetAddress address = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                byte[] data = datagramPacket.getData();
                String dataStr = new String(data);
                System.out.println("接受到的数据  地址:"+address.getHostAddress()+"--端口:"+port+"--数据为:"+dataStr);
                //关闭
                if("over".equals(dataStr)){
                    System.out.println(address.getHostAddress()+"---离开了聊天");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}