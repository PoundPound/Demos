package com.demos.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sihan.liuwei on 5/24/17.
 */
public class ServerCenter implements Server
{
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();

    private static boolean isRunning = false;

    private static int port;

    public ServerCenter(int port)
    {
        this.port = port;
    }

    @Override
    public void start() throws IOException
    {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(port));
        System.out.println("Server start ...");

        while (true)
        {
            try
            {
                System.out.println("Server listening ...");
                // 1.监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行
                executor.execute(new ServerTask(server.accept()));
            }
            finally
            {
//                System.out.println("Server stop ...");
//                server.close();
            }
        }
    }

    @Override
    public void stop()
    {
        isRunning = false;
        executor.shutdown();
    }

    @Override
    public void register(Class serviceInterface, Class interfaceImpl)
    {
        serviceRegistry.put(serviceInterface.getName(), interfaceImpl);
    }

    @Override
    public boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public int getPort()
    {
        return port;
    }

    private static class ServerTask implements Runnable
    {
        Socket client = null;

        public ServerTask(Socket client)
        {
            this.client = client;
        }

        @Override
        public void run()
        {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;

            try
            {
                // 2.将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
                input = new ObjectInputStream(client.getInputStream());

                String serverName = input.readUTF();
                String methodName = input.readUTF();

                Class<?>[] parameterTypes = (Class<?>[])input.readObject();
                Object[] arguments = (Object[])input.readObject();

                Class serviceClass = serviceRegistry.get(serverName);
                if (serviceClass == null)
                    throw new ClassNotFoundException(serverName + "not found");

                Method method = serviceClass.getMethod(methodName, parameterTypes);
                Object result = method.invoke(serviceClass.newInstance(), arguments);

                // 3.将执行结果反序列化，通过socket发送给客户端
                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (output != null)
                {
                    try
                    {
                        output.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (input != null)
                {
                    try
                    {
                        input.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (client != null)
                {
                    try
                    {
                        client.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
