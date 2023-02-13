package com.workmotion.employees.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Validated
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employees implements Serializable {

	private static final long serialVersionUID = -8316296377932111913L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private Long employeeId;

	@Column(name = "employee_name")
	@NonNull
	private String employeeName;

	@Column(name = "employee_username")
	@NonNull
	@Email
	private String employeeUserName;

	@Column(name = "employee_age")
	private Integer employeeAge;
	
	@Column(name = "employee_process_id")
	private String employeeProcessId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_status_id")
	@NonNull
	private EmployeesStatus employeesStatus;

}