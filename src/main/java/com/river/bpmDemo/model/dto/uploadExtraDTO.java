package com.river.bpmDemo.model.dto;

import lombok.Data;

/**
 * @program: bpmDemo
 * @description:
 * @author: River
 * @create: 2019-12-08 19:44
 **/
@Data
public class uploadExtraDTO {
    private String taskId;
    private String assignee;
    private String extraInfo;

}
