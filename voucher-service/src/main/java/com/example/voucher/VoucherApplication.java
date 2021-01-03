package com.example.voucher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class VoucherApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoucherApplication.class, args);
	}

}
