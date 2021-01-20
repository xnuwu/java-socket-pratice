package com.jnet.jdbc.pool.impl;

import com.jnet.jdbc.pool.ConnectionPool;
import com.jnet.jdbc.pool.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @author Xunwu Yang 2021-01-20
 * @version 1.0.0
 */
public class ConnectionPoolImpl implements ConnectionPool {

    private ConnectionProvider provider = new ConnectionProvider();
    private final LinkedList<Connection> connectionPool = new LinkedList<>();
    private Integer poolSize;

    public ConnectionPoolImpl() {
        this(10);
    }

    public ConnectionPoolImpl(int poolSize) {
        this.poolSize = poolSize;
        System.out.println("connection pool size " + poolSize);
    }

    @Override
    public Connection getConnection() throws SQLException {
        synchronized (connectionPool) {
            if(connectionPool.size() > 0) {
                return connectionPool.removeFirst();
            }
        }

        return provider.getConnection();
    }

    @Override
    public void releaseConnection(Connection connection) throws SQLException {
        synchronized (connectionPool) {
            if(poolSize > connectionPool.size()) {
                connectionPool.add(connection);
                return;
            }
        }

        connection.close();
    }

    @Override
    protected void finalize() {
        close();
    }

    @Override
    public void close() {
        synchronized (connectionPool) {
            for(Connection connection : connectionPool) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
