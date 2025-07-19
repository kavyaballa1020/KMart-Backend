package com.kmart.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication//localhost:8761
@EnableEurekaServer

public class KmartEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmartEurekaServerApplication.class, args);
	}

}
