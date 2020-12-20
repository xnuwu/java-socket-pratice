package com.jnet.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * ByteBuffer与字符串的编码转换
 */
public class ByteBufferCodec {

    private static Charset defaultCharset = Charset.forName("UTF-8");

    public static ByteBuffer encode(String string) {
        return encode(string, defaultCharset);
    }

    public static ByteBuffer encode(String string, Charset charset) {
        return charset.encode(string);
    }

    public static String decode(ByteBuffer byteBuffer) {
        return decode(byteBuffer, defaultCharset);
    }

    public static String decode(ByteBuffer byteBuffer, Charset charset) {
        CharBuffer charBuffer = charset.decode(byteBuffer);
        return charBuffer.toString();
    }
}
