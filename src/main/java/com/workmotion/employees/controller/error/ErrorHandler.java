package com.workmotion.employees.controller.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.workmotion.employees.exception.EmployeeException;

import lombok.extern.slf4j.Slf4j;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(EmployeeException.class)
	public ResponseEntity<Object> handleAPIException(EmployeeException ex) {
		log.error("Business Error thrown: ", ex);
		EmployeeError apiError = new EmployeeError(ex.getStatus(), ex.getCode(), ex.getMessage(), ex);
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex) {
		log.error("Technical Error thrown: ", ex);
		EmployeeError apiError = new EmployeeError(HttpStatus.INTERNAL_SERVER_ERROR, "00000", "Internal System 'Employee' Error", ex);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	
}
