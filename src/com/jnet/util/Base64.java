package com.jnet.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64 {

    public static String encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    public static byte[] decode(String buffer) throws IOException {
        return new BASE64Decoder().decodeBuffer(buffer);
    }
}
