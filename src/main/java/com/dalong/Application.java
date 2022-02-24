package com.dalong;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Application {

    public static void main(String[] args) {

//        DataSource dataSource= MyDataSource.newGenericConnectionPoolDataSource("org.apache.pinot.client.PinotDriver","jdbc:pinot://localhost:9000",null
//        ,null
//        ,null, null,100,60);
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        DataSource dataSource= MyDataSource.newDataSource2();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>>  results= jdbcTemplate.queryForList("select * from baseballStats limit 10");
        results.forEach(new Consumer<Map<String, Object>>() {
            @Override
            public void accept(Map<String, Object> stringObjectMap) {
                System.out.println(stringObjectMap.toString());
            }
        });
    }
}
