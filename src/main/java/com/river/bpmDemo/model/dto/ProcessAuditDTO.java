package com.river.bpmDemo.model.dto;

import lombok.Data;

/**
 * @program: bpmDemo
 * @description:
 * @author: River
 * @create: 2019-12-08 19:26
 **/
@Data
public class ProcessAuditDTO {
    private String taskId;
    private Boolean approved;
    private String assignee;
}
