package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication

@OpenAPIDefinition(
        info = @Info(
                title = "EMS Controllers ",
                version = "1.0",
                description = "API for EMS",
                contact = @Contact(name = "Kirankumar Kommu", 
                email = "Kommukirankumar1226@gmail.com")
        ),
        servers = @Server(url = "http://localhost:1234")
)
public class EmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmsApplication.class, args);
	}

}
