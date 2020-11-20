package com.adb.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannal3 {
    /**
     * 将文件1.txt拷贝到文件2.txt
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel channel2 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        while (true){
            //读取数据
            int read = channel1.read(byteBuffer);
            System.out.println("read====="+read);
            if(read==-1){
                break;
            }else {
                //转换byteBuffer(重置byteBuffer的标志位)，为了能从头写入数据
                byteBuffer.flip();
                channel2.write(byteBuffer);
            }
            byteBuffer.clear();
        }

        //关闭
        channel2.close();
        fileOutputStream.close();
        channel1.close();
        fileInputStream.close();
    }
}
