package com.adb.netty.dubbo.server;

import org.apache.commons.lang3.StringUtils;

public class DemoServerImpl implements DemoServer{

    @Override
    public String getData(String str) {
        if(StringUtils.isBlank(str)){
            return "参数不能为空";
        }
        if(str.equals("book")){
            return "书本";
        }else {
            return "未获取到数据";
        }
    }
}
