package com.jnet.socket;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class Sender {

    enum OutputStopType {

        NORMAL,

        //server will throw SocketException: Connection reset
        SUDDEN,

        //server will receive null
        CLOSE,
        OUTPUT_STOP
    }

    private String host;
    private int port;
    private Socket socket;
    private OutputStopType outputStopType;

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "127.0.0.1";
        int port = 20;
        OutputStopType outputStopType = OutputStopType.SUDDEN;

        new Sender(host, port, outputStopType).send();
    }

    public Sender(@NotNull String host, @NotNull Integer port, @NotNull OutputStopType outputStopType) throws IOException {
        this.host = host;
        this.port = port;
        this.outputStopType = outputStopType;
        this.socket = new Socket(host, port);
    }

    public PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    public void send() throws IOException, InterruptedException {
        PrintWriter writer = getWriter(socket);

        String msgPrefix = "hello, now is ";
        for(int i = 0; i < 100; i++) {
            String msg = msgPrefix + i;
            writer.println(msg);
            writer.flush();

            System.out.println("send: " + msg);
            Thread.sleep(200);

            if(i == 4) {

                if(outputStopType == OutputStopType.SUDDEN) {
                    System.out.println("sender exit " + outputStopType);
                    System.exit(0);
                }else if(outputStopType == OutputStopType.OUTPUT_STOP) {
                    System.out.println("sender exit " + outputStopType);
                    socket.shutdownOutput();
                    break;
                }else if(outputStopType == OutputStopType.CLOSE) {
                    System.out.println("sender exit " + outputStopType);
                    socket.close();
                    break;
                }
            }
        }

        if(outputStopType == OutputStopType.NORMAL) {
            System.out.println("sender exit " + outputStopType);
            socket.close();
        }
    }
}
