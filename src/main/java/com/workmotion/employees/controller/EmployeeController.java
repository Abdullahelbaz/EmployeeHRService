package com.workmotion.employees.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.workmotion.employees.dto.EmployeesAddDTO;
import com.workmotion.employees.dto.EmployeesDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "Employee API" })
public interface EmployeeController {

	@ApiOperation("Add Employee")
	@PostMapping(value = "")
	public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeesAddDTO employeesAddDTO,
			BindingResult bindingResult);

	@ApiOperation("find Employee")
	@GetMapping(value = "")
	public ResponseEntity<?> getEmployee(@RequestParam(required = true) String employeeUserName);

	@ApiOperation("update status Employee")
	@PutMapping(value = "")
	public ResponseEntity<?> updateStatusEmployee(@Valid @RequestBody EmployeesDTO employeesDTO,
			BindingResult bindingResult);

}
