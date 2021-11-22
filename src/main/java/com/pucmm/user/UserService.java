package com.pucmm.user;

import com.j256.ormlite.stmt.QueryBuilder;
import com.pucmm.connection.JdbcConnection;
import com.pucmm.connection.JdbcManager;
import com.pucmm.connection.JdbcStatement;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

// This is a service, it should be independent from Javalin
public class UserService extends JdbcStatement<User, Integer> {

    private static UserService sIntance;


    public static UserService getInstance() {
        return new UserService();
    }

    private UserService() {
        super(JdbcManager.get(), User.class);
    }

    public User queryForEmail(String email) throws SQLException {
        final QueryBuilder<User, Integer> statementBuilder = getDao().queryBuilder();
        statementBuilder.where().eq("email", email);

        return getDao().query(statementBuilder.prepare()).stream().findFirst().orElse(null);
    }

    public User login(String email, String password) throws SQLException {
        final QueryBuilder<User, Integer> statementBuilder = getDao().queryBuilder();
        statementBuilder.where().eq("email", email).and().eq("password", password);

        return getDao().query(statementBuilder.prepare()).stream().findFirst().orElse(null);
    }
}
