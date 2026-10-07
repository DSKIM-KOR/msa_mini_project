package com.example.msa_db_sep_hongkd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@EnableFeignClients
@EnableKafka
@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

}
