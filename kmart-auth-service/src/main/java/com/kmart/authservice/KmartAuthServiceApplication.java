package com.kmart.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



@SpringBootApplication
@EnableDiscoveryClient
public class KmartAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmartAuthServiceApplication.class, args);
	}
	
	  

}
