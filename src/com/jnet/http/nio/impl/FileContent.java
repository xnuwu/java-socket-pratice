package com.jnet.http.nio.impl;

import com.jnet.http.nio.Content;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author: yangxunwu
 * @date: 2020/12/22 11:47
 */
public class FileContent implements Content {

    public static File ROOT = new File("root");
    private File file;
    private String type;
    private long position;
    private long length;
    private FileContent fileContent;

    public FileContent(URI uri) {
        file = new File(ROOT, uri.getPath().replace('/', File.separatorChar));
    }

    @Override
    public String type() {
        if(type != null) {
            return type;
        }

        String filename = file.getName();
        if(filename.endsWith(".html") || filename.endsWith(".htm")) {
            type = "text/html;charset=utf-8";
        }else if(filename.contains(".") || filename.endsWith(".txt")) {
            type = "text/plain;charset=utf-8";
        }else {
            type = "application/octet-stream";
        }

        return type;
    }

    @Override
    public long length() {
        return 0;
    }

    @Override
    public void prepare() throws IOException {

    }

    @Override
    public boolean send(ChannelIo channelIo) throws IOException {
        return false;
    }

    @Override
    public void release() throws IOException {

    }
}
