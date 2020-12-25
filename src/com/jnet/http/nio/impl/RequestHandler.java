package com.jnet.http.nio.impl;

import com.jnet.http.nio.IHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * 当程序可读后（OP_READ），读取所有的请求数据，但是只保留Header，目前暂不支持POST等带payload的请求方法
 * 解析对应的请求URL后，通过注册（OP_WRITE）到Selector中，将对应的文件数据返回给客户端，当前类同时处理
 * OP_READ、OP_WRITE的读写操作
 *
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class RequestHandler implements IHandler {

    private ChannelIo channelIo;

    private boolean requestReceived;
    private ByteBuffer requestByteBuffer;

    private Request request;
    private Response response;

    public RequestHandler(ChannelIo channelIo) {
        requestReceived = false;
        this.channelIo = channelIo;
    }

    @Override
    public void handle(SelectionKey selectionKey) {
        System.out.println("connection writable " + (selectionKey.isWritable() ? "true" : "false") + ", readable " + (selectionKey.isReadable() ? "true" : "false"));
        try{
            if(request == null) {

                if(!receive()) {
                    return;
                }
                System.out.println("received finished");

                requestByteBuffer.flip();
                if(parse()) {
                    build();
                }else{
                    System.out.println("parse failed");
                }

                try{
                    response.prepare();
                }catch (Exception e) {
                    response.release();
                    response = new Response(Response.Code.NOT_FOUND, new StringContent(e.getMessage()));
                    response.prepare();
                }

                if(send()) {
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }else{
                    channelIo.close();
                    response.release();
                }

            }else{
                //结束数据读取
                if(!send()) {
                    channelIo.close();
                    response.release();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            try {
                channelIo.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if(response != null) {
                try {
                    response.release();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private boolean receive() throws IOException {
        if(requestReceived) {
            System.out.println("requestReceived finished");
            return true;
        }

        int readBytes = channelIo.read();
        System.out.println("receive " + readBytes + " bytes");
        if(readBytes < 0 || Request.isComplete(channelIo.getReadByteBuffer())) {
            requestByteBuffer = channelIo.getReadByteBuffer();
            return (requestReceived = true);
        }

        System.out.println("requestReceived not finished");
        return false;
    }

    private boolean parse() {
        request = Request.parse(requestByteBuffer);
        return true;
    }

    public void build() {
        Request.Action action = request.getAction();

        if(action != Request.Action.GET && action != Request.Action.HEAD) {
            response = new Response(Response.Code.METHOD_NOT_ALLOWED, new StringContent("Method Not Allowed"));
        }else{
            response = new Response(Response.Code.OK, new FileContent(request.getUri()), action);
        }
    }

    private boolean send() throws IOException {
        System.out.println("start send response data");
        return response.send(channelIo);
    }
}