package com.jnet.jdbc;

import com.jnet.jdbc.config.DbProperties;

import java.sql.*;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class DbTester {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String driverName = DbProperties.get("driverName");
        String url = DbProperties.get("url");
        String user = DbProperties.get("user");
        String password = DbProperties.get("password");

        Class.forName(driverName);
        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM asr_result limit 5");

        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String words = resultSet.getString("words");

            System.out.println(id + " " + words);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}
