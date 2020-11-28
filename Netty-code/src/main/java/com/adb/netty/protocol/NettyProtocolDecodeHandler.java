package com.adb.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class NettyProtocolDecodeHandler extends ByteToMessageDecoder{

    /**
     *粘包和拆包问题原因：
     * 操作系统在发送TCP数据的时候，底层会有一个缓冲区，例如1024个字节大小，如果一次请求发送的数据量比较小，没达到缓冲区大小，
     * TCP则会将多个请求合并为同一个请求进行发送，这就形成了粘包问题；
     * 如果一次请求发送的数据量比较大，超过了缓冲区大小，TCP就会将其拆分为多次发送，这就是拆包，也就是将一个大的包拆分为多个小包进行发送
     *
     * 该方法先读取数据内容的字节数长度，然后再根据长度读取数据内容
     * 此方法会一直循环读取接收到的ByteBuf
     **/
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readInt();
       /* byte[] bytes = new byte[len];
        in.readBytes(bytes);*/

        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        System.out.println("len------"+len);
        System.out.println("readableBytes--------"+in.readableBytes());
        MessageProtocolDto dto = new MessageProtocolDto();
        dto.setLen(len);
        dto.setContent(bytes);
        System.out.println("解码器解码数据为---"+dto.toString());
        out.add(dto);
    }


}
