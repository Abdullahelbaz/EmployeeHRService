package com.workmotion.employees.controller.impl;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.workmotion.employees.controller.EmployeeController;
import com.workmotion.employees.controller.error.EmployeeMessage;
import com.workmotion.employees.dto.EmployeesAddDTO;
import com.workmotion.employees.dto.EmployeesDTO;
import com.workmotion.employees.exception.EmployeeException;
import com.workmotion.employees.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "*" }, allowedHeaders = "*")
@RestController
@RequestMapping(path = "/api/employee/")
@Slf4j
public class EmployeeControllerImpl implements EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Override
	public ResponseEntity<?> addEmployee(@Valid EmployeesAddDTO employeesAddDTO, BindingResult bindingResult) {
		bindingResultValidation(bindingResult);
		return ResponseEntity.ok().body(employeeService.addEmployee(employeesAddDTO));
	}

	@Override
	public ResponseEntity<?> getEmployee(String employeeUserName) {

		return ResponseEntity.ok().body(employeeService.getEmployee(employeeUserName));

	}

	@Override
	public ResponseEntity<?> updateStatusEmployee(@Valid EmployeesDTO employeesDTO, BindingResult bindingResult) {
		bindingResultValidation(bindingResult);
		return ResponseEntity.ok().body(employeeService.updateStatusEmployee(employeesDTO));
	}

	public void bindingResultValidation(BindingResult bindingResult) {
		log.info("bindingResultValidation Validation started  ");

		log.info(" bindingResultValidation   bindingResult is " + bindingResult.toString());

		String errorMessage = "";

		if (bindingResult.hasErrors()) {

			log.error(" bindingResultValidation  bindingResult.hasErrors() we will loop for it ");

			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMessage += " ,this field (" + error.getField() + ") has this error ( " + error.getDefaultMessage()
						+ ")";
				log.error("errorMessage" + errorMessage);
			}

			log.error(
					"bindingResultValidation EMPLOYEE_REQUEST_VALIDATION_INCORRECT APIException  with this message  ..."
							+ errorMessage);
			throw new EmployeeException(EmployeeMessage.EMPLOYEE_REQUEST_VALIDATION_INCORRECT, errorMessage);

		} else {
			log.info("bindingResultValidation Validation without any Exceptions End  ");

		}
		log.info("bindingResultValidation Validation End  ");

	}

}
