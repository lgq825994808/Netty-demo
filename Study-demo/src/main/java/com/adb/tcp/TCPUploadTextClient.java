package com.adb.tcp;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class TCPUploadTextClient {

    /**
     * 使用socket传送文件
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        System.out.println("客户端启动成功--------------");
        Socket socket = new Socket("127.0.0.1",8888);

        String filePath= "D:/work/word.txt";
        //读取文件
        BufferedReader bufr = new BufferedReader(new FileReader(filePath));

        OutputStream outputStream = socket.getOutputStream();

        //获取输出流
        //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        PrintWriter out = new PrintWriter(outputStream, true);

        //读取文件并写出到输出流
        String line=null;
        while ((line=bufr.readLine())!=null){
            out.println(line);
        }
        socket.shutdownOutput();

        //获取socket的输入流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String readLine = bufferedReader.readLine();
        System.out.println("客户端收到数据:------"+readLine);

        bufr.close();
        socket.close();
    }
}
