package com.workmotion.employees.camunda.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkflowKeys {

	public static final String EMPLOYEE_ID = "EMPLOYEE_ID";
	public static final String TRANSACTION_ID = "TRANSACTION_ID";
	public static final String STATUS_IN_CHECK_EVENT = "STATUS_IN_CHECK_EVENT";
	public static final String STATUS_APPROVE_EVENT = "STATUS_APPROVE_EVENT";
	public static final String STATUS_ACTIVE_EVENT = "STATUS_ACTIVE_EVENT";
	public static final String STATUS_WORK_START_EVENT = "STATUS_WORK_START_EVENT";
	public static final String STATUS_WORK_VERIFIED_EVENT = "STATUS_WORK_VERIFIED_EVENT";
	public static final String STATUS_WORK_FINISHED_EVENT = "STATUS_WORK_FINISHED_EVENT";
	public static final String STATUS_SECUIRITY_START_EVENT = "STATUS_SECUIRITY_START_EVENT";
	public static final String STATUS_SECURITY_FINISHED_EVENT = "STATUS_SECURITY_FINISHED_EVENT";

}
