package com.workmotion.employees.exception;

import org.springframework.http.HttpStatus;

import com.workmotion.employees.controller.error.EmployeeMessage;

import lombok.Getter;

public class EmployeeException extends RuntimeException {

	private static final long serialVersionUID = -1169734628844535962L;

	@Getter
	private String code;

	@Getter
	private String debugMessage;

	@Getter
	private HttpStatus status;

	public EmployeeException() {
		super();
	}

	public EmployeeException(EmployeeMessage msg, Object... values) {
		super(String.format(msg.getBusiness(), values));
		this.status = msg.getStatus();
		this.code = msg.getCode();
		this.debugMessage = String.format(msg.getDebug(), values);
	}

	public EmployeeException(String message) {
		super(message);
	}

	public EmployeeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmployeeException(String message, String debugMessage) {
		super(message);
		this.debugMessage = debugMessage;
	}

	public EmployeeException(String message, Throwable cause, String debugMessage) {
		super(message, cause);
		this.debugMessage = debugMessage;
	}

}
