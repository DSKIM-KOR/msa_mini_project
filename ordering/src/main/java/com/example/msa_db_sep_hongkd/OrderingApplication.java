package com.example.msa_db_sep_hongkd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;


@EnableKafka
@EnableFeignClients
@SpringBootApplication
public class OrderingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderingApplication.class, args);
	}

}
