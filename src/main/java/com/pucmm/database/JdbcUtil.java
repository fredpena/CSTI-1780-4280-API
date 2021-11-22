package com.pucmm.database;

import com.pucmm.connection.JdbcManager;
import com.pucmm.connection.JdbcParameter;
import com.pucmm.connection.JdbcStatement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Fred Pena <fantpena@gmail.com>
 */
public final class JdbcUtil {

    private static final Logger LOG = LogManager.getLogger(JdbcUtil.class.getName());

    private JdbcUtil() {
    }

    public static void initialize() {
        try {
            JdbcParameter pConnection = new JdbcParameter();
            pConnection.setUrl("jdbc:mysql://127.0.0.1:3306/test");
            //pConnection.setUrl("jdbc:mysql://127.0.0.1:3306/sakila");
            pConnection.setUser("root");
            //pConnection.setPassword("0410");
            pConnection.setPassword("04118910");
            JdbcManager.loadClassDriver("com.mysql.cj.jdbc.Driver");
            JdbcManager.put(pConnection);
        } catch (ClassNotFoundException e) {
            LOG.error("Error in one of the Database Connection Settings");
            System.exit(0);
        }
    }
}
