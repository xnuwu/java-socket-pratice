package com.jnet.reflect.remoteCall.proxy;

import com.jnet.reflect.remoteCall.IHelloService;
import com.jnet.reflect.remoteCall.impl.HelloServiceImpl;

import java.util.Date;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public class HelloServiceProxy implements IHelloService {

    private IHelloService helloService;

    public HelloServiceProxy(IHelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public String echo(String message) {
        System.out.println("hello proxy echo start");
        String result = helloService.echo(message);
        System.out.println("hello proxy echo end");
        return result;
    }

    @Override
    public Date time() {
        System.out.println("hello proxy time start");
        Date result = helloService.time();
        System.out.println("hello proxy time end");
        return result;
    }

    public static void main(String[] args) {
        HelloServiceProxy helloServiceProxy = new HelloServiceProxy(new HelloServiceImpl());
        helloServiceProxy.echo("this proxy param");
        helloServiceProxy.time();
    }
}
