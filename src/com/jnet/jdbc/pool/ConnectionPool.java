package com.jnet.jdbc.pool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Xunwu Yang 2021-01-20
 * @version 1.0.0
 */
public interface ConnectionPool {

    public Connection getConnection() throws SQLException;

    public void releaseConnection(Connection connection) throws SQLException;

    public void close();
}
