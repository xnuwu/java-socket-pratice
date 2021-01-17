package com.jnet.rmi.dynamicLoad;


import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class SimpleClient {
    public static void main(String[] args) throws Exception {
        Context namingContext = new InitialContext();
        IHelloService helloService = (IHelloService) namingContext.lookup("rmi://localhost/HelloService");
        System.out.println(helloService.echo("xunwu"));
        System.out.println(helloService.time());
    }
}