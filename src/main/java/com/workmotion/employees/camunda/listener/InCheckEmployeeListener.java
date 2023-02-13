
package com.workmotion.employees.camunda.listener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workmotion.employees.camunda.service.AbstractVariableScope;
import com.workmotion.employees.camunda.service.CamundaService;
import com.workmotion.employees.camunda.service.WorkflowKeys;
import com.workmotion.employees.entities.Employees;
import com.workmotion.employees.entities.EmployeesStatus;
import com.workmotion.employees.entities.EmployeesSubStatus;
import com.workmotion.employees.entities.TransactionType;
import com.workmotion.employees.entities.TransactionsHistory;
import com.workmotion.employees.repository.EmployeesSubStatusRepository;
import com.workmotion.employees.service.TransactionHistoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InCheckEmployeeListener extends AbstractVariableScope implements TaskListener {

	@Autowired
	CamundaService camundaService;
	@Autowired
	TransactionHistoryService transactionHistoryService;
	@Autowired
	EmployeesSubStatusRepository employeesSubStatusRepository;

	@Override
	public void notify(DelegateTask delegateTask) {
		String employeeId = getStringVariable(delegateTask, WorkflowKeys.EMPLOYEE_ID);

		log.info("InCheckEmployeeListener notify EmployeeId " + employeeId);

		
		Employees employee = new Employees();
		employee.setEmployeeId(Long.parseLong(employeeId));

		EmployeesStatus employeesStatus = new EmployeesStatus();
		employeesStatus.setEmployeeStatusId(EmployeesStatus.IN_CHECK);

		TransactionType transactionType = new TransactionType();
		transactionType.setTransactionTypeId(TransactionType.TRANSACTION_TYPE_UPDATE_STATUS);

		EmployeesSubStatus employeesSubStatusUpdated = new EmployeesSubStatus();
		employeesSubStatusUpdated.setEmployeeSubStatusId(EmployeesSubStatus.SECURITY_CHECK_STARTED);

		EmployeesSubStatus employeesSubStatusUpdatedWork = new EmployeesSubStatus();
		employeesSubStatusUpdatedWork.setEmployeeSubStatusId(EmployeesSubStatus.WORK_PERMIT_CHECK_STARTED);

		List<TransactionsHistory> transactionsHistoryList = new ArrayList<>();

		TransactionsHistory transactionsHistorySec = TransactionsHistory.builder().transactionType(transactionType)
				.employee(employee).employeesStatus(employeesStatus).employeesSubStatus(employeesSubStatusUpdated)
				.transactionDate(LocalDateTime.now()).build();
		TransactionsHistory transactionsHistoryWork = TransactionsHistory.builder().transactionType(transactionType)
				.employee(employee).employeesStatus(employeesStatus).employeesSubStatus(employeesSubStatusUpdatedWork)
				.transactionDate(LocalDateTime.now()).build();

		transactionsHistoryList.add(transactionsHistorySec);
		transactionsHistoryList.add(transactionsHistoryWork);

		transactionHistoryService.createListOfTransactionsHistory(transactionsHistoryList);

	}

}
