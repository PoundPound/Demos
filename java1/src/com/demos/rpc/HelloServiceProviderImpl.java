package com.demos.rpc;

/**
 * Created by sihan.liuwei on 5/24/17.
 */
public class HelloServiceProviderImpl implements HelloServiceProvider
{
    @Override
    public String sayHi(String name) {
        return "Hi:" + name;
    }
}
