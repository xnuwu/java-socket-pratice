package com.jnet.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

public class PingClient {

    private Boolean shutdown = false;
    private Selector selector;
    final private LinkedList<Target> readyConnectTargetList = new LinkedList<>();
    final private LinkedList<Target> connectFinishedTargetList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        new PingClient();
    }

    public PingClient() throws IOException {

        selector = Selector.open();
        Connector connector = new Connector();
        Printer printer = new Printer();

        connector.start();
        printer.start();
        receiveTarget();
    }

    private void receiveTarget() throws IOException {
        System.out.println("please input a domain name for test ping.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message;

        while ((message = reader.readLine()) != null) {
            if(message.trim().equals("bye")) {
                shutdown = true;
                break;
            }

            addReadyTarget(message);
        }
    }

    private void addReadyTarget(String host) {
        System.out.println("try connect " + host);
        Target target = new Target(host);

        try{
            target.setBeginTime(System.currentTimeMillis());
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(target.getTarget());
            target.setChannel(channel);

            synchronized (readyConnectTargetList) {
                readyConnectTargetList.add(target);
            }
            selector.wakeup();
        }catch (Exception e) {
            e.printStackTrace();
            target.setE(e);
            addFinishTarget(target);
        }
        System.out.println("added target to read list");
    }

    private void addFinishTarget(Target target) {
        synchronized (connectFinishedTargetList) {
            connectFinishedTargetList.add(target);
            connectFinishedTargetList.notifyAll();
        }
    }

    class Connector extends Thread {
        @Override
        public void run() {
            while (!shutdown) {
                try{
                    processRegister();
                    if(selector.select() > 0) {
                        processConnect();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void processRegister() {
            while (readyConnectTargetList.size() > 0) {
                Target target = readyConnectTargetList.removeFirst();
                try {
                    System.out.println("try register " + target.getTarget().getHostName());
                    target.getChannel().register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, target);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                    target.setE(e);
                    addFinishTarget(target);
                }

                System.out.println("registered " + target.getTarget().getHostName());
            }
        }

        private void processConnect() {
            System.out.println("process connect");
            try{
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();
                    Target target = (Target) selectionKey.attachment();
                    try{
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        keyIterator.remove();

                        if(socketChannel.finishConnect()) {
                            target.setEndTime(System.currentTimeMillis());

                            selectionKey.cancel();
                            target.getChannel().close();

                            addFinishTarget(target);
                        }else{
                            System.out.println(target.getTarget().getHostName() + " not finish connect");
                        }
                    }catch (IOException e) {
                        target.setE(e);
                        addFinishTarget(target);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("processed connect");
        }
    }

    class Printer extends Thread {

        public Printer() {
            setDaemon(true);
        }

        @Override
        public void run() {
            try{
                while (true) {
                    System.out.println("start print finished target");
                    synchronized (connectFinishedTargetList) {
                        while (connectFinishedTargetList.isEmpty()) {
                            connectFinishedTargetList.wait();
                        }
                        System.out.println(connectFinishedTargetList.removeFirst());
                    }
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Target {

    private InetSocketAddress target;
    private Long beginTime;
    private Long endTime = null;
    private Exception e = null;
    private SocketChannel channel;

    public Target(String host) {
        target = new InetSocketAddress(host, 80);
    }

    public InetSocketAddress getTarget() {
        return target;
    }

    public void setTarget(InetSocketAddress target) {
        this.target = target;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {

        if(e != null) {
            return target.getHostName() + " error: " + e.getMessage();
        }

        if(endTime != null) {
            return target.getHostName() + " " + (endTime - beginTime) + "ms";
        }

        return target.getHostName() + " timeout";
    }
}
