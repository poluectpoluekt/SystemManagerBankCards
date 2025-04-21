package com.ed.sysbankcards;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "System Management Card - application",
                version = "1.0.0",
                description = "Тестовое задание для кандидата на вакансию Java-разработчика."
        )
)
public class SysbankcardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysbankcardsApplication.class, args);
    }

}
