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

@Validated
@Entity
@Table(name = "employee_sub_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesSubStatus implements Serializable {

	private static final long serialVersionUID = -8316296377932111913L;

	public static final Long SECURITY_CHECK_STARTED = 1L;
	public static final Long SECURITY_CHECK_FINISHED = 2L;
	public static final Long WORK_PERMIT_CHECK_STARTED = 3L;
	public static final Long WORK_PERMIT_CHECK_PENDING_VERIFICATION = 4L;
	public static final Long WORK_PERMIT_CHECK_FINISHED = 5L;

	@Id
	@Column(name = "employee_sub_status_id")
	private Long employeeSubStatusId;

	@Column(name = "employee_sub_status_name")
	private String employeeSubtatusName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_status_id")
	private EmployeesStatus employeesStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "depends_on_sub_status_id")
	private EmployeesSubStatus employeesSubStatusDependsOn;

}
