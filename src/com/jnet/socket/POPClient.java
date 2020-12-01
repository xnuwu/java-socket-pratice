package com.jnet.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class POPClient {

    private String serverHost;
    private Integer serverPort;

    private String username;
    private String password;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public static void main(String[] args) throws IOException {
        POPClient popClient = new POPClient("mail server host", 110)
                .auth("username", "password")
                .command("stat", null)
                .command("stat", null);

        popClient.readAll();
    }

    private POPClient readAll() throws IOException {

        writer.println("stat");
        writer.flush();

        String message = reader.readLine();
        String[] messageArr = message.split(" ");
        int mailCount = Integer.parseInt(messageArr[1]);
        for(int i = 1; i < mailCount; i++) {
            writer.println("retr " + i);
            writer.flush();
            System.out.println("------------------ The 1 mail detail ------------------");
            while (true) {
                String data = reader.readLine();
                System.out.println(data);
                if(data.toLowerCase().equals(".")) {
                    break;
                }
            }
            System.out.println("");
        }

        return this;
    }

    public POPClient(String serverHost, Integer serverPort) throws IOException {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        socket = new Socket(serverHost, serverPort);
        System.out.println("try connecting to " + serverHost + ":" + serverPort);

        writer = new PrintWriter(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println(reader.readLine());
    }



    public POPClient auth(String username, String password) throws IOException {
        this.username = username;
        this.password = password;

        writer.println("user " + username);
        writer.flush();

        String message = reader.readLine();

        if(message != null) {
            StringTokenizer st = new StringTokenizer(message, " ");
            if(st.nextToken().equalsIgnoreCase("+OK")) {
                System.out.println("connect success! res:" + message);
            }
        }

        writer.println("pass " + password);
        writer.flush();

        message = reader.readLine();

        if(message != null) {
            StringTokenizer st = new StringTokenizer(message, " ");
            if(st.nextToken().equalsIgnoreCase("+OK")) {
                System.out.println("auth success! res:" + message);
            }
        }

        return this;
    }

    public POPClient command(String cmd, String param) throws IOException {

        String request = cmd;
        if(cmd != null && param != null) {
            request += " ";
            request += param;
        }
        writer.println(request);
        writer.flush();

        String message = reader.readLine();
        System.out.println(request + " => " + message);

        return this;
    }

    public PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    public BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}

/**
 * 邮件消息类
 */
class Message {

    /**
     * 发信人
     */
    private String from;

    /**
     * 收信人
     */
    private String to;

    /**
     * 主体
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 数据
     */
    private String data;

    public Message(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.data = "Subject:" + subject + "\r\n" + content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}