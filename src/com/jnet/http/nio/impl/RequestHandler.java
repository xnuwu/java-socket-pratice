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
        try{
            if(request == null) {

                if(!receive()) {
                    return;
                }

                requestByteBuffer.flip();

            }else{

                //结束数据读取

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
            return true;
        }

        if(channelIo.read() < 0 || Request.isComplete(channelIo.getReadByteBuffer())) {
            requestByteBuffer = channelIo.getReadByteBuffer();
            return (requestReceived = true);
        }

        return false;
    }


    public void build() {
        Request.Action action = request.getAction();

        if(action != Request.Action.GET && action != Request.Action.HEAD) {
            response = new Response(Response.Code.METHOD_NOT_ALLOWED, new StringContent("Method Not Allowed"));
        }else{
            response = new Response(Response.Code.OK, new FileContent(request.getUri()), action);
        }
    }
}