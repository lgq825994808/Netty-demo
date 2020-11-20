package com.adb.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServe {
    public static void main(String[] args) throws IOException {

        System.out.println("tcp 服务端启动了-----");
        ServerSocket serverSocket = new ServerSocket(8888);

        Socket accept = serverSocket.accept();
        String hostAddress = accept.getInetAddress().getHostAddress();
        System.out.println(hostAddress+":连接到了服务端");

        //获取客户端发送过来的消息
        InputStream inputStream = accept.getInputStream();
        byte[] bytes = new byte[1024];
        int read = inputStream.read(bytes);
        String s = new String(bytes);
        System.out.println("tcp 服务端收到数据为:-------"+s);

        //将消息发送给客户端
        String str="我知道tcp你来了.";
        OutputStream outputStream = accept.getOutputStream();
        outputStream.write(str.getBytes());

        accept.close();
        serverSocket.close();
    }

}
