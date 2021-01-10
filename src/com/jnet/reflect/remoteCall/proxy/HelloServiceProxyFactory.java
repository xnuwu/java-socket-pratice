package com.jnet.reflect.remoteCall.proxy;

import com.jnet.reflect.remoteCall.IHelloService;
import com.jnet.reflect.remoteCall.impl.HelloServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public class HelloServiceProxyFactory {

    public static IHelloService getHelloServiceProxy(IHelloService helloService) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                System.out.println("before call " + method.getName());
                Object result = method.invoke(helloService, args);
//                System.out.println("after call " + method.getName());
                return result;
            }
        };

        Class classType = IHelloService.class;
        return (IHelloService) Proxy.newProxyInstance(classType.getClassLoader(), new Class[] { classType }, handler);
    }

    public static void main(String[] args) {
        IHelloService helloService = new HelloServiceImpl();
        IHelloService helloServiceProxy = getHelloServiceProxy(helloService);

        int testCount = 100000000;

        long t1 = System.currentTimeMillis();
        for(int i = 0; i < testCount; i++) {
            helloServiceProxy.echo("hello");
            helloServiceProxy.time();
        }
        long t2 = System.currentTimeMillis();

        for(int i = 0; i < testCount; i++) {
            helloService.echo("hello");
            helloService.time();
        }
        long t3 = System.currentTimeMillis();

        System.out.printf("%5d %5d", (t2 - t1), (t3 - t2));
    }
}
