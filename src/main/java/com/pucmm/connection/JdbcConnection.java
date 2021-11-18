package com.pucmm.connection;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Fred Pena <fantpena@gmail.com>
 */
public class JdbcConnection {

    private static final Logger LOG = LogManager.getLogger(JdbcConnection.class.getName());

    private ConnectionSource connection = null;
    private JdbcParameter parameter = null;

    private JdbcConnection() {
    }

    public JdbcConnection(final JdbcParameter parameter) {
        this.parameter = parameter;
    }

    private boolean createConnection() throws Exception {
        if (parameter == null) {
            throw new Exception("You must set connection parameters");
        }

        try {

            LOG.info("Successfully loaded driver!");

            connection = new JdbcConnectionSource(parameter.getUrl(),parameter.getUser(),parameter.getPassword());

            LOG.info("Connection Established Successfully!");

            return true;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public ConnectionSource getConnection() {
        if (!isConected()) {
            reconnet();
        }
        return connection;
    }

    private void reconnet() {
        try {
            createConnection();
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    private boolean isConected() {
        if (connection == null) {
            return false;
        }
        return connection.isOpen(null);
    }

    public void close() {
        try {
            connection.close();
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }

    public JdbcParameter getParameter() {
        return parameter;
    }
}
