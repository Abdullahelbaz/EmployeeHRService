package com.workmotion.employees.camunda.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CamundaService {

	private final RuntimeService runtimeService;
	private final TaskService taskService;

	public ProcessInstance startProcess(String processDefinitionKey) {
		return runtimeService.startProcessInstanceByKey(processDefinitionKey);
	}

	@Transactional
	public void completeTask(String processInstanceId, String taskDefinitionKey, Map<String, Object> variables) {
		completeTask(processInstanceId, taskDefinitionKey, null, variables);
	}

	@Transactional
	public void completeTask(String processInstanceId, String taskDefinitionKey, String executionId,
			Map<String, Object> variables) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.executionId(executionId).taskDefinitionKey(taskDefinitionKey).orderByDueDate().asc().list();

		if (taskList != null && taskList.size() == 1) {
			Task taskInstance = taskList.get(0);

			try {
				taskService.complete(taskInstance.getId(), variables);
			} catch (Exception e) {
				log.error("Cannot complete task", e);
			}
		}
	}

	public void addAttachment(String attachmentType, String taskId, String processInstanceId, String attachmentName,
			InputStream content) {

		taskService.createAttachment(attachmentType, taskId, processInstanceId, attachmentName, null, content);
	}

}
