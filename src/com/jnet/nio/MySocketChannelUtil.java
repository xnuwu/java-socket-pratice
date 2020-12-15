package com.jnet.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author: yangxunwu
 * @date: 2020/12/15 13:58
 */
public class MySocketChannelUtil {

    public static String readLine(SocketChannel socketChannel) throws IOException {

        //assume line's length less than 1024 byte
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteBuffer tempBuffer = ByteBuffer.allocate(1);

        int readSize;
        String data = null;

        while (true) {
            tempBuffer.clear();
            readSize = socketChannel.read(tempBuffer);

            if (readSize == -1) {
                break;
            }

            if (readSize == 0) {
                continue;
            }

            tempBuffer.flip();
            buffer.put(tempBuffer);

            buffer.flip();
            Charset utf8 = Charset.forName("UTF-8");
            CharBuffer charBuffer = utf8.decode(buffer);
            data = charBuffer.toString();

            if (data.contains("\n")) {
                data = data.substring(0, data.indexOf("\n"));
                break;
            }

            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
        }

        return data;
    }

    public static void write(SocketChannel socketChannel, byte[] data) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);

        buffer.flip();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
