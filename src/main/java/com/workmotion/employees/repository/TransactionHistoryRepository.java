package com.workmotion.employees.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.workmotion.employees.entities.TransactionsHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionsHistory, Long> {

	@Query(value = " from TransactionsHistory th where th.employee.employeeId =:employeeId and th.employeesStatus.employeeStatusId = :employeeStatusId ")
	Optional<TransactionsHistory> findByEmployeeAndStatus(Long employeeId, Long employeeStatusId);

	@Query(value = " from TransactionsHistory th where th.employee.employeeId =:employeeId and th.employeesStatus.employeeStatusId = :employeeStatusId and th.employeesSubStatus.employeeSubStatusId =:employeeSubStatusId ")
	Optional<TransactionsHistory> findByEmployeeAndStatus(Long employeeId, Long employeeStatusId,
			Long employeeSubStatusId);
}
