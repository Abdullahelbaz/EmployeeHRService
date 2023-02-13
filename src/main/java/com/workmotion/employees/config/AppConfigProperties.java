package com.workmotion.employees.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "")
@Getter
@Order(value = 1)
public class AppConfigProperties {

	// @Value("${employee.value}")
	// Float employee;

}
