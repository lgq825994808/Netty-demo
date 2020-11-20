package com.adb.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class TCPUploadTextServe {
    public static void main(String[] args) throws IOException {

        System.out.println("服务端启动--------------");
        ServerSocket serverSocket = new ServerSocket(8888);
        //获取客户端
        Socket accept = serverSocket.accept();

        //获取输入流
        BufferedReader bufr = new BufferedReader(new InputStreamReader(accept.getInputStream()));

        String filePath= "D:/work/demo.txt";
        //获取输出文件地址
        //BufferedWriter bufw = new BufferedWriter(new FileWriter("D:/work/demo.txt"));
        PrintWriter bufw = new PrintWriter(new FileWriter(filePath), true);
        //读取收到的文件
        String line=null;
        while ((line=bufr.readLine())!=null){
            bufw.print(line);
            bufw.print("\n");
        }

        //返回给客户端信息
        OutputStream outputStream = accept.getOutputStream();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));
        out.write("上传成功------");
        System.out.println("服务端接受完毕----------");
        //关闭
        out.close();
        bufw.close();
        accept.close();
        serverSocket.close();
    }
}
