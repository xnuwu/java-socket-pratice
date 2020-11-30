package com.jnet.echo;

import java.io.*;

public class App {

    public void echo(String message) {
        System.out.println("echo: " + message);
    }

    public void talk() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String msg = null;

        while ((msg = bufferedReader.readLine()) != null) {
            echo(msg);
            if(msg.equals("bye")) {
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new App().talk();
    }
}
