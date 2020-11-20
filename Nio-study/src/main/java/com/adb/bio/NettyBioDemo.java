package com.adb.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyBioDemo {
    /**
     * 使用BIO模型实现通信
     *
     * 要求：
     * 每一个客户端连接到该服务端后，服务端都启动一个线程去处理该连接
     * 客户端用telet模拟连接
     * 1：打开cmd窗口
     *2：输入 telnet 127.0.0.1 8888
     * 3:输入 “curl”+"]"
     * 4:输入 send  qwerr(要发送的数据)
     * @param args
     */
    public static void main(String[] args) throws IOException {
        //创建线程池
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //创建socker服务
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("服务端启动了");
        //循环获取客户端连接
        while (true){
            //获取客户端连接，此处会阻塞，当有连接来的时候就会往下执行
            Socket socket = serverSocket.accept();
            System.out.println("有新连接来了");
            //创建线程处理该连接
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    bioServer(socket);
                }
            });
        }
    }

    /**
     * 获取该连接的通信数据
     * @param socket
     */
    public static void bioServer(Socket socket){
        try {
            System.out.println("有新连接来了 线程名称:"+Thread.currentThread().getName()+"  线程id:"+Thread.currentThread().getId());
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            while (true){
                int read = inputStream.read(bytes);
                System.out.println("read====="+read);
                if(read!=-1){
                    String data = new String(bytes, 0, read);
                    System.out.println("线程名称"+Thread.currentThread().getName()+"获取数据为--"+data);
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("socketg关闭了------");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
