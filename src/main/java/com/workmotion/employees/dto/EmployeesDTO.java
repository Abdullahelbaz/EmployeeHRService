package com.workmotion.employees.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Validated
@Data
public class EmployeesDTO implements Serializable {

	private static final long serialVersionUID = 2641606368573670259L;

	@NotNull(message = "employeeName cannot be null")
	private String employeeName;

	@Email
	@NotNull(message = "employeeUserName cannot be null")
	private String employeeUserName;

	private Integer employeeAge;

	private String lastEmployeeSubtatusName;

	@NotNull(message = "employeesStatus cannot be null")
	private EmployeesStatusDTO employeesStatus;

	private String employeeProcessId;

}