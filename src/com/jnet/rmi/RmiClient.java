package com.jnet.rmi;

import javax.naming.*;
import java.io.IOException;

/**
 * @author Xunwu Yang 2021-01-11
 * @version 1.0.0
 */
public class RmiClient {

    public static void showRemoteObject(Context namingContext) throws Exception {
        NamingEnumeration<NameClassPair> namingEnumeration = namingContext.list("rmi:");
        while (namingEnumeration.hasMore()) {
            System.out.println(namingEnumeration.next().getName());
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "rmi://localhost/";

        Context namingContext = new InitialContext();

        Object obj = namingContext.lookup(url + "HelloService1");
        IRemoteHelloService helloService1 = (IRemoteHelloService) namingContext.lookup(url + "HelloService1");
        IRemoteHelloService helloService2 = (IRemoteHelloService) namingContext.lookup(url + "HelloService2");

        Class stubClass = helloService1.getClass();
        System.out.println("service1 是" + stubClass.getClass() + "的实例");
        Class[] interfaces = stubClass.getInterfaces();
        for(int i = 0; i < interfaces.length; i++) {
            System.out.println("存根类实现了" + interfaces[i] + "接口");
        }

        System.out.println(helloService1.echo("hello1"));
        System.out.println(helloService1.time());

        System.out.println(helloService2.echo("hello2"));
        System.out.println(helloService2.time());

        showRemoteObject(namingContext);
    }
}
