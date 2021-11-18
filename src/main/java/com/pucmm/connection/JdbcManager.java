package com.pucmm.connection;

import com.j256.ormlite.support.ConnectionSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Fred Pena <fantpena@gmail.com>
 */
public final class JdbcManager {

    private static final String DEFAULT_CONNECTION_NAME = "default";
    private static final Map<String, JdbcConnection> CONNECTIONS = new HashMap();
    private static final Set<String> DRIVER_CLASS = new HashSet();

    public static String getDefaultConnectionName() {
        return DEFAULT_CONNECTION_NAME;
    }

    public static Map<String, JdbcConnection> getConnections() {
        return CONNECTIONS;
    }

    public static void put(String connectionName, JdbcConnection connection) {
        CONNECTIONS.put(connectionName, connection);
    }

    public static void put(String connectionName, JdbcParameter parameter) {
        put(connectionName, new JdbcConnection(parameter));
    }

    public static void put(JdbcConnection connection) {
        put(DEFAULT_CONNECTION_NAME, connection);
    }

    public static void put(JdbcParameter parameter) {
        put(DEFAULT_CONNECTION_NAME, new JdbcConnection(parameter));
    }

    public static JdbcConnection get(String connectionName) {
        return CONNECTIONS.get(connectionName);
    }

    public static JdbcConnection get() {
        return get(DEFAULT_CONNECTION_NAME);
    }

    public static ConnectionSource getConnection() {
        return get(DEFAULT_CONNECTION_NAME).getConnection();
    }

    public static JdbcParameter getParameters(String connectionName) {
        return CONNECTIONS.get(connectionName).getParameter();
    }

    public static void loadClassDriver(String driverName) throws ClassNotFoundException {

        if (!DRIVER_CLASS.contains(driverName)) {
            try {
                Class.forName(driverName);
                DRIVER_CLASS.add(driverName);
            } catch (ClassNotFoundException ex) {
                throw ex;
            }
        }
    }
}
