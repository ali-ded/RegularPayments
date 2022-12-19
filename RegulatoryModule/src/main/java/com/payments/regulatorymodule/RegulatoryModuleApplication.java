package com.payments.regulatorymodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RegulatoryModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegulatoryModuleApplication.class, args);
    }

}
