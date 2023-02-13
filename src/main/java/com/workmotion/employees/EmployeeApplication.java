package com.workmotion.employees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2
public class EmployeeApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(EmployeeApplication.class, args);

	}

}
