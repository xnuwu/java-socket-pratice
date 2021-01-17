package com.jnet.rmi.concurrent;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class StackClient {
    public static void main(String[] args) throws NamingException {
        Context context = new InitialContext();
        Stack stack = (Stack) context.lookup("rmi://localhost/myStack");

        Producer producer1 = new Producer(stack, "producer-1");
        Producer producer2 = new Producer(stack, "producer-2");
        Consumer consumer1 = new Consumer(stack, "consumer-1");
        Consumer consumer2 = new Consumer(stack, "consumer-2");
    }
}

class Producer extends Thread {
    private Stack stack;

    public Producer(Stack stack, String name) {
        super(name);
        this.stack = stack;
        start();
    }

    @Override
    public void run() {
        while (true) {

            try {
                int point = stack.getPoint();
                String good = "good-" + (point + 1);
                stack.push(good);
                System.out.println(System.currentTimeMillis() + ": produce " + good);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {
    private Stack stack;

    public Consumer(Stack stack, String name) {
        super(name);
        this.stack = stack;
        start();
    }

    @Override
    public void run() {
        while (true) {

            try{
                String good = stack.pop();
                System.out.println(System.currentTimeMillis() + ": consume " + good);
            }catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}