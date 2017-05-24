package com.demos.rpc;

import java.io.IOException;

/**
 * Created by sihan.liuwei on 5/24/17.
 */
public interface Server
{
    void start() throws IOException;
    void stop();
    void register(Class serviceInterface, Class interfaceImpl);
    boolean isRunning();
    int getPort();
}
