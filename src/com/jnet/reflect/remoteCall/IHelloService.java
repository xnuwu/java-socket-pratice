package com.jnet.reflect.remoteCall;

import java.util.Date;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public interface IHelloService {

    String echo(String message);

    Date time();
}
