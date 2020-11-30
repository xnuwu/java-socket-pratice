package com.jnet.echo.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    private int port = 8000;

    private ServerSocket serverSocket;

    EchoServer() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("echo server started with port " + port);
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        return new PrintWriter(outputStream, true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private String echo(String message) {
        return "echo: " + message;
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try{
                socket = serverSocket.accept();
                System.out.println("new connected from " + socket.getInetAddress() + ":" + socket.getPort());
                PrintWriter writer = getWriter(socket);
                BufferedReader reader = getReader(socket);

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("receive: " + message);
                    writer.println(echo(message));

                    if(message.equals("bye")) {
                        break;
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();

                if(socket != null) {
                    try {
                        System.out.println("close connected from " + socket.getInetAddress() + ":" + socket.getPort());
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoServer().service();
    }
}
