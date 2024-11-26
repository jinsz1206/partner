package com.jsz.partner_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jsz.partner_backend.mapper")
public class PartnerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartnerBackendApplication.class, args);
    }

}
