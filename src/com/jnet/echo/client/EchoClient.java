package com.jnet.echo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {

    private String server = "localhost";
    private int port = 8000;
    private Socket socket;

    EchoClient() throws IOException {
        this.socket = new Socket(server, port);
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void talk() {
        try{
            BufferedReader reader = getReader(socket);
            PrintWriter writer = getWriter(socket);
            BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while ((message = localReader.readLine()) != null) {
                System.out.println("send: " + message);
                writer.println(message);

                System.out.println(reader.readLine());

                if(message.equals("bye")) {
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();

            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoClient().talk();
    }
}
