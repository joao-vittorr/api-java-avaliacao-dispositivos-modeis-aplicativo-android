package com.example.gestao_consultas;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestaoConsultasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoConsultasApplication.class, args);
	}
}
