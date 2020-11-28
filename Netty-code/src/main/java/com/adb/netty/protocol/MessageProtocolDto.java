package com.adb.netty.protocol;

import java.nio.charset.Charset;
import java.util.Arrays;

public class MessageProtocolDto {

    private int len; //关键
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageProtocolDto{" +
                "len=" + len +
                ", content=" + new String(content, Charset.forName("utf-8")) +
                '}';
    }
}
