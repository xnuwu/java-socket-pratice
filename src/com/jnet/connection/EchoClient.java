package com.jnet.connection;

import com.jnet.connection.echo.EchoContentHandlerFactory;
import com.jnet.connection.echo.EchoURLConnection;
import com.jnet.connection.echo.EchoURLStreamHandlerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Xunwu Yang 2020-12-29
 * @version 1.0.0
 */
public class EchoClient {

    public static void main(String[] args) throws IOException {
        URL.setURLStreamHandlerFactory(new EchoURLStreamHandlerFactory());
        URLConnection.setContentHandlerFactory(new EchoContentHandlerFactory());

        URL url = new URL("echo://localhost:8000");
        EchoURLConnection connection = (EchoURLConnection) url.openConnection();
        connection.setDoOutput(true);

        PrintWriter printWriter = new PrintWriter(connection.getOutputStream(), true);
        while (true) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String message = bufferedReader.readLine();
            printWriter.println(message);

            String echoMessage = (String) connection.getContent();
            System.out.println(echoMessage);

            if(echoMessage.equals("echo: bye")) {
                connection.disconnect();
                break;
            }
        }
    }
}
