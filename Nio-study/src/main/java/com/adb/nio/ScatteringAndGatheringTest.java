package com.adb.nio;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：将数据写入到 buffer 时，可以采用 buffer 数组，依次写入 [分散]
 * Gathering: 从 buffer 读取数据时，可以采用 buffer 数组，依次读
 *
 *byteBuffer 的三种方法
 *
 * 清除该byteBuffer的角标状态 回复到初始值
 * public final Buffer clear() {
     position = 0;     //设置为0
     limit = capacity;    //极限和容量相同
     mark = -1;   //取消标记
     return this;
     }
 *
 * 将操作角标置为0 方法重新读写数据
 *public final Buffer rewind() {
    position = 0;
    mark = -1;
    return this;
    }
 *
 *
 * 将操作角标置为0 ，并限定有效值的范围
 * public final Buffer flip() {
    limit = position;
    position = 0;
    mark = -1;
    return this;
    }
 *
 *position：操作角标，当前该byteBuffer的读写角标
 *limit：有效数据角标（由于byteBuffer不会清除数据，故byteBuffer里面可能有脏数据，该角标表示有效数据的最大角标）
 *capacity：容量，该byteBuffer的容量大小
 */
public class ScatteringAndGatheringTest {

    /**
     * 使用byteBuffer数组读取通道里面的数据
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //获取通道
        ServerSocketChannel open = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8888);
        //绑定地址
        open.bind(inetSocketAddress);

        //创建接收数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);
        StringBuffer sb = new StringBuffer();
        //获取连接通道
        SocketChannel socketChannel = open.accept();
        while (true){
            long read = socketChannel.read(byteBuffers);
            if(read==-1){
                break;
            }
            Arrays.asList(byteBuffers).stream().forEach(t -> {
                //转换byteBuffer的操作角标
                t.flip();
                if(t.limit()>0){
                    System.out.println("接收的数据为--" + new String(t.array(),0,t.limit()));
                }
                //清除byteBuffer各种角标值，并没有清除byteBuffer里面的值
                t.clear();
            });
        }
    }
}
