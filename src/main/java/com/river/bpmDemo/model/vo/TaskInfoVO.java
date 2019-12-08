package com.river.bpmDemo.model.vo;

import lombok.Data;
import java.util.Date;

/**
 * @program: bpmDemo
 * @description:
 * @author: River
 * @create: 2019-12-08 16:01
 **/
@Data
public class TaskInfoVO {
    private String taskId;
    private String taskName;
    private String projectId;
    private String assignee;
    private Date createTime;
}
