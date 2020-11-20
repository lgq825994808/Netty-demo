package com.adb.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {

    public static void main(String[] args) throws IOException {

        System.out.println("TCP 客户端启动了-------");
        Socket socket = new Socket("127.0.0.1",8888);

        String str ="注意 tcp 来了-------";

        //获取连接的输出流，进行数据的发送
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(str.getBytes());

        //读取服务端发送过来的消息
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        System.out.println("tcp 客户端收到数据:------"+new String(bytes));

        socket.close();
    }
}
