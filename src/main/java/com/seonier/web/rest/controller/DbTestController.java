package com.seonier.web.rest.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbTestController {

    private final JdbcTemplate jdbcTemplate;

    public DbTestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db-test")
    public String dbTest() {
        // DB에 실제로 접속해서 SELECT 1 실행
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return "DB 연결 성공, SELECT 1 결과 = " + result;
    }
}