package com.jnet.nio;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author: yangxunwu
 * @date: 2020/12/8 7:57
 */
public class MyCharset {

    public static void main(String[] args) {
        String enData = "yangxunwu";
        String data = "杨训武";
        Charset gbk = Charset.forName("GBK");
        Charset utf8 = Charset.forName("UTF-8");

        ByteBuffer gbkBuffer = gbk.encode(data);
        printBuffer(gbkBuffer);
        System.out.println();

        ByteBuffer utf8Buffer = utf8.encode(data);
        printBuffer(utf8Buffer);
        System.out.println();

        ByteBuffer gbkEnBuffer = gbk.encode(enData);
        printBuffer(gbkEnBuffer);
        System.out.println();

        ByteBuffer utf8EnBuffer = utf8.encode(enData);
        printBuffer(utf8EnBuffer);
    }

    public static void printBuffer(ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
