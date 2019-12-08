package com.river.bpmDemo.task;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class SmsServiceTask implements JavaDelegate {

    private final TaskService taskService;

    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> variables = delegateExecution.getVariables();
        log.info("variables is {}", variables);

        String studentId = (String)variables.get("student");
        log.info("success send sms message to the student {}", studentId);
    }
}
