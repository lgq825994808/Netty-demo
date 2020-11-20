package com.adb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ByteBufDemo {

    public static void main(String[] args) throws UnsupportedEncodingException {

        //说明
        //1. 创建 对象，该对象包含一个数组 arr , 是一个 byte[10]
        //2. 在 netty 的 buffer 中，不需要使用 flip 进行反转
        // 底层维护了 readerindex 和 writerIndex
        //3. 通过 readerindex 和 writerIndex 和 capacity， 将 buffer 分成三个区域
        // 0---readerindex 已经读取的区域
        // readerindex---writerIndex ， 可读的区域
        // writerIndex -- capacity, 可写的区域

        readAndWrite();

        read();

    }

    public static void read() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,word", Charset.forName("utf-8"));

        byte[] array = byteBuf.array();
        System.out.println(new String(array,"utf-8"));

        System.out.println(byteBuf.toString());

        int len = byteBuf.readableBytes();
        System.out.println("剩余可读字节数---"+len);

        byte aByte = byteBuf.getByte(5);
        System.out.println("第五个字节的值为---"+aByte);

        byte b = byteBuf.readByte();
        System.out.println("读取的字节数值为---"+b);

        int i = byteBuf.readInt();
        System.out.println("读取四个字节数值为---"+i);

        int i1 = byteBuf.readableBytes();
        System.out.println("剩余可读字节数为---"+i1);

        CharSequence cs = byteBuf.readCharSequence(3,  Charset.forName("utf-8"));
        System.out.println("读取数据的长度为3的值为---"+cs);

        int i2 = byteBuf.readableBytes();
        System.out.println("剩余可读字节数为---"+i2);

        CharSequence charSequence = byteBuf.getCharSequence(0, 4, Charset.forName("utf-8"));
        System.out.println("读取从角标从0开始，长度为4的数据为---"+charSequence);
        CharSequence charSequence1 = byteBuf.getCharSequence(3, 5, Charset.forName("utf-8"));
        System.out.println("读取从角标从3开始，长度为5的数据为---"+charSequence1);
    }


    public static void readAndWrite(){
        ByteBuf buffer = Unpooled.buffer(10);
        for(int i=0;i<10;i++){
            buffer.writeByte(i);
        }
        System.out.println("capacity=" + buffer.capacity());//10
        //get方法只获取数据，不会移动readerindex角标
        for(int i = 0; i<buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }
        //get方法读取数据，会移动readerindex角标
        for(int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
        System.out.println("执行完毕");
    }
}
