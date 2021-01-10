package com.jnet.reflect.remoteCall;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public class CallClient {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Socket socket = new Socket(InetAddress.getLocalHost(), 8000);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        Call call = new Call();
        call.setClassName("com.jnet.reflect.remoteCall.IHelloService");
        call.setMethodName("time");
        call.setParamType(new Class[] {});
        call.setParamValue(new Object[] {});

        oos.writeObject(call);
        call = (Call) ois.readObject();
        System.out.println("call:" + call);
        System.out.println("result:" + call.getResult());

        oos.close();
        ois.close();
        socket.close();
    }
}
