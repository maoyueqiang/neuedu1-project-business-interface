package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neuedu.dao")
public class BusinessInterfaceApplication {

	public static void main(String[] args) {
		System.out.println("hello world");
		SpringApplication.run(BusinessInterfaceApplication.class, args);
	}

}
