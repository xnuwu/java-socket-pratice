package com.jnet.connection;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * httpClient实现
 * @author Xunwu Yang 2020-12-28
 * @version 1.0.0
 */
public class HttpClient {

    public static String get(String targetUrl) throws IOException {
        URL url = new URL(targetUrl);

        InputStream inputStream = url.openStream();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int readDateLen;

        while ((readDateLen = inputStream.read(buff)) != -1) {
            buffer.write(buff, 0, readDateLen);
        }


        return buffer.toString();
    }

    public static void downloadImageByUrl(String imageUrl, String saveFile) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();

        System.out.println("正文类型(guess): " + URLConnection.guessContentTypeFromName(imageUrl));
        System.out.println("正文类型:" + connection.getContentType());
        System.out.println("正文长度:" + connection.getContentLength());
        System.out.println("HTTP请求头:");
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        headerFields.forEach((key, value) -> {
            value.forEach(itemValue -> {
                System.out.println(key + ":" + itemValue);
            });
        });
        System.out.println();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int readByteLen;

        InputStream inputStream = connection.getInputStream();
        System.out.println("正文类型(guess from stream): " + URLConnection.guessContentTypeFromStream(inputStream));
        while ((readByteLen = inputStream.read(buff)) != -1) {
            buffer.write(buff, 0, readByteLen);
        }

        File imageFile = new File(saveFile);
        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
        fileOutputStream.write(buffer.toByteArray(), 0, buffer.size());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static void main(String[] args) throws IOException {
        HttpClient.downloadImageByUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png", "D:/1.png");
    }
}
