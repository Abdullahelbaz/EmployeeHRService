package com.workmotion.employees.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Validated
@Entity
@Table(name = "employee_transaction_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionsHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_history_id")
	private Long transactionHistoryId;

	@Column(name = "transaction_history_date")
	@NonNull
	private LocalDateTime transactionDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_type_id")
	@NonNull
	private TransactionType transactionType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	@NonNull
	private Employees employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_status_id")
	@NonNull
	private EmployeesStatus employeesStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_Sub_status_id")
	private EmployeesSubStatus employeesSubStatus;

}
