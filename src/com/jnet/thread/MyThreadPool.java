package com.jnet.thread;

import java.util.LinkedList;

/**
 * simple thread pool implement
 */
public class MyThreadPool extends ThreadGroup {

    private boolean isClosed = false;
    private int poolSize;
    private static int threadGroupId = 1;
    private LinkedList<Runnable> jobQueue = new LinkedList<Runnable>();
    private int threadId = 1;

    public MyThreadPool(int poolSize) {
        super("MyThreadPool-" + (threadGroupId++));
        setDaemon(true);
        this.poolSize = poolSize;
        for(int i = 0; i < poolSize; i++) {
            new WorkThread().start();
        }
    }

    public synchronized void close() {
        if(!isClosed) {
            isClosed = true;
            jobQueue.clear();
            interrupt();
        }
    }

    public void join() {

        synchronized (this) {
            isClosed = true;
            notifyAll();
        }

        Thread[] threads = new Thread[activeCount()];
        System.out.println("active count " + threads.length);
        int count = enumerate(threads);
        for(int i = 0; i < count; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void execute(Runnable task) {

        if(isClosed) {
            return;
        }
        if(task == null) {
            return;
        }

        jobQueue.add(task);
        notify();
    }

    public synchronized Runnable getTask() throws InterruptedException {

        while (jobQueue.size() == 0) {
            if(isClosed) {
                return null;
            }
            wait();
        }

        return jobQueue.removeFirst();
    }

    class WorkThread extends Thread {
        public WorkThread() {
            super(MyThreadPool.this, "WorkThread-" + (threadId++));
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try{
                    Runnable task = getTask();

                    if(task != null) {
                        task.run();
                    }else{
                        return;
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThreadPool myThreadPool = new MyThreadPool(20);

        for(int i = 0; i < 100; i++) {
            myThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread() + " sleep over!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        myThreadPool.join();

        Thread.sleep(10000L);
    }
}
