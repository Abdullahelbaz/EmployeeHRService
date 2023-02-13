package com.workmotion.employees.service;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workmotion.employees.camunda.service.CamundaConstants;
import com.workmotion.employees.camunda.service.CamundaService;
import com.workmotion.employees.camunda.service.WorkflowKeys;
import com.workmotion.employees.controller.error.EmployeeMessage;
import com.workmotion.employees.dto.EmployeesAddDTO;
import com.workmotion.employees.dto.EmployeesDTO;
import com.workmotion.employees.entities.Employees;
import com.workmotion.employees.entities.EmployeesStatus;
import com.workmotion.employees.entities.EmployeesSubStatus;
import com.workmotion.employees.entities.TransactionType;
import com.workmotion.employees.entities.TransactionsHistory;
import com.workmotion.employees.exception.EmployeeException;
import com.workmotion.employees.repository.EmployeesRepository;
import com.workmotion.employees.repository.EmployeesStatusRepository;
import com.workmotion.employees.repository.EmployeesSubStatusRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	TransactionHistoryService transactionHistoryService;

	@Autowired
	EmployeesRepository employeesRepository;

	@Autowired
	EmployeesStatusRepository employeesStatusRepository;

	@Autowired
	CamundaService camundaService;

	@Autowired
	EmployeesSubStatusRepository employeesSubStatusRepository;

	private final String FROM_SUB_WORK = "WOKE";
	private final String FROM_SUB_SEC = "SEC";

	@Transactional
	public EmployeesDTO addEmployee(EmployeesAddDTO employeesAddDTO) {
		log.info("addEmployee start");

		if (employeesRepository.findByEmployeeUserName(employeesAddDTO.getEmployeeUserName()).isPresent())
			throw new EmployeeException(EmployeeMessage.EMPLOYEE_ALLREADY_EXIST_BY_SAME_USERNAME,
					employeesAddDTO.getEmployeeUserName());

		ProcessInstance proccessInstance = camundaService.startProcess(CamundaConstants.EMPLOYEE_PROCESS);

		Employees employees = modelMapper.map(employeesAddDTO, Employees.class);

		EmployeesStatus employeesStatus = new EmployeesStatus();
		employeesStatus.setEmployeeStatusId(EmployeesStatus.EMPLOYEE_STATUS_ADDED);
		employees.setEmployeesStatus(employeesStatus);
		employees.setEmployeeProcessId(proccessInstance.getProcessInstanceId());

		Employees employeeSaved = employeesRepository.save(employees);

		EmployeesDTO employeeDTO = modelMapper.map(employeeSaved, EmployeesDTO.class);

		TransactionType transactionType = new TransactionType();
		transactionType.setTransactionTypeId(TransactionType.TRANSACTION_TYPE_ADD_EMPLOYEE);
		TransactionsHistory transactionsHistorySaved = transactionHistoryService
				.createTransactionsHistory(transactionType, employeeSaved, employeesStatus, null);

		Map<String, Object> mapVar = new HashMap<>();
		mapVar.put(WorkflowKeys.EMPLOYEE_ID, employeeSaved.getEmployeeId().toString());
		mapVar.put(WorkflowKeys.TRANSACTION_ID, transactionsHistorySaved.getTransactionHistoryId().toString());
		camundaService.completeTask(proccessInstance.getCaseInstanceId(), CamundaConstants.ADDEDD_EMPLOYEE, mapVar);

		log.info("addEmployee End");
		return employeeDTO;
	}

	public EmployeesDTO getEmployee(String employeeUserName) {
		log.info("getEmployee start");

		Employees employee = employeesRepository.findByEmployeeUserName(employeeUserName)
				.orElseThrow(() -> new EmployeeException(EmployeeMessage.EMPLOYEE_NOT_EXIST, employeeUserName));

		EmployeesDTO employeeDTO = modelMapper.map(employee, EmployeesDTO.class);

		TransactionType transactionType = new TransactionType();
		transactionType.setTransactionTypeId(TransactionType.TRANSACTION_TYPE_FIND_EMPLOYEE);
		transactionHistoryService.createTransactionsHistory(transactionType, employee, employee.getEmployeesStatus(),
				null);

		log.info("getEmployee End");
		return employeeDTO;
	}

	@Transactional
	public EmployeesDTO updateStatusEmployee(EmployeesDTO employeesDTO) {
		log.info("updateStatusEmployee start");

		// Check the employee exist or not
		Employees employeeEntity = employeesRepository.findByEmployeeUserName(employeesDTO.getEmployeeUserName())
				.orElseThrow(() -> new EmployeeException(EmployeeMessage.EMPLOYEE_NOT_EXIST,
						employeesDTO.getEmployeeUserName()));

		// Check the status exist or not
		EmployeesStatus employeesStatusToUpdate = employeesStatusRepository
				.findByEmployeeStatusName(employeesDTO.getEmployeesStatus().getEmployeeStatusName())
				.orElseThrow(() -> new EmployeeException(EmployeeMessage.STATUS_NOT_EXIST,
						employeesDTO.getEmployeesStatus().getEmployeeStatusName()));

		// Check the Sub status exist or not if in in-check status
		EmployeesSubStatus employeesSubStatus = null;
		if (employeesStatusToUpdate.getEmployeeStatusId() == EmployeesStatus.IN_CHECK
				&& employeesDTO.getLastEmployeeSubtatusName() != null)
			employeesSubStatus = employeesSubStatusRepository
					.findByEmployeeSubtatusName(employeesDTO.getLastEmployeeSubtatusName())
					.orElseThrow(() -> new EmployeeException(EmployeeMessage.SUB_STATUS_NOT_EXIST,
							employeesDTO.getEmployeesStatus().getEmployeeStatusName()));

		log.info("updateStatusEmployee employeeEntity id " + employeeEntity.getEmployeeId()
				+ ",employeesStatusToUpdate id " + employeesStatusToUpdate.getEmployeeStatusId()
				+ " ,LastEmployeeSubtatusName() " + employeesDTO.getLastEmployeeSubtatusName() + " ,employeesSubStatus "
				+ employeesSubStatus);

		validationEmployeeAndStatus(employeesDTO, employeeEntity, employeesStatusToUpdate, employeesSubStatus);

		EmployeesSubStatus employeesSubStatusUpdated = null;

		// Check the updated status depends on the existing status on employee or not
		if (employeesStatusToUpdate.getEmployeesStatusDependsOn().getEmployeeStatusName()
				.equals(employeeEntity.getEmployeesStatus().getEmployeeStatusName())) {

			log.info("updateStatusEmployee dependancy correct after validation");

			employeeEntity.setEmployeesStatus(employeesStatusToUpdate);

			// added to incheck
			if (employeesStatusToUpdate.getEmployeeStatusId().longValue() == EmployeesStatus.IN_CHECK.longValue())
				camundaCompleteInCheck(EmployeesStatus.IN_CHECK, employeeEntity.getEmployeeId(),
						employeeEntity.getEmployeeProcessId());
			// approve to active
			else if (employeesStatusToUpdate.getEmployeeStatusId().longValue() == EmployeesStatus.ACTIVE.longValue())
				camundaCompleteInCheck(EmployeesStatus.ACTIVE, employeeEntity.getEmployeeId(),
						employeeEntity.getEmployeeProcessId());

		} else if (employeesStatusToUpdate.getEmployeeStatusName()
				.equals(employeeEntity.getEmployeesStatus().getEmployeeStatusName())
				&& employeesStatusToUpdate.getEmployeeStatusId().longValue() == EmployeesStatus.IN_CHECK.longValue()
				&& employeesDTO.getLastEmployeeSubtatusName() != null) {

			log.info("updateStatusEmployee In check correct after validation");

			if (employeesSubStatus.getEmployeeSubStatusId().longValue() == EmployeesSubStatus.SECURITY_CHECK_FINISHED
					.longValue()) {
				camundaCompleteInCheck(EmployeesSubStatus.SECURITY_CHECK_FINISHED, employeeEntity.getEmployeeId(),
						employeeEntity.getEmployeeProcessId());
				employeesSubStatusUpdated = new EmployeesSubStatus();
				employeesSubStatusUpdated.setEmployeeSubStatusId(EmployeesSubStatus.SECURITY_CHECK_FINISHED);
				validateApprove(employeeEntity, FROM_SUB_SEC);

			} else if (employeesSubStatus.getEmployeeSubStatusId()
					.longValue() == EmployeesSubStatus.WORK_PERMIT_CHECK_PENDING_VERIFICATION.longValue()) {
				camundaCompleteInCheck(EmployeesSubStatus.WORK_PERMIT_CHECK_PENDING_VERIFICATION,
						employeeEntity.getEmployeeId(), employeeEntity.getEmployeeProcessId());
				employeesSubStatusUpdated = new EmployeesSubStatus();
				employeesSubStatusUpdated
						.setEmployeeSubStatusId(EmployeesSubStatus.WORK_PERMIT_CHECK_PENDING_VERIFICATION);

			} else if (employeesSubStatus.getEmployeeSubStatusId()
					.longValue() == EmployeesSubStatus.WORK_PERMIT_CHECK_FINISHED.longValue()) {
				camundaCompleteInCheck(EmployeesSubStatus.WORK_PERMIT_CHECK_FINISHED, employeeEntity.getEmployeeId(),
						employeeEntity.getEmployeeProcessId());
				employeesSubStatusUpdated = new EmployeesSubStatus();
				employeesSubStatusUpdated.setEmployeeSubStatusId(EmployeesSubStatus.WORK_PERMIT_CHECK_FINISHED);
				validateApprove(employeeEntity, FROM_SUB_WORK);

			}

		}

		Employees employeeSaved = employeesRepository.save(employeeEntity);
		EmployeesDTO employeesDTOUpdated = modelMapper.map(employeeSaved, EmployeesDTO.class);

		TransactionType transactionType = new TransactionType();
		transactionType.setTransactionTypeId(TransactionType.TRANSACTION_TYPE_UPDATE_STATUS);
		transactionHistoryService.createTransactionsHistory(transactionType, employeeSaved, employeesStatusToUpdate,
				employeesSubStatusUpdated);

		log.info("updateStatusEmployee End");
		return employeesDTOUpdated;

	}

	private void camundaCompleteInCheck(Long status, Long employeeId, String proccessId) {

		log.info("camundaCompleteInCheck Start");

		Map<String, Object> mapVar = new HashMap<>();
		mapVar.put(WorkflowKeys.EMPLOYEE_ID, employeeId.toString());

		if (status.longValue() == EmployeesStatus.IN_CHECK.longValue()) {

			log.info("camundaCompleteInCheck IN_CHECK done");
			mapVar.put(WorkflowKeys.STATUS_IN_CHECK_EVENT, "STATUS_IN_CHECK_EVENT");
			camundaService.completeTask(proccessId, CamundaConstants.IN_CHECK_EVENT, mapVar);
			camundaService.completeTask(proccessId, CamundaConstants.SEC_CHECK_START, mapVar);
			camundaService.completeTask(proccessId, CamundaConstants.WORK_CHECK_START, mapVar);

		} else if (status.longValue() == EmployeesStatus.ACTIVE.longValue()) {
			log.info("camundaCompleteInCheck ACTIVE done");
			mapVar.put(WorkflowKeys.STATUS_ACTIVE_EVENT, "STATUS_ACTIVE_EVENT");
			camundaService.completeTask(proccessId, CamundaConstants.ACTIVE, mapVar);

		} else if (status.longValue() == EmployeesSubStatus.SECURITY_CHECK_FINISHED.longValue()) {
			log.info("camundaCompleteInCheck SECURITY_CHECK_FINISHED done");
			mapVar.put(WorkflowKeys.STATUS_SECURITY_FINISHED_EVENT, "STATUS_SECURITY_FINISHED_EVENT");
			camundaService.completeTask(proccessId, CamundaConstants.SEC_CHECK_FINISH, mapVar);
		}

		else if (status.longValue() == EmployeesSubStatus.WORK_PERMIT_CHECK_PENDING_VERIFICATION.longValue()) {
			log.info("camundaCompleteInCheck WORK_PERMIT_CHECK_PENDING_VERIFICATION done");
			mapVar.put(WorkflowKeys.STATUS_WORK_VERIFIED_EVENT, "STATUS_WORK_VERIFIED_EVENT");
			camundaService.completeTask(proccessId, CamundaConstants.WORK_VERIFICATION, mapVar);

		}

		else if (status.longValue() == EmployeesSubStatus.WORK_PERMIT_CHECK_FINISHED.longValue()) {
			log.info("camundaCompleteInCheck WORK_PERMIT_CHECK_FINISHED done");
			mapVar.put(WorkflowKeys.STATUS_WORK_FINISHED_EVENT, "STATUS_WORK_FINISHED_EVENT");
			camundaService.completeTask(proccessId, CamundaConstants.WORK_CHECK_FINISH, mapVar);

		}
		log.info("camundaCompleteInCheck End");

	}

	private void validationEmployeeAndStatus(EmployeesDTO employeesDTO, Employees employeeEntity,
			EmployeesStatus employeesStatusToUpdate, EmployeesSubStatus employeesSubStatus) {
		log.info("validationEmployeeAndStatus Start");

		// giving status not has dependency > will be #added status (added will assigned
		// automated by add employee so will throw exception)
		if (employeesStatusToUpdate.getEmployeesStatusDependsOn().getEmployeeStatusName() == null)
			throw new EmployeeException(EmployeeMessage.STATUS_IN_CORRECT,
					employeesDTO.getEmployeesStatus().getEmployeeStatusName());
		log.info("validationEmployeeAndStatus all main status validation done");

		// existing status not depend on the giving and not in chack status
		if ((!employeesStatusToUpdate.getEmployeesStatusDependsOn().getEmployeeStatusName()
				.equals(employeeEntity.getEmployeesStatus().getEmployeeStatusName()))
				&& (employeesStatusToUpdate.getEmployeeStatusId().longValue() != EmployeesStatus.IN_CHECK.longValue()
						|| employeeEntity.getEmployeesStatus().getEmployeeStatusId()
								.longValue() != EmployeesStatus.IN_CHECK.longValue()))
			throw new EmployeeException(EmployeeMessage.STATUS_IN_CORRECT,
					employeesDTO.getEmployeesStatus().getEmployeeStatusName());
		log.info("validationEmployeeAndStatus add incheck 1 done");

		// giving status same assigned one now and not #in_check status (only in check
		// will be
		// same to change sub so will throw exception)
		if (employeesStatusToUpdate.getEmployeeStatusName()
				.equals(employeeEntity.getEmployeesStatus().getEmployeeStatusName())
				&& (employeesStatusToUpdate.getEmployeeStatusId().longValue() != EmployeesStatus.IN_CHECK.longValue()
						|| employeeEntity.getEmployeesStatus().getEmployeeStatusId()
								.longValue() != EmployeesStatus.IN_CHECK.longValue()))
			throw new EmployeeException(EmployeeMessage.STATUS_IN_CORRECT,
					employeesDTO.getEmployeesStatus().getEmployeeStatusName());
		log.info("validationEmployeeAndStatus add incheck 2 done");

		// you can set LastEmployeeSubtatusName value only if the status is in check
		// same assigned to change sub )
		if (employeesDTO.getLastEmployeeSubtatusName() != null
				&& (employeesStatusToUpdate.getEmployeeStatusId().longValue() != EmployeesStatus.IN_CHECK.longValue()
						|| employeeEntity.getEmployeesStatus().getEmployeeStatusId()
								.longValue() != EmployeesStatus.IN_CHECK.longValue())) {
			throw new EmployeeException(EmployeeMessage.STATUS_IN_CORRECT_WITH_SUB,
					employeesDTO.getEmployeesStatus().getEmployeeStatusName());
		}

		log.info("validationEmployeeAndStatus add incheck 3 done");

		if (employeesStatusToUpdate.getEmployeeStatusName()
				.equals(employeeEntity.getEmployeesStatus().getEmployeeStatusName())
				&& employeesStatusToUpdate.getEmployeeStatusId().longValue() == EmployeesStatus.IN_CHECK.longValue()
				&& employeesDTO.getLastEmployeeSubtatusName() == null) {
			throw new EmployeeException(EmployeeMessage.STATUS_IN_CORRECT_WITH_SUB_NOT_QULIFIED,
					employeesDTO.getEmployeesStatus().getEmployeeStatusName());

		}
		log.info("validationEmployeeAndStatus add incheck sub 1 done");

		if (employeesStatusToUpdate.getEmployeeStatusName()
				.equals(employeeEntity.getEmployeesStatus().getEmployeeStatusName())
				&& employeesStatusToUpdate.getEmployeeStatusId().longValue() == EmployeesStatus.IN_CHECK.longValue()
				&& employeesDTO.getLastEmployeeSubtatusName() != null) {
			log.info("LastEmployeeSubtatusName() != null and sub status id = "
					+ employeesSubStatus.getEmployeesSubStatusDependsOn().getEmployeeSubStatusId());

			TransactionsHistory transactionsHistory = transactionHistoryService
					.getTransactionHistoryByEmployeeAndStatus(employeeEntity.getEmployeeId(), EmployeesStatus.IN_CHECK,
							employeesSubStatus.getEmployeesSubStatusDependsOn().getEmployeeSubStatusId());
			if (transactionsHistory == null)
				throw new EmployeeException(EmployeeMessage.STATUS_IN_CORRECT_WITH_SUB_NOT_QULIFIED,
						employeesDTO.getEmployeesStatus().getEmployeeStatusName());
			else
				log.info("transactionsHistory is " + transactionsHistory.toString());

		}
		log.info("validationEmployeeAndStatus add incheck sub 2 done");

		log.info("validationEmployeeAndStatus End");

	}

	private void validateApprove(Employees employeeEntity, String fromSubStatus) {

		TransactionsHistory transactionsHistory = null;

		if (fromSubStatus.equals(FROM_SUB_WORK)) {
			transactionsHistory = new TransactionsHistory();
			transactionsHistory = transactionHistoryService.getTransactionHistoryByEmployeeAndStatus(
					employeeEntity.getEmployeeId(), EmployeesStatus.IN_CHECK,
					EmployeesSubStatus.SECURITY_CHECK_FINISHED);
		}
		if (fromSubStatus.equals(FROM_SUB_SEC)) {
			transactionsHistory = new TransactionsHistory();
			transactionsHistory = transactionHistoryService.getTransactionHistoryByEmployeeAndStatus(
					employeeEntity.getEmployeeId(), EmployeesStatus.IN_CHECK,
					EmployeesSubStatus.WORK_PERMIT_CHECK_FINISHED);
		}

		if (transactionsHistory != null) {

			TransactionType transactionType = new TransactionType();
			transactionType.setTransactionTypeId(TransactionType.TRANSACTION_TYPE_UPDATE_STATUS);

			EmployeesStatus employeesStatus = new EmployeesStatus();
			employeesStatus.setEmployeeStatusId(EmployeesStatus.APPROVED);
			employeeEntity.setEmployeesStatus(employeesStatus);

			transactionHistoryService.createTransactionsHistory(transactionType, employeeEntity, employeesStatus, null);
		}
	}
}
