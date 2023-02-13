package com.workmotion.employees.camunda.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CamundaConstants {
/*
    public static final String ORDER_PROCESS_WORKFLOW = "ORDER_PROCESS";
    public static final String TERMINATION_ORDER_WORKFLOW = "TERMINATION_ORDER";

    public static final String SUBMIT_REQUEST_TASK_DEFINITION_KEY = "SUBMIT_REQUEST";
    public static final String REVIEW_REQUEST_TASK_DEFINITION_KEY = "REVIEW_REQUEST";
    public static final String REPORT_FEASIBILITY_STATUS_TASK_DEFINITION_KEY = "REPORT_FEASIBILITY_STATUS";
    public static final String REVIEW_QUOTE = "REVIEW_QUOTE";
    public static final String REVIEW_FRI_REQUEST = "REVIEW_FRI_REQUEST";
    public static final String WAITING_FOR_RFI_TMS_TASK = "WAITING_FOR_RFI_TMS_TASK";
    public static final String REVIEW_CUSTOMER_COMMENT = "REVIEW_CUSTOMER_COMMENT";
    */
    
	public static final String EMPLOYEE_PROCESS = "EMPLOYEE_PROCESS";
	public static final String EMPLOYEE_PROCESS_SUB = "EMPLOYEE_PROCESS_SUB";

	public static final String TERMINATION_EMPLOYEE = "TERMINATION_EMPLOYEE";

	public static final String ADDEDD_EMPLOYEE = "ADDED_EMPLOYEE";
	public static final String IN_CHECK_EMPLOYEE = "IN_CHECK_EMPLOYEE";
	public static final String IN_CHECK_EVENT = "IN_CHECK_EVENT";
	public static final String START_SUB = "START_SUB";
	
	public static final String WORK_CHECK_START = "WORK_CHECK_START";
	public static final String SEC_CHECK_START = "SEC_CHECK_START";
	public static final String WORK_VERIFICATION = "WORK_VERIFICATION";
	public static final String WORK_CHECK_FINISH = "WORK_CHECK_FINISH";
	public static final String SEC_CHECK_FINISH = "SEC_CHECK_FINISH";
	public static final String APPROVED = "APPROVED";
	public static final String ACTIVE = "ACTIVE";
}
