package com.adb.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannal1 {
    /**
     * 将数据写入进文件
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String str="hello netty 加油";
        FileOutputStream fileOutputStream = new FileOutputStream("1.txt");
        FileChannel channel = fileOutputStream.getChannel();

        //获取byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        //重置byteBuffer的数据标志位
        byteBuffer.flip();

        //往通道里面写数据
        channel.write(byteBuffer);

        //关闭
        channel.close();
        fileOutputStream.close();
    }
}
