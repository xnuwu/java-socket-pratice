package com.jnet.http.nio.impl;

import com.jnet.http.nio.Content;
import com.jnet.http.nio.Sendable;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.cert.CRL;

/**
 * @author: yangxunwu
 * @date: 2020/12/22 12:08
 */
public class Response implements Sendable {

    static class Code {
        private int number;
        private String reason;

        private Code(int i, String s) {
            number = i;
            reason = s;
        }

        @Override
        public String toString() {
            return number + " " + reason;
        }

        static Code OK = new Code(200, "0K");
        static Code BAD_REQUEST = new Code(400, "BAD_REQUEST");
        static Code NOT_FOUND = new Code(404, "NOT_FOUND");
        static Code METHOD_NOT_ALLOWED = new Code(405, "METHOD_NOT_ALLOWED");
    }

    private Code code;
    private Content content;
    private boolean headersOnly;
    private ByteBuffer headerBuffer = null;

    public Response(Code rc, Content c) {
        this(rc, c, null);
    }

    public Response(Code code, Content content, Request.Action action) {
        this.code = code;
        this.content = content;
        headersOnly = (action == Request.Action.HEAD);
    }

    private static String CRLF = "\r\n";
    private static Charset responseCharset = Charset.forName("UTF-8");

    private ByteBuffer headers() {
        CharBuffer charBuffer = CharBuffer.allocate(1024);

        while (true) {
            try{
                charBuffer.put("HTTP/1.1").put(code.toString()).put(CRLF);
                charBuffer.put("Server: nio/1.1").put(CRLF);
                charBuffer.put("Content-type: ").put(content.type()).put(CRLF);
                charBuffer.put("Content-length: ").put(Long.toString(content.length())).put(CRLF);
                charBuffer.put(CRLF);
                break;
            }catch (BufferOverflowException e) {
                assert(charBuffer.capacity() < (1 << 16));
                charBuffer = CharBuffer.allocate(charBuffer.capacity() * 2);
                continue;
            }
        }

        charBuffer.flip();
        return responseCharset.encode(charBuffer);
    }


    @Override
    public void prepare() throws IOException {
        content.prepare();
        headerBuffer = headers();
    }

    @Override
    public boolean send(ChannelIo channelIo) throws IOException {
        if(headerBuffer == null) {
            throw new IllegalArgumentException();
        }

        if(headerBuffer.hasRemaining()) {
            if(channelIo.write(headerBuffer) <= 0) {
                return false;
            }
        }

        if(!headersOnly) {
            if(content.send(channelIo)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void release() throws IOException {
        content.release();
    }
}
