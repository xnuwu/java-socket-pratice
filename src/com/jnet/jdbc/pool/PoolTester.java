package com.jnet.jdbc.pool;

import com.jnet.jdbc.pool.impl.ConnectionPoolImpl;

import java.sql.Connection;
import java.sql.Statement;

/**
 * @author Xunwu Yang 2021-01-20
 * @version 1.0.0
 */
public class PoolTester implements Runnable {

    private ConnectionPool connectionPool = new ConnectionPoolImpl(100);

    public static void main(String[] args) throws InterruptedException {
        PoolTester poolTester = new PoolTester();
        Thread[] threads = new Thread[100];
        for(int i = 0; i < 100; i++) {
            Thread thread = new Thread(poolTester);
            thread.start();
            Thread.sleep(200);
            threads[i] = thread;
        }

        for(int i = 0; i < 100; i++) {
            threads[i].join();
        }

        poolTester.connectionPool.close();
    }

    @Override
    public void run() {
        try{
            Connection connection = connectionPool.getConnection();
            Statement statement = connection.createStatement();
            for(int i = 0; i < 5; i++) {
                statement.executeUpdate("INSERT INTO asr_result(sip_call_id) value ('" + Thread.currentThread().getId() + "-" + i + "');");
            }
            statement.close();
            connectionPool.releaseConnection(connection);
            System.out.println("线程" + Thread.currentThread() + "释放连接" + connection);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
