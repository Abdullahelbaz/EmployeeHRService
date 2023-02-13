package com.workmotion.employees.repository;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workmotion.employees.entities.EmployeesStatus;

public interface EmployeesStatusRepository extends JpaRepository<EmployeesStatus, Long> {

	Optional<EmployeesStatus> findByEmployeeStatusName(String employeeStatusName);

}
