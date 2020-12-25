package com.jnet.http.nio.impl;

import com.jnet.http.nio.Content;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;

/**
 * @author: yangxunwu
 * @date: 2020/12/22 11:47
 */
public class FileContent implements Content {

    public static File ROOT = new File("E:\\git\\java-socket-pratice\\src\\com\\jnet\\http");
    private File file;
    private String type;
    private long position;
    private long length;
    private FileChannel fileChannel = null;

    public FileContent(URI uri) {
        file = new File(ROOT, uri.getPath().replace('/', File.separatorChar));
        System.out.println("fileContent:" + file.getAbsolutePath());
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
        return length;
    }

    @Override
    public void prepare() throws IOException {
        if(fileChannel == null) {
            fileChannel = new RandomAccessFile(file, "r").getChannel();
        }

        length = fileChannel.size();
        position = 0;
    }

    @Override
    public boolean send(ChannelIo channelIo) throws IOException {
        if(fileChannel == null) {
            throw new IllegalArgumentException();
        }

        if(position < 0) {
            throw new IllegalArgumentException();
        }

        if(position >= length) {
            return false;
        }

        position += channelIo.transferTo(fileChannel, position, length - position);
        return (position < length);
    }

    @Override
    public void release() throws IOException {
        if(fileChannel != null) {
            fileChannel.close();
            fileChannel = null;
        }
    }
}
