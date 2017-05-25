package com.demos.rpc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by sihan.liuwei on 5/24/17.
 */
public class RpcDemos
{
    public static void main(String[] args)
    {
        /*new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Server service = new ServerCenter(8088);
                    service.register(HelloServiceProvider.class, HelloServiceProviderImpl.class);
                    service.start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();*/

        /*try {
            HelloServiceProvider helloService = RpcClient.getRemoteProxyObj(HelloServiceProvider.class,
                    new InetSocketAddress(InetAddress.getByName("192.168.0.167"), 8088));
            System.out.println(helloService.sayHi("test"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/

        try {
            //用getLocalHost方法创建InetAddress对象
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println(addr.getHostAddress());//返回IP
            System.out.println(addr.getHostName());//返回主机名
            //根据域名得到InetAddress对象
            addr = InetAddress.getByName("www.163.com");
            System.out.println(addr.getHostAddress());//返回163的IP地址
            System.out.println(addr.getHostName());//返回域名
            addr = InetAddress.getByName("192.168.0.167");
            System.out.println(addr.getHostAddress());//返回163的IP地址
            System.out.println(addr.getHostName());//返回域名,如果IP解析则返回域名，不解析还是返回IP
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
