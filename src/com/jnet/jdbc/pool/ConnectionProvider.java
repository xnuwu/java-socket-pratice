package com.jnet.jdbc.pool;

import com.jnet.jdbc.config.DbProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Xunwu Yang 2021-01-20
 * @version 1.0.0
 */
public class ConnectionProvider {

    public Connection getConnection() throws SQLException {

        String driverName = DbProperties.get("driverName");
        String url = DbProperties.get("url");
        String user = DbProperties.get("user");
        String password = DbProperties.get("password");

        try{
            Class.forName(driverName);
        }catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("database driver class error:" + e.getMessage());
        }

        return DriverManager.getConnection(url, user, password);
    }
}
