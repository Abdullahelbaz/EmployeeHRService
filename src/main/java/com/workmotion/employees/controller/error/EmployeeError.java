package com.workmotion.employees.controller.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.workmotion.employees.exception.EmployeeException;

import lombok.Data;

@Data
class EmployeeError {

	   private HttpStatus status;
	   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	   private LocalDateTime timestamp;
	   private String code;
	   private String message;
	   private String debugMessage;
	   
	   public EmployeeError(EmployeeMessage msg) {
		   this.code = msg.getCode();
		   this.message = msg.getBusiness();
		   this.debugMessage = msg.getDebug();
	   }

	   private EmployeeError() {
	       timestamp = LocalDateTime.now();
	   }

	   EmployeeError(HttpStatus status, Throwable ex) {
	       this();
	       this.status = status;
	       this.message = "Unexpected error";
	       this.debugMessage = ex instanceof EmployeeException ? ((EmployeeException)ex).getDebugMessage() : ex.getLocalizedMessage();
	   }

	   EmployeeError(HttpStatus status, String code, String message, Throwable ex) {
	       this();
	       this.code = code;
	       this.status = status;
	       this.message = message;
	       this.debugMessage = ex instanceof EmployeeException ? ((EmployeeException)ex).getDebugMessage() : ex.getLocalizedMessage();
	   }
	}