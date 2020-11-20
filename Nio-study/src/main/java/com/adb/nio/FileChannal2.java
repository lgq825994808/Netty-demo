package com.adb.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannal2 {
    /**
     * 读取文件数据
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel = fileInputStream.getChannel();
        //创建byteBuffer
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        //读取数据
        channel.read(allocate);

        System.out.println("读取的数据为-----"+new String(allocate.array()));

        channel.close();
        fileInputStream.close();
    }
}
