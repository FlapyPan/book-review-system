package com.example.bookreviewsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.bookreviewsystem.mapper") // mybatis mapper 接口路径
public class BookReviewSystem {

    public static void main(String[] args) {
        SpringApplication.run(BookReviewSystem.class, args);
    }

}
