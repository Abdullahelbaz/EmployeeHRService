package com.workmotion.employees.camunda.service;

import org.camunda.bpm.engine.delegate.VariableScope;

import com.workmotion.employees.exception.EmployeeException;

import java.util.Optional;

public abstract class AbstractVariableScope {

    protected String getStringVariable(VariableScope delegateTask, String name) {

        return Optional.ofNullable(delegateTask.getVariable(name))
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .orElseThrow(() -> new EmployeeException("Cant get '" + name + "' variable"));
    }

    protected Integer getIntegerVariable(VariableScope delegateTask, String name) {

        return Optional.ofNullable(delegateTask.getVariable(name))
            .filter(Integer.class::isInstance)
            .map(Integer.class::cast)
            .orElseThrow(() -> new EmployeeException("Cant get '" + name + "' variable"));
    }
}
