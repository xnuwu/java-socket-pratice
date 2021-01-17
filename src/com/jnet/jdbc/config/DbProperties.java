package com.jnet.jdbc.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class DbProperties {

    static private Properties properties;

    static {
        properties = new Properties();

        try{
            InputStream inputStream = DbProperties.class.getResourceAsStream("/db.properties");
            properties.load(inputStream);
            inputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
