package com.jnet.socket;

import java.io.*;
import java.net.Socket;

public class HttpClient {

    private String host;
    private int port;

    private Socket socket;

    public HttpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        String host = "www.bing.com";
        int port = 80;
        if(args.length >= 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        System.out.println("res: " + new HttpClient(host, port).createSocket().communicate());
    }

    public HttpClient createSocket() throws IOException {
        if(host != null && host.length() > 0 && port > 0) {
            socket = new Socket(host, port);
            return this;
        }else{
            System.out.println("socket create failed, wrong host:port");
            return null;
        }
    }

    public String buildRequest() {

        StringBuffer requestData = new StringBuffer();
        requestData.append("GET / HTTP/1.1\r\n");
        requestData.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36\r\n");
        requestData.append("Accept: */*\r\n");
        requestData.append("Cache-Control: no-cache\r\n");
        requestData.append("Host: " + host + ":" + port + "\r\n");
        requestData.append("Accept-Encoding: gzip, deflate, br\r\n");
        requestData.append("Connection: keep-alive\r\n\r\n");

        return requestData.toString();
    }

    public String communicate() throws IOException {

        String requestData = buildRequest();

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(requestData.getBytes());
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String data;
        StringBuffer responseData = new StringBuffer();
        while ((data = bufferedReader.readLine()) != null) {
            responseData.append(data + "\r\n");
        }

        socket.close();

        return responseData.toString();
    }
}
