package com.jnet.reflect.remoteCall;

import com.jnet.reflect.remoteCall.impl.HelloServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public class RemoteCallServer {

    private Map<String, Object> serviceRegisterMap = new HashMap<>();

    public RemoteCallServer() {
        serviceRegisterMap.put("com.jnet.reflect.remoteCall.IHelloService", new HelloServiceImpl());
    }

    public void service() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        while (true) {
            try{
                Socket socket = serverSocket.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                Call call = (Call) ois.readObject();
                Object result = invoke(call);
                System.out.println(call.getClassName() + "::" + call.getMethodName() + "=>" + result);
                call.setResult(result);
                oos.writeObject(call);

                ois.close();
                oos.close();
                socket.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Object invoke(Call call) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class classType = Class.forName(call.getClassName());
        Method method = classType.getMethod(call.getMethodName(), call.getParamType());
        Object target = serviceRegisterMap.get(call.getClassName());
        if(target == null) {
            throw new IllegalArgumentException("class " + call.getClassName() + " not register in map");
        }

        return method.invoke(target, call.getParamValue());
    }

    public static void main(String[] args) throws IOException {
        new RemoteCallServer().service();
    }
}
