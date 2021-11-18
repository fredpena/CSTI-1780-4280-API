package com.pucmm.connection;

import java.io.Serializable;

/**
 *
 * @author Fred Pena <fantpena@gmail.com>
 */
public class JdbcParameter implements Serializable {

    private String url;
    private String user;
    private String password;
    private String driver;

    public String getUrl() {
        return url;
    }

    public JdbcParameter setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUser() {
        return user;
    }

    public JdbcParameter setUser(String user) {
        this.user = user;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public JdbcParameter setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDriver() {
        return driver;
    }

    public JdbcParameter setDriver(String driver) {
        this.driver = driver;
        return this;
    }
}
