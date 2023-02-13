package com.workmotion.employees.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workmotion.employees.entities.Employees;
import com.workmotion.employees.entities.EmployeesStatus;
import com.workmotion.employees.entities.EmployeesSubStatus;
import com.workmotion.employees.entities.TransactionType;
import com.workmotion.employees.entities.TransactionsHistory;
import com.workmotion.employees.repository.TransactionHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionHistoryService {

	@Autowired
	TransactionHistoryRepository transactionHistoryRepository;

	@Transactional
	public TransactionsHistory createTransactionsHistory(TransactionType transactionType, Employees employees,
			EmployeesStatus employeesStatus, EmployeesSubStatus employeesSubStatus) {

		log.info("createTransactionsHistory start");
		TransactionsHistory transactionsHistory = TransactionsHistory.builder().transactionType(transactionType)
				.employee(employees).employeesStatus(employeesStatus).employeesSubStatus(employeesSubStatus)
				.transactionDate(LocalDateTime.now()).build();

		TransactionsHistory transactionsHistorySaved = transactionHistoryRepository.save(transactionsHistory);

		log.info("createTransactionsHistory end");
		return transactionsHistorySaved;
	}

	@Transactional
	public void createListOfTransactionsHistory(List<TransactionsHistory> transactionsHistoryList) {

		log.info("createListOfTransactionsHistory start");

		transactionHistoryRepository.saveAll(transactionsHistoryList);

		log.info("createListOfTransactionsHistory end");
	}

	public TransactionsHistory getTransactionHistoryByEmployeeAndStatus(Long employeeId, Long employeeStatusId,
			Long employeeSubStatusId) {

		log.info("getTransactionHistoryByEmployeeAndStatus start");
		TransactionsHistory transactionsHistory = null;
		if (employeeSubStatusId == null)
			transactionsHistory = transactionHistoryRepository.findByEmployeeAndStatus(employeeId, employeeStatusId)
					.orElse(null);
		else
			transactionsHistory = transactionHistoryRepository
					.findByEmployeeAndStatus(employeeId, employeeStatusId, employeeSubStatusId).orElse(null);

		log.info("getTransactionHistoryByEmployeeAndStatus end");
		return transactionsHistory;
	}

}
