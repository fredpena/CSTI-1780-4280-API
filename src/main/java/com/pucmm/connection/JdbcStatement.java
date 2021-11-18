package com.pucmm.connection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcStatement<T, ID> {

    private static final Logger LOG = LogManager.getLogger(JdbcStatement.class.getName());

    private Dao<T, ID> dao;

    public JdbcStatement(JdbcConnection jdbcConnection, Class<T> clazz) {
        try {
            dao = DaoManager.createDao(jdbcConnection.getConnection(), clazz);
        } catch (SQLException e) {
            LOG.error(e);
        }
    }

    public Dao<T, ID> getDao() {
        return dao;
    }

    public void create(T object) throws SQLException {
            dao.create(object);
    }

    public void update(T object) throws SQLException {
            dao.update(object);
    }

    public void delete(T object) throws SQLException {
            dao.delete(object);
    }

    public T queryForId(ID id) throws SQLException {
            return dao.queryForId(id);
    }
    public Collection<T> queryForAll() throws SQLException {
            return dao.queryForAll();
    }
}
