package com.workmotion.employees.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Validated
@Entity
@Table(name = "employee_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesStatus implements Serializable {

	private static final long serialVersionUID = -8316296377932111913L;

	public static final Long EMPLOYEE_STATUS_ADDED = 1L;
	public static final Long IN_CHECK = 2L;
	public static final Long APPROVED = 3L;
	public static final Long ACTIVE = 4L;

	@Id
	@Column(name = "employee_status_id")
	private Long employeeStatusId;

	@Column(name = "employee_status_name")
	private String employeeStatusName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depends_on_status_id")
	private EmployeesStatus employeesStatusDependsOn;

}