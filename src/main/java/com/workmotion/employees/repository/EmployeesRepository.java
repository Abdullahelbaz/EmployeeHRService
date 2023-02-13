package com.workmotion.employees.repository;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workmotion.employees.entities.Employees;

public interface EmployeesRepository extends JpaRepository<Employees, Long> {

	Optional<Employees> findByEmployeeUserName(@NotNull(message = "employeeUserName cannot be null") String employeeUserName);

}
