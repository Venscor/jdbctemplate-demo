package com.venscor.demo;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.*;

public class SpringJdbcTemplateDemo {

    public static void main(String[] args) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/demo");
        dataSource.setUsername("root");
        dataSource.setPassword("nipc1404");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        //首先，jdbcTemplate是支持 -- 注释的
        List res = jdbcTemplate.queryForList("select id from school -- llll");


        Map map = new HashMap();
        map.put("name","xd");



        //namedParameterJdbcTemplate 也是支持 -- 注解的
        //name='xd' or 1=1 -- name=
        List res1 = namedParameterJdbcTemplate.queryForList("select id from school where name='xd' or 1=1 -- name=:name",map);


        // where处注入测试,首先看到:name 方式是采用了预编译的
        map.put("name","xd' --");
        List res2 = namedParameterJdbcTemplate.queryForList("select id from school where name=:name",map);



        Map map2 = new HashMap();
        // int型注入在强类型语言一定不存在，非数字时，取的是第一个字母
        map2.put("id","1 or 1=1 --");
        List res3 = namedParameterJdbcTemplate.queryForList("select id from school where id=:id",map2);


        // payload1
//        String dbKeyName = "id = '2' or 1=1 -- id"; // under control
//        String keyName = "id";

        // payload2
        String dbKeyName = "id"; // under control
        String keyName = "id or 1=1 --";


        String sql = "select name from school where " + String.format("%s=:%s", dbKeyName, keyName);
        School school = new SpringJdbcTemplateDemo().new School();
        school.id = 1; //正常这个地方接收参数
        //        NamedParameterJdbcTemplate
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(school);
        List res4 = namedParameterJdbcTemplate.queryForList(sql, sqlParameterSource);

        System.out.println();
    }


    @Data
    public class School {
        private int id;
        private String name;
        private String address;
    }

}
