package com.jnet.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class Receiver {

    enum InputStopType {
        NORMAL,

        //if server close suddenly, client will continue send data, because system socket is not close instant,
        //socket config SO_RESUSESOCKET can change it
        SUDDEN,

        CLOSE,
        INPUT_STOP
    }

    private int port;
    private InputStopType inputStopType;
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 20;
        InputStopType inputStopType = InputStopType.CLOSE;

        new Receiver(port, inputStopType).receive();
    }

    public Receiver(int port, InputStopType inputStopType) throws IOException {
        this.port = port;
        this.inputStopType = inputStopType;
        this.serverSocket = new ServerSocket(port);
    }

    public BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void receive() throws IOException, InterruptedException {
        Socket socket = serverSocket.accept();
        BufferedReader reader = getReader(socket);

        for(int i = 0; i < 200; i++) {
            Thread.sleep(500);
            String msg = reader.readLine();
            System.out.println("receive: " + msg);

            if(i == 1) {

                if(inputStopType == InputStopType.SUDDEN) {
                    System.out.println("receiver exit " + inputStopType);
                    System.exit(0);
                }else if(inputStopType == InputStopType.INPUT_STOP) {
                    System.out.println("receiver exit " + inputStopType);
                    socket.shutdownInput();
                    break;
                }else if(inputStopType == InputStopType.CLOSE) {
                    System.out.println("receiver exit " + inputStopType);
                    socket.close();
                    break;
                }
            }
        }

        if(inputStopType == InputStopType.NORMAL) {
            System.out.println("receiver exit " + inputStopType);
            socket.close();
            serverSocket.close();
        }
    }

}