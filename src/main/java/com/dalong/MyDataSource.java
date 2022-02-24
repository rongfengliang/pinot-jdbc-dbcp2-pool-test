package com.dalong;


import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Objects;
import java.util.Properties;

public class MyDataSource {
    public enum CommitMode {
        FORCE_AUTO_COMMIT_MODE, FORCE_MANUAL_COMMIT_MODE, DRIVER_SPECIFIED_COMMIT_MODE,NONE
    }
    public  static  DataSource newDataSource2(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.apache.pinot.client.PinotDriver");
        config.setJdbcUrl("jdbc:pinot://localhost:9000");
        config.setTransactionIsolation("TRANSACTION_READ_UNCOMMITTED");
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        return hikariDataSource;
    }
    public static DataSource newGenericConnectionPoolDataSource(String driver, String url, String username, String password, Properties properties, CommitMode commitMode, int maxIdleConns, long idleTimeSec) {
        Preconditions.checkNotNull(url);
        try {
            Class.forName((String) Preconditions.checkNotNull(driver)).asSubclass(Driver.class);
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IllegalArgumentException(String.format("String '%s' does not denote a valid java.sql.Driver class name.", new Object[]{driver}), e);
        }
        BasicDataSource source = new BasicDataSource();
        source.setMaxTotal(2147483647);
        source.setTestOnBorrow(true);
        source.setDefaultTransactionIsolation(0);
        source.setValidationQueryTimeout(1);
        source.setMaxIdle(maxIdleConns);
        source.setSoftMinEvictableIdleTimeMillis(idleTimeSec);
        source.setTimeBetweenEvictionRunsMillis(10000L);
        source.setNumTestsPerEvictionRun(100);
        source.setDriverClassName(driver);
        source.setUrl(url);
        if (properties != null)
            properties.forEach((name, value) -> source.addConnectionProperty(name.toString(), value.toString()));
        if (username != null)
            source.setUsername(username);
        if (password != null)
            source.setPassword(password);
        source.setDefaultTransactionIsolation(-1);
        if(Objects.nonNull(commitMode)) {
            switch (commitMode) {
                case FORCE_AUTO_COMMIT_MODE:
                    source.setDefaultAutoCommit(Boolean.valueOf(true));
                    break;
                case FORCE_MANUAL_COMMIT_MODE:
                    source.setDefaultAutoCommit(Boolean.valueOf(false));
                    break;
            }
        }
       return source;
    }
}
