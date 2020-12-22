package com.jnet.http.nio.impl;

import com.jnet.util.ByteBufferCodec;
import com.sun.jndi.toolkit.url.Uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class Request {

    static class Action {

        private String name;

        private Action(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        static Action GET = new Action("GET");
        static Action PUT = new Action("PUT");
        static Action POST = new Action("POST");
        static Action HEAD = new Action("HEAD");

        public static Action parse(String string) {
            if(string.equals("GET")) {
                return GET;
            }else if(string.equals("PUT")) {
                return PUT;
            }else if(string.equals("POST")) {
                return POST;
            }else if(string.equals("HEAD")) {
                return HEAD;
            }

            throw new IllegalArgumentException(string);
        }
    }

    private Action action;
    private String version;
    private URI uri;

    public Action getAction() {
        return action;
    }

    public String getVersion() {
        return version;
    }

    public URI getUri() {
        return uri;
    }

    public Request(Action action, String version, URI uri) {
        this.action = action;
        this.version = version;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Request{" +
                "action=" + action +
                ", version='" + version + '\'' +
                ", uri=" + uri +
                '}';
    }

    /**
     * only receive header
     * @param byteBuffer
     * @return
     */
    public static boolean isComplete(ByteBuffer byteBuffer) {
        String decodeData = ByteBufferCodec.decode(byteBuffer);
        return decodeData.contains("\r\n\r\n");
    }

    /**
     * 删除请求正文body内容
     * @param byteBuffer
     * @return
     */
    private static ByteBuffer deleteContent(ByteBuffer byteBuffer) {
        String decodeData = ByteBufferCodec.decode(byteBuffer);
        if(decodeData.contains("\r\n\r\n")) {
            return ByteBufferCodec.encode(decodeData.substring(0, decodeData.indexOf("\r\n\r\n")));
        }

        return byteBuffer;
    }

    private static final Pattern requestPattern = Pattern.compile("\\A([A-Z]+) +[^ ] +HTTP/([0-9\\.]+)$"
                                                                    + ".*^Host:([^ ]+)$.*\r\n\r\n\\z", Pattern.MULTILINE | Pattern.DOTALL);

    public static Request parse(ByteBuffer byteBuffer) {
        byteBuffer = deleteContent(byteBuffer);
        String headerString = ByteBufferCodec.decode(byteBuffer);
        Matcher matcher = requestPattern.matcher(headerString);
        if(!matcher.matches()) {
            throw new IllegalArgumentException(headerString);
        }

        Action a = null;

        try{
            a = Action.parse(matcher.group(1));
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        URI u = null;

        try{
            u = new URI("http://" + matcher.group(4) + matcher.group(2));
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new Request(a, matcher.group(3), u);
    }
}
