package com.jnet.reflect.remoteCall.proxy;

import com.jnet.reflect.remoteCall.Call;
import com.jnet.reflect.remoteCall.IHelloService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public class ProxyFactory {

    public static Object getProxy(Class classType, String host, int port) {

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Connector connector = new Connector(host, port);

                Call call = new Call();
                call.setClassName(classType.getCanonicalName());
                call.setMethodName(method.getName());
                call.setParamType(method.getParameterTypes());
                call.setParamValue(args);

                connector.send(call);
                call = (Call) connector.receive();
                connector.close();

                Object result = call.getResult();
                if(result instanceof Throwable) {
                    throw new RemoteException(((Throwable) result).getMessage());
                }else {
                    return result;
                }
            }
        };

        return Proxy.newProxyInstance(classType.getClassLoader(), new Class[] { classType }, handler);
    }

    public static void main(String[] args) {
        IHelloService helloService = (IHelloService) ProxyFactory.getProxy(IHelloService.class, "localhost", 8080);
        System.out.println("time:" + helloService.time());
        System.out.println("echo:" + helloService.echo("hello"));
    }
}
