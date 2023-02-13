package com.workmotion.employees.controller.error;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class EmployeeMessage {

	private HttpStatus status;

	private String code;

	private String business;

	private String debug;

	public EmployeeMessage(HttpStatus status, String code, String business, String debug) {
		this.status = status;
		this.code = code;
		this.business = business;
		this.debug = debug;
	}

	public static final EmployeeMessage EMPLOYEE_ALLREADY_EXIST_BY_SAME_USERNAME = new EmployeeMessage(//
			HttpStatus.CONFLICT//
			, "EMP-REQ-0001"//
			, "Exception In Add Employee, this username [%s] allready exist!"//
			, "please note this user added before [%s] ");

	public static final EmployeeMessage EMPLOYEE_NOT_EXIST = new EmployeeMessage(HttpStatus.NOT_FOUND, "EMP-REQ-0002",
			"THIS username [%s] No found !", //
			"please add this employee before update the status!");

	public static final EmployeeMessage UPDATE_STATUS_NOT_COMPLETED_SEE_THE_REASON_ON_MESSAGE = new EmployeeMessage(//
			HttpStatus.CONFLICT//
			, "EMP-REQ-0003"//
			, "Exception on Update the status for this user, due to this reason [%s] !"//
			, "Exception on Update the status for this user, due to this reason [%s] !");

	public static final EmployeeMessage EMPLOYEE_REQUEST_VALIDATION_INCORRECT = new EmployeeMessage(//
			HttpStatus.BAD_REQUEST//
			, "EMP-REQ-0004"//
			, "Exception , bad request  [%s] !"//
			, "Exception , bad request  [%s] !");

	public static final EmployeeMessage STATUS_NOT_EXIST = new EmployeeMessage(HttpStatus.NOT_FOUND, "EMP-REQ-0005",
			"THIS Status [%s] No found !", //
			"statu not found!");

	public static final EmployeeMessage STATUS_IN_CORRECT = new EmployeeMessage(HttpStatus.CONFLICT, "EMP-REQ-0006",
			"THIS Status [%s]  has not dependancy  ,if this status is added or same  exist status for this employee ( and not incheck )> this is incorrect!", //
			"THIS Status [%s]  has not dependancy  ,if this status is added or same  exist status for this employee ( and not incheck )> this is incorrect!");

	public static final EmployeeMessage STATUS_IN_CORRECT_WITH_SUB = new EmployeeMessage(HttpStatus.CONFLICT,
			"EMP-REQ-0007",
			"THIS Status [%s]  YOU do not be allowed to set last sub status however the main status not in check to change the sub  !", //
			"THIS Status [%s]  YOU do not be allowed to set last sub , the employee must be in incheck status before changing the sub !");

	public static final EmployeeMessage SUB_STATUS_NOT_EXIST = new EmployeeMessage(HttpStatus.NOT_FOUND, "EMP-REQ-0008",
			"THIS SUB Status [%s] No found !", //
			"Sub statu not found!");

	public static final EmployeeMessage STATUS_IN_CORRECT_WITH_SUB_NOT_QULIFIED = new EmployeeMessage(
			HttpStatus.CONFLICT, "EMP-REQ-0009",
			"THIS Status [%s]  your sub status in correct not depends on the exist Or null !", //
			"THIS Status [%s]  your sub status in correct not depends on the exist  or null !");

}
