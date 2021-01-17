package com.jnet.rmi.dynamicLoad;

import java.io.IOException;
import java.rmi.Remote;
import java.util.Date;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public interface IHelloService {

    String echo(String message) throws IOException;

    Date time() throws Exception;
}
