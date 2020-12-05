package com.adb.netty.dubbo.client;

import com.adb.netty.NettyClient;
import com.adb.netty.dubbo.server.DemoServer;

public class ClientBootstrap {

    //这里定义协议头
    public static final String str="nettyDubbo->demoServer->";

    public static void main(String[] args) throws Exception{
        
        //创建一个消费者
        NettyDubboClient customer = new NettyDubboClient();
        //创建代理对象
        DemoServer service = (DemoServer) customer.getBean(DemoServer.class, str);
        for (;; ) {
            Thread.sleep(3 * 1000);
            //通过代理对象调用服务提供者的方法(服务)
            String str="book";
            String res = service.getData(str);
            System.out.println("调用的结果 res= " + res);
        }
    }
}
