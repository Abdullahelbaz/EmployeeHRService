package com.workmotion.employees.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@Validated
@Entity
@Table(name = "employee_transaction_type")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionType {

	public static final Long TRANSACTION_TYPE_ADD_EMPLOYEE = 1L;
	public static final Long TRANSACTION_TYPE_UPDATE_STATUS = 2L;
	public static final Long TRANSACTION_TYPE_FIND_EMPLOYEE = 3L;

	@Id
	@Column(name = "transaction_type_id")
	private Long transactionTypeId;

	@Column(name = "transaction_type_name")
	private String transactionTypeName;

}
