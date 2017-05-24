package com.demos.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by sihan.liuwei on 5/24/17.
 */
public class RpcDemos
{
    public static void main(String[] args)
    {
        new Thread(new Runnable()
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
        }).start();

        HelloServiceProvider helloService = RpcClient.getRemoteProxyObj(HelloServiceProvider.class, new InetSocketAddress("localhost", 8088));
        System.out.println(helloService.sayHi("test"));
    }
}
