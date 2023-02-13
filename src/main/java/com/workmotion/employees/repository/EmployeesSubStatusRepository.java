package com.workmotion.employees.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workmotion.employees.entities.EmployeesSubStatus;

public interface EmployeesSubStatusRepository extends JpaRepository<EmployeesSubStatus, Long> {

	Optional<EmployeesSubStatus> findByEmployeeSubtatusName(String employeeSubStatusName);

}
