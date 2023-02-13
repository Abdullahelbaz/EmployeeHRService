package com.workmotion.employees.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Validated
@Data
public class EmployeesStatusDTO implements Serializable {

	private static final long serialVersionUID = -6916785252855955555L;

	private Long employeeStatusId;

	@NotNull(message = "employeeStatusName cannot be null")
	private String employeeStatusName;

}