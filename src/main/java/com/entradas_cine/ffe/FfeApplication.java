package com.entradas_cine.ffe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FfeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FfeApplication.class, args);
    }

}
