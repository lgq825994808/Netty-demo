package com.adb.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannal4 {
    /**
     *使用transferFrom将一个通道的数据复制到另一个通道，以实现文件拷贝
     * @param args
     */
    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel channel2 = fileOutputStream.getChannel();

        channel2.transferFrom(channel1,0,channel1.size());

        channel2.close();
        channel1.close();
    }
}
