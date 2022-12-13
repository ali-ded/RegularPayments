package com.payments.regularpayments;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Регулярные платежи",
        version = "${app.version}",
        description = "API для работы с регулярными платежами внешних систем. " +
                "Регулярный платеж - это платеж, который списывается по регламенту " +
                "(каждый заданный период времени)."))
public class RegularPaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegularPaymentsApplication.class, args);
    }

}
